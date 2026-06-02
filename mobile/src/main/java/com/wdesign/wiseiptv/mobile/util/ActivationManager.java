package com.wdesign.wiseiptv.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ActivationManager — Gère le cycle de vie de l'activation :
 * Télécharge toutes les chaînes depuis TOUS les DNS reçus en arrière-plan.
 */
public class ActivationManager {
    private static final String TAG = "ActivationManager";
    private static final String PREFS = "wise_activation";

    public interface OnResult {
        void onActivated(DeviceSecurity.ActivationResult r);
        void onExpired(String status);
        void onError(String msg);
    }

    public interface OnDownloadCallback {
        void onSuccess();
        void onFailure(String msg);
    }

    /** À appeler au démarrage de l'app (WiseApp.onCreate) */
    public static void checkAndSync(Context ctx, OnResult cb) {
        AppDatabase db = AppDatabase.get(ctx);
        Executors.newSingleThreadExecutor().execute(() -> {
            DeviceSecurity.check(ctx, new DeviceSecurity.Callback() {
                @Override 
                public void onActive(DeviceSecurity.ActivationResult r) {
                    saveActivationPrefs(ctx, r);
                    // Exécution en tâche de fond
                    downloadAllPlaylistsAsync(ctx, db, r, new OnDownloadCallback() {
                        @Override
                        public void onSuccess() {
                            cb.onActivated(r);
                        }
                        @Override
                        public void onFailure(String msg) {
                            cb.onError(msg);
                        }
                    });
                }
                
                @Override 
                public void onInactive(String status, String message) {
                    purgeActivationPlaylists(ctx, db);
                    cb.onExpired(status + ": " + message);
                }
                
                @Override 
                public void onError(String message) {
                    cb.onError(message);
                }
            });
        });
    }

    /**
     * Méthode asynchrone globale pour exécuter le téléchargement sur un thread dédié
     */
    public static void downloadAllPlaylistsAsync(Context ctx, AppDatabase db, DeviceSecurity.ActivationResult r, OnDownloadCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                upsertActivationPlaylist(ctx, db, r);
                callback.onSuccess();
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors du téléchargement : " + e.getMessage());
                callback.onFailure(e.getMessage());
            }
        });
    }

    /**
     * Parcourt et configure chaque serveur DNS activé (méthode bloquante interne exécutée hors de l'UI thread)
     */
    private static void upsertActivationPlaylist(Context ctx, AppDatabase db, DeviceSecurity.ActivationResult r) throws Exception {
        if (r.dnsServers == null || r.dnsServers.isEmpty()) {
            Log.e(TAG, "Aucun DNS reçu du serveur d'activation.");
            return;
        }

        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        // Boucle sur l'intégralité des serveurs affectés au terminal
        for (int i = 0; i < r.dnsServers.size(); i++) {
            DeviceSecurity.DnsEntry dns = r.dnsServers.get(i);
            String prefKey = "playlist_id_dns_" + i;
            long existingId = prefs.getLong(prefKey, -1);

            PlaylistEntity pl = existingId > 0 ? db.playlistDao().findById(existingId) : null;
            if (pl == null) {
                pl = new PlaylistEntity();
            }

            pl.name         = "Abonnement - Serveur " + (i + 1);
            pl.type         = PlaylistEntity.TYPE_XTREAM;
            pl.url          = dns.url;
            pl.username     = r.login;
            pl.password     = r.password;
            pl.isActive     = true;
            pl.lastUpdated  = 0; // Force le rechargement complet

            if (pl.id == 0) {
                pl.id = db.playlistDao().insert(pl);
                prefs.edit().putLong(prefKey, pl.id).apply();
            } else {
                db.playlistDao().update(pl);
            }

            Log.d(TAG, "Lancement du téléchargement en tâche de fond pour : " + dns.url);
            
            CountDownLatch latch = new CountDownLatch(1);
            
            // Lancement du loader natif
            PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                @Override 
                public void onDone(int count) {
                    Log.d(TAG, "Serveur " + (dns.url) + " : " + count + " chaînes téléchargées.");
                    latch.countDown();
                }
                
                @Override 
                public void onError(String msg) {
                    Log.e(TAG, "Erreur sur le serveur " + (dns.url) + " : " + msg);
                    latch.countDown();
                }
            });

            // Attend la fin du téléchargement réel du serveur (max 60 secondes) avant le DNS suivant
            latch.await(60, TimeUnit.SECONDS);
        }

        long firstPid = prefs.getLong("playlist_id_dns_0", -1);
        if (firstPid > 0) {
            prefs.edit().putLong("playlist_id", firstPid).apply();
        }
    }

    private static void purgeActivationPlaylists(Context ctx, AppDatabase db) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        for (int i = 0; i < 20; i++) {
            String prefKey = "playlist_id_dns_" + i;
            long pid = prefs.getLong(prefKey, -1);
            if (pid > 0) {
                db.channelDao().deleteByPlaylist(pid);
                PlaylistEntity pl = db.playlistDao().findById(pid);
                if (pl != null) db.playlistDao().delete(pl);
                prefs.edit().remove(prefKey).apply();
            }
        }
        long oldPid = prefs.getLong("playlist_id", -1);
        if (oldPid > 0) {
            db.channelDao().deleteByPlaylist(oldPid);
            PlaylistEntity pl = db.playlistDao().findById(oldPid);
            if (pl != null) db.playlistDao().delete(pl);
            prefs.edit().remove("playlist_id").apply();
        }
        prefs.edit().remove("status").remove("expires_at").remove("login").apply();
    }

    private static void saveActivationPrefs(Context ctx, DeviceSecurity.ActivationResult r) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString("status", "ACTIVE")
            .putString("expires_at", r.expiresAt)
            .putString("login", r.login)
            .apply();
    }

    public static String getSavedStatus(Context ctx) {
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString("status", "UNKNOWN");
    }
}