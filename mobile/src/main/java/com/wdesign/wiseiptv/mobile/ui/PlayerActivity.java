package com.wdesign.wiseiptv.mobile.ui;

import android.content.Context;
import android.media.AudioManager;
import android.os.*;
import android.view.*;
import android.widget.*;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.PlayerView;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.player.PlayerEngine;
import com.wdesign.wiseiptv.mobile.R;
import java.util.concurrent.Executors;

@OptIn(markerClass = UnstableApi.class)
public class PlayerActivity extends AppCompatActivity implements PlayerEngine.Listener {

    public static final String EXTRA_ID    = "ch_id";
    public static final String EXTRA_NAME  = "ch_name";
    public static final String EXTRA_URL   = "ch_url";
    public static final String EXTRA_TYPE  = "ch_type";
    public static final String EXTRA_GROUP = "ch_group";
    public static final String EXTRA_ORDER = "ch_order";

    private PlayerView playerView;
    private PlayerEngine engine;
    private ProgressBar progressBar;
    private TextView tvChannelName, tvQuality, tvError;
    private ImageButton btnFav, btnQuality, btnBack, btnPrev, btnNext;
    private View osdTop;
    private AppDatabase db;
    private long channelId;
    private String channelName, streamUrl, groupTitle;
    private int contentType, sortOrder;
    private boolean isFav;
    private AudioManager audioManager;
    private GestureDetector gestureDetector;
    private final Handler osdHandler = new Handler(Looper.getMainLooper());
    private final Runnable hideOsd = () -> osdTop.animate().alpha(0).setDuration(300).start();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_player);
        db = AppDatabase.get(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        channelId   = getIntent().getLongExtra(EXTRA_ID, -1);
        channelName = getIntent().getStringExtra(EXTRA_NAME);
        streamUrl   = getIntent().getStringExtra(EXTRA_URL);
        contentType = getIntent().getIntExtra(EXTRA_TYPE, 0);
        groupTitle  = getIntent().getStringExtra(EXTRA_GROUP);
        sortOrder   = getIntent().getIntExtra(EXTRA_ORDER, 0);

        playerView    = findViewById(R.id.player_view);
        progressBar   = findViewById(R.id.progress_bar);
        tvChannelName = findViewById(R.id.tv_channel_name);
        tvQuality     = findViewById(R.id.tv_quality);
        tvError       = findViewById(R.id.tv_error);
        btnFav        = findViewById(R.id.btn_fav);
        btnQuality    = findViewById(R.id.btn_quality);
        btnBack       = findViewById(R.id.btn_back);
        btnPrev       = findViewById(R.id.btn_prev);
        btnNext       = findViewById(R.id.btn_next);
        osdTop        = findViewById(R.id.osd_top);

        tvChannelName.setText(channelName);
        btnBack.setOnClickListener(v -> finish());
        btnQuality.setOnClickListener(v -> showQualityPicker());
        btnFav.setOnClickListener(v -> toggleFav());
        if (btnPrev != null) btnPrev.setOnClickListener(v -> zapChannel(-1));
        if (btnNext != null) btnNext.setOnClickListener(v -> zapChannel(+1));
        playerView.setOnClickListener(v -> showOsd());

        engine = new PlayerEngine(this, this);
        engine.attachView(playerView);
        engine.play(streamUrl);
        loadFavState(); setupGestures(); showOsd();
    }

    /** Zapping: -1=précédent, +1=suivant dans le même groupe */
    private void zapChannel(int dir) {
        Executors.newSingleThreadExecutor().execute(() -> {
            ChannelEntity target = dir < 0
                ? db.channelDao().getPrev(sortOrder, contentType, groupTitle)
                : db.channelDao().getNext(sortOrder, contentType, groupTitle);
            if (target == null) return; // Pas de chaîne adjacente
            runOnUiThread(() -> {
                channelId   = target.id;
                channelName = target.name;
                streamUrl   = target.streamUrl;
                groupTitle  = target.groupTitle;
                sortOrder   = target.sortOrder;
                isFav       = target.isFavorite;
                tvChannelName.setText(channelName);
                updateFavIcon();
                engine.play(streamUrl);
                showOsd();
            });
        });
    }

    @Override public void onBuffering(boolean b) { runOnUiThread(() -> progressBar.setVisibility(b ? View.VISIBLE : View.GONE)); }
    @Override public void onPlaying()    { runOnUiThread(() -> { tvError.setVisibility(View.GONE); progressBar.setVisibility(View.GONE); }); }
    @Override public void onError(String msg) { runOnUiThread(() -> { tvError.setText("⚠ " + msg); tvError.setVisibility(View.VISIBLE); progressBar.setVisibility(View.GONE); }); }

    private void showQualityPicker() {
        new android.app.AlertDialog.Builder(this)
            .setTitle("Qualité vidéo")
            .setSingleChoiceItems(PlayerEngine.QUALITY_LABELS, engine.getQuality(), (d, which) -> {
                engine.setQuality(which); tvQuality.setText(PlayerEngine.QUALITY_LABELS[which]); d.dismiss();
            }).show();
    }

    private void showOsd() {
        osdTop.animate().alpha(1).setDuration(200).start();
        osdHandler.removeCallbacks(hideOsd); osdHandler.postDelayed(hideOsd, 4000);
    }

    private void setupGestures() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final float SWIPE_THRESHOLD = 100f;
            private static final float SWIPE_VELOCITY  = 100f;

            @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {
                if (e1 == null || e2 == null) return false;
                float dX = e2.getX() - e1.getX();
                float dY = e2.getY() - e1.getY();
                if (Math.abs(dX) > Math.abs(dY) && Math.abs(dX) > SWIPE_THRESHOLD && Math.abs(vX) > SWIPE_VELOCITY) {
                    // Swipe horizontal → zapping
                    zapChannel(dX < 0 ? +1 : -1); // swipe gauche = suivant, droite = précédent
                    return true;
                }
                return false;
            }

            @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
                if (e1 == null) return false;
                float w = playerView.getWidth(); float absY = Math.abs(dy);
                if (absY > 30 && Math.abs(dy) > Math.abs(dx)) {
                    if (e1.getX() < w / 2) {
                        // Luminosité (côté gauche)
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.screenBrightness = Math.max(0.1f, Math.min(1f,
                            (lp.screenBrightness < 0 ? 0.5f : lp.screenBrightness) + (dy > 0 ? 0.04f : -0.04f)));
                        getWindow().setAttributes(lp);
                    } else {
                        // Volume (côté droit)
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            dy > 0 ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                    }
                    return true;
                }
                return false;
            }
        });
        playerView.setOnTouchListener((v, e) -> {
            gestureDetector.onTouchEvent(e);
            return true; // consomme l'event pour éviter les conflits avec onClick
        });
    }

    private void loadFavState() {
        if (channelId < 0) return;
        Executors.newSingleThreadExecutor().execute(() -> {
            ChannelEntity ch = db.channelDao().findById(channelId);
            if (ch != null) { isFav = ch.isFavorite; runOnUiThread(this::updateFavIcon); }
        });
    }

    private void toggleFav() {
        isFav = !isFav; updateFavIcon();
        if (channelId < 0) return;
        Executors.newSingleThreadExecutor().execute(() -> db.channelDao().setFavorite(channelId, isFav));
    }

    private void updateFavIcon() {
        btnFav.setImageResource(isFav ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
    }

    @Override protected void onStop() { super.onStop(); if (engine != null) engine.pause(); }
    @Override protected void onDestroy() {
        super.onDestroy(); osdHandler.removeCallbacksAndMessages(null);
        if (engine != null) { engine.release(); engine = null; }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
