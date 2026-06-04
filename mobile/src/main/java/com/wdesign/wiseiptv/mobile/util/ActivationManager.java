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
 * ActivationManager — Gère uniquement la BDD des playlists (création/mise à jour/purge).
 *
 * RESPONSABILITÉS :
 *  - upsertAndDownloadAll() : crée ou met à jour les PlaylistEntity depuis les DNS du serveur
 *  - purgeActivationPlaylists() : supprime les playlists révoquées
 *  - NE gère PAS les SharedPreferences de session (c'est ActivationActivity qui s'en charge)
 *
 * Appelé depuis :
 *  - MainActivity.startBackgroundDownload() → via PlaylistLoader directement
 *  - (optionnel) depuis un Service/Worker pour refresh en background
 */
public class ActivationManager {

    private static final String TAG   = "ActivationManager";
    private static final String PREFS = "wise_activation";

    // ── Interfaces ────────────────────────────────────────────────

    /** Suivi de la synchronisation des configurations de serveurs */
    public interface DownloadCallback {
        void onProgress(String playlistName);
        void onDone(int totalPlaylists);
        void onError(String msg);
    }

    // ── upsertAndDownloadAll — Crée ou met à jour les entités DNS ───

    /**
     * Crée ou met à jour les PlaylistEntity dans la BDD depuis les DNS reçus du serveur.
     * Détecte automatiquement le type : M3U URL (.m3u/.m3u8) ou Xtream (base serveur).
     * Purge les anciens serveurs révoqués.
     * NE télécharge PAS les chaînes — c'est MainActivity.downloadDnsSequentially() qui le fait.
     *
     * @param ctx  Context applicatif
     * @param db   AppDatabase
     * @param r    ActivationResult frais depuis DeviceSecurity.check()
     * @param cb   Callback progression (peut être null)
     */
    public static void upsertAndDownloadAll(Context ctx, AppDatabase db,
                                            DeviceSecurity.ActivationResult r,
                                            DownloadCallback cb) {
        final Context appCtx = ctx.getApplicationContext();

        new Thread(() -> {
            SharedPreferences prefs = appCtx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

            String savedIds    = prefs.getString("playlist_ids", "");
            List<Long> existingIds = parseLongs(savedIds);
            List<Long> newIds      = new ArrayList<>();

            int dnsCount = r.dnsServers != null ? r.dnsServers.size() : 0;
            if (dnsCount == 0) {
                if (cb != null) cb.onError("Aucun serveur de flux reçu");
                return;
            }

            try {
                db.runInTransaction(() -> {
                    for (int i = 0; i < dnsCount; i++) {
                        DeviceSecurity.DnsEntry dns = r.dnsServers.get(i);
                        if (dns.url == null || dns.url.isEmpty()) continue;

                        long existingId = i < existingIds.size() ? existingIds.get(i) : 0;
                        PlaylistEntity pl = existingId > 0
                                ? db.playlistDao().findById(existingId) : null;
                        if (pl == null) pl = new PlaylistEntity();

                        // Détection automatique du type
                        String urlLower = dns.url.toLowerCase().trim();
                        if (urlLower.endsWith(".m3u") || urlLower.endsWith(".m3u8")
                                || urlLower.contains("get.php")) {
                            pl.type = PlaylistEntity.TYPE_M3U_URL;
                            pl.name = "Playlist M3U #" + (i + 1);
                        } else {
                            pl.type = PlaylistEntity.TYPE_XTREAM;
                            pl.name = dnsCount == 1 ? "Abonnement IPTV" : "IPTV #" + (i + 1);
                        }

                        pl.url        = dns.url;
                        pl.username   = r.login    != null ? r.login    : "";
                        pl.password   = r.password != null ? r.password : "";
                        pl.isActive   = true;
                        pl.lastUpdated = 0; // force le refresh dans MainActivity

                        if (pl.id == 0) {
                            pl.id = db.playlistDao().insert(pl);
                        } else {
                            db.playlistDao().update(pl);
                        }
                        newIds.add(pl.id);

                        if (cb != null) cb.onProgress(pl.name);
                    }

                    // Purger les serveurs révoqués côté API
                    for (long oldId : existingIds) {
                        if (!newIds.contains(oldId)) {
                            db.channelDao().deleteByPlaylist(oldId);
                            PlaylistEntity old = db.playlistDao().findById(oldId);
                            if (old != null) db.playlistDao().delete(old);
                        }
                    }
                });

                // Mettre à jour la liste des IDs persistants
                prefs.edit().putString("playlist_ids", joinLongs(newIds)).apply();

                if (cb != null) cb.onDone(newIds.size());

            } catch (Exception e) {
                Log.e(TAG, "Erreur transaction BDD DNS", e);
                if (cb != null) cb.onError("Erreur BDD : " + e.getLocalizedMessage());
            }
        }).start();
    }

    // ── Purge (expiration / désactivation) ────────────────────────

    /**
     * Supprime toutes les playlists et chaînes associées à l'activation en cours.
     * Appelé quand le serveur retourne EXPIRED ou DISABLED.
     */
    public static void purgeActivationPlaylists(Context ctx, AppDatabase db) {
        SharedPreferences prefs = ctx.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
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

    // ── Helpers ───────────────────────────────────────────────────

    private static List<Long> parseLongs(String csv) {
        List<Long> result = new ArrayList<>();
        if (csv == null || csv.isEmpty()) return result;
        for (String s : csv.split(",")) {
            try { result.add(Long.parseLong(s.trim())); }
            catch (NumberFormatException ignored) {}
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
}