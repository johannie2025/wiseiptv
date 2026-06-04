package com.wdesign.wiseiptv.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import java.util.ArrayList;
import java.util.List;

/**
 * ActivationManager — Gère la création/mise à jour des configurations de playlists depuis le serveur.
 */
public class ActivationManager {

    private static final String TAG   = "ActivationManager";
    private static final String PREFS = "wise_activation";

    // ── Interfaces ────────────────────────────────────────────────

    public interface OnResult {
        void onActivated(DeviceSecurity.ActivationResult r);
        void onExpired(String status);
        void onError(String msg);
    }

    @Deprecated
    public interface OnDownloadCallback {
        void onSuccess();
        void onFailure(String msg);
    }

    /** Suivi de la synchronisation des configurations de serveurs */
    public interface DownloadCallback {
        void onProgress(String playlistName);
        void onDone(int totalPlaylists); // Clarifié : Renvoie le nombre de configurations synchronisées
        void onError(String msg);
    }

    // ── checkAndSync (background, depuis l'application) ───────────────

    public static void checkAndSync(Context ctx, OnResult cb) {
        // Utiliser le Context applicatif global pour écarter tout risque de fuite de mémoire d'UI
        final Context appCtx = ctx.getApplicationContext();
        AppDatabase db = AppDatabase.get(appCtx);
        
        // Un thread léger à la volée évite l'accumulation d'instances d'Executors non fermées
        new Thread(() -> 
            DeviceSecurity.check(appCtx, new DeviceSecurity.Callback() {
                @Override
                public void onActive(DeviceSecurity.ActivationResult r) {
                    saveStatusPrefs(appCtx, "ACTIVE", r.expiresAt, r.login);
                    
                    // Mise à jour silencieuse des profils de serveurs en arrière-plan
                    upsertAndDownloadAll(appCtx, db, r, new DownloadCallback() {
                        @Override public void onProgress(String n) {}
                        @Override public void onDone(int totalPlaylists) {
                            Log.d(TAG, "Background configuration sync OK. " + totalPlaylists + " serveurs configurés.");
                        }
                        @Override public void onError(String m) {
                            Log.w(TAG, "Background configuration sync error: " + m);
                        }
                    });
                    if (cb != null) cb.onActivated(r);
                }
                
                @Override
                public void onInactive(String status, String message) {
                    purgeActivationPlaylists(appCtx, db);
                    saveStatusPrefs(appCtx, status, "", "");
                    if (cb != null) cb.onExpired(status + ": " + message);
                }
                
                @Override
                public void onError(String message) {
                    if (cb != null) cb.onError(message);
                }
            })
        ).start();
    }

    // ── upsertAndDownloadAll — Crée ou met à jour les entités DNS ───

