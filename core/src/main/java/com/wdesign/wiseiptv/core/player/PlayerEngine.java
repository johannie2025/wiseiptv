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
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;
import androidx.media3.datasource.*;
import androidx.media3.datasource.okhttp.OkHttpDataSource;
import androidx.media3.ui.PlayerView;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;

@OptIn(markerClass = UnstableApi.class)
public class PlayerEngine {
    private static final String TAG = "PlayerEngine";

    /** User-Agent navigateur automatique — obligatoire pour de nombreux flux IPTV. */
    private static final String USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

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
    private String currentUrl;
    /** Nom de la chaîne vue (null pour un simple aperçu : le temps d'aperçu n'est pas compté comme visionnage). */
    private volatile String watchLabel;
    private long watchSince = 0L;

    public PlayerEngine(Context ctx, Listener l) {
        this.context = ctx.getApplicationContext(); 
        this.listener = l; 
        buildPlayer();
    }

    private void buildPlayer() {
        // Client HTTP robuste : timeouts longs + reconnexion auto
        OkHttpClient http = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

        final Context appCtx = context;
        DataSource.Factory dsFactory = new OkHttpDataSource.Factory(http)
            .setUserAgent(USER_AGENT)   // User-Agent automatique (navigateur)
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

        // Buffers adaptés : plus larges en mode économie de données (connexion faible)
        boolean saver = com.wdesign.wiseiptv.core.util.AppSettings.networkMode(context)
                == com.wdesign.wiseiptv.core.util.AppSettings.NET_SAVER;
        DefaultLoadControl lc = new DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                saver ? 45_000 : 25_000,   // min buffer
                saver ? 120_000 : 60_000,  // max buffer
                saver ? 8_000  : 2_500,    // buffer avant démarrage
                saver ? 15_000 : 5_000     // buffer après rebuffer
            )
            .setPrioritizeTimeOverSizeThresholds(true)
            .build();

        // Politique de retry : 3 tentatives sur erreur de chargement (réseau, 404 temporaire, etc.)
        LoadErrorHandlingPolicy errorPolicy = new DefaultLoadErrorHandlingPolicy() {
            @Override
            public long getRetryDelayMsFor(LoadErrorInfo loadErrorInfo) {
                int count = loadErrorInfo.errorCount;
                if (count <= 3) {
                    // 1s, 2s, 4s
                    return 1000L * (1L << (count - 1));
                }
                return C.TIME_UNSET; // abandon après 3 essais
            }
            @Override
            public int getMinimumLoadableRetryCount(int dataType) {
                return 3;
            }
        };

        DefaultTrackSelector ts = new DefaultTrackSelector(context);
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
        ts.setParameters(tsb.build());
        currentQuality = com.wdesign.wiseiptv.core.util.AppSettings.defaultQuality(context);

        exoPlayer = new ExoPlayer.Builder(context)
            .setLoadControl(lc)
            .setTrackSelector(ts)
            .setMediaSourceFactory(
                new DefaultMediaSourceFactory(teeFactory)
                    .setLoadErrorHandlingPolicy(errorPolicy)
            )
            .build();

        exoPlayer.addListener(new Player.Listener() {
            @Override public void onPlaybackStateChanged(int s) {
                if (s == Player.STATE_BUFFERING) {
                    listener.onBuffering(true);
                } else if (s == Player.STATE_READY) { 
                    listener.onBuffering(false); 
                    listener.onPlaying(); 
                }
            }

            @Override public void onIsPlayingChanged(boolean playing) {
                if (playing) { watchSince = android.os.SystemClock.elapsedRealtime(); }
                else flushWatchTime();
            }

            @Override public void onPlayerError(PlaybackException e) {
                Log.e(TAG, "ExoPlayer Error: " + e.getMessage(), e);
                listener.onError(friendlyError(e));
            }
        });
    }

    /** Traduit les erreurs techniques ExoPlayer en messages clairs. */
    private static String friendlyError(PlaybackException e) {
        if (e == null) return "Erreur de lecture du flux";
        int code = e.errorCode;
        String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

        if (code == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED
                || code == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT
                || msg.contains("timeout") || msg.contains("unable to connect")) {
            return "Connexion réseau impossible ou trop lente";
        }
        if (code == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
                || msg.contains("403") || msg.contains("401")) {
            return "Accès refusé par le serveur (403/401)";
        }
        if (code == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND
                || msg.contains("404")) {
            return "Flux introuvable (404) — lien mort ou expiré";
        }
        if (code == PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED
                || code == PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED
                || msg.contains("format") || msg.contains("unsupported")) {
            return "Format de flux non supporté";
        }
        if (code == PlaybackException.ERROR_CODE_DECODER_INIT_FAILED
                || code == PlaybackException.ERROR_CODE_DECODER_QUERY_FAILED) {
            return "Erreur décodeur vidéo/audio de l'appareil";
        }
        if (msg.contains("source error") || msg.contains("source")) {
            return "Erreur source — flux inaccessible ou bloqué";
        }
        // Message original tronqué si trop long
        String raw = e.getMessage();
        if (raw != null && raw.length() > 80) raw = raw.substring(0, 77) + "…";
        return raw != null ? raw : "Erreur de lecture du flux";
    }

    public void attachView(PlayerView v) { 
        v.setPlayer(exoPlayer); 
    }

    // ── Passage de relais aperçu -> lecteur plein écran (sans recharger le flux) ──

    /** Remplace l'écouteur (utilisé quand un autre écran reprend ce lecteur en cours de lecture). */
    public void setListener(Listener l) { if (l != null) this.listener = l; }

    /** Le lecteur ExoPlayer sous-jacent (pour PlayerView.switchTargetView). */
    public Player getPlayer() { return exoPlayer; }

    /** URL actuellement chargée dans le lecteur. */
    public String getCurrentUrl() { return currentUrl; }

    /** Vrai si l'image tourne déjà (état READY ou lecture en cours). */
    public boolean isReady() { return exoPlayer != null && exoPlayer.getPlaybackState() == Player.STATE_READY; }

    public boolean isBuffering() { return exoPlayer != null && exoPlayer.getPlaybackState() == Player.STATE_BUFFERING; }

    /** Donne un nom à la chaîne regardée : le temps de lecture est alors compté dans les statistiques. */
    public void setWatchLabel(String name) {
        flushWatchTime();
        watchLabel = name;
        if (exoPlayer != null && exoPlayer.isPlaying()) watchSince = android.os.SystemClock.elapsedRealtime();
    }

    private void flushWatchTime() {
        long since = watchSince;
        watchSince = 0L;
        String label = watchLabel;
        if (since <= 0L || label == null) return;
        long sec = (android.os.SystemClock.elapsedRealtime() - since) / 1000L;
        if (sec > 0) com.wdesign.wiseiptv.core.util.WatchStats.add(context, label, sec);
    }

    public void play(String url) {
        if (url == null || url.isEmpty()) return;
        flushWatchTime();
        currentUrl = url;
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(url)));
        exoPlayer.setPlayWhenReady(true); 
        exoPlayer.prepare(); 
        applyQuality(currentQuality);
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
        flushWatchTime();
        finishRecording(null);
        com.wdesign.wiseiptv.core.util.DataUsage.flush(context);
        if (exoPlayer != null) { 
            exoPlayer.stop(); 
            exoPlayer.release(); 
            exoPlayer = null; 
        }
    }
}
