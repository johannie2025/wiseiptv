package com.wdesign.wiseiptv.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import java.util.concurrent.Executors;

/**
 * ActivationManager — Gère le cycle de vie de l'activation :
 *  1. Au démarrage : interroge le panel si la playlist est de type ACTIVATION
 *  2. Si ACTIVE → télécharge la playlist depuis le DNS reçu
 *  3. Si EXPIRED/DISABLED → supprime les playlists d'activation et coupe l'accès
 */
public class ActivationManager {
    private static final String PREFS = "wise_activation";
    private static final String KEY_STATUS = "act_status";

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
                @Override public void onActive(DeviceSecurity.ActivationResult r) {
                    // Mettre à jour ou créer la playlist d'activation
                    saveActivationPrefs(ctx, r);
                    upsertActivationPlaylist(ctx, db, r);
                    cb.onActivated(r);
                }
                @Override public void onInactive(String status, String message) {
                    // Supprimer les playlists d'activation
                    purgeActivationPlaylists(ctx, db);
                    cb.onExpired(status + ": " + message);
                }
                @Override public void onError(String message) {
                    // Pas de réseau → on garde les playlists existantes (mode offline)
                    cb.onError(message);
                }
            });
        });
    }

    private static void upsertActivationPlaylist(Context ctx, AppDatabase db,
                                                  DeviceSecurity.ActivationResult r) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        long existingId = prefs.getLong("playlist_id", -1);

        PlaylistEntity pl = existingId > 0 ? db.playlistDao().findById(existingId) : null;
        if (pl == null) pl = new PlaylistEntity();

        pl.name         = "Activation";
        pl.type         = PlaylistEntity.TYPE_XTREAM;
        pl.url          = r.primaryDns();
        pl.username     = r.login;
        pl.password     = r.password;
        pl.isActive     = true;
        pl.lastUpdated  = 0; // Force refresh

        if (pl.id == 0) {
            pl.id = db.playlistDao().insert(pl);
            prefs.edit().putLong("playlist_id", pl.id).apply();
        } else {
            db.playlistDao().update(pl);
        }

        // Charger la playlist immédiatement
        PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
            @Override public void onDone(int count) {}
            @Override public void onError(String msg) {}
        });
    }

    private static void purgeActivationPlaylists(Context ctx, AppDatabase db) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        long pid = prefs.getLong("playlist_id", -1);
        if (pid > 0) {
            db.channelDao().deleteByPlaylist(pid);
            PlaylistEntity pl = db.playlistDao().findById(pid);
            if (pl != null) db.playlistDao().delete(pl);
            prefs.edit().remove("playlist_id").apply();
        }
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
