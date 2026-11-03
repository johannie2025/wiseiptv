package com.wdesign.wiseiptv.mobile.ui;

import android.content.*;
import android.os.*;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.*;
import com.wdesign.wiseiptv.core.util.AdaptiveBg;
import com.wdesign.wiseiptv.core.util.AppSettings;
import com.wdesign.wiseiptv.mobile.util.ParentalControl;
import com.wdesign.wiseiptv.mobile.util.ResumeStore;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.PlayerView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.player.PlayerEngine;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.core.util.SubscriptionInfo;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.adapter.ChannelAdapter;
import com.wdesign.wiseiptv.mobile.util.PlaylistLoader;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * MainActivity — s'ouvre DIRECTEMENT depuis ActivationActivity.
 * Le téléchargement des playlists se fait ici, en background, DNS par DNS.
 *
 * Flux identique à TvMainActivity :
 *  1. Lit les extras passés par ActivationActivity (login + dnsUrls)
 *  2. Sinon lit les SharedPreferences "wise_activation" (reboot / rotation)
 *  3. Sinon refresh stale des playlists manuelles
 *  4. processNextDns() : séquentiel, auto-détect Xtream vs M3U URL
 */
@OptIn(markerClass = UnstableApi.class)
public class MainActivity extends AppCompatActivity implements ChannelAdapter.OnChannelClick {

    // Extras reçus depuis ActivationActivity
    public static final String EXTRA_LOGIN    = "act_login";
    public static final String EXTRA_PASSWORD = "act_password";
    public static final String EXTRA_EXPIRES  = "act_expires";
    public static final String EXTRA_DNS_URLS = "act_dns_urls";
    public static final String EXTRA_DNS_EPGS = "act_dns_epg_urls";

    // Alias attendus par ActivationActivity
    public static final String EXTRA_ACT_LOGIN       = EXTRA_LOGIN;
    public static final String EXTRA_ACT_PASSWORD    = EXTRA_PASSWORD;
    public static final String EXTRA_ACT_EXPIRES     = EXTRA_EXPIRES;
    public static final String EXTRA_ACT_DNS_URLS    = EXTRA_DNS_URLS;
    public static final String EXTRA_ACT_DNS_EPG_URLS = EXTRA_DNS_EPGS;

    // Navigation / actions depuis les autres écrans (Paramètres, barre du bas)
    public static final String EXTRA_NAV        = "nav_target";
    public static final String EXTRA_OPEN_ADD   = "open_add_playlist";
    public static final String EXTRA_FORCE_SYNC = "force_sync";

    private static final String TAG   = "MainActivity";
    private static final String PREFS = "wise_activation";

    private RecyclerView          rvChannels;
    private ChannelAdapter        adapter;
    private ProgressBar           progressBar;
    private TextView              tvEmpty, tvSyncStatus;
    private TabLayout             tabLayout;
    private BottomNavigationView  bottomNav;
    private SearchView            searchView;
    private ImageButton            btnGrid;
    private GridLayoutManager      gridLayoutManager;
    private View                   homePanel;
    private TextView               heroTitle, heroSub, heroBadge;
    private ImageView              heroLogo;
    private PlayerView             heroPreview;
    private PlayerEngine           heroEngine;
    private long                   heroPlayingId = -1;
    private LinearLayout           heroDots;
    private RecyclerView           rvHomeSlider;
    private ChannelAdapter         sliderAdapter;
    private View                   cardSubscription;
    private TextView               tvGreeting, tvSubTitle, tvSubExpiry, tvSubDays, tvAvatar;
    private List<ChannelEntity>    homeChannels = new java.util.ArrayList<>();
    private int                    heroIndex = 0;
    private boolean                showingHome = true;
    private LiveData<List<ChannelEntity>> activeSource;
    private final Observer<List<ChannelEntity>> listObserver = this::updateList;
    private static final String    PREFS_RECENTS = "wise_mobile_recents";
    private static final int       MAX_RECENTS = 20;
    private static final int       HERO_COUNT = 5;
    private AppDatabase           db;
    private int                   currentTab = 0;

