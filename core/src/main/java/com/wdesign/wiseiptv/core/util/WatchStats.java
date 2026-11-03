package com.wdesign.wiseiptv.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/** Temps de visionnage réel (lecteur plein écran, aperçus exclus) : par jour et par chaîne. */
public final class WatchStats {
    private WatchStats() {}
    private static final String P = "wise_watch";
    private static final int MAX_CHANNELS = 40;

    private static SharedPreferences p(Context c) {
        return c.getApplicationContext().getSharedPreferences(P, Context.MODE_PRIVATE);
    }

    private static String key(int daysAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -daysAgo);
        return "d_" + new SimpleDateFormat("yyyyMMdd", Locale.US).format(c.getTime());
    }

    public static synchronized void add(Context ctx, String channel, long seconds) {
        if (seconds <= 0) return;
        SharedPreferences sp = p(ctx);
        String k = key(0);
        SharedPreferences.Editor ed = sp.edit()
            .putLong(k, sp.getLong(k, 0) + seconds)
            .putLong("total", sp.getLong("total", 0) + seconds)
            .putLong("last_ts", System.currentTimeMillis());
        try {
            JSONObject o = new JSONObject(sp.getString("channels", "{}"));
            o.put(channel, o.optLong(channel, 0) + seconds);
            if (o.length() > MAX_CHANNELS) trim(o);
            ed.putString("channels", o.toString()).putString("last_channel", channel);
        } catch (Exception ignored) { }
        ed.apply();
    }

    private static void trim(JSONObject o) {
        List<String> keys = new ArrayList<>();
        Iterator<String> it = o.keys();
        while (it.hasNext()) keys.add(it.next());
        Collections.sort(keys, (a, b) -> Long.compare(o.optLong(a), o.optLong(b)));
        for (int i = 0; i < keys.size() - MAX_CHANNELS; i++) o.remove(keys.get(i));
    }

    public static long forDay(Context ctx, int daysAgo) { return p(ctx).getLong(key(daysAgo), 0); }

    public static long lastDays(Context ctx, int n) {
        long s = 0;
        for (int i = 0; i < n; i++) s += forDay(ctx, i);
        return s;
    }

    public static long total(Context ctx) { return p(ctx).getLong("total", 0); }

    public static String lastChannel(Context ctx) { return p(ctx).getString("last_channel", ""); }

    /** Les {@code n} chaînes les plus regardées : [{"name":..,"seconds":..}, ...] */
    public static JSONArray top(Context ctx, int n) {
        JSONArray out = new JSONArray();
        try {
            JSONObject o = new JSONObject(p(ctx).getString("channels", "{}"));
            List<String> keys = new ArrayList<>();
            Iterator<String> it = o.keys();
            while (it.hasNext()) keys.add(it.next());
            Collections.sort(keys, (a, b) -> Long.compare(o.optLong(b), o.optLong(a)));
            for (int i = 0; i < keys.size() && i < n; i++) {
                JSONObject e = new JSONObject();
                e.put("name", keys.get(i));
                e.put("seconds", o.optLong(keys.get(i)));
                out.put(e);
            }
        } catch (Exception ignored) { }
        return out;
    }
}
