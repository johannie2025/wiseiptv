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
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.adapter.ChannelAdapter;
import com.wdesign.wiseiptv.mobile.util.PlaylistLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * MainActivity — Adapté et sécurisé pour Mobile.
 * Fix du crash au démarrage lié aux requêtes de base de données sur le Main Thread.
 */
public class MainActivity extends AppCompatActivity implements ChannelAdapter.OnChannelClick {

    public static final String EXTRA_ACT_LOGIN        = "act_login";
    public static final String EXTRA_ACT_PASSWORD     = "act_password";
    public static final String EXTRA_ACT_EXPIRES      = "act_expires";
    public static final String EXTRA_ACT_DNS_URLS     = "act_dns_urls";
    public static final String EXTRA_ACT_DNS_EPG_URLS = "act_dns_epg_urls";

    private static final String TAG   = "MainActivity";
    private static final String PREFS = "wise_activation";

    private RecyclerView        rvChannels;
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

    private LiveData<List<ChannelEntity>> currentLiveData;
    private Observer<List<ChannelEntity>> currentObserver;
    private LiveData<List<String>>        currentGroupLiveData;
    private Observer<List<String>>        currentGroupObserver;

    // Executor unique pour toute la chaîne de synchronisation (évite les deadlocks Room)
    private final ExecutorService syncExecutor = Executors.newSingleThreadExecutor();

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

        observeCurrentTab();
        startBackgroundSync();
    }

    // ── Sync background asynchrone — Sécurisé contre les plantages MainThread ──────

    private void startBackgroundSync() {
        Intent intent = getIntent();
        String login    = intent.getStringExtra(EXTRA_ACT_LOGIN);
        String password = intent.getStringExtra(EXTRA_ACT_PASSWORD);
        String expires  = intent.getStringExtra(EXTRA_ACT_EXPIRES);
        String[] dnsUrls    = intent.getStringArrayExtra(EXTRA_ACT_DNS_URLS);
        String[] dnsEpgUrls = intent.getStringArrayExtra(EXTRA_ACT_DNS_EPG_URLS);

        // Déportation complète des vérifications DB et SharedPreferences en arrière-plan
        syncExecutor.execute(() -> {
            SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            long lastSync   = prefs.getLong("last_weekly_sync_timestamp", 0);
            long oneWeekMs  = 7L * 24 * 60 * 60 * 1000;

            boolean dbIsEmpty = false;
            try { 
                dbIsEmpty = (db.channelDao().count() == 0); 
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors du comptage des chaînes", e);
            }

            // Si la base n'est pas vide et que la synchronisation date de moins d'une semaine, on ignore
            if (!dbIsEmpty && (System.currentTimeMillis() - lastSync < oneWeekMs)) {
                Log.d(TAG, "Sync ignoré : dernière sync < 1 semaine et DB non vide");
                return;
            }

           // Chemin 1 : extras frais
			if (login != null && dnsUrls != null && dnsUrls.length > 0) {
				processNextDns(0, dnsUrls, login, password != null ? password : "", 
						expires != null ? expires : "", 
						dnsEpgUrls != null ? dnsEpgUrls : new String[0], 0);
				return;
}

            // Chemin 2 : depuis le cache SharedPreferences
            String savedStatus = prefs.getString("status",     "UNKNOWN");
            String savedLogin  = prefs.getString("login",      null);
            String savedPass   = prefs.getString("password",   "");
            String savedExp    = prefs.getString("expires_at", "");
            String savedDnsRaw = prefs.getString("dns_urls",   "");

            if ("ACTIVE".equals(savedStatus) && savedLogin != null && !savedDnsRaw.isEmpty()) {
                String[] urls = savedDnsRaw.split(",");
                String[] epgs = prefs.getString("dns_epg_urls", "").split(",");
                processNextDns(0, urls, savedLogin, savedPass, savedExp, epgs, 0);
                return;
            }

            // Chemin 3 : aucun DNS -> refresh playlists manuelles périmées
            PlaylistLoader.refreshStaleIfNeeded(db, new PlaylistLoader.Callback() {
                @Override public void onDone(int count) {
                    if (count > 0) {
                        runOnUiThread(() -> {
                            if (!isFinishing() && !isDestroyed()) observeCurrentTab();
                        });
                    }
                }
                @Override public void onError(String msg) {}
            });
        });
    }

    private void loadDnsSequentially(String login, String password, String expires,
                                     String[] dnsUrls, String[] epgUrls) {
        showSyncStatus();
        syncExecutor.execute(() -> processNextDns(0, dnsUrls, login, password, expires, epgUrls, 0));
    }

  private void processNextDns(int index, String[] dnsUrls, String login, String password,
                            String expires, String[] epgUrls, final int totalAccumulated) {
    
    // === CORRECTION : protection contre tableau null ou index invalide ===
    if (index >= (dnsUrls != null ? dnsUrls.length : 0)) {
        // code de fin...
        return;
    }

    String url = (dnsUrls != null && index < dnsUrls.length && dnsUrls[index] != null) 
                 ? dnsUrls[index].trim() : "";

    if (url.isEmpty()) {
        processNextDns(index + 1, dnsUrls, login, password, expires, epgUrls, totalAccumulated);
        return;
    }

        int idx   = index + 1;
        int total = dnsUrls.length;

        // Appel sécurisé en tâche de fond (déjà sur syncExecutor)
        PlaylistEntity pl = findOrCreatePlaylist(login, password, url, idx);

        runOnUiThread(() -> {
            if (!isFinishing() && !isDestroyed()) {
                showSyncStatus();
                // Si vous avez un TextView de statut, mettez-le à jour ici. Exemple :
                // tvEmpty.setText("📥 Configuration Serveur " + idx + "/" + total + "…");
            }
        });

        PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
            @Override public void onDone(int count) {
                Log.d(TAG, "DNS " + idx + " : " + count + " chaînes");
                syncExecutor.execute(() ->
                        processNextDns(index + 1, dnsUrls, login, password, expires, epgUrls, totalAccumulated + count));
            }
            @Override public void onError(String msg) {
                Log.w(TAG, "DNS " + idx + " erreur : " + msg);
                syncExecutor.execute(() ->
                        processNextDns(index + 1, dnsUrls, login, password, expires, epgUrls, totalAccumulated));
            }
        });
    }

