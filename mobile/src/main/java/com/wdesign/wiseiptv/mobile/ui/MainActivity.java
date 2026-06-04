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

// ─────────────────────────────────────────────────────────────────────────
    // SYNC BACKGROUND — Même logique que TvMainActivity (processNextDns)
    //
    // 3 chemins (exactement comme TV) :
    //   1. Extras depuis ActivationActivity (login + dnsUrls frais)
    //   2. SharedPreferences "wise_activation" (reboot / rotation)
    //   3. Playlists manuelles stale
    //
    // Chaque DNS → findOrCreatePlaylist() → PlaylistLoader.load() séquentiel.
    // L'UI affiche les chaînes dès qu'elles arrivent via observeCurrentTab().
    // ─────────────────────────────────────────────────────────────────────────
    private void startBackgroundSync() {
        Intent intent = getIntent();
        if (intent == null) { tryStaleRefresh(); return; }

        String login    = intent.getStringExtra(EXTRA_ACT_LOGIN);
        String password = intent.getStringExtra(EXTRA_ACT_PASSWORD);
        String[] dnsUrls    = intent.getStringArrayExtra(EXTRA_ACT_DNS_URLS);
        String[] dnsEpgUrls = intent.getStringArrayExtra(EXTRA_ACT_DNS_EPG_URLS);

        // Chemin 1 : extras frais depuis ActivationActivity
        if (login != null && dnsUrls != null && dnsUrls.length > 0) {
            Log.d("MainActivity", "Sync ch1: extras DNS=" + dnsUrls.length);
            processNextDns(0, dnsUrls, login,
                password != null ? password : "",
                dnsEpgUrls != null ? dnsEpgUrls : new String[0], 0);
            return;
        }

        // Chemin 2 : prefs sauvegardées par ActivationActivity
        SharedPreferences prefs = getSharedPreferences("wise_activation", Context.MODE_PRIVATE);
        String savedStatus = prefs.getString("status", "UNKNOWN");
        String savedLogin  = prefs.getString("login",  null);
        String savedPass   = prefs.getString("password", "");
        String savedDnsRaw = prefs.getString("dns_urls", "");

        if ("ACTIVE".equals(savedStatus) && savedLogin != null && !savedDnsRaw.isEmpty()) {
            Log.d("MainActivity", "Sync ch2: prefs dns=" + savedDnsRaw);
            String[] urls = savedDnsRaw.split(",");
            String[] epgs = prefs.getString("dns_epg_urls", "").split(",");
            processNextDns(0, urls, savedLogin, savedPass, epgs, 0);
            return;
        }

        // Chemin 3 : playlists manuelles stale
        Log.d("MainActivity", "Sync ch3: stale");
        tryStaleRefresh();
    }

    /**
     * Traitement séquentiel des DNS — identique à TvMainActivity.processNextDns().
     * Chaque DNS → PlaylistLoader.load() (auto-détecte Xtream ou M3U URL).
     * Passe au suivant qu'il y ait succès ou erreur.
     */
    private void processNextDns(int index, String[] dnsUrls, String login,
                                 String password, String[] epgUrls,
                                 int accumulated) {
        if (index >= dnsUrls.length) {
            // Tous les DNS traités
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                showSyncBanner(false, null);
                observeCurrentTab();
                if (accumulated > 0)
                    Toast.makeText(this, "✅ " + accumulated + " chaînes chargées",
                        Toast.LENGTH_SHORT).show();
                else {
                    // Aucune chaîne → proposer ajout manuel si DB vide
                    Executors.newSingleThreadExecutor().execute(() -> {
                        if (db.channelDao().count() == 0)
                            runOnUiThread(this::showAddPlaylistDialog);
                    });
                }
                // Sauvegarder timestamp pour éviter re-sync inutile
                getSharedPreferences("wise_activation", Context.MODE_PRIVATE)
                    .edit().putLong("last_sync_ts", System.currentTimeMillis()).apply();
            });
            return;
        }

        String url = dnsUrls[index].trim();
        if (url.isEmpty()) {
            processNextDns(index + 1, dnsUrls, login, password, epgUrls, accumulated);
            return;
        }

        final int idx   = index + 1;
        final int total = dnsUrls.length;
        runOnUiThread(() -> showSyncBanner(true, "📥 Serveur " + idx + "/" + total + "…"));

        // findOrCreatePlaylist accède à Room, on l'exécute sur un thread secondaire
        Executors.newSingleThreadExecutor().execute(() -> {
            PlaylistEntity pl = findOrCreatePlaylist(login, password, url, idx);
            
            // Lancement du chargement de la playlist (PlaylistLoader gère son propre thread)
            PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                @Override 
                public void onDone(int count) {
                    Log.d("MainActivity", "DNS " + idx + " OK: " + count + " ch");
                    processNextDns(index + 1, dnsUrls, login, password, epgUrls, accumulated + count);
                }

                @Override 
                public void onError(String msg) {
                    Log.w("MainActivity", "DNS " + idx + " err: " + msg);
                    processNextDns(index + 1, dnsUrls, login, password, epgUrls, accumulated);
                }
            });
        });
    }

    /** Retrouve ou crée la PlaylistEntity pour un DNS donné (auto-détect Xtream/M3U). */
    private PlaylistEntity findOrCreatePlaylist(String login, String password,
                                                 String dnsUrl, int idx) {
        // Chercher par URL + login existant en DB
        PlaylistEntity existing = db.playlistDao().findByUrlAndLogin(dnsUrl, login);
        if (existing != null) {
            existing.password = password;
            existing.isActive = true;
            db.playlistDao().update(existing);
            return existing;
        }

        PlaylistEntity pl = new PlaylistEntity();
        // Auto-détection : Xtream si pas d'extension .m3u/.m3u8, sinon URL M3U
        String urlLower = dnsUrl.toLowerCase();
        if (urlLower.endsWith(".m3u") || urlLower.endsWith(".m3u8")
                || urlLower.contains("m3u") && !urlLower.contains("get.php")
                   && !urlLower.contains("username=")) {
            pl.type = PlaylistEntity.TYPE_M3U_URL;
            pl.name = "M3U #" + idx;
        } else {
            pl.type = PlaylistEntity.TYPE_XTREAM;
            pl.name = "IPTV #" + idx;
        }

        pl.url      = dnsUrl;
        pl.username = login;
        pl.password = password;
        pl.isActive = true;
        pl.lastUpdated = 0;
        pl.id = db.playlistDao().insert(pl);
        return pl;
    }

    private void tryStaleRefresh() {
        PlaylistLoader.refreshStaleIfNeeded(db, new PlaylistLoader.Callback() {
            @Override public void onDone(int c) {
                if (c > 0 && !isFinishing() && !isDestroyed())
                    runOnUiThread(() -> observeCurrentTab());
            }
            @Override public void onError(String msg) {
                Log.w("MainActivity", "Stale refresh: " + msg);
            }
        });
    }

    /** Bandeau de statut discret pendant le chargement (non bloquant). */
    private void showSyncBanner(boolean show, String msg) {
        if (tvEmpty == null) return;
        if (show) {
            tvEmpty.setText(msg);
            tvEmpty.setVisibility(View.VISIBLE);
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            if (progressBar != null) progressBar.setVisibility(View.GONE);
        }
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
