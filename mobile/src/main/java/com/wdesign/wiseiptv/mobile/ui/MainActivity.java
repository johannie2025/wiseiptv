package com.wdesign.wiseiptv.mobile.ui;

import android.content.*;
import android.os.*;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.adapter.ChannelAdapter;
import com.wdesign.wiseiptv.mobile.util.PlaylistLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * MainActivity
 *
 * CORRECTIONS :
 *  1. Observers LiveData — removeObserver() avant chaque nouveau observe()
 *     → un seul observer actif à la fois → plus d'ANR.
 *  2. startBackgroundDownload() reçoit les extras d'ActivationActivity
 *     (login/password/dns_urls) et télécharge APRÈS que l'UI soit visible.
 *  3. Détection automatique Xtream vs M3U URL :
 *     - URL contenant "get.php", "/get.php", login+password → TYPE_XTREAM
 *     - URL finissant par .m3u/.m3u8 ou sans login → TYPE_M3U_URL
 */
public class MainActivity extends AppCompatActivity implements ChannelAdapter.OnChannelClick {

    // Extras transmis par ActivationActivity
    public static final String EXTRA_ACT_LOGIN        = "act_login";
    public static final String EXTRA_ACT_PASSWORD     = "act_password";
    public static final String EXTRA_ACT_EXPIRES      = "act_expires";
    public static final String EXTRA_ACT_DNS_URLS     = "act_dns_urls";
    public static final String EXTRA_ACT_DNS_EPG_URLS = "act_dns_epg_urls";

    private RecyclerView         rvChannels;
    private ChannelAdapter       adapter;
    private ProgressBar          progressBar;
    private TextView             tvEmpty;
    private TabLayout            tabLayout;
    private BottomNavigationView bottomNav;
    private SearchView           searchView;
    private Spinner              spinnerGroup;
    private AppDatabase          db;

    private int    currentTab   = 0;
    private String currentGroup = null;

    // Un seul observer LiveData actif à la fois
    private LiveData<List<ChannelEntity>> currentLiveData;
    private Observer<List<ChannelEntity>> currentObserver;
    private LiveData<List<String>>        currentGroupLiveData;
    private Observer<List<String>>        currentGroupObserver;

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

        // Affiche le cache immédiatement
        observeCurrentTab();

        // Télécharge en background
        startBackgroundDownload();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Téléchargement background
    // ─────────────────────────────────────────────────────────────────────────

    private void startBackgroundDownload() {
        Intent intent   = getIntent();
        String login    = intent.getStringExtra(EXTRA_ACT_LOGIN);
        String password = intent.getStringExtra(EXTRA_ACT_PASSWORD);
        String expires  = intent.getStringExtra(EXTRA_ACT_EXPIRES);
        String[] dnsUrls    = intent.getStringArrayExtra(EXTRA_ACT_DNS_URLS);
        String[] dnsEpgUrls = intent.getStringArrayExtra(EXTRA_ACT_DNS_EPG_URLS);

        // ── Chemin 1 : extras frais depuis ActivationActivity ─────────────
        if (dnsUrls != null && dnsUrls.length > 0 && login != null && !login.isEmpty()) {
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            final String fLogin    = login;
            final String fPassword = password != null ? password : "";
            final String[] fUrls   = dnsUrls;
            final String[] fEpgs   = dnsEpgUrls != null ? dnsEpgUrls : new String[0];

            Executors.newSingleThreadExecutor().execute(() ->
                downloadDnsSequentially(fUrls, fEpgs, fLogin, fPassword, 0, 0)
            );
            return;
        }

        // ── Chemin 2 : rotation/retour — refresh stale depuis la BDD ──────
        PlaylistLoader.refreshStaleIfNeeded(db, new PlaylistLoader.Callback() {
            @Override public void onDone(int count) {
                if (count > 0) runOnUiThread(() -> observeCurrentTab());
            }
            @Override public void onError(String msg) {}
        });
    }

