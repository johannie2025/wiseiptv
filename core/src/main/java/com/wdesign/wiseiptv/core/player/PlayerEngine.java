package com.wdesign.wiseiptv.core.player;

import androidx.media3.exoplayer.DefaultLoadControl;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.OptIn;
import androidx.media3.common.*;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.*;
import androidx.media3.exoplayer.trackselection.*;
import androidx.media3.datasource.*;
import androidx.media3.datasource.okhttp.OkHttpDataSource;
import androidx.media3.ui.PlayerView;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter;
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection;
import android.os.Handler;
import android.os.Looper;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;

@OptIn(markerClass = UnstableApi.class)
public class PlayerEngine {
    private static final String TAG = "PlayerEngine";

    public interface Listener {
        void onBuffering(boolean buffering);
        void onPlaying();
        void onError(String message);
    }

    public static final int[] QUALITY_BITRATES = {0, 200_000, 500_000, 1_000_000, 2_500_000, 5_000_000, 15_000_000};
    public static final String[] QUALITY_LABELS = {"Auto","240p","360p","480p","720p","1080p","4K"};
    
    private ExoPlayer exoPlayer;
    private final Context context;
    private volatile Listener listener;
    private int currentQuality = 0;
    // ── Reprise sur erreur / détection du type de flux ──
    private static final int MAX_RETRIES = 6;
    private static final String[] MIME_LADDER = { null, MimeTypes.APPLICATION_M3U8, MimeTypes.APPLICATION_MPD, MimeTypes.APPLICATION_SS };
    private final Handler retryHandler = new Handler(Looper.getMainLooper());
    private String currentUrl;
    private int retryCount = 0;
    private int mimeStep = 0;
    private boolean released = false;
    private Runnable pendingRetry;
    private DefaultBandwidthMeter meter;
    private boolean forcedLowest = false, triedM3u8 = false;
    private final java.util.ArrayDeque<Long> rebuffers = new java.util.ArrayDeque<>();
    private boolean wasReady = false;
    private final Runnable watchTick = new Runnable() {
        @Override public void run() {
            if (released) return;
            if (exoPlayer != null && exoPlayer.isPlaying()) com.wdesign.wiseiptv.core.util.WatchStats.tick(context, 5);
            adaptToBandwidth();
            retryHandler.postDelayed(this, 5000);
        }
    };

    public PlayerEngine(Context ctx, Listener l) {
        this.context = ctx.getApplicationContext(); 
        this.listener = l; 
        buildPlayer();
    }