    // Executor unique pour toute la chaîne processNextDns — évite conflits Room multi-thread
    private final java.util.concurrent.ExecutorService syncExecutor =
        java.util.concurrent.Executors.newSingleThreadExecutor();
    private final Handler heroHandler = new Handler(Looper.getMainLooper());
    private final Runnable heroRotate = new Runnable() {
        @Override public void run() {
            if (!showingHome || homeChannels.size() < 2) return;
            heroIndex = (heroIndex + 1) % Math.min(HERO_COUNT, homeChannels.size());
            bindHero();
            heroHandler.postDelayed(this, 12000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db           = AppDatabase.get(this);
        rvChannels   = findViewById(R.id.rv_channels);
        progressBar  = findViewById(R.id.progress_bar);
        tvEmpty      = findViewById(R.id.tv_empty);
        tvSyncStatus = findViewById(R.id.tv_sync_status);
        tabLayout    = findViewById(R.id.tab_layout);
        bottomNav    = findViewById(R.id.bottom_nav);
        searchView   = findViewById(R.id.search_view);
        btnGrid      = findViewById(R.id.btn_grid);
        homePanel    = findViewById(R.id.home_panel);
        heroTitle    = findViewById(R.id.hero_title);
        heroSub      = findViewById(R.id.hero_sub);
        heroBadge    = findViewById(R.id.hero_badge);
        heroLogo     = findViewById(R.id.hero_logo);
        heroPreview  = findViewById(R.id.hero_preview);
        heroDots     = findViewById(R.id.hero_dots);
        rvHomeSlider = findViewById(R.id.rv_home_slider);
        cardSubscription  = findViewById(R.id.card_subscription);
        tvGreeting        = findViewById(R.id.tv_greeting);
        tvSubTitle        = findViewById(R.id.tv_sub_title);
        tvSubExpiry       = findViewById(R.id.tv_sub_expiry);
        tvSubDays         = findViewById(R.id.tv_sub_days);
        tvAvatar          = findViewById(R.id.tv_avatar);
        tvAvatar.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        cardSubscription.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        Fx.pressable(cardSubscription);

        adapter = new ChannelAdapter(this);
        gridLayoutManager = new GridLayoutManager(this, AppSettings.gridColumns(this));
        rvChannels.setLayoutManager(gridLayoutManager);
        rvChannels.setAdapter(adapter);
        applyLayoutMode();

        // Slide horizontal des chaînes (accueil)
        sliderAdapter = new ChannelAdapter(this::selectHero, R.layout.item_channel_h);
        rvHomeSlider.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvHomeSlider.setAdapter(sliderAdapter);
        findViewById(R.id.btn_watch).setOnClickListener(v -> {
            if (!homeChannels.isEmpty()) onClick(homeChannels.get(Math.min(heroIndex, homeChannels.size() - 1)));
        });
        Fx.pressable(findViewById(R.id.btn_watch));
        findViewById(R.id.btn_see_all).setOnClickListener(v -> openTab(1));

        setupGridChooser();
        setupTabs();
        setupSearch();
        setupBottomNav();
        startBackgroundSync();
        if (savedInstanceState == null) restoreSession(getIntent());
        handleExtras(getIntent());
        Fx.stagger(40, 70, findViewById(R.id.header_row), cardSubscription, searchView, tabLayout);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleExtras(intent);
    }

    /** Reprise à l'ouverture : onglet, puis écran (lecteur / compte / paramètres / à propos) laissés ouverts. */
    private void restoreSession(Intent in) {
        if (in == null || in.hasExtra(EXTRA_NAV) || in.getBooleanExtra(EXTRA_OPEN_ADD, false)
            || in.getBooleanExtra(EXTRA_FORCE_SYNC, false)) return;
        int tab = ResumeStore.tab(this);
        if (tab >= 0 && tab <= 4) openTab(tab);
        String screen = ResumeStore.screen(this);
        if (ResumeStore.SCREEN_PLAYER.equals(screen)) {
            final long id = ResumeStore.channelId(this);
            if (id < 0) return;
            syncExecutor.execute(() -> {
                ChannelEntity ch = db.channelDao().findById(id);
                if (ch == null || ParentalControl.isRestricted(this, ch)) return;
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    Intent i = playerIntent(ch);
                    long pos = ResumeStore.position(this);
                    if (pos > 0 && ch.contentType != ChannelEntity.TYPE_LIVE)
                        i.putExtra(PlayerActivity.EXTRA_START_POSITION, pos);
                    i.putExtra(PlayerActivity.EXTRA_FULLSCREEN, ResumeStore.fullscreen(this));
                    startActivity(i);
                });
            });
        } else if (ResumeStore.SCREEN_ACCOUNT.equals(screen)) {
            startActivity(new Intent(this, AccountActivity.class));
        } else if (ResumeStore.SCREEN_SETTINGS.equals(screen)) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (ResumeStore.SCREEN_ABOUT.equals(screen)) {
            startActivity(new Intent(this, AboutActivity.class));
        }
    }

