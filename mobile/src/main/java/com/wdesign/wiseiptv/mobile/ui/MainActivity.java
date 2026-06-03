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

/**
 * MainActivity — Corrigée pour éliminer les 16 erreurs de compilation Javac.
 */
public class MainActivity extends AppCompatActivity implements ChannelAdapter.OnChannelClick {

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

    // FIX INT TYPE : Utilisation d'un type primitif entier pour correspondre à ChannelEntity
    private int currentContentType = ChannelEntity.TYPE_LIVE;

    // Un seul observer actif à la fois pour éviter les ANR / Fuites mémoire
    private LiveData<List<ChannelEntity>>  currentLiveData;
    private Observer<List<ChannelEntity>>  currentObserver;
    private LiveData<List<String>>          currentGroupLiveData;
    private Observer<List<String>>          currentGroupObserver;

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

        // Affichage immédiat du cache local
        observeCurrentTab();

        // Synchronisation en tâche de fond
        startBackgroundSync();
    }

    private void observeChannels(LiveData<List<ChannelEntity>> liveData) {
        if (currentLiveData != null && currentObserver != null) {
            currentLiveData.removeObserver(currentObserver);
        }
        currentObserver = list -> updateList(list);
        currentLiveData = liveData;
        currentLiveData.observe(this, currentObserver);
    }

    private void observeGroups(LiveData<List<String>> liveData) {
        if (currentGroupLiveData != null && currentGroupObserver != null) {
            currentGroupLiveData.removeObserver(currentGroupObserver);
        }
        currentGroupObserver = groups -> updateGroupSpinner(groups);
        currentGroupLiveData = liveData;
        currentGroupLiveData.observe(this, currentGroupObserver);
    }

    private void observeCurrentTab() {
        // Sélection du type de contenu selon l'onglet actuel
        switch (currentTab) {
            case 1: currentContentType = ChannelEntity.TYPE_LIVE; break;
            case 2: currentContentType = ChannelEntity.TYPE_VOD; break;
            case 3: currentContentType = ChannelEntity.TYPE_SERIES; break;
            default: currentContentType = ChannelEntity.TYPE_LIVE; break;
        }

        // Chargement des groupes filtrés par le type de contenu
        switch (currentTab) {
            case 1: case 2: case 3:
                observeGroups(db.channelDao().getGroups(currentContentType));
                break;
            default:
                updateGroupSpinner(null);
                break;
        }

        // Filtrage des chaînes par groupe sélectionné ou par type global
        if (currentGroup != null) {
            if (currentTab >= 1 && currentTab <= 3) {
                observeChannels(db.channelDao().getChannelsByGroup(currentContentType, currentGroup));
                return;
            }
        }

        switch (currentTab) {
            case 0: observeChannels(db.channelDao().getAllChannels()); break;
            case 1: case 2: case 3:
                observeChannels(db.channelDao().getChannels(currentContentType));
                break;
            case 4: observeChannels(db.channelDao().getFavorites()); break;
        }
    }

    private void updateList(List<ChannelEntity> list) {
        if (adapter != null) adapter.setData(list);
        if (tvEmpty != null)
            tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void startBackgroundSync() {
        Intent intent = getIntent();
        String login    = intent.getStringExtra(EXTRA_ACT_LOGIN);
        String password = intent.getStringExtra(EXTRA_ACT_PASSWORD);
        String expires  = intent.getStringExtra(EXTRA_ACT_EXPIRES);
        String[] dnsUrls    = intent.getStringArrayExtra(EXTRA_ACT_DNS_URLS);
        String[] dnsEpgUrls = intent.getStringArrayExtra(EXTRA_ACT_DNS_EPG_URLS);

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

        SharedPreferences prefs = getSharedPreferences("wise_activation", Context.MODE_PRIVATE);
        String savedStatus  = prefs.getString("status", "UNKNOWN");
        String savedLogin   = prefs.getString("login", null);
        String savedPass    = prefs.getString("password", "");
        String savedExpires = prefs.getString("expires_at", "");

        if ("ACTIVE".equals(savedStatus) && savedLogin != null) {
            final String fLogin = savedLogin, fPass = savedPass, fExpires = savedExpires;
            Executors.newSingleThreadExecutor().execute(() -> {
                List<PlaylistEntity> all = db.playlistDao().getAllSync();
                List<DeviceSecurity.DnsEntry> entries = new ArrayList<>();
                boolean anyStale = false;
                for (PlaylistEntity pl : all) {
                    if (pl.isActive && pl.type == PlaylistEntity.TYPE_XTREAM) {
                        entries.add(new DeviceSecurity.DnsEntry(pl.url, "", entries.size()));
                        if (PlaylistLoader.needsRefresh(pl)) anyStale = true;
                    }
                }
                if (!entries.isEmpty() && anyStale) {
                    DeviceSecurity.ActivationResult result = new DeviceSecurity.ActivationResult(
                            DeviceSecurity.getOrCreateKey(this), fLogin, fPass, fExpires, entries);
                    runOnUiThread(() -> launchDownload(result));
                }
            });
            return;
        }

        PlaylistLoader.refreshStaleIfNeeded(db, null);
    }

    private void launchDownload(DeviceSecurity.ActivationResult result) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        ActivationManager.downloadAllPlaylistsAsync(
            getApplicationContext(), db, result,
            new ActivationManager.OnDownloadCallback() {
                @Override public void onSuccess() {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        observeCurrentTab();
                        Toast.makeText(MainActivity.this, "✅ Chaînes synchronisées !", Toast.LENGTH_SHORT).show();
                    });
                }
                @Override public void onFailure(String msg) {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                    });
                }
            });
    }

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
        
        // FIX IDs : Utilisation des identifiants exacts de dialog_add_playlist.xml
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
        if (currentLiveData != null && currentObserver != null) {
            currentLiveData.removeObserver(currentObserver);
        }
        if (currentGroupLiveData != null && currentGroupObserver != null) {
            currentGroupLiveData.removeObserver(currentGroupObserver);
        }
    }
}