    private void buildPlayer() {
        // OPTIMISATION CLIENT HTTP : Reconnexion automatique active (Anti-Freezing)
        OkHttpClient http = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .callTimeout(0, TimeUnit.SECONDS)
            .followRedirects(true).followSslRedirects(true)
            .connectionPool(new okhttp3.ConnectionPool(4, 2, TimeUnit.MINUTES))
            .retryOnConnectionFailure(true) // Relance instantanément la requête si le flux lâche
            .build();

        final Context appCtx = context;
        DataSource.Factory dsFactory = new OkHttpDataSource.Factory(http)
            .setUserAgent("VLC/3.0.20 LibVLC/3.0.20")
            .setTransferListener(new androidx.media3.datasource.TransferListener() {
                @Override public void onTransferInitializing(DataSource s, DataSpec d, boolean n) {}
                @Override public void onTransferStart(DataSource s, DataSpec d, boolean n) {}
                @Override public void onBytesTransferred(DataSource s, DataSpec d, boolean isNetwork, int bytes) {
                    if (isNetwork) com.wdesign.wiseiptv.core.util.DataUsage.add(appCtx, bytes);
                }
                @Override public void onTransferEnd(DataSource s, DataSpec d, boolean n) {}
            });

        final DataSource.Factory baseFactory = dsFactory;
        final DataSource.Factory teeFactory = () -> new TeeDataSource(baseFactory.createDataSource(), PlayerEngine.this);

        // OPTIMISATION BUFFERING (DefaultLoadControl) : Évite les micro-coupures
        // Débits très bas : démarrage rapide (1,5 s), grosse marge anti-rebuffering (4 s), tampon max 50 s
        DefaultLoadControl lc = new DefaultLoadControl.Builder()
            .setBufferDurationsMs(25_000, 90_000, 2_500, 7_000)
            .setBackBuffer(10_000, false)
            .setTargetBufferBytes(C.LENGTH_UNSET)
            .setPrioritizeTimeOverSizeThresholds(true)
            .build();
        // Estimation de débit initiale prudente (≈ 1,2 Mb/s) puis adaptation continue
        meter = new DefaultBandwidthMeter.Builder(context)
            .setInitialBitrateEstimate(500_000L).setResetOnNetworkTypeChange(true).build();
        final LoadErrorHandlingPolicy retryPolicy = new DefaultLoadErrorHandlingPolicy() {
            @Override public int getMinimumLoadableRetryCount(int dataType) { return 8; }
            @Override public long getRetryDelayMsFor(LoadErrorInfo info) {
                long d = super.getRetryDelayMsFor(info);
                return d == C.TIME_UNSET ? d : Math.min(d, 4000);
            }
        };

        // Adaptation prudente : on descend vite (6 s), on remonte lentement (8 s), 65 % du débit mesuré utilisé
        DefaultTrackSelector ts = new DefaultTrackSelector(context,
            new AdaptiveTrackSelection.Factory(8_000, 6_000, 25_000, 0.65f));
        DefaultTrackSelector.Parameters.Builder tsb = ts.buildUponParameters()
            .setAllowVideoMixedMimeTypeAdaptiveness(true);
        int al = com.wdesign.wiseiptv.core.util.AppSettings.audioLangIndex(context);
        String code = com.wdesign.wiseiptv.core.util.AppSettings.AUDIO_CODES[al];
        if (!code.isEmpty()) tsb.setPreferredAudioLanguage(code);
        if (com.wdesign.wiseiptv.core.util.AppSettings.subtitles(context)) {
            tsb.setPreferredTextLanguage(code.isEmpty() ? "fr" : code)
               .setSelectUndeterminedTextLanguage(true);
        } else {
            tsb.setIgnoredTextSelectionFlags(C.SELECTION_FLAG_DEFAULT | C.SELECTION_FLAG_FORCED | C.SELECTION_FLAG_AUTOSELECT)
               .setDisabledTrackTypes(java.util.Collections.singleton(C.TRACK_TYPE_TEXT));
        }
        tsb.setAllowVideoNonSeamlessAdaptiveness(true)
           .setAllowAudioMixedMimeTypeAdaptiveness(true)
           .setExceedVideoConstraintsIfNecessary(true)
           .setExceedRendererCapabilitiesIfNecessary(true)
           .setForceHighestSupportedBitrate(false);
        ts.setParameters(tsb.build());
        currentQuality = com.wdesign.wiseiptv.core.util.AppSettings.defaultQuality(context);

        DefaultRenderersFactory rf = new DefaultRenderersFactory(context)
            .setEnableDecoderFallback(true)
            .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON);
        exoPlayer = new ExoPlayer.Builder(context, rf)
            .setLoadControl(lc)
            .setTrackSelector(ts)
            .setBandwidthMeter(meter)
            .setMediaSourceFactory(new DefaultMediaSourceFactory(teeFactory)
                .setLoadErrorHandlingPolicy(retryPolicy)
                .setLiveTargetOffsetMs(8_000))
            .build();
        exoPlayer.setHandleAudioBecomingNoisy(true);
        retryHandler.postDelayed(watchTick, 5000);

