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
 * 1. Au démarrage : interroge le panel si la playlist est de type ACTIVATION
 * 2. Si ACTIVE → télécharge toutes les chaînes depuis TOUS les DNS reçus
 * 3. Si EXPIRED/DISABLED → supprime les playlists d'activation et coupe l'accès
 */
public class ActivationManager {
    private static final String TAG = "ActivationManager";
    private static final String PREFS = "wise_activation";

    public interface OnResult {
        void onActivated(DeviceSecurity.ActivationResult r);
        void onExpired(String status);
        void onError(String msg);
    }

    /** À appeler au démarrage de l'app (WiseApp.onCreate) */
    public static void checkAndSync(Context ctx, OnResult cb) {
        AppDatabase db = AppDatabase.get(ctx);
        Executors.newSingleThreadExecutor().execute(() -> {
            DeviceSecurity.check(ctx, new DeviceSecurity.Callback() {
                @Override 
                public void onActive(DeviceSecurity.ActivationResult r) {
                    try {
                        saveActivationPrefs(ctx, r);
                        upsertActivationPlaylist(ctx, db, r);
                        cb.onActivated(r);
                    } catch (Exception e) {
                        Log.e(TAG, "Erreur lors de la synchronisation : " + e.getMessage());
                        cb.onError(e.getMessage());
                    }
                }
                
                @Override 
                public void onInactive(String status, String message) {
                    purgeActivationPlaylists(ctx, db);
                    cb.onExpired(status + ": " + message);
                }
                
                @Override 
                public void onError(String message) {
                    // Pas de réseau → on garde les playlists existantes (mode offline)
                    cb.onError(message);
                }
            });
        });
    }

    /**
     * Parcourt et configure chaque serveur DNS activé pour l'appareil,
     * puis télécharge et enregistre de manière bloquante toutes les chaînes associées.
     */
    public static void upsertActivationPlaylist(Context ctx, AppDatabase db, DeviceSecurity.ActivationResult r) throws Exception {
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
            pl.lastUpdated  = 0; // Force le PlaylistLoader à recharger à zéro les chaînes

            if (pl.id == 0) {
                pl.id = db.playlistDao().insert(pl);
                prefs.edit().putLong(prefKey, pl.id).apply();
            } else {
                db.playlistDao().update(pl);
            }

            Log.d(TAG, "Lancement du téléchargement des chaînes pour : " + dns.url);
            
            // Verrou pour forcer le téléchargement complet du DNS courant avant de passer au suivant
            CountDownLatch latch = new CountDownLatch(1);
            
            PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                @Override 
                public void onDone(int count) {
                    Log.d(TAG, "Serveur " + (dns.url) + " : " + count + " chaînes synchronisées.");
                    latch.countDown();
                }
                
                @Override 
                public void onError(String msg) {
                    Log.e(TAG, "Erreur sur le serveur " + (dns.url) + " : " + msg);
                    latch.countDown();
                }
            });

            // Attend jusqu'à 45 secondes la fin du téléchargement effectif des flux pour ce serveur
            latch.await(45, TimeUnit.SECONDS);
        }

        // On conserve le stockage du premier playlist_id pour la rétrocompatibilité si nécessaire
        long firstPid = prefs.getLong("playlist_id_dns_0", -1);
        if (firstPid > 0) {
            prefs.edit().putLong("playlist_id", firstPid).apply();
        }
    }

    private static void purgeActivationPlaylists(Context ctx, AppDatabase db) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        
        // Purge dynamique de tous les DNS potentiels stockés en cache local
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

        // Purge de l'ancienne clé racine par sécurité
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