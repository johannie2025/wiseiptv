package com.wdesign.wiseiptv.tv.ui;

import android.os.*;
import android.view.*;
import android.widget.*;
import androidx.annotation.OptIn;
import androidx.fragment.app.FragmentActivity;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.PlayerView;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.epg.EpgClient;
import com.wdesign.wiseiptv.core.player.PlayerEngine;
import com.wdesign.wiseiptv.core.util.DataUsage;
import com.wdesign.wiseiptv.core.util.Recorder;
import java.util.List;
import com.wdesign.wiseiptv.tv.R;
import java.util.concurrent.Executors;

@OptIn(markerClass = UnstableApi.class)
public class TvPlayerActivity extends FragmentActivity implements PlayerEngine.Listener {
    public static final String EXTRA_ID    = "id";
    public static final String EXTRA_NAME  = "name";
    public static final String EXTRA_URL   = "url";
    public static final String EXTRA_TYPE  = "type";
    public static final String EXTRA_GROUP = "group";
    public static final String EXTRA_ORDER = "order";
    public static final String EXTRA_START_POSITION = "start_position";

    private PlayerView playerView;
    private PlayerEngine engine;
    private TextView tvName, tvQuality, tvStatus, tvGroup, tvNow;
    private final Recorder recorder = new Recorder();
    private ProgressBar progressBar;
    private View osd;
    private AppDatabase db;
    private long channelId;
    private String channelName, streamUrl, groupTitle;
    private int contentType, sortOrder;
    private final Handler osdHandler = new Handler(Looper.getMainLooper());
    private final Runnable hideOsd = () -> osd.animate().alpha(0).setDuration(400).start();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_tv_player);
        db = AppDatabase.get(this);

        channelId   = getIntent().getLongExtra(EXTRA_ID, -1);
        channelName = getIntent().getStringExtra(EXTRA_NAME);
        streamUrl   = getIntent().getStringExtra(EXTRA_URL);
        contentType = getIntent().getIntExtra(EXTRA_TYPE, 0);
        groupTitle  = getIntent().getStringExtra(EXTRA_GROUP);
        sortOrder   = getIntent().getIntExtra(EXTRA_ORDER, 0);

        playerView  = findViewById(R.id.tv_player_view);
        progressBar = findViewById(R.id.progress_bar);
        tvName      = findViewById(R.id.tv_channel_name);
        tvQuality   = findViewById(R.id.tv_quality);
        tvStatus    = findViewById(R.id.tv_status);
        tvGroup     = findViewById(R.id.tv_group);
        osd         = findViewById(R.id.osd);
        tvNow       = findViewById(R.id.tv_now);

        tvName.setText(channelName);
        if (tvGroup != null && groupTitle != null) tvGroup.setText(groupTitle);
        tvQuality.setText("Auto");

        com.wdesign.wiseiptv.core.util.WatchStats.setChannel(channelName);
        engine = com.wdesign.wiseiptv.core.player.PlayerHandoff.take(streamUrl);
        long startPosition = getIntent().getLongExtra(EXTRA_START_POSITION, 0);
        if (engine != null) {            // flux de l'aperçu repris sans rechargement
            engine.setListener(this);
            engine.attachView(playerView);
            engine.setMuted(false);
            if (engine.isPlaying()) onPlaying();
        } else {
            engine = new PlayerEngine(this, this);
            engine.attachView(playerView);
            engine.play(streamUrl);
            if (startPosition > 0) engine.seekTo(startPosition);
        }
        saveState(true);
        loadNow();
        showOsd();
    }

    /** Mémorise la lecture en cours pour la reprise à la prochaine ouverture. */
    private void saveState(boolean open) {
        android.content.SharedPreferences.Editor e =
            getSharedPreferences("wise_tv_state", MODE_PRIVATE).edit()
                .putBoolean("player_open", open).putLong("player_channel", channelId);
        if (open && engine != null && contentType != ChannelEntity.TYPE_LIVE)
            e.putLong("player_pos", engine.getPosition());
        e.apply();
    }

    /** Programme en cours / suivant (EPG) dans l'OSD. */
    private void loadNow() {
        tvNow.setVisibility(View.GONE);
        if (streamUrl == null || streamUrl.contains("/timeshift/")) return;
        final long id = channelId;
        ChannelEntity stub = new ChannelEntity();
        stub.id = channelId; stub.name = channelName == null ? "" : channelName;
        stub.streamUrl = streamUrl; stub.groupTitle = groupTitle; stub.contentType = contentType;
        EpgClient.load(this, stub, new EpgClient.Callback() {
            @Override public void onResult(List<EpgClient.Program> programs, EpgClient.Source source) {
                if (isFinishing() || isDestroyed() || id != channelId || programs == null) return;
                long now = System.currentTimeMillis() / 1000;
                String cur = null, next = null;
                for (int i = 0; i < programs.size(); i++) {
                    if (programs.get(i).isNow(now)) {
                        cur = programs.get(i).time() + "  " + programs.get(i).title;
                        if (i + 1 < programs.size())
                            next = programs.get(i + 1).time() + "  " + programs.get(i + 1).title;
                        break;
                    }
                }
                if (cur == null) return;
                tvNow.setText("En cours : " + cur + (next != null ? "     Ensuite : " + next : ""));
                tvNow.setVisibility(View.VISIBLE);
            }
            @Override public void onError(String message) { }
        });
    }

    /** Zapping TV: DPAD_LEFT = précédent, DPAD_RIGHT = suivant */
    private void zapChannel(int dir) {
        Executors.newSingleThreadExecutor().execute(() -> {
            ChannelEntity target = dir < 0
                ? db.channelDao().getPrev(sortOrder, contentType, groupTitle)
                : db.channelDao().getNext(sortOrder, contentType, groupTitle);
            if (target == null) return;
            runOnUiThread(() -> {
                channelId   = target.id;
                channelName = target.name;
                streamUrl   = target.streamUrl;
                groupTitle  = target.groupTitle;
                sortOrder   = target.sortOrder;
                tvName.setText(channelName);
                if (tvGroup != null && groupTitle != null) tvGroup.setText(groupTitle);
                engine.play(streamUrl);
                saveState(true);
                loadNow();
                showOsd();
            });
        });
    }

    @Override public void onBuffering(boolean b) {
        runOnUiThread(() -> { progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
            tvStatus.setVisibility(b ? View.VISIBLE : View.GONE);
            if (b) tvStatus.setText("Chargement…"); });
    }
    @Override public void onPlaying() { runOnUiThread(() -> { tvStatus.setVisibility(View.GONE); progressBar.setVisibility(View.GONE); }); }
    @Override public void onError(String msg) { runOnUiThread(() -> { tvStatus.setText("⚠ " + msg); tvStatus.setVisibility(View.VISIBLE); progressBar.setVisibility(View.GONE); }); }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (engine == null) return super.onKeyDown(keyCode, event);
        showOsd();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                if (engine.isPlaying()) engine.pause(); else engine.resume(); return true;

            // Zapping chaîne (gauche/droite)
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
            case KeyEvent.KEYCODE_PAGE_DOWN:
                zapChannel(-1); return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_CHANNEL_UP:
            case KeyEvent.KEYCODE_PAGE_UP:
                zapChannel(+1); return true;

            // Qualité (haut/bas)
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD: {
                int q = Math.min(engine.getQuality()+1, PlayerEngine.QUALITY_LABELS.length-1);
                engine.setQuality(q); tvQuality.setText(PlayerEngine.QUALITY_LABELS[q]); return true; }
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_MEDIA_REWIND: {
                int q = Math.max(engine.getQuality()-1, 0);
                engine.setQuality(q); tvQuality.setText(PlayerEngine.QUALITY_LABELS[q]); return true; }

            case KeyEvent.KEYCODE_PROG_RED:
            case KeyEvent.KEYCODE_MEDIA_RECORD:
                toggleRecord(); return true;
            case KeyEvent.KEYCODE_BACK: saveState(false); finish(); return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleRecord() {
        if (engine == null) return;
        if (engine.isRecording() || recorder.isRecording()) { engine.stopRecording(); recorder.stop(); return; }
        Recorder.Listener l = new Recorder.Listener() {
            @Override public void onStarted(java.io.File f) {
                runOnUiThread(() -> { tvStatus.setText("⏺ REC"); tvStatus.setVisibility(View.VISIBLE); });
            }
            @Override public void onFinished(java.io.File f, long bytes, String error) {
                runOnUiThread(() -> {
                    tvStatus.setVisibility(View.GONE);
                    if (isDestroyed()) return;
                    Toast.makeText(TvPlayerActivity.this, error != null ? "Enregistrement interrompu : " + error
                        : bytes > 0 ? "✅ Enregistré : " + f.getAbsolutePath()
                        : "Rien n'a été enregistré : attendez que la vidéo démarre puis réessayez.",
                        Toast.LENGTH_LONG).show();
                });
            }
        };
        if (contentType == ChannelEntity.TYPE_LIVE || streamUrl.contains("/timeshift/")) engine.startRecording(channelName, l);
        else recorder.start(this, streamUrl, channelName, l);
    }

    private void showOsd() {
        osd.animate().alpha(1).setDuration(200).start();
        osdHandler.removeCallbacks(hideOsd); osdHandler.postDelayed(hideOsd, 5000);
    }
    @Override protected void onStop() {
        super.onStop();
        if (engine != null) { saveState(!isFinishing()); engine.pause(); }
        DataUsage.flush(this);
    }
    @Override protected void onDestroy() {
        recorder.stop();
        super.onDestroy(); osdHandler.removeCallbacksAndMessages(null);
        if (engine != null) { engine.release(); engine = null; }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