        exoPlayer.addListener(new Player.Listener() {
            @Override public void onPlaybackStateChanged(int st) {
                if (st == Player.STATE_BUFFERING) {
                    if (wasReady) onRebuffer();
                    listener.onBuffering(true);
                } else if (st == Player.STATE_READY) {
                    retryCount = 0;
                    wasReady = true;
                    listener.onBuffering(false);
                    listener.onPlaying();
                }
            }

            @Override public void onPlayerError(PlaybackException e) {
                Log.e(TAG, "ExoPlayer Error (" + e.getErrorCodeName() + "): " + e.getMessage());
                handleError(e);
            }
        });
    }

    /** Mode Auto : mesure le débit réel et force la plus basse qualité quand il est très faible. */
    private void adaptToBandwidth() {
        if (exoPlayer == null || meter == null || currentQuality != 0) return;
        long est = meter.getBitrateEstimate();
        if (!forcedLowest && est > 0 && est < 450_000L) {
            forcedLowest = true;
            exoPlayer.setTrackSelectionParameters(exoPlayer.getTrackSelectionParameters().buildUpon().setForceLowestBitrate(true).build());
        } else if (forcedLowest && est > 1_200_000L) {
            forcedLowest = false;
            exoPlayer.setTrackSelectionParameters(exoPlayer.getTrackSelectionParameters().buildUpon().setForceLowestBitrate(false).build());
        }
    }

    /** Reprise automatique : fenêtre live dépassée, mauvais type détecté, coupures réseau (backoff 1→8 s). */
    private void handleError(PlaybackException e) {
        if (released || exoPlayer == null || currentUrl == null) return;
        int code = e.errorCode;
        if (code == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
            exoPlayer.seekToDefaultPosition(); exoPlayer.prepare(); return;
        }
        boolean formatProblem = code == PlaybackException.ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED
            || code == PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED
            || code == PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED
            || code == PlaybackException.ERROR_CODE_PARSING_MANIFEST_UNSUPPORTED;
        if (formatProblem && mimeStep < MIME_LADDER.length - 1) {
            mimeStep++;                       // essaie HLS, puis DASH, puis Smooth Streaming
            load(currentUrl);
            return;
        }
        if (retryCount < MAX_RETRIES) {
            final long delay = Math.min(8000, 1000L << retryCount);
            retryCount++;
            listener.onBuffering(true);
            if (pendingRetry != null) retryHandler.removeCallbacks(pendingRetry);
            pendingRetry = () -> { if (!released && exoPlayer != null) { exoPlayer.seekToDefaultPosition(); exoPlayer.prepare(); exoPlayer.play(); } };
            retryHandler.postDelayed(pendingRetry, delay);
            return;
        }
        listener.onError("Flux indisponible. Vérifiez votre connexion ou changez de chaîne.");
    }

    /** 3 coupures en 90 s sur un flux Xtream .ts → bascule automatique vers .m3u8 (souvent plus tolérant). */
    private void onRebuffer() {
        long now = System.currentTimeMillis();
        rebuffers.addLast(now);
        while (!rebuffers.isEmpty() && now - rebuffers.peekFirst() > 90_000) rebuffers.pollFirst();
        if (rebuffers.size() >= 3 && !triedM3u8 && currentUrl != null
            && currentUrl.matches("(?i)^https?://[^?]+/(live/)?[^/]+/[^/]+/\\d+\\.ts$")) {
            triedM3u8 = true; rebuffers.clear(); wasReady = false;
            currentUrl = currentUrl.replaceAll("(?i)\\.ts$", ".m3u8");
            load(currentUrl);
        }
    }

    private void load(String url) {
        MediaItem.Builder mb = new MediaItem.Builder().setUri(Uri.parse(url))
            .setLiveConfiguration(new MediaItem.LiveConfiguration.Builder()
                .setTargetOffsetMs(8_000).setMaxOffsetMs(30_000)
                .setMinPlaybackSpeed(0.97f).setMaxPlaybackSpeed(1.03f).build());
        String mime = MIME_LADDER[mimeStep];
        if (mime != null) mb.setMimeType(mime);
        exoPlayer.setMediaItem(mb.build());
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.prepare();
        applyQuality(currentQuality);
    }

    public void setListener(Listener l) { if (l != null) this.listener = l; }

    public void attachView(PlayerView v) { 
        v.setPlayer(exoPlayer); 
    }

    public void play(String url) {
        if (url == null || url.isEmpty() || exoPlayer == null) return;
        if (pendingRetry != null) retryHandler.removeCallbacks(pendingRetry);
        currentUrl = url; retryCount = 0; mimeStep = 0; triedM3u8 = false; wasReady = false; rebuffers.clear();
        load(url);
    }

    public void stop()   { if (exoPlayer != null) exoPlayer.stop(); }
    public void pause()  { if (exoPlayer != null) exoPlayer.pause(); }
    public void resume() { if (exoPlayer != null) exoPlayer.play(); }
    
    public boolean isPlaying() { return exoPlayer != null && exoPlayer.isPlaying(); }
    public long getPosition()  { return exoPlayer != null ? exoPlayer.getCurrentPosition() : 0; }
    public void seekTo(long ms) { if (exoPlayer != null) exoPlayer.seekTo(ms); }
    
    public void setQuality(int index) { currentQuality = index; applyQuality(index); }
    public int getQuality() { return currentQuality; }

    private void applyQuality(int i) {
        if (exoPlayer == null) return;
        TrackSelectionParameters.Builder b = exoPlayer.getTrackSelectionParameters().buildUpon();
        int cap = i == 0 ? Integer.MAX_VALUE : QUALITY_BITRATES[i];
        if (com.wdesign.wiseiptv.core.util.AppSettings.networkMode(context)
                == com.wdesign.wiseiptv.core.util.AppSettings.NET_SAVER)
            cap = Math.min(cap, com.wdesign.wiseiptv.core.util.AppSettings.SAVER_MAX_BITRATE);
        b.setMaxVideoBitrate(cap);
        exoPlayer.setTrackSelectionParameters(b.build());
    }

    public void setPlayWhenReady(boolean v) { if (exoPlayer != null) exoPlayer.setPlayWhenReady(v); }
    public void setMuted(boolean muted) { if (exoPlayer != null) exoPlayer.setVolume(muted ? 0f : 1f); }
    public boolean isMuted() { return exoPlayer != null && exoPlayer.getVolume() == 0f; }

    // ── Enregistrement (copie du flux déjà lu par le lecteur) ─────

    private final Object recLock = new Object();
    private java.io.OutputStream recOut;
    private java.io.File recFile;
    private long recBytes;
    private com.wdesign.wiseiptv.core.util.Recorder.Listener recListener;

    public boolean isRecording() { synchronized (recLock) { return recOut != null; } }

    /** Démarre l'enregistrement du flux en cours de lecture (direct / replay). */
    public boolean startRecording(String name, com.wdesign.wiseiptv.core.util.Recorder.Listener l) {
        java.io.File f;
        synchronized (recLock) {
            if (recOut != null) return true;
            java.io.File dir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_MOVIES);
            if (dir == null) dir = context.getFilesDir();
            dir = new java.io.File(dir, "WiseIPTV");
            if (!dir.exists()) dir.mkdirs();
            String clean = (name == null ? "enregistrement" : name).replaceAll("[^A-Za-z0-9._-]+", "_");
            String stamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.US)
                .format(new java.util.Date());
            f = new java.io.File(dir, clean + "_" + stamp + ".ts");
            try { recOut = new java.io.BufferedOutputStream(new java.io.FileOutputStream(f), 256 * 1024); }
            catch (java.io.IOException e) { recOut = null; return false; }
            recFile = f; recBytes = 0; recListener = l;
        }
        if (l != null) l.onStarted(f);
        return true;
    }

    public void stopRecording() { finishRecording(null); }

    private void finishRecording(String error) {
        java.io.File f; long n; com.wdesign.wiseiptv.core.util.Recorder.Listener l;
        synchronized (recLock) {
            if (recOut == null) return;
            try { recOut.flush(); recOut.close(); } catch (Exception ignored) { }
            recOut = null; f = recFile; n = recBytes; l = recListener; recListener = null;
        }
        if (n <= 0 && f != null) f.delete();
        if (l != null) l.onFinished(f, n, error);
    }

    /** Appelé par TeeDataSource pour chaque bloc de média lu. */
    void onMediaBytes(byte[] b, int off, int len) {
        String err = null;
        synchronized (recLock) {
            if (recOut == null) return;
            try {
                recOut.write(b, off, len);
                recBytes += len;
                if (recFile != null && recFile.getUsableSpace() < 150L * 1024 * 1024) err = "Espace de stockage insuffisant";
            } catch (java.io.IOException e) { err = e.getMessage() != null ? e.getMessage() : "Erreur d'écriture"; }
        }
        if (err != null) finishRecording(err);
    }

    public void release() {
        released = true;
        retryHandler.removeCallbacksAndMessages(null);
        finishRecording(null);
        com.wdesign.wiseiptv.core.util.DataUsage.flush(context);
        com.wdesign.wiseiptv.core.util.StatsReporter.send(context, false);
        if (exoPlayer != null) { 
            exoPlayer.stop(); 
            exoPlayer.release(); 
            exoPlayer = null; 
        }
    }
}