    private Intent playerIntent(ChannelEntity ch) {
        Intent i = new Intent(this, PlayerActivity.class);
        i.putExtra(PlayerActivity.EXTRA_ID,   ch.id);
        i.putExtra(PlayerActivity.EXTRA_NAME, ch.name);
        i.putExtra(PlayerActivity.EXTRA_URL,  ch.streamUrl);
        i.putExtra(PlayerActivity.EXTRA_TYPE, ch.contentType);
        i.putExtra(PlayerActivity.EXTRA_GROUP, ch.groupTitle);
        i.putExtra(PlayerActivity.EXTRA_ORDER, ch.sortOrder);
        return i;
    }

    /** Actions demandées par les autres écrans (barre du bas, Paramètres). */
    private void handleExtras(Intent in) {
        if (in == null) return;
        if (in.getBooleanExtra(EXTRA_FORCE_SYNC, false)) {
            in.removeExtra(EXTRA_FORCE_SYNC);
            getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putLong("last_sync_ts", 0).apply();
            startBackgroundSync();
        }
        if (in.getBooleanExtra(EXTRA_OPEN_ADD, false)) {
            in.removeExtra(EXTRA_OPEN_ADD);
            showAddPlaylistDialog();
        }
        int nav = in.getIntExtra(EXTRA_NAV, 0);
        if (nav != 0) {
            in.removeExtra(EXTRA_NAV);
            if (nav == R.id.nav_live) openTab(1);
            else if (nav == R.id.nav_fav) openTab(4);
            else if (nav == R.id.nav_home) { showRecentHome(); }
        }
    }

    /** Ouvre un onglet (0=Tout 1=Live 2=Films 3=Séries 4=Favoris). */
    private void openTab(int pos) {
        TabLayout.Tab t = tabLayout.getTabAt(pos);
        if (t != null && !t.isSelected()) { t.select(); return; }
        currentTab = pos;
        showingHome = false;
        showCategoryList();
        observeCurrentTab();
    }

    /** Carte "Abonnement" + salutation (données du cache d'activation). */
    private void refreshSubscriptionCard() {
        SubscriptionInfo si = SubscriptionInfo.read(this, SubscriptionInfo.PREFS_MOBILE);
        if (si == null) {
            tvGreeting.setText("Bonjour 👋");
            tvAvatar.setText("W");
            cardSubscription.setVisibility(View.GONE);
            return;
        }
        String name = si.displayName();
        tvGreeting.setText(name.isEmpty() ? "Bonjour 👋" : "Bonjour " + name + " 👋");
        tvAvatar.setText(si.initial());
        tvSubExpiry.setText("Expire le : " + si.formattedExpiry());
        boolean soon = si.isExpiringSoon(5);
        if (!si.active) {
            tvSubTitle.setText("Abonnement expiré");
            tvSubDays.setText("Expiré");
        } else {
            tvSubTitle.setText(soon ? "Expire bientôt" : "Abonnement Actif");
            tvSubDays.setText(si.daysLeft + (si.daysLeft > 1 ? " jours" : " jour"));
        }
        cardSubscription.setBackgroundResource(soon ? R.drawable.bg_alert : R.drawable.bg_card_hero);
        if (showingHome) cardSubscription.setVisibility(View.VISIBLE);
    }

    // ── Sync background ───────────────────────────────────────────