    /**
     * Télécharge chaque DNS séquentiellement en arrière-plan.
     * Détecte automatiquement le type : Xtream (login/password) ou M3U URL directe.
     *
     * Exemples gérés :
     *   Xtream base : http://server.com:8080  + login + password → /get.php?username=...
     *   Xtream full : http://server.com/get.php?username=d:user&password=xxx&type=m3u_plus
     *   M3U URL     : https://iptv-org.github.io/iptv/languages/eng.m3u
     */
    private void downloadDnsSequentially(String[] urls, String[] epgs,
                                          String login, String password,
                                          int index, int totalAccumulated) {
        if (index >= urls.length) {
            // Tous les DNS traités
            final int total = totalAccumulated;
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                if (total > 0) {
                    observeCurrentTab();
                    Toast.makeText(this, "✅ " + total + " chaînes chargées",
                        Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        String rawUrl = urls[index].trim();
        if (rawUrl.isEmpty()) {
            downloadDnsSequentially(urls, epgs, login, password, index + 1, totalAccumulated);
            return;
        }

        // ── Détection du type ─────────────────────────────────────────────
        int playlistType = detectPlaylistType(rawUrl, login);

        // ── Créer ou mettre à jour la PlaylistEntity ──────────────────────
        PlaylistEntity pl = db.playlistDao().findByUrlAndLogin(rawUrl, login);
        if (pl == null) pl = new PlaylistEntity();
        pl.name        = urls.length == 1 ? "Abonnement IPTV" : "IPTV #" + (index + 1);
        pl.type        = playlistType;
        pl.isActive    = true;
        pl.lastUpdated = 0; // forcer refresh

        if (playlistType == PlaylistEntity.TYPE_XTREAM && !isFullM3uUrl(rawUrl)) {
            // URL de base Xtream : http://server.com:8080 → PlaylistLoader construira /get.php
            pl.url      = rawUrl;
            pl.username = login;
            pl.password = password;
        } else {
            // URL complète (get.php déjà inclus) ou M3U directe
            pl.url      = rawUrl;
            pl.username = login;
            pl.password = password;
        }

        if (pl.id == 0) {
            pl.id = db.playlistDao().insert(pl);
        } else {
            db.playlistDao().update(pl);
        }

        final PlaylistEntity fPl    = pl;
        final int            fIndex = index;

        PlaylistLoader.load(fPl, db, new PlaylistLoader.Callback() {
            @Override public void onDone(int count) {
                downloadDnsSequentially(urls, epgs, login, password,
                    fIndex + 1, totalAccumulated + count);
            }
            @Override public void onError(String msg) {
                // Erreur sur ce DNS → passer au suivant sans bloquer
                downloadDnsSequentially(urls, epgs, login, password,
                    fIndex + 1, totalAccumulated);
            }
        });
    }

    /**
     * Détecte le type de playlist depuis l'URL et les credentials.
     *
     * Règles :
     *  → TYPE_XTREAM si :
     *    - L'URL contient "get.php" (URL M3U Xtream complète)
     *    - OU l'URL est une base serveur ET login non vide (Xtream credentials)
     *  → TYPE_M3U_URL sinon (URL M3U directe, pas de login nécessaire)
     */
    private int detectPlaylistType(String url, String login) {
        String lower = url.toLowerCase();
        // URL M3U directe évidente
        if (lower.endsWith(".m3u") || lower.endsWith(".m3u8")) {
            return PlaylistEntity.TYPE_M3U_URL;
        }
        // URL Xtream complète avec get.php
        if (lower.contains("get.php")) {
            return PlaylistEntity.TYPE_XTREAM;
        }
        // Base serveur Xtream + credentials fournis
        if (login != null && !login.isEmpty()) {
            return PlaylistEntity.TYPE_XTREAM;
        }
        // Par défaut : M3U URL
        return PlaylistEntity.TYPE_M3U_URL;
    }

    /**
     * Retourne true si l'URL est une URL M3U Xtream complète (avec get.php).
     * Dans ce cas PlaylistLoader.loadXtreamSync ne doit PAS reconstruire l'URL.
     */
    private boolean isFullM3uUrl(String url) {
        return url.toLowerCase().contains("get.php");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Observers LiveData — un seul actif à la fois
    // ─────────────────────────────────────────────────────────────────────────

    private void observeChannels(LiveData<List<ChannelEntity>> liveData) {
        if (currentLiveData != null && currentObserver != null)
            currentLiveData.removeObserver(currentObserver);
        currentObserver = list -> updateList(list);
        currentLiveData = liveData;
        currentLiveData.observe(this, currentObserver);
    }

    private void observeGroups(LiveData<List<String>> liveData) {
        if (currentGroupLiveData != null && currentGroupObserver != null)
            currentGroupLiveData.removeObserver(currentGroupObserver);
        currentGroupObserver = groups -> updateGroupSpinner(groups);
        currentGroupLiveData = liveData;
        currentGroupLiveData.observe(this, currentGroupObserver);
    }

    private void observeCurrentTab() {
        switch (currentTab) {
            case 1: observeGroups(db.channelDao().getLiveGroups());   break;
            case 2: observeGroups(db.channelDao().getFilmGroups());   break;
            case 3: observeGroups(db.channelDao().getSeriesGroups()); break;
            default: updateGroupSpinner(null); break;
        }
        if (currentGroup != null) {
            switch (currentTab) {
                case 1: observeChannels(db.channelDao().getLiveByGroup(currentGroup));   return;
                case 2: observeChannels(db.channelDao().getFilmsByGroup(currentGroup));  return;
                case 3: observeChannels(db.channelDao().getSeriesByGroup(currentGroup)); return;
            }
        }
        switch (currentTab) {
            case 0: observeChannels(db.channelDao().getAll());       break;
            case 1: observeChannels(db.channelDao().getLive());      break;
            case 2: observeChannels(db.channelDao().getFilms());     break;
            case 3: observeChannels(db.channelDao().getSeries());    break;
            case 4: observeChannels(db.channelDao().getFavorites()); break;
        }
    }

    private void updateList(List<ChannelEntity> list) {
        if (adapter != null) adapter.setData(list);
        if (tvEmpty != null)
            tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UI setup
    // ─────────────────────────────────────────────────────────────────────────

    private void setupTabs() {
        String[] tabs = {"Tout", "Live", "Films", "Séries", "Favoris"};
        for (String t : tabs) tabLayout.addTab(tabLayout.newTab().setText(t));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab t) {
                currentTab = t.getPosition(); currentGroup = null;
                observeCurrentTab();
            }
            @Override public void onTabUnselected(TabLayout.Tab t) {}
            @Override public void onTabReselected(TabLayout.Tab t) {}
        });
    }

    private void updateGroupSpinner(List<String> groups) {
        if (spinnerGroup == null) return;
        if (groups == null || groups.isEmpty()) { spinnerGroup.setVisibility(View.GONE); return; }
        spinnerGroup.setVisibility(View.VISIBLE);
        ArrayList<String> items = new ArrayList<>();
        items.add("Tous les groupes");
        items.addAll(groups);
        ArrayAdapter<String> a = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, items);
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

    private void setupSearch() {
        if (searchView == null) return;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { return false; }
            @Override public boolean onQueryTextChange(String q) {
                if (TextUtils.isEmpty(q)) { observeCurrentTab(); return true; }
                observeChannels(db.channelDao().search(q));
                return true;
            }
        });
    }

