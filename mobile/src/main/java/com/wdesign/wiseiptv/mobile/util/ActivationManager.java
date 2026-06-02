package com.wdesign.wiseiptv.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * ActivationManager — Gère le cycle de vie de l'activation :
 * Télécharge toutes les chaînes depuis TOUS les DNS de manière séquentielle asynchrone.
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
     * Déclenche le processus de téléchargement séquentiel
     */
    public static void downloadAllPlaylistsAsync(Context ctx, AppDatabase db, DeviceSecurity.ActivationResult r, OnDownloadCallback callback) {
        if (r.dnsServers == null || r.dnsServers.isEmpty()) {
            Log.e(TAG, "Aucun DNS reçu du serveur d'activation.");
            callback.onFailure("Aucun serveur de chaînes configuré.");
            return;
        }
        
        // Lance le téléchargement à partir de l'index 0
        downloadDnsStep(ctx, db, r, 0, callback);
    }

    /**
     * Télécharge un DNS spécifique puis s'appelle lui-même pour le suivant (Récursion asynchrone)
     */
    private static void downloadDnsStep(Context ctx, AppDatabase db, DeviceSecurity.ActivationResult r, int index, OnDownloadCallback callback) {
        List<DeviceSecurity.DnsEntry> list = r.dnsServers;
        
        // Condition de fin : si on a traité tous les DNS
        if (index >= list.size()) {
            SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            long firstPid = prefs.getLong("playlist_id_dns_0", -1);
            if (firstPid > 0) {
                prefs.edit().putLong("playlist_id", firstPid).apply();
            }
            callback.onSuccess();
            return;
        }

        DeviceSecurity.DnsEntry dns = list.get(index);
        
        // Exécution des opérations de base de données hors du thread principal
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
                String prefKey = "playlist_id_dns_" + index;
                long existingId = prefs.getLong(prefKey, -1);

                PlaylistEntity pl = existingId > 0 ? db.playlistDao().findById(existingId) : null;
                if (pl == null) {
                    pl = new PlaylistEntity();
                }

                pl.name         = "Abonnement - Serveur " + (index + 1);
                pl.type         = PlaylistEntity.TYPE_XTREAM;
                pl.url          = dns.url;
                pl.username     = r.login;
                pl.password     = r.password;
                pl.isActive     = true;
                pl.lastUpdated  = 0; // Force le PlaylistLoader à tout synchroniser

                if (pl.id == 0) {
                    pl.id = db.playlistDao().insert(pl);
                    prefs.edit().putLong(prefKey, pl.id).apply();
                } else {
                    db.playlistDao().update(pl);
                }

                Log.d(TAG, "Téléchargement en cours pour le serveur [" + index + "] : " + dns.url);
                
                // Appel du chargeur de chaînes
                PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                    @Override 
                    public void onDone(int count) {
                        Log.d(TAG, "Serveur [" + index + "] terminé : " + count + " chaînes.");
                        // Succès : Passage immédiat au DNS suivant
                        downloadDnsStep(ctx, db, r, index + 1, callback);
                    }
                    
                    @Override 
                    public void onError(String msg) {
                        Log.e(TAG, "Erreur sur le serveur [" + index + "] : " + msg);
                        // Même en cas d'erreur sur un serveur, on continue sur les suivants
                        downloadDnsStep(ctx, db, r, index + 1, callback);
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Erreur fatale BDD à l'index " + index + " : " + e.getMessage());
                downloadDnsStep(ctx, db, r, index + 1, callback);
            }
        });
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