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
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.adapter.ChannelAdapter;
import com.wdesign.wiseiptv.mobile.util.ActivationManager;
import com.wdesign.wiseiptv.mobile.util.PlaylistLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ChannelAdapter.OnChannelClick {

    // Extras reçus depuis ActivationActivity
    public static final String EXTRA_ACT_LOGIN        = "act_login";
    public static final String EXTRA_ACT_PASSWORD     = "act_password";
    public static final String EXTRA_ACT_EXPIRES      = "act_expires";
    public static final String EXTRA_ACT_DNS_URLS     = "act_dns_urls";
    public static final String EXTRA_ACT_DNS_EPG_URLS = "act_dns_epg_urls";

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

    // FIX : un seul observer LiveData actif à la fois
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

        // ÉTAPE 1 : Traitement de la synchronisation d'activation ou vérification du cache local
        startBackgroundSync();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GESTION DU CHARGEMENT DES DONNÉES ET ACTIVATION
    // ─────────────────────────────────────────────────────────────────────────

    private void startBackgroundSync() {
        Intent intent = getIntent();
        String login    = intent.getStringExtra(EXTRA_ACT_LOGIN);
        String password = intent.getStringExtra(EXTRA_ACT_PASSWORD);
        String expires  = intent.getStringExtra(EXTRA_ACT_EXPIRES);
        String[] dnsUrls    = intent.getStringArrayExtra(EXTRA_ACT_DNS_URLS);
        String[] dnsEpgUrls = intent.getStringArrayExtra(EXTRA_ACT_DNS_EPG_URLS);

        // Si on vient de cliquer sur "Accéder au contenu" dans ActivationActivity, on reçoit ces extras
        if (login != null && dnsUrls != null && dnsUrls.length > 0) {
            List<DeviceSecurity.DnsEntry> entries = new ArrayList<>();
            for (int i = 0; i < dnsUrls.length; i++) {
                String epg = (dnsEpgUrls != null && i < dnsEpgUrls.length) ? dnsEpgUrls[i] : "";
                entries.add(new DeviceSecurity.DnsEntry(dnsUrls[i], epg, i));
            }
            DeviceSecurity.ActivationResult result = new DeviceSecurity.ActivationResult(
                    DeviceSecurity.getOrCreateKey(this), login, password != null ? password : "",
                    expires != null ? expires : "", entries);
            
            launchDownload(result);
            return;
        }

        // Sinon, c'est un démarrage normal de l'application (Vérification classique de la base de données)
        Executors.newSingleThreadExecutor().execute(() -> {
            List<PlaylistEntity> all = db.playlistDao().getAllSync();
            runOnUiThread(() -> {
                if (all.isEmpty()) {
                    // FIX CRASH : L'affichage du Dialog doit impérativement se faire sur l'UI Thread
                    showAddPlaylistDialog();
                } else {
                    // Des playlists existent → afficher le cache immédiatement
                    observeCurrentTab();
                    // Rafraîchir en background de manière transparente si obsolète (> 7 jours)
                    PlaylistLoader.refreshStaleIfNeeded(db, new PlaylistLoader.Callback() {
                        @Override public void onDone(int count) {
                            if (count > 0) runOnUiThread(() -> observeCurrentTab());
                        }
                        @Override public void onError(String msg) {}
                    });
                }
            });
        });
    }

    private void launchDownload(DeviceSecurity.ActivationResult result) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            // Sauvegarde les SharedPreferences et injecte / charge la playlist d'activation via PlaylistLoader
            ActivationManager.checkAndSync(getApplicationContext(), new ActivationManager.OnResult() {
                @Override
                public void onActivated(DeviceSecurity.ActivationResult r) {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        observeCurrentTab(); // Se déclenche dès que les chaînes commencent à arriver
                        Toast.makeText(MainActivity.this, "Contenu activé avec succès !", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onExpired(String status) {
                    runOnUiThread(() -> {
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Compte expiré ou désactivé", Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onError(String msg) {
                    runOnUiThread(() -> {
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Erreur réseau: " + msg, Toast.LENGTH_LONG).show();
                    });
                }
            });
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GESTION SÉCURISÉE DES OBSERVERS (Un seul observer actif à la fois)
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
                case 1: observeChannels(db.channelDao().getLiveByGroup(currentGroup));  return;
                case 2: observeChannels(db.channelDao().getFilmsByGroup(currentGroup)); return;
                case 3: observeChannels(db.channelDao().getSeriesByGroup(currentGroup));return;
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
    // COMPOSANTS COMPORTEMENTAUX (TABS / SEARCH / BOTTOMNAV / SPINNER)
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
            else if (id == R.id.nav_settings) { startActivity(new Intent(this, SettingsActivity.class)); return true; }
            return false;
        });
    }

    public void showAddPlaylistDialog() {
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(this);
        b.setTitle("Ajouter une playlist");
        android.view.View v = getLayoutInflater().inflate(R.layout.dialog_add_playlist, null);
        EditText etName    = v.findViewById(R.id.et_playlist_name);
        EditText etUrl     = v.findViewById(R.id.et_playlist_url);
        EditText etServer  = v.findViewById(R.id.et_xtream_server);
        EditText etUser    = v.findViewById(R.id.et_xtream_user);
        EditText etPass    = v.findViewById(R.id.et_xtream_pass);
        RadioGroup rgType  = v.findViewById(R.id.rg_type);
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