    private void setupBottomNav() {
        if (bottomNav == null) return;
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if      (id == R.id.nav_home)     { observeCurrentTab(); return true; }
            else if (id == R.id.nav_add)      { showAddPlaylistDialog(); return true; }
            else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class)); return true;
            }
            return false;
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Ajout manuel
    // ─────────────────────────────────────────────────────────────────────────

    public void showAddPlaylistDialog() {
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(this);
        b.setTitle("Ajouter une playlist");
        android.view.View v = getLayoutInflater().inflate(R.layout.dialog_add_playlist, null);
        EditText   etName   = v.findViewById(R.id.et_playlist_name);
        EditText   etUrl    = v.findViewById(R.id.et_playlist_url);
        EditText   etServer = v.findViewById(R.id.et_xtream_server);
        EditText   etUser   = v.findViewById(R.id.et_xtream_user);
        EditText   etPass   = v.findViewById(R.id.et_xtream_pass);
        RadioGroup rgType   = v.findViewById(R.id.rg_type);
        android.view.View layoutXtream = v.findViewById(R.id.layout_xtream);

        rgType.setOnCheckedChangeListener((g, checked) -> {
            boolean isX = checked == R.id.rb_xtream;
            layoutXtream.setVisibility(isX ? View.VISIBLE : View.GONE);
            etUrl.setVisibility(isX ? View.GONE : View.VISIBLE);
        });

        b.setView(v);
        b.setPositiveButton("Charger", (d, w) -> {
            String name = etName.getText().toString().trim();
            int checkedId = rgType.getCheckedRadioButtonId();
            PlaylistEntity pl = new PlaylistEntity();
            pl.name = name.isEmpty() ? "Playlist" : name;
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
                            Toast.makeText(MainActivity.this,
                                count + " chaînes chargées", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override public void onError(String msg) {
                        runOnUiThread(() -> {
                            if (progressBar != null) progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this,
                                "Erreur : " + msg, Toast.LENGTH_LONG).show();
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

    @Override protected void onResume() {
        super.onResume();
        observeCurrentTab();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (currentLiveData != null && currentObserver != null)
            currentLiveData.removeObserver(currentObserver);
        if (currentGroupLiveData != null && currentGroupObserver != null)
            currentGroupLiveData.removeObserver(currentGroupObserver);
    }
}