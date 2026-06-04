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

    public static void load(PlaylistEntity pl, AppDatabase db, Callback cb) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<ChannelEntity> list = pl.type == PlaylistEntity.TYPE_XTREAM
                    ? loadXtreamSync(pl) : loadUrlSync(pl);
                if (list == null || list.isEmpty()) { cb.onError("Playlist vide"); return; }
                for (ChannelEntity ch : list) ch.playlistId = pl.id;
                db.runInTransaction(() -> {
                    db.channelDao().deleteByPlaylist(pl.id);
                    db.channelDao().insertAll(list);
                });
                db.playlistDao().updateTimestamp(pl.id, System.currentTimeMillis());
                cb.onDone(list.size());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                cb.onError(e.getMessage() != null ? e.getMessage() : "Erreur réseau");
            }
        });
    }

    /**
     * Charge une playlist Xtream.
     *
     * Gère deux cas :
     *  1. URL de BASE serveur  → http://server.com:8080
     *     On construit : base + /get.php?username=...&password=...&type=m3u_plus&output=ts
     *
     *  2. URL COMPLÈTE M3U     → http://server.com/get.php?username=...&password=...&type=m3u_plus
     *     On utilise l'URL telle quelle sans rien ajouter.
     *
     * Exemples :
     *   "https://tvradiozap.eu/get.php?username=d:tvrztv&password=public&type=m3u_plus"
     *   → utilisée directement
     *
     *   "http://myserver.com:8080"  + username="user" + password="pass"
     *   → construit "http://myserver.com:8080/get.php?username=user&password=pass&type=m3u_plus&output=ts"
     */
    public static List<ChannelEntity> loadXtreamSync(PlaylistEntity pl) throws IOException {
        String url = pl.url.trim();
        if (isFullXtreamUrl(url)) {
            // URL complète avec get.php → utiliser directement
            return parseUrl(url);
        }
        // URL de base → construire l'URL M3U
        String base = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        String fullUrl = base + "/get.php?username=" + encode(pl.username)
                + "&password=" + encode(pl.password)
                + "&type=m3u_plus&output=ts";
        return parseUrl(fullUrl);
    }

    /**
     * Charge une playlist M3U depuis une URL directe ou un fichier local.
     *
     * Exemples :
     *   "https://iptv-org.github.io/iptv/languages/eng.m3u"  → téléchargement HTTP
     *   "file:///sdcard/playlist.m3u"                        → lecture locale
     */
    public static List<ChannelEntity> loadUrlSync(PlaylistEntity pl) throws IOException {
        String url = pl.url.trim();
        if (url.startsWith("file://") || url.startsWith("/")) {
            InputStream is = new FileInputStream(url.replace("file://", ""));
            List<ChannelEntity> list = M3UParser.parse(is);
            is.close();
            return list;
        }
        return parseUrl(url);
    }

    /**
     * Retourne true si l'URL est déjà une URL M3U Xtream complète.
     * Critères : contient "get.php" dans le path.
     */
    public static boolean isFullXtreamUrl(String url) {
        return url != null && url.toLowerCase().contains("get.php");
    }

    private static String encode(String s) {
        if (s == null) return "";
        try { return URLEncoder.encode(s, "UTF-8"); }
        catch (Exception e) { return s; }
    }

    private static List<ChannelEntity> parseUrl(String url) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setConnectTimeout(15_000);
        c.setReadTimeout(120_000);
        c.setRequestProperty("User-Agent", "WiseIPTV/2.0");
        if (c.getResponseCode() != 200)
            throw new IOException("HTTP " + c.getResponseCode() + " → " + url);
        InputStream is = c.getInputStream();
        List<ChannelEntity> list = M3UParser.parse(is);
        is.close();
        return list;
    }

    public static boolean needsRefresh(PlaylistEntity pl) {
        return (System.currentTimeMillis() - pl.lastUpdated) >= PlaylistEntity.REFRESH_INTERVAL_MS;
    }

    public static void refreshStaleIfNeeded(AppDatabase db, Callback cb) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<PlaylistEntity> all = db.playlistDao().getAllSync();
            int total = 0;
            for (PlaylistEntity pl : all) {
                if (!pl.isActive || !needsRefresh(pl)) continue;
                try {
                    List<ChannelEntity> list = pl.type == PlaylistEntity.TYPE_XTREAM
                        ? loadXtreamSync(pl) : loadUrlSync(pl);
                    if (list != null && !list.isEmpty()) {
                        for (ChannelEntity ch : list) ch.playlistId = pl.id;
                        final List<ChannelEntity> fl  = list;
                        final long                fid = pl.id;
                        db.runInTransaction(() -> {
                            db.channelDao().deleteByPlaylist(fid);
                            db.channelDao().insertAll(fl);
                        });
                        db.playlistDao().updateTimestamp(pl.id, System.currentTimeMillis());
                        total += list.size();
                    }
                } catch (Exception e) { Log.w(TAG, "refresh failed: " + e.getMessage()); }
            }
            if (cb != null && total > 0) cb.onDone(total);
        });
    }
}