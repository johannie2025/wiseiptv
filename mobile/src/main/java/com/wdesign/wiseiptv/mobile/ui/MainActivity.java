package com.wdesign.wiseiptv.mobile.ui;

import android.content.*;
import android.os.*;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.adapter.ChannelAdapter;
import com.wdesign.wiseiptv.mobile.util.ActivationManager;
import com.wdesign.wiseiptv.mobile.util.PlaylistLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * MainActivity — Écran principal après activation.
 *
 * CORRECTIONS :
 *  1. Reçoit l'ActivationResult depuis ActivationActivity via extras Intent.
 *     → Pas de deuxième appel réseau DeviceSecurity.check(), pas de deadlock Room.
 *  2. Si les extras sont absents (retour depuis Settings, rotation, etc.) :
 *     → Reconstruit un ActivationResult depuis les SharedPreferences (mode cache).
 *     → Si le cache est absent → refreshStaleIfNeeded() pour les playlists manuelles.
 *  3. Le téléchargement se fait dans downloadAllPlaylistsAsync() (ActivationManager)
 *     qui est déjà thread-safe et séquentiel (pas de transactions parallèles).
 *  4. WiseApp ne fait plus checkAndSync() → suppression du double appel.
 */
public class MainActivity extends AppCompatActivity implements ChannelAdapter.OnChannelClick {

    // Extras transmis par ActivationActivity
    public static final String EXTRA_ACT_LOGIN       = "act_login";
    public static final String EXTRA_ACT_PASSWORD    = "act_password";
    public static final String EXTRA_ACT_EXPIRES     = "act_expires";
    public static final String EXTRA_ACT_DNS_URLS    = "act_dns_urls";
    public static final String EXTRA_ACT_DNS_EPG_URLS= "act_dns_epg_urls";

    private RecyclerView        rvChannels;
    private ChannelAdapter      adapter;
    private ProgressBar         progressBar;
    private TextView            tvEmpty;
    private TabLayout           tabLayout;
    private BottomNavigationView bottomNav;
    private SearchView          searchView;
    private Spinner             spinnerGroup;
    private AppDatabase         db;
    private int    currentTab   = 0;
    private String currentGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db           = AppDatabase.get(this);
        rvChannels   = findViewById(R.id.rv_channels);
        progressBar  = findViewById(R.id.progress_bar);
        tvEmpty      = findViewById(R.id.tv_empty);
        tabLayout    = findViewById(R.id.tab_layout);
        bottomNav    = findViewById(R.id.bottom_nav);
        searchView   = findViewById(R.id.search_view);
        spinnerGroup = findViewById(R.id.spinner_group);

        adapter = new ChannelAdapter(this);
        rvChannels.setLayoutManager(new GridLayoutManager(this, 3));
        rvChannels.setAdapter(adapter);

        setupTabs();
        setupSearch();
        setupBottomNav();
        setupGroupSpinner();

        // Affiche immédiatement les chaînes déjà en cache (bonne UX)
        observeCurrentTab();

