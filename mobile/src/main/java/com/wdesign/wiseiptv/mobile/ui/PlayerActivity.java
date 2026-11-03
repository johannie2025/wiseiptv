package com.wdesign.wiseiptv.mobile.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.*;
import android.view.*;
import android.widget.*;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.epg.EpgClient;
import com.wdesign.wiseiptv.core.player.PlayerEngine;
import com.wdesign.wiseiptv.core.util.AdaptiveBg;
import com.wdesign.wiseiptv.core.util.AppSettings;
import com.wdesign.wiseiptv.core.util.Recorder;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.adapter.ChannelAdapter;
import com.wdesign.wiseiptv.mobile.util.ParentalControl;
import com.wdesign.wiseiptv.mobile.util.ResumeStore;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Lecteur : mode normal (vidéo 16:9 + infos, actions, chaînes similaires) puis plein écran
 * (bouton ou rotation). L'état (chaîne, position, plein écran) est mémorisé pour la reprise.
 */
@OptIn(markerClass = UnstableApi.class)
public class PlayerActivity extends AppCompatActivity
        implements PlayerEngine.Listener, ChannelAdapter.OnChannelClick {

    public static final String EXTRA_ID = "ch_id", EXTRA_NAME = "ch_name", EXTRA_URL = "ch_url", EXTRA_TYPE = "ch_type";
    public static final String EXTRA_GROUP = "extra_group";
    public static final String EXTRA_ORDER = "extra_order";
    public static final String EXTRA_START_POSITION = "start_position";
    public static final String EXTRA_FULLSCREEN = "start_fullscreen";

    private PlayerView playerView;
    private PlayerEngine engine;
    private ProgressBar progressBar;
    private TextView tvChannelName, tvQuality, tvError, tvTopTitle, infoBadge, infoName, infoGroup, actFavLabel;
    private ImageButton btnFav, btnQuality, btnBack, btnBackTop, btnPlayPause, btnMute, btnFullscreen;
    private ImageView actFavIcon;
    private View osdTop, osdBottom, zapControls, topBar, infoSection, videoContainer, similarHeader, epgEmpty;
    private RecyclerView rvSimilar;
    private ChannelAdapter similarAdapter;
    private AppDatabase db;
    private long channelId;
    private String channelName, streamUrl, groupTitle;
    private int contentType, sortOrder;
    private boolean isFav, fullscreen, paused, replaying;
    private LinearLayout epgList;
    private TextView btnBackLive;
    private EpgClient.Source epgSource;
    private ChannelEntity current;
    private final Recorder recorder = new Recorder();
    private String playingUrl;
    private TextView actRecLabel;
    private ImageView actRecIcon;
    private boolean orientationLocked;
    private int resizeIndex = 1;   // 0 = ajusté, 1 = remplir (zoom), 2 = étirer
    private AudioManager audioManager;
    private GestureDetector gestureDetector;
    private final ExecutorService zapExecutor = Executors.newSingleThreadExecutor();
    private final Handler osdHandler = new Handler(Looper.getMainLooper());
    private final Runnable hideOsd = () -> {
        osdBottom.animate().alpha(0).setDuration(300).start();
        if (fullscreen) {
            osdTop.animate().alpha(0).setDuration(300).start();
            zapControls.animate().alpha(0).setDuration(300).start();
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams wl = getWindow().getAttributes();
            wl.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(wl);
        }
        setContentView(R.layout.activity_player);
        db = AppDatabase.get(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        channelId   = getIntent().getLongExtra(EXTRA_ID, -1);
        channelName = getIntent().getStringExtra(EXTRA_NAME);
        streamUrl   = getIntent().getStringExtra(EXTRA_URL);
        contentType = getIntent().getIntExtra(EXTRA_TYPE, 0);
        groupTitle  = getIntent().getStringExtra(EXTRA_GROUP);
        sortOrder   = getIntent().getIntExtra(EXTRA_ORDER, 0);
        rememberRecentChannel();

        playerView     = findViewById(R.id.player_view);
        progressBar    = findViewById(R.id.progress_bar);
        tvChannelName  = findViewById(R.id.tv_channel_name);
        tvQuality      = findViewById(R.id.tv_quality);
        tvError        = findViewById(R.id.tv_error);
        tvTopTitle     = findViewById(R.id.tv_top_title);
        infoBadge      = findViewById(R.id.info_badge);
        infoName       = findViewById(R.id.info_name);
        infoGroup      = findViewById(R.id.info_group);
        actFavIcon     = findViewById(R.id.act_fav_icon);
        actFavLabel    = findViewById(R.id.act_fav_label);
        btnFav         = findViewById(R.id.btn_fav);
        btnQuality     = findViewById(R.id.btn_quality);
        btnBack        = findViewById(R.id.btn_back);
        btnBackTop     = findViewById(R.id.btn_back_top);
        btnPlayPause   = findViewById(R.id.btn_play_pause);
        btnMute        = findViewById(R.id.btn_mute);
        btnFullscreen  = findViewById(R.id.btn_fullscreen);
        osdTop         = findViewById(R.id.osd_top);
        osdBottom      = findViewById(R.id.osd_bottom);
        zapControls    = findViewById(R.id.osd_zap_controls);
        topBar         = findViewById(R.id.top_bar);
        infoSection    = findViewById(R.id.info_section);
        videoContainer = findViewById(R.id.video_container);
        similarHeader  = findViewById(R.id.similar_header);
        epgEmpty       = findViewById(R.id.tv_epg_empty);
        rvSimilar      = findViewById(R.id.rv_similar);
        epgList        = findViewById(R.id.epg_list);
        btnBackLive    = findViewById(R.id.btn_back_live);
        actRecLabel    = findViewById(R.id.act_rec_label);
        actRecIcon     = findViewById(R.id.act_rec_icon);
        btnBackLive.setOnClickListener(v -> backToLive());

        similarAdapter = new ChannelAdapter(this, R.layout.item_channel_h);
        rvSimilar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSimilar.setAdapter(similarAdapter);

        View.OnClickListener back = v -> finish();
        btnBack.setOnClickListener(back);
        btnBackTop.setOnClickListener(back);
        findViewById(R.id.btn_previous).setOnClickListener(v -> zapChannel(-1));
        findViewById(R.id.btn_next).setOnClickListener(v -> zapChannel(1));
        btnQuality.setOnClickListener(v -> showQualityPicker());
        btnFav.setOnClickListener(v -> toggleFav());
        findViewById(R.id.act_fav).setOnClickListener(v -> toggleFav());
        findViewById(R.id.act_share).setOnClickListener(v -> shareChannel());
        findViewById(R.id.act_record).setOnClickListener(v -> toggleRecord());
        findViewById(R.id.act_info).setOnClickListener(v -> showInfoDialog());
        findViewById(R.id.btn_similar_all).setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra(MainActivity.EXTRA_NAV, R.id.nav_live);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });
        btnFullscreen.setOnClickListener(v -> toggleFullscreen());
        btnFullscreen.setOnLongClickListener(v -> {
            resizeIndex = (resizeIndex + 1) % 3;
            applyResizeMode();
            Toast.makeText(this, resizeIndex == 0 ? "Image ajustée" : resizeIndex == 1
                ? "Image remplie" : "Image étirée", Toast.LENGTH_SHORT).show();
            return true;
        });
        btnPlayPause.setOnClickListener(v -> togglePause());
        btnMute.setOnClickListener(v -> {
            if (engine == null) return;
            engine.setMuted(!engine.isMuted());
            btnMute.setImageResource(engine.isMuted() ? R.drawable.ic_volume_off : R.drawable.ic_volume);
            showOsd();
        });

        // Hauteur 16:9 de la zone vidéo
        videoContainer.post(this::applyLayoutForMode);

        engine = new PlayerEngine(this, this);
        engine.attachView(playerView);
        playingUrl = streamUrl;
        engine.play(streamUrl);
        tvQuality.setText(PlayerEngine.QUALITY_LABELS[engine.getQuality()]);
        long startPosition = getIntent().getLongExtra(EXTRA_START_POSITION, 0);
        if (startPosition > 0) engine.seekTo(startPosition);

        bindChannelViews();
        loadFavState();
        loadSimilar();
        loadEpg();
        setupGestures();
        showOsd();

        boolean startFull = getIntent().getBooleanExtra(EXTRA_FULLSCREEN, false)
            || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (startFull) setFullscreen(true, getIntent().getBooleanExtra(EXTRA_FULLSCREEN, false));
    }

    // ── Plein écran / mise en page ────────────────────────────────

    private void toggleFullscreen() {
        setFullscreen(!fullscreen, true);
    }

    private void setFullscreen(boolean on, boolean forceOrientation) {
        fullscreen = on;
        if (forceOrientation) {
            // Bascule immédiate et verrouillée : paysage tant qu'on est en plein écran,
            // portrait à la sortie. Le capteur n'est rendu qu'une fois la rotation effective.
            orientationLocked = true;
            setRequestedOrientation(on ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                                       : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (!on) osdHandler.postDelayed(this::releaseOrientation, 1200);
        }
        applyLayoutForMode();
        persistState();
    }

    private void releaseOrientation() {
        if (isFinishing() || isDestroyed() || fullscreen) return;
        orientationLocked = false;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    private void applyResizeMode() {
        if (playerView == null) return;
        if (!fullscreen) { playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT); return; }
        playerView.setResizeMode(resizeIndex == 0 ? AspectRatioFrameLayout.RESIZE_MODE_FIT
            : resizeIndex == 1 ? AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            : AspectRatioFrameLayout.RESIZE_MODE_FILL);
    }

    private void applyLayoutForMode() {
        ViewGroup.LayoutParams lp = videoContainer.getLayoutParams();
        if (fullscreen) {
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            topBar.setVisibility(View.GONE);
            infoSection.setVisibility(View.GONE);
            osdTop.setVisibility(View.VISIBLE);
            zapControls.setVisibility(View.VISIBLE);
            btnFullscreen.setImageResource(R.drawable.ic_fullscreen_exit);
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            int w = getResources().getDisplayMetrics().widthPixels;
            lp.height = Math.round(Math.min(w, getResources().getDisplayMetrics().heightPixels) * 9f / 16f);
            topBar.setVisibility(View.VISIBLE);
            infoSection.setVisibility(View.VISIBLE);
            osdTop.setVisibility(View.GONE);
            zapControls.setVisibility(View.GONE);
            btnFullscreen.setImageResource(R.drawable.ic_fullscreen);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        videoContainer.setLayoutParams(lp);
        applyResizeMode();
        View root = findViewById(R.id.player_root);
        if (fullscreen) root.setBackgroundColor(0xFF000000);
        else AdaptiveBg.apply(root, current != null ? current.logoUrl : null);
        showOsd();
    }

    @Override public void onConfigurationChanged(Configuration c) {
        super.onConfigurationChanged(c);
        boolean land = c.orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (orientationLocked) { if (land == fullscreen) applyLayoutForMode(); return; }
        if (land != fullscreen) setFullscreen(land, false);
    }

    @Override public void onBackPressed() {
        if (fullscreen) { setFullscreen(false, true); return; }
        super.onBackPressed();
    }

    // ── Lecture ───────────────────────────────────────────────────

    private void togglePause() {
        if (engine == null) return;
        paused = !paused;
        if (paused) engine.pause(); else engine.resume();
        btnPlayPause.setImageResource(paused ? R.drawable.ic_play : R.drawable.ic_pause);
        showOsd();
    }

    @Override public void onBuffering(boolean b) { runOnUiThread(() -> progressBar.setVisibility(b ? View.VISIBLE : View.GONE)); }
    @Override public void onPlaying()           { runOnUiThread(() -> { tvError.setVisibility(View.GONE); progressBar.setVisibility(View.GONE); }); }
    @Override public void onError(String msg)   { runOnUiThread(() -> { tvError.setText("⚠ " + msg); tvError.setVisibility(View.VISIBLE); progressBar.setVisibility(View.GONE); }); }

    private void showQualityPicker() {
        new android.app.AlertDialog.Builder(this)
            .setTitle("Qualité vidéo")
            .setSingleChoiceItems(PlayerEngine.QUALITY_LABELS, engine.getQuality(), (d, which) -> {
                engine.setQuality(which); tvQuality.setText(PlayerEngine.QUALITY_LABELS[which]); d.dismiss();
            }).show();
    }

    private void showOsd() {
        osdBottom.animate().alpha(1).setDuration(200).start();
        if (fullscreen) {
            osdTop.animate().alpha(1).setDuration(200).start();
            zapControls.setVisibility(View.VISIBLE);
            zapControls.animate().alpha(1).setDuration(200).start();
        }
        osdHandler.removeCallbacks(hideOsd);
        osdHandler.postDelayed(hideOsd, 4000);
    }

    private void setupGestures() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_DISTANCE = 90;
            private static final int SWIPE_VELOCITY = 100;

            @Override public boolean onDown(MotionEvent event) { return true; }

            @Override public boolean onSingleTapUp(MotionEvent e) { showOsd(); return true; }

            @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
                if (e1 == null || !fullscreen) return false;
                float w = playerView.getWidth(); float absY = Math.abs(dy);
                if (absY > 30) {
                    if (e1.getX() < w / 2) {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.screenBrightness = Math.max(0.1f, Math.min(1f,
                            (lp.screenBrightness < 0 ? 0.5f : lp.screenBrightness) + (dy > 0 ? 0.04f : -0.04f)));
                        getWindow().setAttributes(lp);
                    } else {
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            dy > 0 ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                    }
                    return true;
                }
                return false;
            }

            @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;
                float deltaX = e2.getX() - e1.getX();
                float deltaY = e2.getY() - e1.getY();
                if (Math.abs(deltaX) > Math.abs(deltaY)
                    && Math.abs(deltaX) > SWIPE_DISTANCE
                    && Math.abs(velocityX) > SWIPE_VELOCITY) {
                    zapChannel(deltaX < 0 ? 1 : -1);
                    return true;
                }
                return false;
            }
        });
        playerView.setOnTouchListener((v, e) -> gestureDetector.onTouchEvent(e));
    }

    // ── Chaîne courante ───────────────────────────────────────────

    private void zapChannel(int direction) {
        if (channelId < 0) return;
        zapExecutor.execute(() -> {
            ChannelEntity target = direction < 0
                ? db.channelDao().getPrev(sortOrder, contentType, groupTitle)
                : db.channelDao().getNext(sortOrder, contentType, groupTitle);
            if (target == null) return;
            runOnUiThread(() -> switchTo(target));
        });
    }

    /** Charge une autre chaîne dans le même lecteur. */
    private void switchTo(ChannelEntity target) {
        if (isFinishing() || isDestroyed()) return;
        channelId = target.id;
        channelName = target.name;
        streamUrl = target.streamUrl;
        groupTitle = target.groupTitle;
        sortOrder = target.sortOrder;
        contentType = target.contentType;
        paused = false;
        replaying = false;
        btnPlayPause.setImageResource(R.drawable.ic_pause);
        rememberRecentChannel();
        bindChannelViews();
        tvChannelName.setAlpha(0f);
        tvChannelName.animate().alpha(1).setDuration(160).start();
        playingUrl = streamUrl;
        engine.play(streamUrl);
        showOsd();
        loadFavState();
        loadSimilar();
        loadEpg();
        persistState();
    }

    @Override public void onClick(ChannelEntity ch) { switchTo(ch); }

    private void bindChannelViews() {
        boolean live = contentType == ChannelEntity.TYPE_LIVE && AppSettings.liveBadges(this);
        tvChannelName.setText(channelName);
        tvTopTitle.setText(channelName);
        infoName.setText(channelName);
        infoGroup.setText(groupTitle != null && !groupTitle.isEmpty() ? groupTitle : "");
        infoGroup.setVisibility(infoGroup.length() == 0 ? View.GONE : View.VISIBLE);
        findViewById(R.id.tv_live_badge).setVisibility(live ? View.VISIBLE : View.GONE);
        infoBadge.setVisibility(live ? View.VISIBLE : View.GONE);
        btnBackLive.setVisibility(replaying ? View.VISIBLE : View.GONE);
    }

    // ── Guide des programmes (EPG) + Replay ───────────────────────

    private void loadEpg() {
        epgList.removeAllViews();
        View section = findViewById(R.id.epg_section);
        if (contentType != ChannelEntity.TYPE_LIVE) { section.setVisibility(View.GONE); return; }
        section.setVisibility(View.VISIBLE);
        epgEmpty.setVisibility(View.VISIBLE);
        ((TextView) epgEmpty).setText("Chargement du programme…");
        final long id = channelId;
        zapExecutor.execute(() -> {
            ChannelEntity full = db.channelDao().findById(id);
            final ChannelEntity ch = full != null ? full : channelStub();
            runOnUiThread(() -> { if (isFinishing() || isDestroyed() || id != channelId) return; EpgClient.load(this, ch, new EpgClient.Callback() {
            @Override public void onResult(List<EpgClient.Program> programs, EpgClient.Source source) {
                if (isFinishing() || isDestroyed() || id != channelId) return;
                epgSource = source;
                renderEpg(programs);
            }
            @Override public void onError(String message) {
                if (isFinishing() || isDestroyed() || id != channelId) return;
                epgSource = null;
                ((TextView) epgEmpty).setText(message);
                epgEmpty.setVisibility(View.VISIBLE);
            }
        }); });
        });
    }

    /** Chaîne minimale (URL + nom) pour le guide quand la fiche complète n'est pas encore chargée. */
    private ChannelEntity channelStub() {
        ChannelEntity c = new ChannelEntity();
        c.id = channelId; c.name = channelName == null ? "" : channelName;
        c.streamUrl = streamUrl; c.groupTitle = groupTitle; c.contentType = contentType;
        return c;
    }

    private void renderEpg(List<EpgClient.Program> list) {
        epgEmpty.setVisibility(View.GONE);
        epgList.removeAllViews();
        final long now = System.currentTimeMillis() / 1000;
        LayoutInflater inf = getLayoutInflater();
        for (EpgClient.Program p : list) {
            View row = inf.inflate(R.layout.row_epg, epgList, false);
            boolean isNow = p.isNow(now), past = p.isPast(now);
            ((TextView) row.findViewById(R.id.tv_epg_time)).setText(p.time());
            ((TextView) row.findViewById(R.id.tv_epg_title)).setText(p.title.isEmpty() ? "Programme" : p.title);
            row.findViewById(R.id.tv_epg_now).setVisibility(isNow ? View.VISIBLE : View.GONE);
            ProgressBar pb = row.findViewById(R.id.epg_progress);
            pb.setVisibility(isNow ? View.VISIBLE : View.GONE);
            pb.setProgress(p.progress(now));
            row.findViewById(R.id.iv_epg_replay).setVisibility(past && p.archive ? View.VISIBLE : View.GONE);
            if (isNow) row.setBackgroundResource(R.drawable.bg_card_hero);
            row.setOnClickListener(v -> onProgramClick(p, past, isNow));
            epgList.addView(row);
        }
    }

    private void onProgramClick(EpgClient.Program p, boolean past, boolean isNow) {
        if (isNow) { backToLive(); return; }
        if (past) {
            if (!p.archive || epgSource == null) {
                Toast.makeText(this, "Replay indisponible pour ce programme.", Toast.LENGTH_SHORT).show();
                return;
            }
            replaying = true;
            paused = false;
            btnPlayPause.setImageResource(R.drawable.ic_pause);
            playingUrl = EpgClient.replayUrl(epgSource, p);
            engine.play(playingUrl);
            infoGroup.setText("Replay · " + p.title);
            infoGroup.setVisibility(View.VISIBLE);
            infoBadge.setVisibility(View.GONE);
            btnBackLive.setVisibility(View.VISIBLE);
            showOsd();
        } else {
            new android.app.AlertDialog.Builder(this)
                .setTitle(p.time() + "  " + p.title)
                .setMessage(p.desc.isEmpty() ? "Aucune description." : p.desc)
                .setPositiveButton("OK", null).show();
        }
    }

    private void backToLive() {
        if (!replaying) return;
        replaying = false;
        paused = false;
        btnPlayPause.setImageResource(R.drawable.ic_pause);
        playingUrl = streamUrl;
        engine.play(streamUrl);
        bindChannelViews();
    }

    private void loadSimilar() {
        final long id = channelId; final int type = contentType; final String grp = groupTitle;
        zapExecutor.execute(() -> {
            List<ChannelEntity> list = db.channelDao().similar(type, id, grp, 30);
            ChannelEntity cur = db.channelDao().findById(id);
            List<ChannelEntity> out = new ArrayList<>();
            if (list != null) for (ChannelEntity c : list) if (!ParentalControl.isRestricted(this, c)) out.add(c);
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed() || id != channelId) return;
                current = cur;
                if (!fullscreen) AdaptiveBg.apply(findViewById(R.id.player_root), cur != null ? cur.logoUrl : null);
                similarAdapter.setData(out);
                int vis = out.isEmpty() ? View.GONE : View.VISIBLE;
                similarHeader.setVisibility(vis);
                rvSimilar.setVisibility(vis);
            });
        });
    }

    // ── Enregistrement ────────────────────────────────────────────

    private void toggleRecord() {
        if (engine == null) return;
        if (engine.isRecording() || recorder.isRecording()) {
            engine.stopRecording();
            recorder.stop();
            return;
        }
        Recorder.Listener l = new Recorder.Listener() {
            @Override public void onStarted(java.io.File file) {
                runOnUiThread(() -> {
                    setRecordingUi(true);
                    Toast.makeText(PlayerActivity.this, "⏺ Enregistrement démarré", Toast.LENGTH_SHORT).show();
                });
            }
            @Override public void onFinished(java.io.File file, long bytes, String error) {
                runOnUiThread(() -> {
                    setRecordingUi(false);
                    if (isDestroyed()) return;
                    String msg = error != null ? "Enregistrement interrompu : " + error
                        : bytes > 0 ? "✅ Enregistré (" + com.wdesign.wiseiptv.core.util.DataUsage.format(bytes)
                            + ")\n" + file.getAbsolutePath()
                        : "Rien n'a été enregistré : lancez la lecture puis réessayez.";
                    Toast.makeText(PlayerActivity.this, msg, Toast.LENGTH_LONG).show();
                });
            }
        };
        // Direct / replay : copie du flux déjà lu par le lecteur (une seule connexion au serveur).
        boolean fromPlayer = contentType == ChannelEntity.TYPE_LIVE || replaying;
        if (fromPlayer) {
            if (!engine.startRecording(channelName, l))
                Toast.makeText(this, "Impossible de créer le fichier d'enregistrement.", Toast.LENGTH_LONG).show();
        } else if (playingUrl != null && !playingUrl.isEmpty()) {
            recorder.start(this, playingUrl, channelName, l);
        }
    }

    private void setRecordingUi(boolean on) {
        if (actRecLabel != null) actRecLabel.setText(on ? "Arrêter" : "Enregistrer");
        if (actRecIcon != null) actRecIcon.setColorFilter(on ? 0xFFE5324F : 0xFFFFFFFF);
    }

    private void showInfoDialog() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nom : ").append(channelName);
        if (groupTitle != null && !groupTitle.isEmpty()) sb.append("\nCatégorie : ").append(groupTitle);
        sb.append("\nType : ").append(contentType == ChannelEntity.TYPE_LIVE ? "Chaîne en direct"
            : contentType == ChannelEntity.TYPE_FILM ? "Film" : "Série");
        if (current != null) {
            if (current.country != null && !current.country.isEmpty()) sb.append("\nPays : ").append(current.country);
            if (current.language != null && !current.language.isEmpty()) sb.append("\nLangue : ").append(current.language);
        }
        new android.app.AlertDialog.Builder(this).setTitle("Plus d'infos")
            .setMessage(sb.toString()).setPositiveButton("OK", null).show();
    }

    private void shareChannel() {
        // On ne partage jamais l'URL du flux (elle contient les identifiants de l'abonnement).
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, "Je regarde " + channelName + " sur Wise IPTV 📺");
        startActivity(Intent.createChooser(i, "Partager"));
    }

    private void loadFavState() {
        if (channelId < 0) return;
        final long id = channelId;
        zapExecutor.execute(() -> {
            ChannelEntity ch = db.channelDao().findById(id);
            if (ch != null) { isFav = ch.isFavorite; runOnUiThread(this::updateFavIcon); }
        });
    }

    private void rememberRecentChannel() {
        if (channelId < 0) return;
        android.content.SharedPreferences prefs = getSharedPreferences("wise_mobile_recents", MODE_PRIVATE);
        String oldIds = prefs.getString("ids", "");
        LinkedHashSet<String> ids = new LinkedHashSet<>();
        ids.add(String.valueOf(channelId));
        if (!oldIds.isEmpty()) {
            for (String id : oldIds.split(",")) {
                if (ids.size() >= 20) break;
                if (!id.equals(String.valueOf(channelId))) ids.add(id);
            }
        }
        prefs.edit().putString("ids", android.text.TextUtils.join(",", ids)).apply();
    }

    private void toggleFav() {
        isFav = !isFav; updateFavIcon();
        if (channelId < 0) return;
        final long id = channelId; final boolean f = isFav;
        zapExecutor.execute(() -> db.channelDao().setFavorite(id, f));
    }

    private void updateFavIcon() {
        int res = isFav ? R.drawable.ic_favorite : R.drawable.ic_favorite_border;
        btnFav.setImageResource(res);
        actFavIcon.setImageResource(res);
        actFavIcon.setColorFilter(isFav ? 0xFFE5324F : 0xFFFFFFFF);
        actFavLabel.setText(isFav ? "Favori ✓" : "Favori");
        btnFav.animate().cancel();
        btnFav.setScaleX(0.7f); btnFav.setScaleY(0.7f);
        btnFav.animate().scaleX(1f).scaleY(1f).setDuration(260)
            .setInterpolator(new android.view.animation.OvershootInterpolator(3f)).start();
    }

    // ── Persistance / cycle de vie ────────────────────────────────

    private void persistState() {
        long pos = engine != null && contentType != ChannelEntity.TYPE_LIVE ? engine.getPosition() : 0;
        ResumeStore.setPlayer(this, channelId, pos, fullscreen);
        ResumeStore.setScreen(this, ResumeStore.SCREEN_PLAYER);
    }

    @Override protected void onResume() {
        super.onResume();
        persistState();
        if (engine != null && !paused) engine.resume();
    }

    @Override protected void onStop() {
        persistState();
        super.onStop();
        if (engine != null) engine.pause();
    }

    @Override protected void onDestroy() {
        // Fermeture volontaire du lecteur (retour) → la reprise se fera sur l'accueil
        if (isFinishing()) ResumeStore.setScreen(this, ResumeStore.SCREEN_MAIN);
        super.onDestroy(); osdHandler.removeCallbacksAndMessages(null);
        recorder.stop();
        zapExecutor.shutdownNow();
        if (engine != null) { engine.release(); engine = null; }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
