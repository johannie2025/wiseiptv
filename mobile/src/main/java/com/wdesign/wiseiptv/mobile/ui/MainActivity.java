package com.wdesign.wiseiptv.mobile.ui;

import android.content.*;
import android.os.*;
import android.text.TextUtils;
import android.util.Log;
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

    // Un seul observer LiveData actif à la fois (fix crash accumulation)
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

        // Affiche le cache local immédiatement
        observeCurrentTab();

        // Téléchargement en arrière-plan (non bloquant)
        startBackgroundSync();
    }

    // ── Observer unique ──────────────────────────────────────────────────────

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

  

// ── Téléchargement Arrière-plan géré par MainActivity (Style Android TV) ──

private void startBackgroundSync() {
    Intent intent = getIntent();
    if (intent == null) return;

    String login    = intent.getStringExtra(EXTRA_ACT_LOGIN);
    String password = intent.getStringExtra(EXTRA_ACT_PASSWORD);
    String expires  = intent.getStringExtra(EXTRA_ACT_EXPIRES);
    String[] dnsUrls    = intent.getStringArrayExtra(EXTRA_ACT_DNS_URLS);
    String[] dnsEpgUrls = intent.getStringArrayExtra(EXTRA_ACT_DNS_EPG_URLS);

    // CHEMIN 1 : Arrivée fraîche depuis l'activation
    if (login != null && dnsUrls != null && dnsUrls.length > 0) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        
        List<DeviceSecurity.DnsEntry> entries = new ArrayList<>();
        for (int i = 0; i < dnsUrls.length; i++) {
            if (dnsUrls[i] == null || dnsUrls[i].isEmpty()) continue;
            String epg = (dnsEpgUrls != null && i < dnsEpgUrls.length && dnsEpgUrls[i] != null) ? dnsEpgUrls[i] : "";
            entries.add(new DeviceSecurity.DnsEntry(dnsUrls[i], epg, i));
        }

        DeviceSecurity.ActivationResult result = new DeviceSecurity.ActivationResult(
                DeviceSecurity.getOrCreateKey(this),
                login, 
                password != null ? password : "",
                expires != null ? expires : "", 
                entries
        );

        // Étape 1 : Enregistrement en BDD en arrière-plan
        ActivationManager.upsertAndDownloadAll(getApplicationContext(), db, result, new ActivationManager.DownloadCallback() {
            @Override 
            public void onProgress(String playlistName) {
                // Optionnel : afficher quelle playlist est créée
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed() && tvEmpty != null) {
                        tvEmpty.setText("Configuring: " + playlistName);
                    }
                });
            }

            @Override
            public void onDone(int totalChannels) {
                // Étape 2 : Profils créés avec succès, on lance le téléchargement séquentiel des flux
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (tvEmpty != null) tvEmpty.setText("");
                    loadPlaylistsSequentially(0);
                });
            }

            @Override
            public void onError(String msg) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    if (tvEmpty != null) tvEmpty.setText("");
                    Toast.makeText(MainActivity.this, "Erreur de configuration: " + msg, Toast.LENGTH_LONG).show();
                    observeCurrentTab(); // On essaie quand même de charger le cache au cas où
                });
            }
        });
        return;
    }

    // CHEMIN 2 : Ouverture directe via le cache existant
    SharedPreferences prefs = getSharedPreferences("wise_activation", Context.MODE_PRIVATE);
    String savedStatus = prefs.getString("status", "UNKNOWN");
    
    if ("ACTIVE".equals(savedStatus)) {
        PlaylistLoader.refreshStaleIfNeeded(db, new PlaylistLoader.Callback() {
            @Override
            public void onDone(int count) {
                if (count > 0 && !isFinishing() && !isDestroyed()) {
                    runOnUiThread(() -> observeCurrentTab());
                }
            }
            @Override 
            public void onError(String msg) { 
                Log.w("MainActivity", "Refresh caché échoué: " + msg); 
            }
        });
    }
}

    /** Télécharge les playlists enregistrées les unes après les autres sans bloquer l'interface */
    private void loadPlaylistsSequentially(final int index) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<PlaylistEntity> playlists = db.playlistDao().getAllSync();
            if (playlists == null || index >= playlists.size()) {
                // Fin de la chaîne de téléchargement
                runOnUiThread(() -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    observeCurrentTab(); // Rafraîchit l'UI immédiatement avec les nouvelles chaînes !
                    Toast.makeText(MainActivity.this, "✅ Synchronisation du contenu terminée", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            PlaylistEntity pl = playlists.get(index);
            // On utilise le chargeur universel (qui gère automatiquement XTREAM ou M3U_URL)
            PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                @Override
                public void onDone(int count) {
                    // Passage à la playlist suivante
                    loadPlaylistsSequentially(index + 1);
                }

                @Override
                public void onError(String msg) {
                    // Même s'il y a une erreur sur une ligne, on continue sur la suivante
                    loadPlaylistsSequentially(index + 1);
                }
            });
        });
    }

    private void launchDownload(DeviceSecurity.ActivationResult result) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        ActivationManager.upsertAndDownloadAll(
            getApplicationContext(), db, result,
            new ActivationManager.DownloadCallback() {
                @Override public void onProgress(String playlistName) {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        // Feedback discret — pas de blocage UI
                        if (tvEmpty != null) tvEmpty.setText("📥 " + playlistName + "…");
                    });
                }
                @Override public void onDone(int totalChannels) {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        if (tvEmpty != null) tvEmpty.setText("");
                        observeCurrentTab();
                        if (totalChannels > 0)
                            Toast.makeText(MainActivity.this,
                                "✅ " + totalChannels + " chaînes chargées",
                                Toast.LENGTH_SHORT).show();
                    });
                }
                @Override public void onError(String msg) {
                    // Erreur non bloquante : UI reste accessible avec le cache
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        if (tvEmpty != null) tvEmpty.setText("");
                        observeCurrentTab();
                        Log.w("MainActivity", "Download non-fatal: " + msg);
                    });
                }
            });
    }

    // ── Tabs / Search / BottomNav / Spinner ──────────────────────────────────

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

    // ── Ajout manuel de playlist ─────────────────────────────────────────────

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