private PlaylistEntity findOrCreatePlaylist(String login, String password, String dnsUrl, int idx) {
    
    // === CORRECTIONS : sécurisation des chaînes null ===
    String safeUrl = dnsUrl != null ? dnsUrl.trim() : "";
    
    PlaylistEntity existing = db.playlistDao().findByUrlAndLogin(safeUrl, 
                                login != null ? login : "");
    
    if (existing != null) {
        existing.password  = password != null ? password : "";
        existing.isActive  = true;
        existing.lastUpdated = 0;
        db.playlistDao().update(existing);
        return existing;
    }

    // ... création de la nouvelle playlist avec protections
    PlaylistEntity pl = new PlaylistEntity();
    pl.name       = "IPTV #" + idx;
    pl.url        = safeUrl;
    pl.isActive   = true;
    pl.lastUpdated = 0;

    if (isXtreamUrl(pl.url)) {
        pl.type     = PlaylistEntity.TYPE_XTREAM;
        pl.username = login != null ? login : "";
        pl.password = password != null ? password : "";
    } else {
        pl.type     = PlaylistEntity.TYPE_M3U_URL;
        pl.username = "";
        pl.password = "";
    }

    pl.id = db.playlistDao().insert(pl);
    return pl;
}

    private boolean isXtreamUrl(String url) {
        if (url == null || url.isEmpty()) return false;
        String lower = url.toLowerCase();
        if (lower.contains("/get.php") || lower.contains("/player_api.php")
                || lower.contains("/apiget.php") || lower.contains("/panel_api.php"))
            return true;
        if (lower.matches(".*https?://.+/[^/]+/[^/]+/(m3u_plus|m3u|ts).*"))
            return true;
        if (lower.contains("username=") && lower.contains("password="))
            return true;
        return false;
    }

    // ── Sync manuelle ─────────────────────────────────────────────

    private void syncNow() {
        showSyncStatus();
        syncExecutor.execute(() -> {
            SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            String savedDnsRaw = prefs.getString("dns_urls", "");
            String savedLogin  = prefs.getString("login",    null);
            String savedPass   = prefs.getString("password", "");
            String savedExp    = prefs.getString("expires_at", "");

            if (savedLogin != null && !savedDnsRaw.isEmpty()) {
                String[] urls = savedDnsRaw.split(",");
                String[] epgs = prefs.getString("dns_epg_urls", "").split(",");
                prefs.edit().putLong("last_weekly_sync_timestamp", 0).apply();
                processNextDns(0, urls, savedLogin, savedPass, savedExp, epgs, 0);
            } else {
                PlaylistLoader.refreshStaleIfNeeded(db, new PlaylistLoader.Callback() {
                    @Override public void onDone(int c) {
                        runOnUiThread(() -> {
                            hideSyncStatus();
                            if (!isFinishing() && !isDestroyed()) {
                                observeCurrentTab();
                                Toast.makeText(MainActivity.this, "✅ " + c + " chaînes", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override public void onError(String msg) {
                        runOnUiThread(() -> {
                            hideSyncStatus();
                            if (!isFinishing() && !isDestroyed()) {
                                Toast.makeText(MainActivity.this, "Aucune mise à jour disponible", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    // ── Statut sync ───────────────────────────────────────────────

    private void showSyncStatus() {
        runOnUiThread(() -> {
            if (progressBar != null && !isFinishing() && !isDestroyed()) progressBar.setVisibility(View.VISIBLE);
        });
    }

    private void hideSyncStatus() {
        runOnUiThread(() -> {
            if (progressBar != null && !isFinishing() && !isDestroyed()) progressBar.setVisibility(View.GONE);
        });
    }

    // ── Observers LiveData — Nettoyage propre et ciblé ──────────────

    private void observeChannels(LiveData<List<ChannelEntity>> liveData) {
        if (currentLiveData != null && currentObserver != null)
            currentLiveData.removeObserver(currentObserver);
        currentObserver = this::updateList;
        currentLiveData = liveData;
        currentLiveData.observe(this, currentObserver);
    }

    private void observeGroups(LiveData<List<String>> liveData) {
        if (currentGroupLiveData != null && currentGroupObserver != null)
            currentGroupLiveData.removeObserver(currentGroupObserver);
        currentGroupObserver = this::updateGroupSpinner;
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

    // ── UI setup ──────────────────────────────────────────────────

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
            else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class)); return true;
            }
            return false;
        });
    }

    // ── Ajout manuel de playlist ──────────────────────────────────

    public void showAddPlaylistDialog() {
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(this);
        b.setTitle("Ajouter une playlist");
        View v = getLayoutInflater().inflate(R.layout.dialog_add_playlist, null);
        EditText   etName   = v.findViewById(R.id.et_playlist_name);
        EditText   etUrl    = v.findViewById(R.id.et_playlist_url);
        EditText   etServer = v.findViewById(R.id.et_xtream_server);
        EditText   etUser   = v.findViewById(R.id.et_xtream_user);
        EditText   etPass   = v.findViewById(R.id.et_xtream_pass);
        RadioGroup rgType   = v.findViewById(R.id.rg_type);
        View layoutXtream   = v.findViewById(R.id.layout_xtream);

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
            pl.lastUpdated = 0; pl.isActive = true;

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

            showSyncStatus();
            // Insertion asynchrone pour l'ajout manuel
            syncExecutor.execute(() -> {
                pl.id = db.playlistDao().insert(pl);
                PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                    @Override public void onDone(int count) {
                        runOnUiThread(() -> {
                            hideSyncStatus();
                            if (!isFinishing() && !isDestroyed()) {
                                observeCurrentTab();
                                Toast.makeText(MainActivity.this, count + " chaînes chargées", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override public void onError(String msg) {
                        runOnUiThread(() -> {
                            hideSyncStatus();
                            if (!isFinishing() && !isDestroyed()) {
                                Toast.makeText(MainActivity.this, "Erreur : " + msg, Toast.LENGTH_LONG).show();
                            }
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
        syncExecutor.shutdown();
    }
}