    public static void upsertAndDownloadAll(Context ctx, AppDatabase db,
                                            DeviceSecurity.ActivationResult r,
                                            DownloadCallback cb) {
        final Context appCtx = ctx.getApplicationContext();
        
        new Thread(() -> {
            SharedPreferences prefs = appCtx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

            String savedIds = prefs.getString("playlist_ids", "");
            List<Long> existingIds = parseLongs(savedIds);

            List<Long> newIds = new ArrayList<>();
            int dnsCount = r.dnsServers != null ? r.dnsServers.size() : 0;

            if (dnsCount == 0) {
                if (cb != null) cb.onError("Aucun serveur de flux reçu");
                return;
            }

            try {
                // SÉCURISATION : Toute la manipulation BDD est exécutée de façon atomique
                db.runInTransaction(() -> {
                    for (int i = 0; i < dnsCount; i++) {
                        DeviceSecurity.DnsEntry dns = r.dnsServers.get(i);
                        if (dns.url == null || dns.url.isEmpty()) continue;

                        long existingId = i < existingIds.size() ? existingIds.get(i) : 0;
                        PlaylistEntity pl = existingId > 0 ? db.playlistDao().findById(existingId) : null;
                        if (pl == null) pl = new PlaylistEntity();

                        String urlBasse = dns.url.toLowerCase().trim();
                        if (urlBasse.endsWith(".m3u") || urlBasse.endsWith(".m3u8") || urlBasse.contains("m3u")) {
                            pl.type = PlaylistEntity.TYPE_M3U_URL;
                            pl.name = "Playlist M3U #" + (i + 1);
                        } else {
                            pl.type = PlaylistEntity.TYPE_XTREAM;
                            pl.name = dnsCount == 1 ? "Abonnement IPTV" : "IPTV #" + (i + 1);
                        }

                        pl.url = dns.url;
                        pl.username = r.login;
                        pl.password = r.password;
                        pl.isActive = true;
                        pl.lastUpdated = 0; // Confirme le passage de témoin à MainActivity pour le téléchargement réel

                        if (pl.id == 0) {
                            pl.id = db.playlistDao().insert(pl);
                        } else {
                            db.playlistDao().update(pl);
                        }
                        newIds.add(pl.id);

                        if (cb != null) cb.onProgress(pl.name);
                    }

                    // Nettoyer proprement les serveurs qui ont été révoqués côté API admin
                    for (long oldId : existingIds) {
                        if (!newIds.contains(oldId)) {
                            db.channelDao().deleteByPlaylist(oldId);
                            PlaylistEntity old = db.playlistDao().findById(oldId);
                            if (old != null) db.playlistDao().delete(old);
                        }
                    }
                });

                // Les préférences persistantes ne sont mises à jour que si la transaction BDD réussit
                prefs.edit().putString("playlist_ids", joinLongs(newIds)).apply();

                if (cb != null) cb.onDone(newIds.size());

            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de la transaction d'enregistrement des DNS", e);
                if (cb != null) cb.onError("Erreur d'écriture BDD : " + e.getLocalizedMessage());
            }
        }).start();
    }

    public static void upsertActivationPlaylist(Context ctx, AppDatabase db,
                                                 DeviceSecurity.ActivationResult r) {
        upsertAndDownloadAll(ctx, db, r, new DownloadCallback() {
            @Override public void onProgress(String n) {}
            @Override public void onDone(int t) {}
            @Override public void onError(String m) {}
        });
    }

    // ── Purge (expiration/désactivation) ──────────────────────────

    private static void purgeActivationPlaylists(Context ctx, AppDatabase db) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        List<Long> ids = parseLongs(prefs.getString("playlist_ids", ""));
        
        db.runInTransaction(() -> {
            for (long id : ids) {
                db.channelDao().deleteByPlaylist(id);
                PlaylistEntity pl = db.playlistDao().findById(id);
                if (pl != null) db.playlistDao().delete(pl);
            }
        });
        
        prefs.edit().remove("playlist_ids").apply();
    }

    // ── Prefs ─────────────────────────────────────────────────────

    private static void saveStatusPrefs(Context ctx, String status, String expires, String login) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString("status", status)
            .putString("expires_at", expires)
            .putString("login", login)
            .apply();
    }

    public static String getSavedStatus(Context ctx) {
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString("status", "UNKNOWN");
    }

    // ── Helpers ───────────────────────────────────────────────────

    private static List<Long> parseLongs(String csv) {
        List<Long> result = new ArrayList<>();
        if (csv == null || csv.isEmpty()) return result;
        for (String s : csv.split(",")) {
            try { result.add(Long.parseLong(s.trim())); } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    private static String joinLongs(List<Long> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    public static void downloadAllPlaylistsAsync(
            Context ctx,
            AppDatabase db,
            DeviceSecurity.ActivationResult r,
            OnDownloadCallback cb) {
        upsertAndDownloadAll(ctx, db, r, new DownloadCallback() {
            @Override public void onProgress(String name) {}
            @Override public void onDone(int total) { if (cb != null) cb.onSuccess(); }
            @Override public void onError(String msg) { if (cb != null) cb.onFailure(msg); }
        });
    }
}