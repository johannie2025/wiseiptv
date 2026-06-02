package com.wdesign.wiseiptv.tv.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.tv.util.PlaylistLoader; // À adapter selon l'emplacement de votre Loader
import java.util.concurrent.Executors;

/**
 * ActivationManager (Version TV) — Gère le cycle de validation IPTV via la télécommande.
 */
public class ActivationManager {
    private static final String PREFS = "wise_activation_tv";

    public interface OnResult {
        void onActivated(DeviceSecurity.ActivationResult r);
        void onExpired(String status);
        void onError(String msg);
    }

    public static void checkAndSync(Context ctx, OnResult cb) {
        AppDatabase db = AppDatabase.get(ctx);
        Executors.newSingleThreadExecutor().execute(() -> {
            DeviceSecurity.check(ctx, new DeviceSecurity.Callback() {
                @Override 
                public void onActive(DeviceSecurity.ActivationResult r) {
                    saveActivationPrefs(ctx, r);
                    upsertActivationPlaylist(ctx, db, r);
                    if (cb != null) cb.onActivated(r);
                }

                @Override 
                public void onInactive(String status, String msg) {
                    purgeActivationPlaylists(ctx, db);
                    ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                        .putString("status", status).apply();
                    if (cb != null) cb.onExpired(status);
                }

                @Override 
                public void onError(String msg) {
                    if (cb != null) cb.onError(msg);
                }
            });
        });
    }

  private static void upsertActivationPlaylist(Context ctx, AppDatabase db, DeviceSecurity.ActivationResult r) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        long pid = prefs.getLong("playlist_id", 0);

        PlaylistEntity pl = (pid > 0) ? db.playlistDao().findById(pid) : null;
        if (pl == null) {
            pl = new PlaylistEntity();
            // Si pid == 0, Room générera automatiquement un ID unique lors de l'insert
            if (pid > 0) {
                pl.id = pid; 
            }
        }

        pl.name = "Abonnement IPTV (Actif)";
        pl.type = "XTREAM"; // CORRECTION : Type en String pour correspondre au Core / Room
        
        // CORRECTION : Utilisation des champs existants de PlaylistEntity et ActivationResult
        pl.url = r.url;       // ou r.server selon la structure exacte définie dans votre package core
        pl.username = r.login;
        pl.password = r.password;
        pl.isActive = true;
        pl.lastUpdated = System.currentTimeMillis(); // Préférable à 0 pour le suivi de synchronisation

        if (pl.id == 0) {
            pl.id = db.playlistDao().insert(pl);
            prefs.edit().putLong("playlist_id", pl.id).apply();
        } else {
            db.playlistDao().update(pl);
        }

        // CORRECTION : Alignement du Callback anonyme avec l'interface de PlaylistLoader
        PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
            @Override
            public void onSuccess() {
                // Flux chargés ou mis à jour avec succès en arrière-plan
            }

            @Override
            public void onFailure(Exception e) {
                // Gestion de l'échec de parsing de l'abonnement
            }
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
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString("status", "NONE");
    }
}