package com.wdesign.wiseiptv.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

/** Temps de visionnage total + par chaîne (les 10 premières sont envoyées au panel). */
public final class WatchStats {
    private static final String PREFS = "wise_watch";
    private static volatile String current = "";
    private WatchStats() {}

    public static void setChannel(String name) { current = name == null ? "" : name; }

    public static synchronized void tick(Context ctx, int seconds) {
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit().putLong("total", p.getLong("total", 0) + seconds);
        if (!current.isEmpty()) e.putLong("c_" + current, p.getLong("c_" + current, 0) + seconds);
        e.apply();
    }

    public static long total(Context ctx) { return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getLong("total", 0); }

    public static JSONArray top(Context ctx, int n) {
        Map<String, ?> all = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getAll();
        List<Map.Entry<String, Long>> l = new ArrayList<>();
        for (Map.Entry<String, ?> en : all.entrySet())
            if (en.getKey().startsWith("c_") && en.getValue() instanceof Long)
                l.add(new AbstractMap.SimpleEntry<>(en.getKey().substring(2), (Long) en.getValue()));
        Collections.sort(l, (a, b) -> Long.compare(b.getValue(), a.getValue()));
        JSONArray out = new JSONArray();
        try {
            for (int i = 0; i < Math.min(n, l.size()); i++) {
                JSONObject o = new JSONObject(); o.put("n", l.get(i).getKey()); o.put("s", l.get(i).getValue()); out.put(o);
            }
        } catch (Exception ignored) { }
        return out;
    }
}
