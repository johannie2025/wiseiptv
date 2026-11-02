package com.wdesign.wiseiptv.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/** Consommation de données réseau (octets, cumul par jour) : lecteur + téléchargement des playlists. */
public final class DataUsage {
    private DataUsage() {}
    private static final String P = "wise_usage";
    private static long pending = 0;

    private static SharedPreferences p(Context c) {
        return c.getApplicationContext().getSharedPreferences(P, Context.MODE_PRIVATE);
    }

    private static String key(int daysAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -daysAgo);
        return "d_" + new SimpleDateFormat("yyyyMMdd", Locale.US).format(c.getTime());
    }

    public static synchronized void add(Context ctx, long bytes) {
        if (bytes <= 0) return;
        pending += bytes;
        if (pending >= 512 * 1024) flush(ctx);
    }

    public static synchronized void flush(Context ctx) {
        if (pending <= 0) return;
        SharedPreferences p = p(ctx);
        String k = key(0);
        p.edit().putLong(k, p.getLong(k, 0) + pending)
            .putLong("total", p.getLong("total", 0) + pending).apply();
        pending = 0;
    }

    /** Octets du jour J-daysAgo (0 = aujourd'hui). */
    public static long forDay(Context ctx, int daysAgo) {
        flush(ctx);
        return p(ctx).getLong(key(daysAgo), 0);
    }

    public static long lastDays(Context ctx, int n) {
        long s = 0;
        for (int i = 0; i < n; i++) s += forDay(ctx, i);
        return s;
    }

    /** Cumul du mois civil en cours. */
    public static long thisMonth(Context ctx) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return lastDays(ctx, day);
    }

    public static long total(Context ctx) {
        flush(ctx);
        return p(ctx).getLong("total", 0);
    }

    public static synchronized void reset(Context ctx) {
        pending = 0;
        p(ctx).edit().clear().apply();
    }

    public static String format(long b) {
        if (b < 1024 * 1024) return Math.max(0, b / 1024) + " Ko";
        if (b < 1024L * 1024 * 1024) return String.format(Locale.US, "%.1f Mo", b / 1048576.0);
        return String.format(Locale.US, "%.2f Go", b / 1073741824.0);
    }
}
