package com.wdesign.wiseiptv.tv.ui;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.epg.EpgClient;
import com.wdesign.wiseiptv.core.player.PlayerEngine;
import com.wdesign.wiseiptv.core.player.PlayerHandoff;
import com.wdesign.wiseiptv.core.util.StatsReporter;
import com.wdesign.wiseiptv.tv.util.ActivationManager;
import com.wdesign.wiseiptv.core.util.AdaptiveBg;
import com.wdesign.wiseiptv.core.util.AppSettings;
import com.wdesign.wiseiptv.core.util.Recorder;
import com.wdesign.wiseiptv.core.util.DataUsage;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.core.util.LegalTexts;
import com.wdesign.wiseiptv.core.util.ParentalControl;
import com.wdesign.wiseiptv.core.util.SubscriptionHistory;
import com.wdesign.wiseiptv.core.util.SubscriptionInfo;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.tv.R;
import com.wdesign.wiseiptv.tv.util.PlaylistLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TvMainActivity — design TV : menu à gauche, liste des chaînes, aperçu vidéo + guide (EPG)
 * et replay à droite. Les pages Compte / Paramètres / À propos reprennent celles du mobile.
 */
@OptIn(markerClass = UnstableApi.class)
public class TvMainActivity extends FragmentActivity {

    public static final String EXTRA_ACT_LOGIN        = "act_login";
    public static final String EXTRA_ACT_PASSWORD     = "act_password";
    public static final String EXTRA_ACT_EXPIRES      = "act_expires";
    public static final String EXTRA_ACT_DNS_URLS     = "act_dns_urls";
    public static final String EXTRA_ACT_DNS_EPG_URLS = "act_dns_epg_urls";

    private static final String TAG           = "TvMainActivity";
    private static final String PREFS_RECENTS = "wise_tv_recents";
    private static final String PREFS_STATE   = "wise_tv_state";
    private static final String PREFS_ACT     = "wise_activation_tv";
    private static final int    MAX_RECENTS   = 30;

    private static final int S_HOME = 0, S_LIVE = 1, S_FILMS = 2, S_SERIES = 3, S_FAVS = 4,
        S_SEARCH = 5, S_ACCOUNT = 6, S_SETTINGS = 7, S_ABOUT = 8;
    private static final String[] NAV_LABELS = {"Accueil", "Live TV", "Films", "Séries",
        "Favoris", "Rechercher", "Mon compte", "Paramètres", "À propos"};
    private static final int[] NAV_ICONS = {R.drawable.ic_home, R.drawable.ic_tv, R.drawable.ic_grid,
        R.drawable.ic_playlist, R.drawable.ic_favorite_border, R.drawable.ic_search,
        R.drawable.ic_person, R.drawable.ic_tune, R.drawable.ic_info};

    private AppDatabase db;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Vues
    private LinearLayout navItems, pageContent, epgList;
    private View panelBrowse, panelPage, btnWatch, btnFav, btnLive;
    private TextView btnRec;
    private final Recorder recorder = new Recorder();
    private TextView tvListTitle, tvInfoName, tvInfoGroup, tvEpgEmpty, tvSyncStatus, tvEmpty,
        tvPreviewLive, tvSubPill;
    private ProgressBar progressBar, previewLoading;
    private PlayerView previewView;
    private RecyclerView rvList;
    private RowsAdapter adapter;
    private final View[] navViews = new View[NAV_LABELS.length];
    private boolean pillShown, initialFocusDone;
    // Synchronisation : DNS gardés pendant un cycle + dernière erreur + nombre de tentatives auto
    private final Set<Long> syncKeepIds = Collections.synchronizedSet(new HashSet<Long>());
    private volatile String lastSyncError;
    private final java.util.concurrent.atomic.AtomicBoolean syncRunning = new java.util.concurrent.atomic.AtomicBoolean(false);
    private volatile boolean syncAgain;
    private int syncRetries = 0;

