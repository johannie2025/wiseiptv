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
import java.util.concurrent.ExecutorService;
import java.util.LinkedHashSet;

@OptIn(markerClass = UnstableApi.class)
public class PlayerActivity extends AppCompatActivity implements PlayerEngine.Listener {
    public static final String EXTRA_ID="ch_id", EXTRA_NAME="ch_name", EXTRA_URL="ch_url", EXTRA_TYPE="ch_type";
	public static final String EXTRA_GROUP = "extra_group";
	public static final String EXTRA_ORDER = "extra_order";
    public static final String EXTRA_START_POSITION = "start_position";
    private PlayerView playerView;
    private PlayerEngine engine;
    private ProgressBar progressBar;
    private TextView tvChannelName, tvQuality, tvError;
    private ImageButton btnFav, btnQuality, btnBack;
    private View osdTop;
    private AppDatabase db;
    private long channelId;
    private String channelName, streamUrl;
    private int contentType;
        private int sortOrder;
        private String groupTitle;
    private boolean isFav;
    private AudioManager audioManager;
    private GestureDetector gestureDetector;
        private ImageButton btnPrevious, btnNext;
        private View zapControls;
        private final ExecutorService zapExecutor = Executors.newSingleThreadExecutor();
    private final Handler osdHandler = new Handler(Looper.getMainLooper());
        private final Runnable hideOsd = () -> {
            osdTop.animate().alpha(0).setDuration(300).start();
            zapControls.animate().alpha(0).setDuration(300)
                .withEndAction(() -> zapControls.setVisibility(View.GONE)).start();
        };
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
        rememberRecentChannel();
            groupTitle = getIntent().getStringExtra(EXTRA_GROUP);
            sortOrder = getIntent().getIntExtra(EXTRA_ORDER, 0);
        playerView    = findViewById(R.id.player_view);
        progressBar   = findViewById(R.id.progress_bar);
        tvChannelName = findViewById(R.id.tv_channel_name);
        tvQuality     = findViewById(R.id.tv_quality);
        tvError       = findViewById(R.id.tv_error);
        btnFav        = findViewById(R.id.btn_fav);
        btnQuality    = findViewById(R.id.btn_quality);
        btnBack       = findViewById(R.id.btn_back);
            btnPrevious   = findViewById(R.id.btn_previous);
            btnNext       = findViewById(R.id.btn_next);
        osdTop        = findViewById(R.id.osd_top);
            zapControls  = findViewById(R.id.osd_zap_controls);
        tvChannelName.setText(channelName);
        btnBack.setOnClickListener(v -> finish());
            btnPrevious.setOnClickListener(v -> zapChannel(-1));
            btnNext.setOnClickListener(v -> zapChannel(1));
        btnQuality.setOnClickListener(v -> showQualityPicker());
        btnFav.setOnClickListener(v -> toggleFav());
        playerView.setOnClickListener(v -> showOsd());
        engine = new PlayerEngine(this, this);
        engine.attachView(playerView);
        engine.play(streamUrl);
        long startPosition = getIntent().getLongExtra(EXTRA_START_POSITION, 0);
        if (startPosition > 0) engine.seekTo(startPosition);
        loadFavState(); setupGestures(); showOsd();
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
        osdTop.animate().alpha(1).setDuration(200).start();
        zapControls.setVisibility(View.VISIBLE);
        zapControls.animate().alpha(1).setDuration(200).start();
        osdHandler.removeCallbacks(hideOsd); osdHandler.postDelayed(hideOsd, 4000);
    }
    private void setupGestures() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_DISTANCE = 90;
            private static final int SWIPE_VELOCITY = 100;

            @Override public boolean onDown(MotionEvent event) { return true; }

            @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
                if (e1 == null) return false;
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

    private void zapChannel(int direction) {
        if (channelId < 0) return;
        zapExecutor.execute(() -> {
            ChannelEntity target = direction < 0
                ? db.channelDao().getPrev(sortOrder, contentType, groupTitle)
                : db.channelDao().getNext(sortOrder, contentType, groupTitle);
            if (target == null) return;
            runOnUiThread(() -> {
                channelId = target.id;
                channelName = target.name;
                streamUrl = target.streamUrl;
                groupTitle = target.groupTitle;
                sortOrder = target.sortOrder;
                contentType = target.contentType;
                rememberRecentChannel();
                tvChannelName.animate().alpha(0).setDuration(100).withEndAction(() -> {
                    tvChannelName.setText(channelName);
                    tvChannelName.animate().alpha(1).setDuration(160).start();
                }).start();
                engine.play(streamUrl);
                showOsd();
                loadFavState();
            });
        });
    }
    private void loadFavState() {
        if (channelId < 0) return;
        Executors.newSingleThreadExecutor().execute(() -> {
            ChannelEntity ch = db.channelDao().findById(channelId);
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
        Executors.newSingleThreadExecutor().execute(() -> db.channelDao().setFavorite(channelId, isFav));
    }
    private void updateFavIcon() {
        btnFav.setImageResource(isFav ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
    }
    @Override protected void onStop() { super.onStop(); if (engine != null) engine.pause(); }
    @Override protected void onDestroy() {
        super.onDestroy(); osdHandler.removeCallbacksAndMessages(null);
        zapExecutor.shutdownNow();
        if (engine != null) { engine.release(); engine = null; }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