    private void startBackgroundSync() {
        Intent intent = getIntent();

        // Chemin 1 : extras frais depuis ActivationActivity
        String login    = intent.getStringExtra(EXTRA_LOGIN);
        String password = intent.getStringExtra(EXTRA_PASSWORD);
        String[] dnsUrls = intent.getStringArrayExtra(EXTRA_DNS_URLS);
        String[] dnsEpgs = intent.getStringArrayExtra(EXTRA_DNS_EPGS);

        if (login != null && dnsUrls != null && dnsUrls.length > 0) {
            Log.d(TAG, "Sync ch1: extras DNS=" + dnsUrls.length);
            syncExecutor.execute(() ->
                processNextDns(0, dnsUrls, login,
                    password != null ? password : "",
                    dnsEpgs != null ? dnsEpgs : new String[0], 0));
            return;
        }

        // Chemin 2 : SharedPreferences (reboot / rotation)
        SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String savedStatus = prefs.getString("status",   "UNKNOWN");
        String savedLogin  = prefs.getString("login",    null);
        String savedPass   = prefs.getString("password", "");
        String savedDnsRaw = prefs.getString("dns_urls", "");

        if ("ACTIVE".equals(savedStatus) && savedLogin != null && !savedDnsRaw.isEmpty()) {
            // Vérifier si DB déjà peuplée — éviter re-sync inutile
            syncExecutor.execute(() -> {
                boolean empty = (db.channelDao().count() == 0);
                long lastSync = prefs.getLong("last_sync_ts", 0);
                long interval = AppSettings.refreshIntervalMs(this);   // -1 = manuel
                boolean stale = interval >= 0 && (System.currentTimeMillis() - lastSync) >= interval;
                boolean forced = lastSync == 0;                    // « Actualiser maintenant »
                if (!empty && !stale && !forced) {
                    Log.d(TAG, "Sync ch2: DB ok, pas d'actualisation due, skip");
                    return;
                }
                if (!empty && !forced && AppSettings.refreshWifiOnly(this) && !AppSettings.isOnWifi(this)) {
                    Log.d(TAG, "Sync ch2: Wi-Fi requis, skip");
                    return;
                }
                Log.d(TAG, "Sync ch2: prefs dns=" + savedDnsRaw);
                String[] urls = savedDnsRaw.split(",");
                String[] epgs = prefs.getString("dns_epg_urls", "").split(",");
                processNextDns(0, urls, savedLogin, savedPass, epgs, 0);
            });
            return;
        }

        // Chemin 3 : playlists manuelles stale
        Log.d(TAG, "Sync ch3: stale refresh");
        PlaylistLoader.refreshStaleIfNeeded(db, new PlaylistLoader.Callback() {
            @Override public void onDone(int c) {
                if (c > 0 && !isFinishing() && !isDestroyed())
                    runOnUiThread(MainActivity.this::observeCurrentTab);
            }
            @Override public void onError(String msg) { Log.w(TAG, "stale: " + msg); }
        });
    }

