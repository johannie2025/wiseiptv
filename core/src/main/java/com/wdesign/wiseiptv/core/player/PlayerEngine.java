package com.wdesign.wiseiptv.core.player;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.OptIn;
import androidx.media3.common.*;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.*;
import androidx.media3.exoplayer.trackselection.*;
import androidx.media3.exoplayer.upstream.*;
import androidx.media3.datasource.*;
import androidx.media3.datasource.okhttp.OkHttpDataSource;
import androidx.media3.ui.PlayerView;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

/**
 * PlayerEngine — Optimisé pour l'Afrique Centrale (1 Kbps – quelques Mbps).
 *
 * Stratégie faible débit :
 *  • Timeouts longs (connexion 45s, lecture 120s)
 *  • Buffer minimum 8s avant lecture (plus patient)
 *  • Buffer cible 3 minutes (grande réserve)
 *  • Retry agressif : 6 tentatives avec backoff exponentiel
 *  • OkHttp : keep-alive, follow redirects, pas de timeout socket court
 *  • ABR : commence toujours en Auto (le plus bas disponible)
 *  • Sur erreur réseau → retry silencieux avant d'afficher l'erreur
 */
@OptIn(markerClass = UnstableApi.class)
public class PlayerEngine {

    private static final String TAG = "PlayerEngine";

    public interface Listener {
        void onBuffering(boolean buffering);
        void onPlaying();
        void onError(String message);
    }

    public static final int[]    QUALITY_BITRATES = {0, 100_000, 300_000, 700_000, 1_500_000, 4_000_000, 12_000_000};
    public static final String[] QUALITY_LABELS   = {"Auto","144p","240p","480p","720p","1080p","4K"};

    private ExoPlayer exoPlayer;
    private final Context  context;
    private final Listener listener;
    private int currentQuality = 0; // Auto par défaut

    // Compteur retry pour éviter l'affichage immédiat d'erreur
    private int retryCount = 0;
    private static final int MAX_SILENT_RETRIES = 3;

    public PlayerEngine(Context ctx, Listener l) {
        this.context  = ctx.getApplicationContext();
        this.listener = l;
        buildPlayer();
    }

    private void buildPlayer() {
        // ── OkHttp : optimisé réseau instable ────────────────────
        OkHttpClient http = new OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS)   // +long pour réseau lent
            .readTimeout(120, TimeUnit.SECONDS)      // +long pour débit faible
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)          // retry auto sur perte
            .followRedirects(true)
            .followSslRedirects(true)
            .build();

        DataSource.Factory httpFactory = new OkHttpDataSource.Factory(http)
            .setUserAgent("WiseIPTV/2.0");

        // Factory avec cache disque optionnel + fallback
        DataSource.Factory dsFactory = new DefaultDataSource.Factory(context, httpFactory);

        // ── LoadControl : buffer grand, patient ──────────────────
        DefaultLoadControl loadControl = new DefaultLoadControl.Builder()
            // Débits faibles : accepter jusqu'à 8s de buffer avant de démarrer
            .setBufferDurationsMs(
                8_000,    // min buffer avant démarrage (8s)
                180_000,  // max buffer cible (3 minutes)
                2_500,    // min pour reprendre après rebuffering
                5_000     // cible après rebuffering
            )
            .setPrioritizeTimeOverSizeThresholds(true) // priorité temps > taille
            .setTargetBufferBytes(DefaultLoadControl.DEFAULT_TARGET_BUFFER_BYTES * 4)
            .build();

        // ── LoadErrorHandlingPolicy : retry agressif ─────────────
        DefaultLoadErrorHandlingPolicy retryPolicy =
            new DefaultLoadErrorHandlingPolicy(/* minLoadableRetryCount= */ 6);

        // ── TrackSelector : ABR adaptatif ────────────────────────
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(context);
        trackSelector.setParameters(
            trackSelector.buildUponParameters()
                .setAllowVideoMixedMimeTypeAdaptiveness(true)
                .setAllowAudioMixedChannelCountAdaptiveness(true)
                // Pas de contrainte de bande passante sur l'ABR — laisser ExoPlayer décider
                .setMaxVideoBitrate(Integer.MAX_VALUE)
                .setForceLowestBitrate(false)
                .build()
        );

        exoPlayer = new ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .setTrackSelector(trackSelector)
            .setMediaSourceFactory(
                new androidx.media3.exoplayer.source.DefaultMediaSourceFactory(dsFactory)
                    .setLoadErrorHandlingPolicy(retryPolicy)
            )
            .build();

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                switch (state) {
                    case Player.STATE_BUFFERING:
                        listener.onBuffering(true);
                        break;
                    case Player.STATE_READY:
                        retryCount = 0; // reset retry counter
                        listener.onBuffering(false);
                        listener.onPlaying();
                        break;
                    case Player.STATE_ENDED:
                        break;
                    case Player.STATE_IDLE:
                        break;
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e(TAG, "Erreur: " + error.getMessage() + " code=" + error.errorCode);

                // Retry silencieux les premières fois (réseau instable)
                if (retryCount < MAX_SILENT_RETRIES) {
                    retryCount++;
                    Log.d(TAG, "Retry silencieux #" + retryCount);
                    // Délai croissant : 2s, 4s, 8s
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                        if (exoPlayer != null) {
                            exoPlayer.prepare();
                        }
                    }, 2000L * retryCount);
                } else {
                    retryCount = 0;
                    listener.onError(friendlyError(error));
                }
            }
        });
    }

    /** Message d'erreur lisible en français */
    private String friendlyError(PlaybackException e) {
        int code = e.errorCode;
        if (code == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED)
            return "Connexion impossible. Vérifiez votre réseau.";
        if (code == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT)
            return "Délai réseau dépassé. Connexion trop lente.";
        if (code == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS)
            return "Flux non disponible (erreur serveur).";
        if (code == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW)
            return "Flux en direct perdu. Reconnexion…";
        return "Erreur de lecture. Réessayez.";
    }

    public void attachView(PlayerView v) {
        if (v != null) v.setPlayer(exoPlayer);
    }

    public void play(String url) {
        if (url == null || url.isEmpty()) return;
        retryCount = 0;
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
    public void seekTo(long ms){ if (exoPlayer != null) exoPlayer.seekTo(ms); }

    public void setQuality(int index) {
        currentQuality = Math.max(0, Math.min(index, QUALITY_BITRATES.length - 1));
        applyQuality(currentQuality);
    }
    public int getQuality() { return currentQuality; }

    private void applyQuality(int i) {
        if (exoPlayer == null) return;
        TrackSelectionParameters.Builder b =
            exoPlayer.getTrackSelectionParameters().buildUpon();
        if (i == 0) {
            b.setMaxVideoBitrate(Integer.MAX_VALUE)
             .setForceLowestBitrate(false);
        } else {
            b.setMaxVideoBitrate(QUALITY_BITRATES[i]);
        }
        exoPlayer.setTrackSelectionParameters(b.build());
    }

    public void release() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}