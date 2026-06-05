package com.wdesign.wiseiptv.mobile.util;
import android.util.Log;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.parser.M3UParser;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.Executors;
public class PlaylistLoader {
    private static final String TAG = "PlaylistLoader";
    public interface Callback { void onDone(int count); void onError(String msg); }
    public static void load(String url, AppDatabase db, Callback cb) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                InputStream is;
                if (url.startsWith("file://")) {
                    is = new FileInputStream(url.replace("file://",""));
                } else {
                    HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
                    c.setConnectTimeout(15_000); c.setReadTimeout(60_000);
                    c.setRequestProperty("User-Agent","WiseIPTV/2.0");
                    if (c.getResponseCode() != 200) { cb.onError("HTTP " + c.getResponseCode()); return; }
                    is = c.getInputStream();
                }
                List<ChannelEntity> list = M3UParser.parse(is);
                if (list.isEmpty()) { cb.onError("Playlist vide"); return; }
                db.runInTransaction(() -> { db.channelDao().deleteAll(); db.channelDao().insertAll(list); });
                cb.onDone(list.size());
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage(), e);
                cb.onError(e.getMessage() != null ? e.getMessage() : "Erreur");
            }
        });
    }
}
