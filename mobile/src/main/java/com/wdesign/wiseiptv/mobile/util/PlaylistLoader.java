package com.wdesign.wiseiptv.mobile.util;

import android.util.Log;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.parser.M3UParser;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.Executors;

public class PlaylistLoader {
    private static final String TAG = "PlaylistLoader";

    public interface Callback { void onDone(int count); void onError(String msg); }

    /** Charge une playlist (M3U URL, fichier local ou Xtream) */
    public static void load(PlaylistEntity pl, AppDatabase db, Callback cb) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<ChannelEntity> list;
                if (pl.type == PlaylistEntity.TYPE_XTREAM) {
                    list = loadXtream(pl);
                } else {
                    InputStream is = openStream(pl);
                    list = M3UParser.parse(is);
                    is.close();
                }
                if (list == null || list.isEmpty()) { cb.onError("Playlist vide"); return; }
                // Tag chaque chaîne avec l'id de la playlist
                for (ChannelEntity ch : list) ch.playlistId = pl.id;
                db.runInTransaction(() -> {
                    db.channelDao().deleteByPlaylist(pl.id);
                    db.channelDao().insertAll(list);
                });
                db.playlistDao().updateTimestamp(pl.id, System.currentTimeMillis());
                cb.onDone(list.size());
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage(), e);
                cb.onError(e.getMessage() != null ? e.getMessage() : "Erreur réseau");
            }
        });
    }

    /** Vérifie si la playlist doit être rafraîchie (> 7 jours) */
    public static boolean needsRefresh(PlaylistEntity pl) {
        return (System.currentTimeMillis() - pl.lastUpdated) >= PlaylistEntity.REFRESH_INTERVAL_MS;
    }

    /** Rafraîchit toutes les playlists dont la date est dépassée */
    public static void refreshStaleIfNeeded(AppDatabase db, Callback cb) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<PlaylistEntity> all = db.playlistDao().getAllSync();
            int total = 0;
            for (PlaylistEntity pl : all) {
                if (pl.isActive && needsRefresh(pl)) {
                    // Synchronous mini-load (on est déjà dans un thread bg)
                    try {
                        List<ChannelEntity> list;
                        if (pl.type == PlaylistEntity.TYPE_XTREAM) list = loadXtream(pl);
                        else { InputStream is = openStream(pl); list = M3UParser.parse(is); is.close(); }
                        if (list != null && !list.isEmpty()) {
                            for (ChannelEntity ch : list) ch.playlistId = pl.id;
                            final List<ChannelEntity> fList = list;
                            db.runInTransaction(() -> {
                                db.channelDao().deleteByPlaylist(pl.id);
                                db.channelDao().insertAll(fList);
                            });
                            db.playlistDao().updateTimestamp(pl.id, System.currentTimeMillis());
                            total += list.size();
                        }
                    } catch (Exception e) { Log.w(TAG, "refresh failed for " + pl.name + ": " + e.getMessage()); }
                }
            }
            final int finalTotal = total;
            if (finalTotal > 0 && cb != null) cb.onDone(finalTotal);
        });
    }

    private static InputStream openStream(PlaylistEntity pl) throws IOException {
        String url = pl.url;
        if (url.startsWith("file://") || url.startsWith("/")) {
            return new FileInputStream(url.replace("file://", ""));
        }
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setConnectTimeout(15_000); c.setReadTimeout(60_000);
        c.setRequestProperty("User-Agent", "WiseIPTV/2.0");
        if (c.getResponseCode() != 200) throw new IOException("HTTP " + c.getResponseCode());
        return c.getInputStream();
    }

    /** Construit l'URL M3U Xtream et la parse normalement */
    private static List<ChannelEntity> loadXtream(PlaylistEntity pl) throws IOException {
        // Format Xtream Codes API: http://server/get.php?username=X&password=Y&type=m3u_plus&output=ts
        String url = pl.url.trim();
        if (!url.endsWith("/")) url += "/";
        String m3uUrl = url + "get.php?username=" + pl.username
                + "&password=" + pl.password + "&type=m3u_plus&output=ts";
        HttpURLConnection c = (HttpURLConnection) new URL(m3uUrl).openConnection();
        c.setConnectTimeout(15_000); c.setReadTimeout(120_000);
        c.setRequestProperty("User-Agent", "WiseIPTV/2.0");
        if (c.getResponseCode() != 200) throw new IOException("Xtream HTTP " + c.getResponseCode());
        InputStream is = c.getInputStream();
        List<ChannelEntity> list = M3UParser.parse(is);
        is.close();
        return list;
    }
}