    // État
    private int section = S_HOME;
    private PlayerEngine previewEngine;
    private ChannelEntity selected;
    private String previewUrl;
    private boolean replaying;
    private EpgClient.Source epgSource;
    private LiveData<List<ChannelEntity>> liveData;
    private final Observer<List<ChannelEntity>> listObserver = this::setList;
    private final Runnable previewTask = this::startPreviewAndEpg;
    private android.animation.ObjectAnimator logoPulse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_main);
        db = AppDatabase.get(this);

        navItems     = findViewById(R.id.nav_items);
        pageContent  = findViewById(R.id.page_content);
        epgList      = findViewById(R.id.epg_list);
        panelBrowse  = findViewById(R.id.panel_browse);
        panelPage    = findViewById(R.id.panel_page);
        btnWatch     = findViewById(R.id.btn_watch);
        btnFav       = findViewById(R.id.btn_fav);
        btnLive      = findViewById(R.id.btn_live);
        btnRec       = findViewById(R.id.btn_rec);
        btnRec.setOnClickListener(v -> toggleRecord());
        findViewById(R.id.preview_card).setOnClickListener(v -> { if (selected != null) openPlayer(selected, previewUrl); });
        tvListTitle  = findViewById(R.id.tv_list_title);
        tvInfoName   = findViewById(R.id.tv_info_name);
        tvInfoGroup  = findViewById(R.id.tv_info_group);
        tvEpgEmpty   = findViewById(R.id.tv_epg_empty);
        tvSyncStatus = findViewById(R.id.tv_sync_status);
        tvEmpty      = findViewById(R.id.tv_empty);
        tvPreviewLive = findViewById(R.id.tv_preview_live);
        tvSubPill    = findViewById(R.id.tv_sub_pill);
        progressBar  = findViewById(R.id.progress_bar);
        previewLoading = findViewById(R.id.preview_loading);
        previewView  = findViewById(R.id.tv_preview_player);
        rvList       = findViewById(R.id.rv_list);

        adapter = new RowsAdapter();
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);

        buildNav();
        bindSubscriptionPill();

        btnWatch.setOnClickListener(v -> { if (selected != null) openPlayer(selected, previewUrl); });
        btnFav.setOnClickListener(v -> toggleFavorite());
        btnLive.setOnClickListener(v -> backToLive());

        Fx.stagger(40, 70, findViewById(R.id.nav_rail), findViewById(R.id.panel_browse));

        // Reprise : ouvrir directement le lecteur si l'app a été fermée pendant la lecture
        if (savedInstanceState == null) resumePlayerIfNeeded();

        section = getSharedPreferences(PREFS_STATE, MODE_PRIVATE).getInt("section", S_HOME);
        if (section == S_SEARCH) section = S_HOME;
        showSection(section);
        if (getIntent().getStringExtra(EXTRA_ACT_LOGIN) == null) {
            // Ouverture « à froid » depuis le cache : on re-vérifie le panel en arrière-plan pour récupérer
            // une nouvelle playlist / de nouveaux DNS sans avoir à vider les données de l'app.
            maybeSync(false);
            refreshFromPanel(false);
        } else {
            maybeSync(false);
        }
    }

    private void bg(Runnable r) {
        try { io.execute(r); } catch (Exception ignored) { }
    }

    // ── Navigation ────────────────────────────────────────────────

    private void buildNav() {
        LayoutInflater inf = getLayoutInflater();
        for (int i = 0; i < NAV_LABELS.length; i++) {
            final int idx = i;
            View v = inf.inflate(R.layout.item_tv_nav, navItems, false);
            ((ImageView) v.findViewById(R.id.nav_icon)).setImageResource(NAV_ICONS[i]);
            ((TextView) v.findViewById(R.id.nav_label)).setText(NAV_LABELS[i]);
            if (i >= S_SEARCH) {   // Recherche / Compte / Paramètres / À propos : icônes seules (place en haut)
                v.findViewById(R.id.nav_label).setVisibility(View.GONE);
                v.setContentDescription(NAV_LABELS[i]);
                v.setPadding(dp(12), v.getPaddingTop(), dp(12), v.getPaddingBottom());
            }
            v.setOnClickListener(x -> showSection(idx));
            v.setOnFocusChangeListener((x, f) -> { if (f && idx != S_SEARCH) showSection(idx); });
            navItems.addView(v);
            navViews[i] = v;
        }
    }

    private void showSection(int s) {
        if (s == S_SEARCH) { showSearchDialog(); return; }
        if (s == section && liveData != null && isBrowse(s) && panelBrowse.getVisibility() == View.VISIBLE) {
            highlightNav(s);
            return;
        }
        section = s;
        getSharedPreferences(PREFS_STATE, MODE_PRIVATE).edit().putInt("section", s).apply();
        highlightNav(s);
        detachLiveData();
        if (isBrowse(s)) {
            previewUrl = null;
            panelPage.setVisibility(View.GONE);
            panelBrowse.setVisibility(View.VISIBLE);
            tvListTitle.setText(NAV_LABELS[s]);
            loadBrowse(s);
        } else {
            stopPreview();
            AdaptiveBg.reset(findViewById(R.id.main_root));
            panelBrowse.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
            panelPage.setVisibility(View.VISIBLE);
            pageContent.removeAllViews();
            if (s == S_ACCOUNT) buildAccountPage();
            else if (s == S_SETTINGS) buildSettingsPage();
            else buildAboutPage();
            Fx.enter(pageContent, 0);
        }
    }

    private boolean isBrowse(int s) { return s <= S_FAVS; }

    private void highlightNav(int s) {
        for (int i = 0; i < navViews.length; i++) navViews[i].setSelected(i == s);
    }

    private void detachLiveData() {
        if (liveData != null) { liveData.removeObserver(listObserver); liveData = null; }
    }

    private void loadBrowse(int s) {
        switch (s) {
            case S_LIVE:   observe(db.channelDao().getLive()); break;
            case S_FILMS:  observe(db.channelDao().getFilms()); break;
            case S_SERIES: observe(db.channelDao().getSeries()); break;
            case S_FAVS:   observe(db.channelDao().getFavorites()); break;
            default:       loadHome(); break;
        }
    }

    private void observe(LiveData<List<ChannelEntity>> ld) {
        liveData = ld;
        ld.observe(this, listObserver);
    }

    /** Accueil : chaînes regardées ; au premier lancement, sélection au hasard pour ne pas être vide. */
    private void loadHome() {
        liveData = null;
        bg(() -> {
            List<ChannelEntity> out = new ArrayList<>();
            String raw = getSharedPreferences(PREFS_RECENTS, MODE_PRIVATE).getString("ids", "");
            if (!raw.isEmpty()) {
                for (String id : raw.split(",")) {
                    try {
                        ChannelEntity c = db.channelDao().findById(Long.parseLong(id));
                        if (c != null) out.add(c);
                    } catch (NumberFormatException ignored) { }
                }
            }
            final boolean recents = !out.isEmpty();
            if (!recents) {
                out = db.channelDao().randomLiveWithLogo(30);
                if (out == null || out.size() < 10) out = db.channelDao().randomLive(30);
            }
            final List<ChannelEntity> fl = out;
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed() || section != S_HOME) return;
                tvListTitle.setText(recents ? "Récemment regardées" : "Chaînes à découvrir");
                setList(fl);
            });
        });
    }

    private void setList(List<ChannelEntity> raw) {
        List<ChannelEntity> list = ParentalControl.filter(this, raw);
        adapter.set(list);
        boolean empty = list == null || list.isEmpty();
        tvEmpty.setText(section == S_FAVS ? "Aucun favori pour le moment."
            : "Aucune chaîne.\nLa synchronisation est peut-être en cours…");
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        if (empty) { selected = null; stopPreview(); clearInfo(); return; }
        long last = getSharedPreferences(PREFS_STATE, MODE_PRIVATE).getLong("last_channel", -1);
        ChannelEntity pick = list.get(0);
        if (selected != null || last > 0) {
            long want = selected != null ? selected.id : last;
            for (ChannelEntity c : list) if (c.id == want) { pick = c; break; }
        }
        select(pick, false);
        if (!initialFocusDone && isBrowse(section)) {
            initialFocusDone = true;
            btnWatch.requestFocus();
        }
    }

    // ── Sélection, aperçu, guide ──────────────────────────────────

    private void select(ChannelEntity ch, boolean fromFocus) {
        if (ch == null) return;
        boolean changed = selected == null || selected.id != ch.id;
        selected = ch;
        if (changed) AdaptiveBg.apply(findViewById(R.id.main_root), ch.logoUrl);
        getSharedPreferences(PREFS_STATE, MODE_PRIVATE).edit().putLong("last_channel", ch.id).apply();
        tvInfoName.setText(ch.name);
        tvInfoGroup.setText(ch.groupTitle == null ? "" : ch.groupTitle);
        updateFavLabel();
        tvPreviewLive.setVisibility(ch.contentType == ChannelEntity.TYPE_LIVE
            && AppSettings.liveBadges(this) ? View.VISIBLE : View.GONE);
        if (changed || previewUrl == null) {
            replaying = false;
            previewUrl = ch.streamUrl;
            btnLive.setVisibility(View.GONE);
            if (previewEngine != null && previewEngine.isRecording()) previewEngine.stopRecording();
            handler.removeCallbacks(previewTask);
            handler.postDelayed(previewTask, fromFocus ? 700 : 250);
        }
    }

    private void clearInfo() {
        tvInfoName.setText("");
        tvInfoGroup.setText("");
        epgList.removeAllViews();
        tvEpgEmpty.setText("");
        tvPreviewLive.setVisibility(View.GONE);
    }

    private void startPreviewAndEpg() {
        if (selected == null || isFinishing() || isDestroyed()) return;
        playPreview(previewUrl);
        loadEpg(selected);
    }

    private void playPreview(String url) {
        if (url == null || url.isEmpty()) return;
        if (previewEngine == null) {
            previewEngine = new PlayerEngine(this, new PlayerEngine.Listener() {
                @Override public void onBuffering(boolean b) {
                    runOnUiThread(() -> previewLoading.setVisibility(b ? View.VISIBLE : View.GONE));
                }
                @Override public void onPlaying() {
                    runOnUiThread(() -> previewLoading.setVisibility(View.GONE));
                }
                @Override public void onError(String message) {
                    runOnUiThread(() -> previewLoading.setVisibility(View.GONE));
                }
            });
            previewEngine.attachView(previewView);
            previewEngine.setMuted(false);
        }
        previewEngine.play(url);
    }

    private void stopPreview() {
        handler.removeCallbacks(previewTask);
        if (previewEngine != null) previewEngine.pause();
    }

    private void loadEpg(final ChannelEntity ch) {
        epgList.removeAllViews();
        tvEpgEmpty.setVisibility(View.VISIBLE);
        tvEpgEmpty.setText("Chargement du programme…");
        EpgClient.load(this, ch, new EpgClient.Callback() {
            @Override public void onResult(List<EpgClient.Program> programs, EpgClient.Source source) {
                if (isFinishing() || isDestroyed() || selected == null || selected.id != ch.id) return;
                epgSource = source;
                renderEpg(programs);
            }
            @Override public void onError(String message) {
                if (isFinishing() || isDestroyed() || selected == null || selected.id != ch.id) return;
                epgSource = null;
                epgList.removeAllViews();
                tvEpgEmpty.setVisibility(View.VISIBLE);
                tvEpgEmpty.setText(message);
            }
        });
    }

    private void renderEpg(List<EpgClient.Program> list) {
        epgList.removeAllViews();
        if (list == null || list.isEmpty()) {
            tvEpgEmpty.setVisibility(View.VISIBLE);
            tvEpgEmpty.setText("Aucun programme disponible pour cette chaîne.");
            return;
        }
        tvEpgEmpty.setVisibility(View.GONE);
        long now = System.currentTimeMillis() / 1000;
        LayoutInflater inf = getLayoutInflater();
        for (final EpgClient.Program p : list) {
            View row = inf.inflate(R.layout.row_tv_epg, epgList, false);
            ((TextView) row.findViewById(R.id.tv_epg_time)).setText(p.time());
            ((TextView) row.findViewById(R.id.tv_epg_title)).setText(p.title);
            boolean isNow = p.isNow(now);
            boolean canReplay = p.isPast(now) && p.archive;
            if (isNow) {
                row.findViewById(R.id.tv_epg_now).setVisibility(View.VISIBLE);
                ProgressBar pb = row.findViewById(R.id.epg_progress);
                pb.setVisibility(View.VISIBLE);
                pb.setProgress(p.progress(now));
            }
            if (canReplay) row.findViewById(R.id.iv_epg_replay).setVisibility(View.VISIBLE);
            row.setOnClickListener(v -> {
                if (isNow) backToLive();
                else if (canReplay) playReplay(p);
                else Toast.makeText(this, p.isPast(now) ? "Replay indisponible pour ce programme."
                    : "Programme à venir.", Toast.LENGTH_SHORT).show();
            });
            epgList.addView(row);
        }
    }

    private void playReplay(EpgClient.Program p) {
        if (epgSource == null || selected == null) return;
        replaying = true;
        previewUrl = EpgClient.replayUrl(epgSource, p);
        btnLive.setVisibility(View.VISIBLE);
        tvPreviewLive.setVisibility(View.GONE);
        tvInfoGroup.setText("Replay · " + p.title);
        if (previewEngine != null) previewEngine.setMuted(false);
        playPreview(previewUrl);
    }

    private void backToLive() {
        if (selected == null) return;
        replaying = false;
        previewUrl = selected.streamUrl;
        btnLive.setVisibility(View.GONE);
        tvInfoGroup.setText(selected.groupTitle == null ? "" : selected.groupTitle);
        tvPreviewLive.setVisibility(selected.contentType == ChannelEntity.TYPE_LIVE
            && AppSettings.liveBadges(this) ? View.VISIBLE : View.GONE);
        if (previewEngine != null) previewEngine.setMuted(false);
        playPreview(previewUrl);
    }

    private void toggleRecord() {
        if (selected == null) return;
        if ((previewEngine != null && previewEngine.isRecording()) || recorder.isRecording()) {
            if (previewEngine != null) previewEngine.stopRecording();
            recorder.stop();
            return;
        }
        final String url = previewUrl != null ? previewUrl : selected.streamUrl;
        if (url == null || url.isEmpty()) return;
        Recorder.Listener l = new Recorder.Listener() {
            @Override public void onStarted(java.io.File file) {
                runOnUiThread(() -> { btnRec.setText("⏹ Arrêter"); toast("⏺ Enregistrement démarré"); });
            }
            @Override public void onFinished(java.io.File file, long bytes, String error) {
                runOnUiThread(() -> {
                    btnRec.setText("⏺ Enregistrer");
                    if (isDestroyed()) return;
                    Toast.makeText(TvMainActivity.this, error != null ? "Enregistrement interrompu : " + error
                        : bytes > 0 ? "✅ Enregistré : " + file.getAbsolutePath()
                        : "Rien n'a été enregistré : attendez que la vidéo démarre puis réessayez.",
                        Toast.LENGTH_LONG).show();
                });
            }
        };
        if (selected.contentType == ChannelEntity.TYPE_LIVE || replaying) {
            if (previewEngine == null || !previewEngine.startRecording(selected.name, l))
                toast("Impossible de démarrer l'enregistrement.");
        } else {
            recorder.start(this, url, selected.name, l);
        }
    }

    private void toggleFavorite() {
        if (selected == null) return;
        final ChannelEntity ch = selected;
        ch.isFavorite = !ch.isFavorite;
        updateFavLabel();
        bg(() -> db.channelDao().setFavorite(ch.id, ch.isFavorite));
    }

    private void updateFavLabel() {
        ((TextView) btnFav).setText(selected != null && selected.isFavorite ? "♥  Favori" : "♡  Favori");
    }

    // ── Lecteur plein écran + reprise ─────────────────────────────

    private void openPlayer(ChannelEntity ch, String url) {
        saveRecent(ch.id);
        handler.removeCallbacks(previewTask);
        long startPosition = 0;
        if (ch.contentType != ChannelEntity.TYPE_LIVE && previewEngine != null && !replaying
            && selected != null && selected.id == ch.id) startPosition = previewEngine.getPosition();
        final String playUrl = url != null ? url : ch.streamUrl;
        // L'aperçu joue déjà ce flux : on transmet CE lecteur au plein écran (même connexion, image continue).
        if (previewEngine != null && playUrl != null && playUrl.equals(previewEngine.getCurrentUrl())
                && (previewEngine.isReady() || previewEngine.isBuffering())) {
            if (previewEngine.isRecording()) previewEngine.stopRecording();
            PlayerHandoff.offer(previewEngine, playUrl, previewView);
            previewEngine = null;
        } else if (previewEngine != null) {
            previewEngine.pause();
        }
        Intent i = new Intent(this, TvPlayerActivity.class);
        i.putExtra(TvPlayerActivity.EXTRA_ID,    ch.id);
        i.putExtra(TvPlayerActivity.EXTRA_NAME,  ch.name);
        i.putExtra(TvPlayerActivity.EXTRA_URL,   playUrl);
        i.putExtra(TvPlayerActivity.EXTRA_TYPE,  ch.contentType);
        i.putExtra(TvPlayerActivity.EXTRA_GROUP, ch.groupTitle);
        i.putExtra(TvPlayerActivity.EXTRA_ORDER, ch.sortOrder);
        if (startPosition > 0) i.putExtra(TvPlayerActivity.EXTRA_START_POSITION, startPosition);
        startActivity(i);
    }

    private void resumePlayerIfNeeded() {
        final SharedPreferences st = getSharedPreferences(PREFS_STATE, MODE_PRIVATE);
        if (!st.getBoolean("player_open", false)) return;
        final long id = st.getLong("player_channel", -1);
        if (id <= 0) return;
        bg(() -> {
            final ChannelEntity ch = db.channelDao().findById(id);
            if (ch == null) { st.edit().putBoolean("player_open", false).apply(); return; }
            final long pos = st.getLong("player_pos", 0);
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                Intent i = new Intent(this, TvPlayerActivity.class);
                i.putExtra(TvPlayerActivity.EXTRA_ID,    ch.id);
                i.putExtra(TvPlayerActivity.EXTRA_NAME,  ch.name);
                i.putExtra(TvPlayerActivity.EXTRA_URL,   ch.streamUrl);
                i.putExtra(TvPlayerActivity.EXTRA_TYPE,  ch.contentType);
                i.putExtra(TvPlayerActivity.EXTRA_GROUP, ch.groupTitle);
                i.putExtra(TvPlayerActivity.EXTRA_ORDER, ch.sortOrder);
                if (pos > 0 && ch.contentType != ChannelEntity.TYPE_LIVE)
                    i.putExtra(TvPlayerActivity.EXTRA_START_POSITION, pos);
                startActivity(i);
            });
        });
    }

    private void saveRecent(long channelId) {
        SharedPreferences p = getSharedPreferences(PREFS_RECENTS, MODE_PRIVATE);
        String raw = p.getString("ids", "");
        java.util.LinkedHashSet<String> ids = new java.util.LinkedHashSet<>();
        ids.add(String.valueOf(channelId));
        if (!raw.isEmpty()) {
            for (String id : raw.split(",")) {
                if (ids.size() >= MAX_RECENTS) break;
                ids.add(id);
            }
        }
        p.edit().putString("ids", TextUtils.join(",", ids)).apply();
    }

    // ── Recherche ─────────────────────────────────────────────────

    private void showSearchDialog() {
        final EditText et = new EditText(this);
        et.setHint("Nom de chaîne, film, série…");
        et.setSingleLine(true);
        et.setTextColor(0xFFFFFFFF);
        et.setHintTextColor(0xFF8FA6CC);
        new AlertDialog.Builder(this).setTitle("🔍 Rechercher").setView(et)
            .setPositiveButton("Chercher", (d, w) -> {
                final String q = et.getText().toString().trim();
                if (q.isEmpty()) return;
                bg(() -> {
                    final List<ChannelEntity> res = db.channelDao().searchSync(q);
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        detachLiveData();
                        section = S_SEARCH;
                        highlightNav(S_SEARCH);
                        panelPage.setVisibility(View.GONE);
                        panelBrowse.setVisibility(View.VISIBLE);
                        tvListTitle.setText("Résultats : " + q);
                        selected = null;
                        setList(res);
                    });
                });
            })
            .setNegativeButton("Annuler", null).show();
    }

    // ── Synchronisation (fréquence réglable dans Paramètres) ──────

    private void maybeSync(final boolean force) {
        Intent in = getIntent();
        final SharedPreferences prefs = getSharedPreferences(PREFS_ACT, MODE_PRIVATE);
        String login = in.getStringExtra(EXTRA_ACT_LOGIN);
        String password = in.getStringExtra(EXTRA_ACT_PASSWORD);
        String[] urls = in.getStringArrayExtra(EXTRA_ACT_DNS_URLS);
        if (login == null || urls == null || urls.length == 0) {
            if (!"ACTIVE".equals(prefs.getString("status", ""))) { refreshManualPlaylists(force); return; }
            login = prefs.getString("login", null);
            password = prefs.getString("password", "");
            String raw = prefs.getString("dns_urls", "");
            urls = raw.isEmpty() ? new String[0] : raw.split(",");
        }
        if (login == null || urls.length == 0) { refreshManualPlaylists(force); return; }
        final String fLogin = login, fPass = password != null ? password : "";
        final String[] fUrls = urls;
        bg(() -> {
            boolean empty = db.channelDao().count() == 0;
            long interval = AppSettings.refreshIntervalMs(this);
            long last = prefs.getLong("last_sync_ts", 0);
            boolean due = force || empty || interval == 0
                || (interval > 0 && System.currentTimeMillis() - last > interval);
            if (due && !force && !empty && AppSettings.refreshWifiOnly(this) && !AppSettings.isOnWifi(this)) due = false;
            if (!due) { Log.d(TAG, "Sync ignorée (fréquence réglée)"); return; }
            if (!syncRunning.compareAndSet(false, true)) {   // un cycle est déjà en cours
                if (force) syncAgain = true;
                return;
            }
            runOnUiThread(() -> showSyncStatus("Chargement de vos chaînes…"));
            processNextDns(0, fUrls, fLogin, fPass, 0);
        });
    }

    private void refreshManualPlaylists(boolean force) {
        long interval = AppSettings.refreshIntervalMs(this);
        if (!force && interval < 0) return;
        PlaylistLoader.refreshStaleIfNeeded(db, new PlaylistLoader.Callback() {
            @Override public void onDone(int c) { runOnUiThread(() -> { refreshCurrent(); hideSyncStatus(); }); }
            @Override public void onError(String msg) { runOnUiThread(() -> hideSyncStatus()); }
        });
    }

    private void processNextDns(final int index, final String[] urls, final String login,
                                final String password, final int accumulated) {
        try {
            processNextDnsStep(index, urls, login, password, accumulated);
        } catch (Throwable t) {   // ne jamais laisser le cycle bloqué « en cours »
            Log.e(TAG, "sync: " + t.getMessage(), t);
            lastSyncError = t instanceof OutOfMemoryError ? "mémoire insuffisante"
                : (t.getMessage() != null ? t.getMessage() : "erreur inattendue");
            finishSync(accumulated);
        }
    }

    private void processNextDnsStep(final int index, final String[] urls, final String login,
                                    final String password, final int accumulated) {
        if (index == 0) { syncKeepIds.clear(); lastSyncError = null; }
        if (index >= urls.length) { finishSync(accumulated); return; }
        String url = urls[index].trim();
        if (url.isEmpty()) { processNextDns(index + 1, urls, login, password, accumulated); return; }
        final int idx = index + 1;
        runOnUiThread(() -> showSyncStatus("📥 Serveur " + idx + "/" + urls.length + "…"));
        PlaylistEntity pl = findOrCreatePlaylist(login, password, url, idx);
        syncKeepIds.add(pl.id);
        PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
            @Override public void onDone(int count) {
                bg(() -> processNextDns(index + 1, urls, login, password, accumulated + count));
            }
            @Override public void onError(String msg) {
                Log.w(TAG, "DNS " + idx + " : " + msg);
                lastSyncError = msg;
                bg(() -> processNextDns(index + 1, urls, login, password, accumulated));
            }
        });
    }

    /** Fin d'un cycle : purge des anciens DNS, message clair si rien n'a été chargé, nouvelle tentative auto. */
    private void finishSync(final int total) {
        if (total > 0) purgeObsoletePlaylists();   // DNS retirés du panel : on supprime leurs chaînes
        final String err = lastSyncError;
        syncRunning.set(false);
        final boolean again = syncAgain;
        syncAgain = false;
        runOnUiThread(() -> {
            hideSyncStatus();
            if (isFinishing() || isDestroyed()) return;
            if (total > 0) {
                syncRetries = 0;
                Toast.makeText(this, "✅ " + total + " chaînes chargées", Toast.LENGTH_SHORT).show();
                getSharedPreferences(PREFS_ACT, MODE_PRIVATE).edit()
                    .putLong("last_sync_ts", System.currentTimeMillis()).apply();
            } else {
                // Avant : l'échec était avalé et l'écran restait vide sans explication.
                String why = err != null ? err : "aucune chaîne reçue";
                showSyncStatus("⚠ Chargement impossible : " + why);
                Toast.makeText(this, "Chargement des chaînes impossible : " + why, Toast.LENGTH_LONG).show();
                if (syncRetries < 3) {
                    syncRetries++;
                    handler.postDelayed(() -> { if (!isFinishing() && !isDestroyed()) maybeSync(true); }, 20_000);
                }
            }
            refreshCurrent();
            if (again) maybeSync(true);
        });
    }

    /** Playlists créées par l'activation (noms « IPTV #n », « M3U #n », « Abonnement IPTV »), pas celles ajoutées à la main. */
    private static boolean isActivationPlaylist(PlaylistEntity pl) {
        return pl.name != null && (pl.name.startsWith("IPTV #") || pl.name.startsWith("M3U #")
            || pl.name.equals("Abonnement IPTV"));
    }

    private void purgeObsoletePlaylists() {
        try {
            for (PlaylistEntity pl : db.playlistDao().getAllSync()) {
                if (isActivationPlaylist(pl) && !syncKeepIds.contains(pl.id)) {
                    db.channelDao().deleteByPlaylist(pl.id);
                    db.playlistDao().delete(pl);
                }
            }
        } catch (Exception e) { Log.w(TAG, "purge: " + e.getMessage()); }
    }

    /**
     * Interroge le panel : si les identifiants ou la liste de DNS ont changé (nouvelle playlist choisie par
     * l'admin), le cache est mis à jour et la synchronisation est relancée. Plus besoin de vider les données.
     * Hors ligne : on garde le cache et on ne fait rien.
     */
    private void refreshFromPanel(final boolean force) {
        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override public void onActive(DeviceSecurity.ActivationResult r) {
                SharedPreferences p = getSharedPreferences(PREFS_ACT, MODE_PRIVATE);
                boolean changed = !r.login.equals(p.getString("login", ""))
                    || !r.password.equals(p.getString("password", ""))
                    || !ActivationManager.dnsKey(r).equals(p.getString("dns_urls", ""));
                ActivationManager.cacheActive(TvMainActivity.this, r);
                if (changed || force) runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (changed) syncRetries = 0;
                    // Le cache vient d'être mis à jour : maybeSync relit les nouveaux DNS.
                    getIntent().removeExtra(EXTRA_ACT_LOGIN);
                    maybeSync(true);
                });
            }
            @Override public void onInactive(String status, String message) {
                if ("ERROR".equals(status)) return;   // réponse panel illisible : on ne casse rien
                ActivationManager.clearCache(TvMainActivity.this, status);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    startActivity(new Intent(TvMainActivity.this, TvActivationActivity.class));
                    finish();
                });
            }
            @Override public void onError(String message) { /* hors ligne : on garde le cache */ }
        });
    }

    private PlaylistEntity findOrCreatePlaylist(String login, String password, String dnsUrl, int idx) {
        PlaylistEntity existing = db.playlistDao().findByUrlAndLogin(dnsUrl, login);
        if (existing != null) {
            existing.password = password;
            existing.isActive = true;
            db.playlistDao().update(existing);
            return existing;
        }
        PlaylistEntity pl = new PlaylistEntity();
        pl.url = dnsUrl.trim();
        pl.isActive = true;
        pl.lastUpdated = 0;
        if (isXtreamUrl(pl.url)) {
            pl.type = PlaylistEntity.TYPE_XTREAM;
            pl.name = "IPTV #" + idx;
            pl.username = login;
            pl.password = password;
        } else {
            pl.type = PlaylistEntity.TYPE_M3U_URL;
            pl.name = "M3U #" + idx;
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
            || lower.contains("/apiget.php") || lower.contains("/panel_api.php")) return true;
        if (lower.matches(".*https?://.+/[^/]+/[^/]+/(m3u_plus|m3u|ts).*")) return true;
        if (lower.contains("username=") && lower.contains("password=")) return true;
        return !(lower.endsWith(".m3u") || lower.endsWith(".m3u8") || lower.contains("m3u"));
    }

    private void refreshCurrent() {
        if (isFinishing() || isDestroyed()) return;
        if (section == S_HOME) loadHome();
        else if (isBrowse(section) && liveData == null) loadBrowse(section);
    }

    private void showSyncStatus(String msg) {
        tvSyncStatus.setText(msg);
        tvSyncStatus.setVisibility(View.VISIBLE);
    }

    private void hideSyncStatus() { tvSyncStatus.setVisibility(View.GONE); }

    // ── Pages : Mon compte ────────────────────────────────────────

    private void bindSubscriptionPill() {
        SubscriptionInfo si = SubscriptionInfo.read(this, SubscriptionInfo.PREFS_TV);
        if (si == null) { pillShown = false; tvSubPill.setVisibility(View.GONE); return; }
        pillShown = true;
        tvSubPill.setVisibility(View.VISIBLE);
        tvSubPill.setText(si.active ? "Premium · " + si.daysLeft + " j" : "Expiré");
        if (!si.active || si.isExpiringSoon(5)) tvSubPill.setBackgroundResource(R.drawable.bg_alert);
    }

    private void buildAccountPage() {
        final String key = DeviceSecurity.getOrCreateKey(this);
        addTitle("Mon compte");
        SharedPreferences act = getSharedPreferences(PREFS_ACT, MODE_PRIVATE);
        SubscriptionHistory.record(this, act.getString("expires_at", ""));
        SubscriptionInfo si = SubscriptionInfo.read(this, SubscriptionInfo.PREFS_TV);
        if (si == null) {
            addRow(R.drawable.ic_warning, "Aucun abonnement actif",
                "Communiquez votre Device Key à votre revendeur.", null);
        } else {
            if (si.isExpiringSoon(5)) {
                addRow(R.drawable.ic_warning, si.active
                        ? "Votre abonnement expire dans " + si.daysLeft + (si.daysLeft > 1 ? " jours !" : " jour !")
                        : "Votre abonnement a expiré !",
                    "Renouveler maintenant via WhatsApp",
                    () -> openWhatsApp("Bonjour, je souhaite renouveler mon abonnement Wise IPTV. Device Key : " + key));
            }
            addRow(R.drawable.ic_person, si.displayName().isEmpty() ? "Appareil Wise IPTV" : si.displayName(),
                (si.active ? "Abonnement actif" : "Abonnement expiré") + " · Expire le " + si.formattedExpiry()
                    + " · " + Math.max(0, si.daysLeft) + " jours restants", null);
            ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            pb.setMax(100);
            pb.setProgressDrawable(getResources().getDrawable(R.drawable.progress_gradient));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(8));
            lp.bottomMargin = dp(14);
            pageContent.addView(pb, lp);
            Fx.progressTo(pb, si.active ? si.remainingPercent() : 0, 900);
        }

        addSection("Historique des abonnements");
        List<SubscriptionHistory.Entry> hist = SubscriptionHistory.list(this);
        if (hist.isEmpty()) addInfo("Aucun historique pour le moment.");
        for (SubscriptionHistory.Entry e : hist) {
            addRow(R.drawable.ic_check_circle, e.title(), e.range() + "  ·  " + (e.active() ? "Actif" : "Expiré"), null);
        }

        addSection("Statistiques");
        final TextView stats = addInfo("Chargement…");
        bg(() -> {
            final int live = db.channelDao().countByType(ChannelEntity.TYPE_LIVE);
            final int films = db.channelDao().countByType(ChannelEntity.TYPE_FILM);
            final int series = db.channelDao().countByType(ChannelEntity.TYPE_SERIES);
            final int favs = db.channelDao().countFavorites();
            runOnUiThread(() -> stats.setText("Chaînes : " + live + "   ·   Films : " + films
                + "   ·   Séries : " + series + "   ·   Favoris : " + favs));
        });

        addSection("Consommation de données");
        final TextView usage = addInfo("");
        bindUsage(usage);
        addRow(R.drawable.ic_refresh, "Réinitialiser les statistiques", "Remettre les compteurs à zéro", () ->
            new AlertDialog.Builder(this).setTitle("Réinitialiser ?")
                .setMessage("Les statistiques de consommation seront remises à zéro.")
                .setPositiveButton("Réinitialiser", (d, w) -> { DataUsage.reset(this); bindUsage(usage); })
                .setNegativeButton("Annuler", null).show());

        addSection("Appareil");
        addRow(R.drawable.ic_link, "Device Key : " + key, "Appuyez pour copier", () -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) cm.setPrimaryClip(ClipData.newPlainText("device_key", key));
            Toast.makeText(this, "Clé copiée !", Toast.LENGTH_SHORT).show();
        });
        addRow(R.drawable.ic_chat, "Besoin d'aide ?", "Contactez notre support WhatsApp",
            () -> openWhatsApp("Bonjour, j'ai besoin d'aide avec Wise IPTV. Device Key : " + key));
    }

    private void bindUsage(TextView tv) {
        tv.setText("Aujourd'hui : " + DataUsage.format(DataUsage.forDay(this, 0))
            + "\n7 derniers jours : " + DataUsage.format(DataUsage.lastDays(this, 7))
            + "\nCe mois-ci : " + DataUsage.format(DataUsage.thisMonth(this))
            + "\nTotal : " + DataUsage.format(DataUsage.total(this)));
    }

    // ── Pages : Paramètres ────────────────────────────────────────

    private void buildSettingsPage() {
        addTitle("Paramètres");
        addRow(R.drawable.ic_tune, "Apparence", "Thème, couleurs, affichage", this::showAppearance);
        addRow(R.drawable.ic_info, "Langue",
            "Langue audio préférée : " + AppSettings.AUDIO_LABELS[AppSettings.audioLangIndex(this)], this::showLanguage);
        addRow(R.drawable.ic_play, "Lecteur vidéo",
            "Qualité par défaut : " + PlayerEngine.QUALITY_LABELS[AppSettings.defaultQuality(this)]
                + " · Sous-titres : " + (AppSettings.subtitles(this) ? "oui" : "non"), this::showPlayerOptions);
        addRow(R.drawable.ic_search, "Contrôle parental",
            ParentalControl.isEnabled(this) ? "Activé" : "Désactivé", this::showParental);
        addRow(R.drawable.ic_grid, "Qualité réseau", networkLabel(), this::showNetwork);
        addRow(R.drawable.ic_refresh, "Actualisation des chaînes",
            AppSettings.REFRESH_LABELS[AppSettings.refreshFreq(this)]
                + (AppSettings.refreshWifiOnly(this) ? " · Wi-Fi uniquement" : ""), this::showRefreshFreq);
        addRow(R.drawable.ic_refresh, "Actualiser maintenant", "Retélécharger les chaînes", () -> {
            Toast.makeText(this, "Actualisation en cours…", Toast.LENGTH_SHORT).show();
            syncRetries = 0;
            refreshFromPanel(true);
        });
        addRow(R.drawable.ic_chat, "Support WhatsApp", "Obtenir de l'aide rapidement", () -> openWhatsApp(""));
        addRow(R.drawable.ic_info, "À propos", "Wise IPTV — par W Design", () -> showSection(S_ABOUT));
    }

    private String networkLabel() {
        int m = AppSettings.networkMode(this);
        return m == AppSettings.NET_SAVER ? "Économie de données (480p max)"
            : m == AppSettings.NET_MAX ? "Qualité maximale" : "Automatique";
    }

    private void showAppearance() {
        final String[] items = {"Animations de l'interface", "Badges « EN DIRECT »"};
        final boolean[] on = {AppSettings.animations(this), AppSettings.liveBadges(this)};
        new AlertDialog.Builder(this).setTitle("Apparence")
            .setMultiChoiceItems(items, on, (d, i, checked) -> on[i] = checked)
            .setPositiveButton("OK", (d, w) -> {
                AppSettings.setAnimations(this, on[0]);
                AppSettings.setLiveBadges(this, on[1]);
            }).setNegativeButton("Annuler", null).show();
    }

    private void showLanguage() {
        new AlertDialog.Builder(this).setTitle("Langue audio préférée")
            .setSingleChoiceItems(AppSettings.AUDIO_LABELS, AppSettings.audioLangIndex(this), (d, which) -> {
                AppSettings.setAudioLangIndex(this, which);
                d.dismiss();
                rebuildSettings();
            }).show();
    }

    private void showPlayerOptions() {
        new AlertDialog.Builder(this).setTitle("Lecteur vidéo")
            .setItems(new String[]{"Qualité par défaut", "Sous-titres : "
                + (AppSettings.subtitles(this) ? "activés" : "désactivés")}, (d, which) -> {
                if (which == 0) {
                    new AlertDialog.Builder(this).setTitle("Qualité par défaut")
                        .setSingleChoiceItems(PlayerEngine.QUALITY_LABELS, AppSettings.defaultQuality(this), (d2, q) -> {
                            AppSettings.setDefaultQuality(this, q); d2.dismiss(); rebuildSettings();
                        }).show();
                } else {
                    AppSettings.setSubtitles(this, !AppSettings.subtitles(this));
                    rebuildSettings();
                }
            }).show();
    }

    private void showNetwork() {
        String[] items = {"Automatique", "Économie de données (480p max)", "Qualité maximale"};
        new AlertDialog.Builder(this).setTitle("Qualité réseau")
            .setSingleChoiceItems(items, AppSettings.networkMode(this), (d, which) -> {
                AppSettings.setNetworkMode(this, which); d.dismiss(); rebuildSettings();
            }).show();
    }

    private void showRefreshFreq() {
        new AlertDialog.Builder(this).setTitle("Actualisation des chaînes")
            .setSingleChoiceItems(AppSettings.REFRESH_LABELS, AppSettings.refreshFreq(this), (d, which) -> {
                AppSettings.setRefreshFreq(this, which); d.dismiss(); rebuildSettings();
            })
            .setNeutralButton("Wi-Fi uniquement : " + (AppSettings.refreshWifiOnly(this) ? "oui" : "non"), (d, w) -> {
                AppSettings.setRefreshWifiOnly(this, !AppSettings.refreshWifiOnly(this)); rebuildSettings();
            })
            .setNegativeButton("Fermer", null).show();
    }

    private void showParental() {
        if (!ParentalControl.isEnabled(this)) {
            askPin("Créer un code PIN (4 chiffres)", pin -> {
                if (pin.length() != 4) { toast("Le code doit comporter 4 chiffres."); return; }
                ParentalControl.enable(this, pin);
                toast("Contrôle parental activé.");
                rebuildSettings();
            });
            return;
        }
        askPin("Entrez votre code PIN", pin -> {
            if (!ParentalControl.checkPin(this, pin)) { toast("Code PIN incorrect."); return; }
            new AlertDialog.Builder(this).setTitle("Contrôle parental")
                .setItems(new String[]{"Changer le code PIN", "Désactiver le contrôle parental"}, (d, which) -> {
                    if (which == 0) {
                        askPin("Nouveau code PIN (4 chiffres)", np -> {
                            if (np.length() != 4) { toast("Le code doit comporter 4 chiffres."); return; }
                            ParentalControl.enable(this, np);
                            toast("Code PIN modifié.");
                        });
                    } else {
                        ParentalControl.disable(this);
                        toast("Contrôle parental désactivé.");
                        rebuildSettings();
                    }
                }).show();
        });
    }

    private interface PinCallback { void onPin(String pin); }

    private void askPin(String title, final PinCallback cb) {
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        et.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(4)});
        et.setTextColor(0xFFFFFFFF);
        new AlertDialog.Builder(this).setTitle(title).setView(et)
            .setPositiveButton("OK", (d, w) -> cb.onPin(et.getText().toString()))
            .setNegativeButton("Annuler", null).show();
    }

    private void rebuildSettings() {
        if (section != S_SETTINGS) return;
        pageContent.removeAllViews();
        buildSettingsPage();
    }

    // ── Pages : À propos ──────────────────────────────────────────

    private void buildAboutPage() {
        addTitle("À propos");
        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        ImageView logo = new ImageView(this);
        logo.setImageResource(R.mipmap.ic_launcher);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        hero.addView(logo, new LinearLayout.LayoutParams(dp(140), dp(140)));
        if (logoPulse != null) logoPulse.cancel();
        logoPulse = Fx.pulse(logo, 1.06f, 1400);
        hero.addView(label("Wise IPTV", 26, 0xFFFFFFFF, true));
        hero.addView(label("v" + versionName(), 14, 0xFF4DA3FF, false));
        hero.addView(label("Plus qu'une application, une expérience.", 15, 0xFF8FA6CC, false));
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        hp.bottomMargin = dp(18);
        pageContent.addView(hero, hp);

        addRow(R.drawable.ic_info, "Développé par W Design",
            "Agence d'Architecture · Design · Innovation", null);
        addRow(R.drawable.ic_link, "Site web", LegalTexts.WEB_URL, () -> openUrl(LegalTexts.WEB_URL));
        addRow(R.drawable.ic_phone, "Nous contacter", "+240 222 886 007", () -> openWhatsApp(""));
        addRow(R.drawable.ic_info, "Mentions légales", "Éditeur, service, propriété intellectuelle",
            () -> showText("Mentions légales", LegalTexts.LEGAL_TEXT));
        addRow(R.drawable.ic_info, "Politique de confidentialité", "Données collectées et vos droits",
            () -> showText("Politique de confidentialité", LegalTexts.PRIVACY_TEXT));
    }

    private void showText(String title, String text) {
        ScrollView sv = new ScrollView(this);
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTextSize(15);
        tv.setPadding(dp(24), dp(12), dp(24), dp(12));
        tv.setFocusable(true);
        tv.setFocusableInTouchMode(true);
        sv.addView(tv);
        new AlertDialog.Builder(this).setTitle(title).setView(sv)
            .setPositiveButton("Fermer", null).show();
    }

    // ── Aides de construction des pages ───────────────────────────

    private int dp(int v) { return Math.round(v * getResources().getDisplayMetrics().density); }

    private TextView label(String text, int sp, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(sp);
        t.setTextColor(color);
        t.setGravity(android.view.Gravity.CENTER);
        if (bold) t.setTypeface(null, android.graphics.Typeface.BOLD);
        return t;
    }

    private void addTitle(String t) {
        TextView v = label(t, 28, 0xFFFFFFFF, true);
        v.setGravity(android.view.Gravity.START);
        v.setPadding(0, 0, 0, dp(14));
        pageContent.addView(v);
    }

    private void addSection(String t) {
        TextView v = label(t, 18, 0xFFFFFFFF, true);
        v.setGravity(android.view.Gravity.START);
        v.setPadding(0, dp(16), 0, dp(8));
        pageContent.addView(v);
    }

    private TextView addInfo(String t) {
        TextView v = label(t, 15, 0xFFC9D8F2, false);
        v.setGravity(android.view.Gravity.START);
        v.setBackgroundResource(R.drawable.bg_card);
        v.setPadding(dp(16), dp(14), dp(16), dp(14));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(8);
        pageContent.addView(v, lp);
        return v;
    }

    private View addRow(int icon, String title, String sub, Runnable action) {
        View r = getLayoutInflater().inflate(R.layout.row_tv_setting, pageContent, false);
        ((ImageView) r.findViewById(R.id.row_icon)).setImageResource(icon);
        ((TextView) r.findViewById(R.id.row_title)).setText(title);
        ((TextView) r.findViewById(R.id.row_sub)).setText(sub);
        if (action == null) { r.setClickable(false); r.setFocusable(false); }
        else r.setOnClickListener(v -> action.run());
        pageContent.addView(r);
        return r;
    }

    private void toast(String m) { Toast.makeText(this, m, Toast.LENGTH_SHORT).show(); }

    private String versionName() {
        try { return getPackageManager().getPackageInfo(getPackageName(), 0).versionName; }
        catch (Exception e) { return "2.0.0"; }
    }

    private void openUrl(String url) {
        try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); }
        catch (Exception e) { toast("Ouvrez : " + url); }
    }

    private void openWhatsApp(String message) {
        String u = "https://wa.me/" + SubscriptionInfo.WHATSAPP
            + (message.isEmpty() ? "" : "?text=" + Uri.encode(message));
        try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(u))); }
        catch (Exception e) { toast("WhatsApp : +240 222 886 007"); }
    }

    // ── Liste des chaînes ─────────────────────────────────────────

    private class ChAdapter extends RecyclerView.Adapter<ChAdapter.VH> {
        private List<ChannelEntity> data = new ArrayList<>();

        void set(List<ChannelEntity> l) {
            data = l != null ? l : new ArrayList<>();
            notifyDataSetChanged();
        }

        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_tv_channel, p, false));
        }

        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            final ChannelEntity ch = data.get(pos);
            h.name.setText(ch.name);
            h.group.setText(ch.groupTitle == null ? "" : ch.groupTitle);
            h.live.setVisibility(ch.contentType == ChannelEntity.TYPE_LIVE
                && AppSettings.liveBadges(TvMainActivity.this) ? View.VISIBLE : View.GONE);
            if (ch.logoUrl != null && !ch.logoUrl.isEmpty()) {
                Glide.with(h.logo).load(ch.logoUrl).fitCenter()
                    .placeholder(R.drawable.ic_channel_placeholder).into(h.logo);
            } else {
                h.logo.setImageResource(R.drawable.ic_channel_placeholder);
            }
            h.itemView.setScaleX(1f); h.itemView.setScaleY(1f);
            h.itemView.setOnFocusChangeListener((v, f) -> {
                v.animate().scaleX(f ? 1.08f : 1f).scaleY(f ? 1.08f : 1f).setDuration(140).start();
                if (f) select(ch, true);
            });
            h.itemView.setOnClickListener(v -> { select(ch, false); openPlayer(ch, ch.streamUrl); });
        }

        @Override public int getItemCount() { return data.size(); }

        class VH extends RecyclerView.ViewHolder {
            final ImageView logo; final TextView name, group, live;
            VH(View v) {
                super(v);
                logo = v.findViewById(R.id.img_logo);
                name = v.findViewById(R.id.tv_name);
                group = v.findViewById(R.id.tv_group);
                live = v.findViewById(R.id.tv_live);
            }
        }
    }

    /** Rangées style Netflix : une rangée horizontale par catégorie (une seule rangée pour Accueil / Favoris). */
    private class RowsAdapter extends RecyclerView.Adapter<RowsAdapter.RVH> {
        private final List<String> titles = new ArrayList<>();
        private final List<List<ChannelEntity>> rows = new ArrayList<>();
        private final RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();

        RowsAdapter() { pool.setMaxRecycledViews(0, 30); }

        void set(List<ChannelEntity> list) {
            titles.clear(); rows.clear();
            if (list != null && !list.isEmpty()) {
                if (section == S_HOME || section == S_FAVS) {
                    titles.add(""); rows.add(list);
                } else {
                    LinkedHashMap<String, List<ChannelEntity>> map = new LinkedHashMap<>();
                    for (ChannelEntity c : list) {
                        String g = c.groupTitle == null || c.groupTitle.trim().isEmpty() ? "Autres" : c.groupTitle.trim();
                        List<ChannelEntity> l = map.get(g);
                        if (l == null) { l = new ArrayList<>(); map.put(g, l); }
                        l.add(c);
                    }
                    for (Map.Entry<String, List<ChannelEntity>> e : map.entrySet()) {
                        titles.add(e.getKey()); rows.add(e.getValue());
                    }
                }
            }
            notifyDataSetChanged();
        }

        @NonNull @Override public RVH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new RVH(LayoutInflater.from(p.getContext()).inflate(R.layout.row_tv_rail, p, false));
        }

        @Override public void onBindViewHolder(@NonNull RVH h, int pos) {
            String t = titles.get(pos);
            h.title.setText(t);
            h.title.setVisibility(t.isEmpty() ? View.GONE : View.VISIBLE);
            h.inner.set(rows.get(pos));
        }

        @Override public int getItemCount() { return rows.size(); }

        class RVH extends RecyclerView.ViewHolder {
            final TextView title; final ChAdapter inner;
            RVH(View v) {
                super(v);
                title = v.findViewById(R.id.rail_row_title);
                RecyclerView rv = v.findViewById(R.id.rail_row_list);
                rv.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false));
                rv.setRecycledViewPool(pool);
                inner = new ChAdapter();
                rv.setAdapter(inner);
            }
        }
    }

    // ── Cycle de vie / touches ────────────────────────────────────

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && section != S_HOME) {
            showSection(S_HOME);
            if (navViews[S_HOME] != null) navViews[S_HOME].requestFocus();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override protected void onResume() {
        super.onResume();
        PlayerHandoff.discard();                 // lecteur déposé mais jamais repris → libéré
        StatsReporter.report(this, false);       // stats + modèle de l'appareil → panel (1 envoi / 10 min max)
        bindSubscriptionPill();
        if (section == S_HOME) loadHome();
        if (isBrowse(section) && selected != null) {
            handler.removeCallbacks(previewTask);
            handler.postDelayed(previewTask, 400);
        }
    }

    @Override protected void onPause() {
        handler.removeCallbacks(previewTask);
        if (previewEngine != null) previewEngine.pause();
        DataUsage.flush(this);
        super.onPause();
    }

    @Override protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if (logoPulse != null) logoPulse.cancel();
        if (previewView != null) previewView.setPlayer(null);
        if (previewEngine != null) { previewEngine.release(); previewEngine = null; }
        recorder.stop();
        io.shutdown();
        super.onDestroy();
    }
}
