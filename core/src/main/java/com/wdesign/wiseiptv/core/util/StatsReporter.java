package com.wdesign.wiseiptv.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.json.JSONObject;

/**
 * Envoie au panel les statistiques de CET appareil : modèle, visionnage, consommation de données.
 * POST JSON vers  …/api/index.php?action=report-stats  (voir docs/PANEL_API.md).
 * Silencieux : une erreur réseau n'a aucun effet visible et on réessaiera plus tard.
 */
public final class StatsReporter {
    private StatsReporter() {}

    private static final String TAG = "StatsReporter";
    private static final String P = "wise_stats_report";
    private static final long MIN_INTERVAL_MS = 10L * 60 * 1000;     // pas plus d'un envoi / 10 min
    private static final Executor EXEC = Executors.newSingleThreadExecutor();
    private static volatile boolean running = false;

    /** @param force ignore le délai minimum (ex. fermeture du lecteur) */
    public static void report(Context ctx, boolean force) {
        final Context app = ctx.getApplicationContext();
        final SharedPreferences sp = app.getSharedPreferences(P, Context.MODE_PRIVATE);
        long now = System.currentTimeMillis();
        if (running) return;
        if (!force && now - sp.getLong("last_ok", 0) < MIN_INTERVAL_MS) return;
        if (force && now - sp.getLong("last_try", 0) < 60_000L) return;   // jamais plus d'1 envoi / min
        running = true;
        sp.edit().putLong("last_try", now).apply();
        EXEC.execute(() -> {
            try {
                DataUsage.flush(app);
                JSONObject o = new JSONObject();
                o.put("device_key", DeviceSecurity.getOrCreateKey(app));
                o.put("device", DeviceInfo.toJson(app));
                JSONObject data = new JSONObject();
                data.put("today_bytes", DataUsage.forDay(app, 0));
                data.put("week_bytes", DataUsage.lastDays(app, 7));
                data.put("month_bytes", DataUsage.thisMonth(app));
                data.put("total_bytes", DataUsage.total(app));
                o.put("data_usage", data);
                JSONObject watch = new JSONObject();
                watch.put("today_seconds", WatchStats.forDay(app, 0));
                watch.put("week_seconds", WatchStats.lastDays(app, 7));
                watch.put("total_seconds", WatchStats.total(app));
                watch.put("last_channel", WatchStats.lastChannel(app));
                watch.put("top_channels", WatchStats.top(app, 10));
                o.put("watch", watch);
                o.put("sent_at", now / 1000);

                HttpURLConnection c = (HttpURLConnection) new URL(DeviceSecurity.panelUrl("report-stats")).openConnection();
                c.setRequestMethod("POST");
                c.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                c.setRequestProperty("User-Agent", "WiseIPTV-APK/2.0");
                c.setConnectTimeout(10_000);
                c.setReadTimeout(10_000);
                c.setDoOutput(true);
                try (OutputStream os = c.getOutputStream()) { os.write(o.toString().getBytes(StandardCharsets.UTF_8)); }
                int code = c.getResponseCode();
                if (code >= 200 && code < 300) sp.edit().putLong("last_ok", System.currentTimeMillis()).apply();
                else Log.w(TAG, "HTTP " + code);
                c.disconnect();
            } catch (Throwable t) {
                Log.w(TAG, "report: " + t.getMessage());
            } finally {
                running = false;
            }
        });
    }
}