        // Lance le téléchargement en arrière-plan avec l'ActivationResult reçu
        startBackgroundSync();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Téléchargement arrière-plan
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Reconstruit l'ActivationResult depuis les extras Intent (chemin normal)
     * ou depuis les SharedPreferences (cache / retour arrière).
     * Puis lance downloadAllPlaylistsAsync() qui est séquentiel et thread-safe.
     */
    private void startBackgroundSync() {
        Intent intent = getIntent();

        // ── Chemin 1 : extras frais reçus depuis ActivationActivity ──────────
        String login    = intent.getStringExtra(EXTRA_ACT_LOGIN);
        String password = intent.getStringExtra(EXTRA_ACT_PASSWORD);
        String expires  = intent.getStringExtra(EXTRA_ACT_EXPIRES);
        String[] dnsUrls    = intent.getStringArrayExtra(EXTRA_ACT_DNS_URLS);
        String[] dnsEpgUrls = intent.getStringArrayExtra(EXTRA_ACT_DNS_EPG_URLS);

        if (login != null && dnsUrls != null && dnsUrls.length > 0) {
            List<DeviceSecurity.DnsEntry> dnsEntries = new ArrayList<>();
            for (int i = 0; i < dnsUrls.length; i++) {
                String epg = (dnsEpgUrls != null && i < dnsEpgUrls.length) ? dnsEpgUrls[i] : "";
                dnsEntries.add(new DeviceSecurity.DnsEntry(dnsUrls[i], epg, i));
            }
            DeviceSecurity.ActivationResult result =
                    new DeviceSecurity.ActivationResult(
                            DeviceSecurity.getOrCreateKey(this),
                            login, password, expires != null ? expires : "", dnsEntries);
            launchDownload(result);
            return;
        }

        // ── Chemin 2 : pas d'extras (rotation, retour depuis Settings…) ──────
        // Reconstruction depuis les SharedPreferences sauvegardées par ActivationManager
        SharedPreferences prefs = getSharedPreferences("wise_activation", Context.MODE_PRIVATE);
        String savedStatus  = prefs.getString("status", "UNKNOWN");
        String savedLogin   = prefs.getString("login", null);
        String savedExpires = prefs.getString("expires_at", "");

        if ("ACTIVE".equals(savedStatus) && savedLogin != null) {
            // Chercher les playlists d'activation existantes en BDD pour reconstruire les DNS
            Executors.newSingleThreadExecutor().execute(() -> {
                List<PlaylistEntity> all = db.playlistDao().getAllSync();
                List<DeviceSecurity.DnsEntry> dnsEntries = new ArrayList<>();
                for (PlaylistEntity pl : all) {
                    if (pl.isActive && pl.type == PlaylistEntity.TYPE_XTREAM) {
                        dnsEntries.add(new DeviceSecurity.DnsEntry(pl.url, "", dnsEntries.size()));
                    }
                }
                if (!dnsEntries.isEmpty()) {
                    String pass = prefs.getString("password", ""); // Optionnel si stocké
                    DeviceSecurity.ActivationResult result =
                            new DeviceSecurity.ActivationResult(
                                    DeviceSecurity.getOrCreateKey(this),
                                    savedLogin, pass, savedExpires, dnsEntries);
                    // Ne retélécharge que si stale (> 7j)
                    boolean anyStale = false;
                    for (PlaylistEntity pl : all) {
                        if (pl.isActive && PlaylistLoader.needsRefresh(pl)) { anyStale = true; break; }
                    }
                    if (anyStale) {
                        runOnUiThread(() -> launchDownload(result));
                    }
                } else {
                    // Aucune playlist d'activation → fallback playlists manuelles
                    PlaylistLoader.refreshStaleIfNeeded(db, null);
                }
            });
            return;
        }

        // ── Chemin 3 : pas de cache d'activation → playlists manuelles ───────
        PlaylistLoader.refreshStaleIfNeeded(db, null);
    }

