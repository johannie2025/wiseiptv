package com.wdesign.wiseiptv.tv.util;

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
    private static final String TAG = "TvPlaylistLoader";

    public interface Callback { void onDone(int count); void onError(String msg); }

    public static void load(PlaylistEntity pl, AppDatabase db, Callback cb) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                int n = loadIntoDb(pl, db);
                db.playlistDao().updateTimestamp(pl.id, System.currentTimeMillis());
                cb.onDone(n);
            } catch (Throwable e) {   // Throwable : une OutOfMemoryError ne doit plus tuer le thread en silence
                Log.e(TAG, String.valueOf(e.getMessage()), e);
                cb.onError(e instanceof OutOfMemoryError ? "Mémoire insuffisante (liste trop volumineuse)"
                    : e.getMessage() != null ? e.getMessage() : "Erreur de chargement");
            }
        });
    }

    /**
     * Télécharge la playlist dans un fichier temporaire, puis la lit en flux et la range dans la base
     * par lots, dans UNE transaction : si quelque chose échoue (ou si la liste est vide) la base garde
     * l'ancienne liste de chaînes au lieu de se retrouver vide.
     * @return nombre de chaînes chargées
     */
    public static int loadIntoDb(PlaylistEntity pl, AppDatabase db) throws Exception {
        if (pl.type == PlaylistEntity.TYPE_XTREAM) {
            Exception last = null;
            for (String u : xtreamCandidates(pl)) {
                try {
                    Log.d(TAG, "Trying Xtream URL: " + u);
                    int n = streamUrlToDb(u, pl.id, db);
                    if (n > 0) return n;
                } catch (Exception e) {
                    Log.w(TAG, "Failed: " + u + " -> " + e.getMessage());
                    last = e;
                }
            }
            throw last != null ? last : new IOException("Toutes les URLs Xtream ont échoué");
        }
        String url = pl.url;
        if (url.startsWith("file://") || url.startsWith("/")) {
            return parseFileToDb(new File(url.replace("file://", "")), pl.id, db);
        }
        return streamUrlToDb(url, pl.id, db);
    }

    private static int streamUrlToDb(String url, long playlistId, AppDatabase db) throws Exception {
        File tmp = File.createTempFile("wise_pl_", ".m3u");
        try {
            HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
            c.setConnectTimeout(15_000);
            c.setReadTimeout(120_000);
            c.setRequestProperty("User-Agent", "WiseIPTV/2.0");
            c.setInstanceFollowRedirects(true);
            int code = c.getResponseCode();
            if (code != 200) { c.disconnect(); throw new IOException("HTTP " + code); }
            try (InputStream in = c.getInputStream(); OutputStream out = new FileOutputStream(tmp)) {
                byte[] buf = new byte[64 * 1024];
                int n;
                while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            } finally { c.disconnect(); }
            return parseFileToDb(tmp, playlistId, db);
        } finally {
            //noinspection ResultOfMethodCallIgnored
            tmp.delete();
        }
    }

    private static int parseFileToDb(final File f, final long playlistId, final AppDatabase db) throws Exception {
        final int[] count = {0};
        final Exception[] failure = {null};
        db.runInTransaction(() -> {
            db.channelDao().deleteByPlaylist(playlistId);
            try (InputStream is = new BufferedInputStream(new FileInputStream(f), 64 * 1024)) {
                M3UParser.parseStreaming(is, 1000, batch -> {
                    for (ChannelEntity ch : batch) ch.playlistId = playlistId;
                    db.channelDao().insertAll(batch);
                    count[0] += batch.size();
                });
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage() != null ? e.getMessage() : "Lecture impossible", e);
            }
            // Liste vide → on annule la transaction : l'ancien contenu est conservé
            if (count[0] == 0) throw new IllegalStateException("Playlist vide");
        });
        return count[0];
    }

    /** URLs Xtream à essayer, dans l'ordre. */
    private static String[] xtreamCandidates(PlaylistEntity pl) {
        String base = pl.url.trim();
        base = base.replaceAll("(?i)/get\\.php.*$", "")
                   .replaceAll("(?i)/player_api\\.php.*$", "");
        if (!base.endsWith("/")) base += "/";
        String user = pl.username, pass = pl.password;
        return new String[] {
            base + "get.php?username=" + user + "&password=" + pass + "&type=m3u_plus&output=ts",
            base + "get.php?username=" + user + "&password=" + pass + "&type=m3u_plus",
            base + "get.php?username=" + user + "&password=" + pass + "&type=m3u",
            base + user + "/" + pass + "/m3u_plus",
            base + "apiget.php?username=" + user + "&password=" + pass + "&type=m3u_plus",
        };
    }

    /**
     * FIX HTTP 404 : essaie plusieurs formats d'URL Xtream dans l'ordre.
     * Certains serveurs n'ont pas /get.php mais répondent sur /player_api.php
     * ou directement sur la racine.
     */
    public static List<ChannelEntity> loadXtreamSync(PlaylistEntity pl) throws IOException {
        String[] candidates = xtreamCandidates(pl);

        IOException lastException = null;
        for (String url : candidates) {
            try {
                Log.d(TAG, "Trying Xtream URL: " + url);
                List<ChannelEntity> result = parseUrl(url);
                if (result != null && !result.isEmpty()) {
                    Log.d(TAG, "Success with: " + url + " -> " + result.size() + " ch");
                    return result;
                }
            } catch (IOException e) {
                Log.w(TAG, "Failed: " + url + " -> " + e.getMessage());
                lastException = e;
            }
        }
        throw lastException != null ? lastException : new IOException("Toutes les URLs Xtream ont échoué");
    }

    public static List<ChannelEntity> loadUrlSync(PlaylistEntity pl) throws IOException {
        String url = pl.url;
        if (url.startsWith("file://") || url.startsWith("/")) {
            InputStream is = new FileInputStream(url.replace("file://", ""));
            List<ChannelEntity> l = M3UParser.parse(is); is.close(); return l;
        }
        return parseUrl(url);
    }

    /** Charge depuis une URL M3U brute (mode legacy / dialog simple) */
    public static void load(String url, AppDatabase db, Callback cb) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<ChannelEntity> list = parseUrl(url);
                if (list.isEmpty()) { cb.onError("Playlist vide"); return; }
                db.runInTransaction(() -> { db.channelDao().deleteAll(); db.channelDao().insertAll(list); });
                cb.onDone(list.size());
            } catch (Exception e) { cb.onError(e.getMessage()); }
        });
    }

    public static boolean needsRefresh(PlaylistEntity pl) {
        long age = System.currentTimeMillis() - pl.lastUpdated;
        return age > PlaylistEntity.REFRESH_INTERVAL_MS;
    }

    private static List<ChannelEntity> parseUrl(String url) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setConnectTimeout(15_000);
        c.setReadTimeout(120_000);
        c.setRequestProperty("User-Agent", "WiseIPTV/2.0");
        c.setInstanceFollowRedirects(true);
        int code = c.getResponseCode();
        if (code != 200) throw new IOException("HTTP " + code);
        InputStream is = c.getInputStream();
        List<ChannelEntity> l = M3UParser.parse(is);
        is.close();
        return l;
    }

    public static void refreshStaleIfNeeded(AppDatabase db, Callback cb) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<PlaylistEntity> all = db.playlistDao().getAllSync();
            int total = 0;
            for (PlaylistEntity pl : all) {
                if (!pl.isActive || !needsRefresh(pl)) continue;
                try {
                    int n = loadIntoDb(pl, db);
                    db.playlistDao().updateTimestamp(pl.id, System.currentTimeMillis());
                    total += n;
                } catch (Throwable e) { Log.w(TAG, "refresh stale: " + e.getMessage()); }
            }
            if (cb != null && total > 0) cb.onDone(total);
        });
    }
}