    /**
     * Traitement séquentiel DNS — identique à TvMainActivity.
     * Appels DB via syncExecutor uniquement → pas de conflit Room.
     * PlaylistLoader.load() callbacks re-postés sur syncExecutor.
     */
    private void processNextDns(int index, String[] dnsUrls, String login,
                                 String password, String[] epgUrls, int accumulated) {
        if (index >= dnsUrls.length) {
            // Tous les DNS traités
            final int total = accumulated;
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                showSync(false, null);
                if (showingHome) showRecentHome(); else observeCurrentTab();
                if (total > 0)
                    Toast.makeText(this, "✅ " + total + " chaînes chargées",
                        Toast.LENGTH_SHORT).show();
                else {
                    // DB vide → proposer ajout manuel
                    syncExecutor.execute(() -> {
                        if (db.channelDao().count() == 0)
                            runOnUiThread(this::showAddPlaylistDialog);
                    });
                }
                getSharedPreferences(PREFS, Context.MODE_PRIVATE)
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
        runOnUiThread(() -> showSync(true, "📥 Serveur " + idx + "/" + total + "…"));

        // findOrCreatePlaylist s'exécute sur syncExecutor (déjà le cas ici)
        PlaylistEntity pl = findOrCreatePlaylist(login, password, url, idx);

        PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
            @Override public void onDone(int count) {
                Log.d(TAG, "DNS " + idx + " OK: " + count + " ch");
                // Re-poster sur syncExecutor — évite appel DB sur thread PlaylistLoader
                syncExecutor.execute(() ->
                    processNextDns(index + 1, dnsUrls, login, password, epgUrls,
                        accumulated + count));
            }
            @Override public void onError(String msg) {
                Log.w(TAG, "DNS " + idx + " err: " + msg);
                syncExecutor.execute(() ->
                    processNextDns(index + 1, dnsUrls, login, password, epgUrls, accumulated));
            }
        });
    }

    /**
     * Auto-détecte Xtream (get.php / username= / base serveur) vs M3U URL (.m3u/.m3u8).
     * Exemples :
     *   https://tvradiozap.eu/get.php?username=x&password=y  → XTREAM
     *   https://iptv-org.github.io/iptv/languages/eng.m3u    → M3U URL
     */
    private PlaylistEntity findOrCreatePlaylist(String login, String password,
                                                 String dnsUrl, int idx) {
        // Chercher une playlist existante avec cette URL + login
        PlaylistEntity existing = db.playlistDao().findByUrlAndLogin(dnsUrl, login);
        if (existing != null) {
            existing.password = password;
            existing.isActive = true;
            db.playlistDao().update(existing);
            return existing;
        }
        PlaylistEntity pl = new PlaylistEntity();
        String low = dnsUrl.toLowerCase();
        // Détecter M3U : se termine par .m3u ou .m3u8, ou contient "m3u" SANS "get.php"/"username"
        boolean isM3uUrl = low.endsWith(".m3u") || low.endsWith(".m3u8")
            || (low.contains("m3u") && !low.contains("get.php") && !low.contains("username="));
        if (isM3uUrl) {
            pl.type = PlaylistEntity.TYPE_M3U_URL;
            pl.name = "M3U #" + idx;
        } else {
            // Tout le reste → Xtream (get.php, username=, base serveur)
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

    // ── UI helpers ────────────────────────────────────────────────

    private void showSync(boolean show, String msg) {
        if (progressBar  != null) progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (tvSyncStatus != null) {
            tvSyncStatus.setVisibility(show ? View.VISIBLE : View.GONE);
            if (show && msg != null) tvSyncStatus.setText(msg);
        }
        if (!show && tvEmpty != null) tvEmpty.setVisibility(View.GONE);
    }

    // ── Tabs, search, nav ─────────────────────────────────────────

    private void setupTabs() {
        String[] tabs = {"Tout","Live","Films","Séries","Favoris"};
        final android.content.res.ColorStateList colors = new android.content.res.ColorStateList(
            new int[][]{{android.R.attr.state_selected}, {}},
            new int[]{0xFFFFFFFF, 0xFF8FA6CC});
        final int pad = Math.round(14 * getResources().getDisplayMetrics().density);
        for (String t : tabs) {
            TextView tv = new TextView(this);
            tv.setText(t);
            tv.setGravity(android.view.Gravity.CENTER);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTextColor(colors);
            tv.setTextSize(14);
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
            tv.setSingleLine(true);
            tv.setPadding(pad, 0, pad, 0);
            tv.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
            tabLayout.addTab(tabLayout.newTab().setCustomView(tv));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab t) {
                currentTab = t.getPosition();
                showingHome = false;
                ResumeStore.setTab(MainActivity.this, currentTab);
                showCategoryList();
                observeCurrentTab();
            }
            @Override public void onTabUnselected(TabLayout.Tab t) {}
            @Override public void onTabReselected(TabLayout.Tab t) { onTabSelected(t); }
        });
    }

    private void observeCurrentTab() {
        LiveData<List<ChannelEntity>> src;
        switch (currentTab) {
            case 1:  src = db.channelDao().getLive(); break;
            case 2:  src = db.channelDao().getFilms(); break;
            case 3:  src = db.channelDao().getSeries(); break;
            case 4:  src = db.channelDao().getFavorites(); break;
            default: src = db.channelDao().getAll(); break;
        }
        observeSource(src);
    }

    private void observeSource(LiveData<List<ChannelEntity>> src) {
        if (activeSource != null) activeSource.removeObserver(listObserver);
        activeSource = src;
        src.observe(this, listObserver);
    }

    private void updateList(List<ChannelEntity> raw) {
        if (showingHome) return;
        List<ChannelEntity> list = ParentalControl.filter(this, raw);
        if (adapter != null) adapter.setData(list);
        if (tvEmpty != null) {
            tvEmpty.setText(currentTab == 4 ? "Aucun favori pour le moment." : "Aucune chaîne.\nAjoutez une playlist M3U");
            tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void showCategoryList() {
        homePanel.setVisibility(View.GONE);
        rvChannels.setVisibility(View.VISIBLE);
        cardSubscription.setVisibility(View.GONE);
        heroHandler.removeCallbacks(heroRotate);
        if (heroEngine != null) heroEngine.pause();
        AdaptiveBg.reset(findViewById(R.id.main_root));
    }

    private void showRecentHome() {
        showingHome = true;
        ResumeStore.setTab(this, -1);
        refreshSubscriptionCard();
        homePanel.setVisibility(View.VISIBLE);
        rvChannels.setVisibility(View.GONE);
        syncExecutor.execute(() -> {
            final List<ChannelEntity> picks = loadHomeChannels();
            final boolean fromHistory = hasRecents();
            runOnUiThread(() -> {
                if (!showingHome || isFinishing() || isDestroyed()) return;
                homeChannels = picks;
                heroIndex = 0;
                sliderAdapter.setData(picks);
                ((TextView) findViewById(R.id.tv_home_section))
                    .setText(fromHistory ? "Vos chaînes regardées" : "Chaînes populaires");
                if (tvEmpty != null) {
                    tvEmpty.setText("Aucune chaîne pour le moment.\nPatientez pendant le chargement…");
                    tvEmpty.setVisibility(picks.isEmpty() ? View.VISIBLE : View.GONE);
                }
                findViewById(R.id.home_hero).setVisibility(picks.isEmpty() ? View.GONE : View.VISIBLE);
                heroDots.setVisibility(picks.isEmpty() ? View.GONE : View.VISIBLE);
                bindHero();
                heroHandler.removeCallbacks(heroRotate);
                if (picks.size() > 1) heroHandler.postDelayed(heroRotate, 12000);
            });
        });
    }

    private boolean hasRecents() {
        return !getSharedPreferences(PREFS_RECENTS, MODE_PRIVATE).getString("ids", "").isEmpty();
    }

    /**
     * Chaînes de l'accueil : celles déjà regardées (récentes d'abord) ; à la première utilisation
     * (aucun historique) une sélection aléatoire, mémorisée pour rester stable jusqu'au premier visionnage.
     * Appelé sur syncExecutor.
     */
    private List<ChannelEntity> loadHomeChannels() {
        SharedPreferences sp = getSharedPreferences(PREFS_RECENTS, MODE_PRIVATE);
        List<ChannelEntity> out = new java.util.ArrayList<>();
        String raw = sp.getString("ids", "");
        if (!raw.isEmpty()) {
            for (String id : raw.split(",")) {
                try {
                    ChannelEntity c = db.channelDao().findById(Long.parseLong(id));
                    if (c != null && !ParentalControl.isRestricted(this, c)) out.add(c);
                } catch (NumberFormatException ignored) {}
            }
            if (!out.isEmpty()) return out;
        }
        String seed = sp.getString("seed_ids", "");
        if (!seed.isEmpty()) {
            for (String id : seed.split(",")) {
                try {
                    ChannelEntity c = db.channelDao().findById(Long.parseLong(id));
                    if (c != null && !ParentalControl.isRestricted(this, c)) out.add(c);
                } catch (NumberFormatException ignored) {}
            }
            if (out.size() >= 3) return out;
            out.clear();
        }
        List<ChannelEntity> rnd = db.channelDao().randomLiveWithLogo(60);
        if (rnd == null || rnd.size() < 8) rnd = db.channelDao().randomLive(60);
        if (rnd != null) {
            for (ChannelEntity c : rnd) {
                if (ParentalControl.isRestricted(this, c)) continue;
                out.add(c);
                if (out.size() >= MAX_RECENTS) break;
            }
        }
        if (!out.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ChannelEntity c : out) { if (sb.length() > 0) sb.append(','); sb.append(c.id); }
            sp.edit().putString("seed_ids", sb.toString()).apply();
        }
        return out;
    }

    private void bindHero() {
        if (homeChannels.isEmpty()) return;
        int n = Math.min(HERO_COUNT, homeChannels.size());
        if (heroIndex >= homeChannels.size()) heroIndex = 0;
        ChannelEntity c = homeChannels.get(heroIndex);
        heroTitle.setText(c.name);
        String sub = c.groupTitle != null && !c.groupTitle.isEmpty() ? c.groupTitle
            : (c.country != null ? c.country : "");
        heroSub.setText(sub);
        heroBadge.setVisibility(c.contentType == ChannelEntity.TYPE_LIVE && AppSettings.liveBadges(this)
            ? View.VISIBLE : View.GONE);
        if (c.logoUrl != null && !c.logoUrl.isEmpty())
            Glide.with(this).load(c.logoUrl).fitCenter().placeholder(R.drawable.ic_channel_placeholder).into(heroLogo);
        else heroLogo.setImageResource(R.drawable.ic_channel_placeholder);
        AdaptiveBg.apply(findViewById(R.id.main_root), c.logoUrl);
        playHeroPreview(c);
        heroDots.removeAllViews();
        for (int i = 0; i < n; i++) {
            View d = new View(this);
            int sz = Math.round(7 * getResources().getDisplayMetrics().density);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(sz, sz);
            lp.setMargins(sz / 2 + 1, 0, sz / 2 + 1, 0);
            d.setLayoutParams(lp);
            d.setBackgroundResource(i == heroIndex ? R.drawable.dot_on : R.drawable.dot_off);
            heroDots.addView(d);
        }
    }

    /** Le héros joue réellement le contenu de la chaîne sélectionnée (flux muet). */
    private void playHeroPreview(ChannelEntity c) {
        if (c.streamUrl == null || c.streamUrl.isEmpty()) return;
        if (heroEngine == null) {
            heroEngine = new PlayerEngine(this, new PlayerEngine.Listener() {
                @Override public void onBuffering(boolean b) {}
                @Override public void onPlaying() { runOnUiThread(() -> heroPreview.setVisibility(View.VISIBLE)); }
                @Override public void onError(String message) { runOnUiThread(() -> heroPreview.setVisibility(View.INVISIBLE)); }
            });
            heroEngine.attachView(heroPreview);
            heroEngine.setMuted(true);
        }
        if (heroPlayingId != c.id) {
            heroPlayingId = c.id;
            heroPreview.setVisibility(View.INVISIBLE);
            heroEngine.play(c.streamUrl);
        } else if (!heroEngine.isPlaying()) {
            heroEngine.resume();
        }
    }

    /** Toucher une vignette du slide la lance dans le héros ; un second toucher ouvre le lecteur. */
    private void selectHero(ChannelEntity ch) {
        int idx = -1;
        for (int i = 0; i < homeChannels.size(); i++) if (homeChannels.get(i).id == ch.id) { idx = i; break; }
        if (idx < 0 || idx == heroIndex) { onClick(ch); return; }
        heroIndex = idx;
        heroHandler.removeCallbacks(heroRotate);
        bindHero();
    }

    private void saveRecent(ChannelEntity channel) {
        String raw = getSharedPreferences(PREFS_RECENTS, MODE_PRIVATE).getString("ids", "");
        java.util.LinkedHashSet<String> ids = new java.util.LinkedHashSet<>();
        ids.add(String.valueOf(channel.id));
        if (!raw.isEmpty()) {
            for (String id : raw.split(",")) {
                if (ids.size() >= MAX_RECENTS) break;
                if (!id.equals(String.valueOf(channel.id))) ids.add(id);
            }
        }
        getSharedPreferences(PREFS_RECENTS, MODE_PRIVATE).edit()
            .putString("ids", TextUtils.join(",", ids)).apply();
    }

    private void setupSearch() {
        if (searchView == null) return;
        int searchTextId = getResources().getIdentifier("search_src_text", "id", "android");
        TextView searchText = searchView.findViewById(searchTextId);
        if (searchText != null) {
            searchText.setTextColor(getResources().getColor(R.color.white));
            searchText.setHintTextColor(0xFFB8C0CC);
        }
        int plateId = getResources().getIdentifier("search_plate", "id", "android");
        View plate = searchView.findViewById(plateId);
        if (plate != null) plate.setBackgroundColor(0x00000000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { return false; }
            @Override public boolean onQueryTextChange(String q) {
                if (TextUtils.isEmpty(q)) {
                    if (showingHome) showRecentHome(); else observeCurrentTab();
                    return true;
                }
                showingHome = false;
                showCategoryList();
                observeSource(db.channelDao().search(q));
                return true;
            }
        });
    }

    private void setupGridChooser() {
        if (btnGrid == null) return;
        btnGrid.setOnClickListener(anchor -> {
            android.widget.PopupMenu menu = new android.widget.PopupMenu(this, anchor);
            for (int columns = 2; columns <= 5; columns++) {
                menu.getMenu().add(0, columns, columns, "Grille · " + columns + " colonnes");
            }
            menu.getMenu().add(0, 10, 10, "Liste");
            menu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == 10) AppSettings.setListLayout(this, true);
                else { AppSettings.setListLayout(this, false); AppSettings.setGridColumns(this, item.getItemId()); }
                applyLayoutMode();
                return true;
            });
            menu.show();
        });
    }

    /** Applique Grille/Liste (réglage persistant, aussi modifiable depuis Paramètres). */
    private void applyLayoutMode() {
        boolean list = AppSettings.isListLayout(this);
        gridLayoutManager.setSpanCount(list ? 1 : AppSettings.gridColumns(this));
        adapter.setListMode(list);
    }

    private void setupBottomNav() {
        if (bottomNav == null) return;
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if      (id == R.id.nav_home)    { showRecentHome(); return true; }
            else if (id == R.id.nav_live)    { openTab(1); return true; }
            else if (id == R.id.nav_fav)     { openTab(4); return true; }
            else if (id == R.id.nav_account) {
                startActivity(new Intent(this, AccountActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return false;
            }
            else if (id == R.id.nav_settings){
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return false;
            }
            return false;
        });
    }

    // ── Dialog ajout playlist manuel ──────────────────────────────

    public void showAddPlaylistDialog() {
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(this);
        b.setTitle("➕ Ajouter une playlist");
        android.view.View v = getLayoutInflater().inflate(R.layout.dialog_add_playlist, null);
        EditText etName = v.findViewById(R.id.et_playlist_name);
        EditText etUrl  = v.findViewById(R.id.et_playlist_url);
        b.setView(v);
        b.setPositiveButton("Charger", (d, w) -> {
            String name = etName.getText().toString().trim();
            String url  = etUrl.getText().toString().trim();
            if (TextUtils.isEmpty(url)) return;
            showSync(true, "Chargement…");
            syncExecutor.execute(() -> {
                PlaylistEntity pl = new PlaylistEntity();
                pl.name = name.isEmpty() ? "Playlist" : name;
                String low = url.toLowerCase();
                pl.type = (low.endsWith(".m3u") || low.endsWith(".m3u8"))
                    ? PlaylistEntity.TYPE_M3U_URL : PlaylistEntity.TYPE_XTREAM;
                pl.url = url; pl.isActive = true; pl.lastUpdated = 0;
                pl.id = db.playlistDao().insert(pl);
                PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                    @Override public void onDone(int c) {
                        runOnUiThread(() -> {
                            showSync(false, null);
                            if (showingHome) showRecentHome(); else observeCurrentTab();
                            Toast.makeText(MainActivity.this, c + " chaînes ✅",
                                Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override public void onError(String msg) {
                        runOnUiThread(() -> {
                            showSync(false, null);
                            Toast.makeText(MainActivity.this, "Erreur : " + msg,
                                Toast.LENGTH_LONG).show();
                        });
                    }
                });
            });
        });
        b.setNegativeButton("Annuler", null);
        b.show();
    }

    // ── Clic sur une chaîne ───────────────────────────────────────

    @Override
    public void onClick(ChannelEntity ch) {
        if (heroEngine != null) heroEngine.pause();
        saveRecent(ch);
        startActivity(playerIntent(ch));
    }

    @Override protected void onResume() {
        super.onResume();
        ResumeStore.setScreen(this, ResumeStore.SCREEN_MAIN);
        applyLayoutMode();
        int navId = showingHome ? R.id.nav_home
            : currentTab == 1 ? R.id.nav_live
            : currentTab == 4 ? R.id.nav_fav : R.id.nav_home;
        android.view.MenuItem navItem = bottomNav.getMenu().findItem(navId);
        if (navItem != null) navItem.setChecked(true);
        if (showingHome) showRecentHome(); else observeCurrentTab();
    }

    @Override protected void onPause() {
        heroHandler.removeCallbacks(heroRotate);
        if (heroEngine != null) heroEngine.pause();
        super.onPause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        heroHandler.removeCallbacksAndMessages(null);
        if (heroPreview != null) heroPreview.setPlayer(null);
        if (heroEngine != null) { heroEngine.release(); heroEngine = null; }
        syncExecutor.shutdownNow();
    }
}
