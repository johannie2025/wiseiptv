package com.wdesign.wiseiptv.core.util;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Envoie au panel : modèle d'appareil, consommation de données, temps de visionnage, top chaînes. */
public final class StatsReporter {
    private static final String URL_STATS = "https://wise.alwaysdata.net/iptv/api/device_stats.php";
    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();
    private static long lastSent = 0;
    private StatsReporter() {}

    /** @param force ignore la limite d'une fois toutes les 10 minutes */
    public static void send(Context ctx, boolean force) {
        final Context app = ctx.getApplicationContext();
        long now = System.currentTimeMillis();
        synchronized (StatsReporter.class) {
            if (!force && now - lastSent < 10 * 60_000L) return;
            lastSent = now;
        }
        EXEC.execute(() -> {
            try {
                DataUsage.flush(app);
                UiModeManager um = (UiModeManager) app.getSystemService(Context.UI_MODE_SERVICE);
                boolean tv = um != null && um.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
                String ver = "";
                try { ver = app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName; } catch (Exception ignored) { }
                JSONObject o = new JSONObject();
                o.put("key", DeviceSecurity.getOrCreateKey(app));
                o.put("platform", tv ? "tv" : "mobile");
                o.put("brand", Build.MANUFACTURER);
                o.put("model", Build.MODEL);
                o.put("os", "Android " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")");
                o.put("app", ver);
                o.put("bytes_total", DataUsage.total(app));
                o.put("bytes_month", DataUsage.thisMonth(app));
                o.put("watch_s", WatchStats.total(app));
                o.put("top", WatchStats.top(app, 10));
                HttpURLConnection c = (HttpURLConnection) new URL(URL_STATS).openConnection();
                c.setRequestMethod("POST");
                c.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                c.setConnectTimeout(10_000); c.setReadTimeout(10_000); c.setDoOutput(true);
                try (OutputStream os = c.getOutputStream()) { os.write(o.toString().getBytes(StandardCharsets.UTF_8)); }
                c.getResponseCode(); c.disconnect();
            } catch (Exception ignored) { }
        });
    }
}