    /**
     * Lance le téléchargement arrière-plan de toutes les playlists du résultat
     * et gère l'affichage de la barre de progression.
     */
    private void launchDownload(DeviceSecurity.ActivationResult result) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        ActivationManager.downloadAllPlaylistsAsync(
                getApplicationContext(), db, result,
                new ActivationManager.OnDownloadCallback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(() -> {
                            if (isFinishing() || isDestroyed()) return;
                            if (progressBar != null) progressBar.setVisibility(View.GONE);
                            observeCurrentTab();
                            Toast.makeText(MainActivity.this,
                                    "✅ Chaînes synchronisées !", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onFailure(String msg) {
                        runOnUiThread(() -> {
                            if (isFinishing() || isDestroyed()) return;
                            if (progressBar != null) progressBar.setVisibility(View.GONE);
                            // On reste sur le cache silencieusement
                        });
                    }
                });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UI — Tabs / Search / BottomNav / Spinner
    // ─────────────────────────────────────────────────────────────────────────

    private void setupTabs() {
        String[] tabs = {"Tout", "Live", "Films", "Séries", "Favoris"};
        for (String t : tabs) tabLayout.addTab(tabLayout.newTab().setText(t));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab t) {
                currentTab = t.getPosition(); currentGroup = null;
                loadGroupsForCurrentTab(); observeCurrentTab();
            }
            @Override public void onTabUnselected(TabLayout.Tab t) {}
            @Override public void onTabReselected(TabLayout.Tab t) {}
        });
    }

    private void loadGroupsForCurrentTab() {
        switch (currentTab) {
            case 1: db.channelDao().getLiveGroups().observe(this, this::updateGroupSpinner); break;
            case 2: db.channelDao().getFilmGroups().observe(this, this::updateGroupSpinner); break;
            case 3: db.channelDao().getSeriesGroups().observe(this, this::updateGroupSpinner); break;
            default: updateGroupSpinner(null); break;
        }
    }

    private void updateGroupSpinner(List<String> groups) {
        if (spinnerGroup == null) return;
        if (groups == null || groups.isEmpty()) { spinnerGroup.setVisibility(View.GONE); return; }
        spinnerGroup.setVisibility(View.VISIBLE);
        ArrayList<String> items = new ArrayList<>();
        items.add("Tous les groupes");
        items.addAll(groups);
        ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(a);
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                currentGroup = pos == 0 ? null : items.get(pos);
                observeCurrentTab();
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });
    }

    private void setupGroupSpinner() { loadGroupsForCurrentTab(); }

    private void observeCurrentTab() {
        if (currentGroup != null) {
            switch (currentTab) {
                case 1: db.channelDao().getLiveByGroup(currentGroup).observe(this, this::updateList); return;
                case 2: db.channelDao().getFilmsByGroup(currentGroup).observe(this, this::updateList); return;
                case 3: db.channelDao().getSeriesByGroup(currentGroup).observe(this, this::updateList); return;
            }
        }
        switch (currentTab) {
            case 0: db.channelDao().getAll().observe(this, this::updateList);       break;
            case 1: db.channelDao().getLive().observe(this, this::updateList);      break;
            case 2: db.channelDao().getFilms().observe(this, this::updateList);     break;
            case 3: db.channelDao().getSeries().observe(this, this::updateList);    break;
            case 4: db.channelDao().getFavorites().observe(this, this::updateList); break;
        }
    }

    private void updateList(List<ChannelEntity> list) {
        adapter.setData(list);
        if (tvEmpty != null)
            tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void setupSearch() {
        if (searchView == null) return;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { return false; }
            @Override public boolean onQueryTextChange(String q) {
                if (TextUtils.isEmpty(q)) { observeCurrentTab(); return true; }
                db.channelDao().search(q).observe(MainActivity.this, MainActivity.this::updateList);
                return true;
            }
        });
    }

    private void setupBottomNav() {
        if (bottomNav == null) return;
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if      (id == R.id.nav_home)    { observeCurrentTab(); return true; }
            else if (id == R.id.nav_add)     { showAddPlaylistDialog(); return true; }
            else if (id == R.id.nav_settings){ startActivity(new Intent(this, SettingsActivity.class)); return true; }
            return false;
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Ajout manuel de playlist
    // ─────────────────────────────────────────────────────────────────────────

    public void showAddPlaylistDialog() {
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(this);
        b.setTitle("Ajouter une playlist");
        android.view.View v = getLayoutInflater().inflate(R.layout.dialog_add_playlist, null);
        EditText etName   = v.findViewById(R.id.et_playlist_name);
        EditText etUrl    = v.findViewById(R.id.et_playlist_url);
        EditText etServer = v.findViewById(R.id.et_xtream_server);
        EditText etUser   = v.findViewById(R.id.et_xtream_user);
        EditText etPass   = v.findViewById(R.id.et_xtream_pass);
        RadioGroup rgType = v.findViewById(R.id.rg_type);
        android.view.View layoutXtream = v.findViewById(R.id.layout_xtream);

        rgType.setOnCheckedChangeListener((g, checked) -> {
            boolean isXtream = checked == R.id.rb_xtream;
            layoutXtream.setVisibility(isXtream ? View.VISIBLE : View.GONE);
            etUrl.setVisibility(isXtream ? View.GONE : View.VISIBLE);
        });

        b.setView(v);
        b.setPositiveButton("Charger", (d, w) -> {
            String name     = etName.getText().toString().trim();
            int    checkedId = rgType.getCheckedRadioButtonId();
            PlaylistEntity pl = new PlaylistEntity();
            pl.name        = name.isEmpty() ? "Playlist" : name;
            pl.lastUpdated = 0;

            if (checkedId == R.id.rb_xtream) {
                String server = etServer.getText().toString().trim();
                String user   = etUser.getText().toString().trim();
                String pass   = etPass.getText().toString().trim();
                if (server.isEmpty() || user.isEmpty()) return;
                pl.type = PlaylistEntity.TYPE_XTREAM;
                pl.url = server; pl.username = user; pl.password = pass;
            } else {
                String url = etUrl.getText().toString().trim();
                if (TextUtils.isEmpty(url)) return;
                pl.type = checkedId == R.id.rb_m3u_file
                        ? PlaylistEntity.TYPE_M3U_FILE : PlaylistEntity.TYPE_M3U_URL;
                pl.url = url;
            }

            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            Executors.newSingleThreadExecutor().execute(() -> {
                pl.id = db.playlistDao().insert(pl);
                PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                    @Override public void onDone(int count) {
                        runOnUiThread(() -> {
                            if (progressBar != null) progressBar.setVisibility(View.GONE);
                            observeCurrentTab();
                            Toast.makeText(MainActivity.this, count + " chaînes chargées", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override public void onError(String msg) {
                        runOnUiThread(() -> {
                            if (progressBar != null) progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Erreur : " + msg, Toast.LENGTH_LONG).show();
                        });
                    }
                });
            });
        });
        b.setNegativeButton("Annuler", null);
        b.show();
    }

    @Override public void onClick(ChannelEntity ch) {
        Intent i = new Intent(this, PlayerActivity.class);
        i.putExtra(PlayerActivity.EXTRA_ID,    ch.id);
        i.putExtra(PlayerActivity.EXTRA_NAME,  ch.name);
        i.putExtra(PlayerActivity.EXTRA_URL,   ch.streamUrl);
        i.putExtra(PlayerActivity.EXTRA_TYPE,  ch.contentType);
        i.putExtra(PlayerActivity.EXTRA_GROUP, ch.groupTitle);
        i.putExtra(PlayerActivity.EXTRA_ORDER, ch.sortOrder);
        startActivity(i);
    }

    @Override protected void onResume() { super.onResume(); observeCurrentTab(); }
}
