package com.wdesign.wiseiptv.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * ActivationManager — Gère la création/mise à jour des playlists depuis le serveur.
 *
 * Nouveautés :
 *  - upsertAndDownloadAll() : crée UNE playlist par DNS reçu (jusqu'à 6)
 *    et les télécharge toutes séquentiellement avec progress callback
 *  - checkAndSync() : appelé au background depuis WiseApp pour garder les
 *    playlists à jour sans bloquer l'UI
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

    /**
     * Alias de compatibilité — l'ancien code utilise OnDownloadCallback avec onSuccess/onFailure.
     * @deprecated Utiliser DownloadCallback (onDone/onError) dans le nouveau code.
     */
    public interface OnDownloadCallback {
        void onSuccess();
        void onFailure(String msg);
    }


    /** Progress détaillé pendant le téléchargement des playlists */
    public interface DownloadCallback {
        void onProgress(String playlistName);
        void onDone(int totalChannels);
        void onError(String msg);
    }

    // ── checkAndSync (background, depuis WiseApp) ─────────────────

    public static void checkAndSync(Context ctx, OnResult cb) {
        AppDatabase db = AppDatabase.get(ctx);
        Executors.newSingleThreadExecutor().execute(() ->
            DeviceSecurity.check(ctx, new DeviceSecurity.Callback() {
                @Override
                public void onActive(DeviceSecurity.ActivationResult r) {
                    saveStatusPrefs(ctx, "ACTIVE", r.expiresAt, r.login);
                    // Mise à jour silencieuse en background (pas de progress affiché)
                    upsertAndDownloadAll(ctx, db, r, new DownloadCallback() {
                        @Override public void onProgress(String n) {}
                        @Override public void onDone(int t) {
                            Log.d(TAG, "Background sync OK: " + t + " chaînes");
                        }
                        @Override public void onError(String m) {
                            Log.w(TAG, "Background sync error: " + m);
                        }
                    });
                    cb.onActivated(r);
                }
                @Override
                public void onInactive(String status, String message) {
                    purgeActivationPlaylists(ctx, db);
                    saveStatusPrefs(ctx, status, "", "");
                    cb.onExpired(status + ": " + message);
                }
                @Override
                public void onError(String message) {
                    cb.onError(message);
                }
            })
        );
    }

    // ── upsertAndDownloadAll — crée/màj + télécharge chaque DNS ───

    /**
     * Pour chaque DNS reçu du serveur (jusqu'à 6), crée ou met à jour
     * une PlaylistEntity de type XTREAM et la télécharge immédiatement.
     * Appelé depuis ActivationActivity.downloadAndGo() avec feedback UI.
     */
    public static void upsertAndDownloadAll(Context ctx, AppDatabase db,
                                             DeviceSecurity.ActivationResult r,
                                             DownloadCallback cb) {
        Executors.newSingleThreadExecutor().execute(() -> {
            SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

            // Récupérer les IDs de playlists déjà enregistrés
            String savedIds = prefs.getString("playlist_ids", "");
            List<Long> existingIds = parseLongs(savedIds);

            List<Long> newIds    = new ArrayList<>();
            int        total     = 0;
            String     lastError = null;
            int        dnsCount  = r.dnsServers.size();

            for (int i = 0; i < dnsCount; i++) {
                DeviceSecurity.DnsEntry dns = r.dnsServers.get(i);
                if (dns.url == null || dns.url.isEmpty()) continue;

                // Chercher la playlist existante pour ce DNS ou en créer une
                // Chercher la playlist existante pour ce DNS ou en créer une
long existingId = i < existingIds.size() ? existingIds.get(i) : 0;
PlaylistEntity pl = existingId > 0 ? db.playlistDao().findById(existingId) : null;
if (pl == null) pl = new PlaylistEntity();

// ── AJOUT : Détection automatique du type de lien (M3U ou Xtream) ──
String urlBasse = dns.url.toLowerCase().trim();
if (urlBasse.endsWith(".m3u") || urlBasse.endsWith(".m3u8") || urlBasse.contains("m3u")) {
    pl.type = PlaylistEntity.TYPE_M3U_URL;
    pl.name = "Playlist M3U #" + (i + 1);
} else {
    pl.type = PlaylistEntity.TYPE_XTREAM;
    pl.name = dnsCount == 1 ? "Abonnement IPTV" : "IPTV #" + (i + 1);
}

pl.url        = dns.url;
pl.username   = r.login;
pl.password   = r.password;
pl.isActive   = true;
pl.lastUpdated = 0; // forcer refresh

                if (pl.id == 0) {
                    pl.id = db.playlistDao().insert(pl);
                } else {
                    db.playlistDao().update(pl);
                }
                newIds.add(pl.id);

                // Feedback UI
                final String pName = pl.name;
                cb.onProgress(pName);

                // Téléchargement synchrone (on est déjà dans un thread bg)
                try {
                    List<com.wdesign.wiseiptv.core.db.entity.ChannelEntity> channels;
                    if (pl.type == PlaylistEntity.TYPE_XTREAM) {
                        channels = PlaylistLoader.loadXtreamSync(pl);
                    } else {
                        channels = PlaylistLoader.loadUrlSync(pl);
                    }
                    if (channels != null && !channels.isEmpty()) {
                        for (com.wdesign.wiseiptv.core.db.entity.ChannelEntity ch : channels)
                            ch.playlistId = pl.id;
                        final List<com.wdesign.wiseiptv.core.db.entity.ChannelEntity> fCh = channels;
                        final long fId = pl.id;
                        db.runInTransaction(() -> {
                            db.channelDao().deleteByPlaylist(fId);
                            db.channelDao().insertAll(fCh);
                        });
                        db.playlistDao().updateTimestamp(pl.id, System.currentTimeMillis());
                        total += channels.size();
                    }
                } catch (Exception e) {
                    Log.w(TAG, "Erreur DL " + pl.name + ": " + e.getMessage());
                    lastError = e.getMessage();
                }
            }

            // Supprimer les anciennes playlists qui ne sont plus dans le nouveau set
            for (long oldId : existingIds) {
                if (!newIds.contains(oldId)) {
                    db.channelDao().deleteByPlaylist(oldId);
                    PlaylistEntity old = db.playlistDao().findById(oldId);
                    if (old != null) db.playlistDao().delete(old);
                }
            }

            // Sauvegarder les nouveaux IDs
            prefs.edit().putString("playlist_ids", joinLongs(newIds)).apply();

            if (total > 0) cb.onDone(total);
            else if (lastError != null) cb.onError(lastError);
            else cb.onError("Aucune chaîne reçue");
        });
    }

    // ── Méthode legacy gardée pour compatibilité ──────────────────
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
        for (long id : ids) {
            db.channelDao().deleteByPlaylist(id);
            PlaylistEntity pl = db.playlistDao().findById(id);
            if (pl != null) db.playlistDao().delete(pl);
        }
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

    /**
     * Alias de compatibilité pour l'ancien appel dans MainActivity.java.
     * Délègue vers upsertAndDownloadAll.
     */
    public static void downloadAllPlaylistsAsync(
            android.content.Context ctx,
            com.wdesign.wiseiptv.core.db.AppDatabase db,
            DeviceSecurity.ActivationResult r,
            OnDownloadCallback cb) {
        upsertAndDownloadAll(ctx, db, r, new DownloadCallback() {
            @Override public void onProgress(String name) {}
            @Override public void onDone(int total) { if (cb != null) cb.onSuccess(); }
            @Override public void onError(String msg) { if (cb != null) cb.onFailure(msg); }
        });
    }

}
