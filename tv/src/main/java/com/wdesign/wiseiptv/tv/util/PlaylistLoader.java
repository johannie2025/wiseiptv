package com.wdesign.wiseiptv.tv.util;

import android.net.Uri;
import android.util.Log;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.parser.M3UParser;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * PlaylistLoader TV — supporte String URL (ancien) ET PlaylistEntity (nouveau).
 * Les deux signatures coexistent pour compatibilité TvMainActivity et PlaylistManagerActivity.
 */
public class PlaylistLoader {
    private static final String TAG = "TvPlaylistLoader";
    public interface Callback { void onDone(int count); void onError(String msg); }

    /** Charger depuis une PlaylistEntity (URL M3U, fichier local, Xtream) */
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
                for (ChannelEntity ch : list) ch.playlistId = pl.id;
                db.runInTransaction(() -> {
                    db.channelDao().deleteByPlaylist(pl.id);
                    db.channelDao().insertAll(list);
                });
                db.playlistDao().updateTimestamp(pl.id, System.currentTimeMillis());
                cb.onDone(list.size());
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage(), e);
                cb.onError(e.getMessage() != null ? e.getMessage() : "Erreur");
            }
        });
    }

    /** Charger depuis une URL simple (compatibilité TvMainActivity) */
    public static void load(String url, AppDatabase db, Callback cb) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                InputStream is;
                if (url.startsWith("content://")) {
                    // URI Android (fichier local)
                    is = null; // Nécessite Context — utilisez load(PlaylistEntity) à la place
                    cb.onError("Utilisez load(PlaylistEntity) pour les fichiers locaux");
                    return;
                } else {
                    HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
                    c.setConnectTimeout(15_000); c.setReadTimeout(60_000);
                    c.setRequestProperty("User-Agent", "WiseIPTV/2.0");
                    if (c.getResponseCode() != 200) { cb.onError("HTTP " + c.getResponseCode()); return; }
                    is = c.getInputStream();
                }
                List<ChannelEntity> list = M3UParser.parse(is);
                is.close();
                if (list.isEmpty()) { cb.onError("Playlist vide"); return; }
                db.runInTransaction(() -> { db.channelDao().deleteAll(); db.channelDao().insertAll(list); });
                cb.onDone(list.size());
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage(), e);
                cb.onError(e.getMessage() != null ? e.getMessage() : "Erreur");
            }
        });
    }

    private static InputStream openStream(PlaylistEntity pl) throws IOException {
        String url = pl.url;
        if (url.startsWith("content://")) {
            // URI persistante Android (fichier local sélectionné)
            // Nécessite Context → déléguer à load(PlaylistEntity, Context, db, cb)
            throw new IOException("Fichier local: utilisez load avec Context");
        }
        if (url.startsWith("file://") || url.startsWith("/")) {
            return new FileInputStream(url.replace("file://", ""));
        }
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setConnectTimeout(15_000); c.setReadTimeout(60_000);
        c.setRequestProperty("User-Agent", "WiseIPTV/2.0");
        if (c.getResponseCode() != 200) throw new IOException("HTTP " + c.getResponseCode());
        return c.getInputStream();
    }

    private static List<ChannelEntity> loadXtream(PlaylistEntity pl) throws IOException {
        String base = pl.url.trim();
        if (!base.endsWith("/")) base += "/";
        String m3uUrl = base + "get.php?username=" + pl.username + "&password=" + pl.password
                + "&type=m3u_plus&output=ts";
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