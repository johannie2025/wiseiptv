package com.wdesign.wiseiptv.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Historique local des abonnements. Le serveur ne renvoie que la date d'expiration :
 * chaque nouvelle date d'expiration observée (activation ou renouvellement) crée une ligne.
 * Début = fin de la ligne précédente (renouvellement) ou date de première activation.
 */
public final class SubscriptionHistory {
    private SubscriptionHistory() {}

    private static final String PREFS = "wise_sub_history";
    private static final String KEY = "entries";
    private static final String FMT = "yyyy-MM-dd HH:mm:ss";
    private static final String[] MONTHS = {"Janv.", "Févr.", "Mars", "Avr.", "Mai", "Juin",
        "Juil.", "Août", "Sept.", "Oct.", "Nov.", "Déc."};

    public static final class Entry {
        public final Date start, end;
        public Entry(Date s, Date e) { start = s; end = e; }
        public boolean active() { return end.getTime() > System.currentTimeMillis(); }
        public int months() {
            long days = Math.max(1, Math.round((end.getTime() - start.getTime()) / 86400000.0));
            return Math.max(1, (int) Math.round(days / 30.4));
        }
        public String title() { return "Abonnement - " + months() + " mois"; }
        public String range() { return fmt(start) + " → " + fmt(end); }
    }

    public static String fmt(Date d) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(d);
        return String.format(Locale.FRANCE, "%02d %s %d", c.get(java.util.Calendar.DAY_OF_MONTH),
            MONTHS[c.get(java.util.Calendar.MONTH)], c.get(java.util.Calendar.YEAR));
    }

    /** Enregistre une date d'expiration (format serveur yyyy-MM-dd HH:mm:ss). Idempotent. */
    public static synchronized void record(Context ctx, String expiresAt) {
        if (expiresAt == null || expiresAt.isEmpty()) return;
        try {
            SimpleDateFormat f = new SimpleDateFormat(FMT, Locale.US);
            Date end = f.parse(expiresAt);
            if (end == null) return;
            SharedPreferences p = ctx.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            JSONArray arr = new JSONArray(p.getString(KEY, "[]"));
            Date prevEnd = null;
            if (arr.length() > 0) {
                prevEnd = f.parse(arr.getJSONObject(arr.length() - 1).getString("end"));
                if (prevEnd != null && prevEnd.getTime() == end.getTime()) return;
            }
            Date start = (prevEnd != null && prevEnd.before(end)) ? prevEnd : new Date();
            if (start.after(end)) start = new Date(end.getTime() - 30L * 86400000L);
            arr.put(new JSONObject().put("start", f.format(start)).put("end", f.format(end)));
            p.edit().putString(KEY, arr.toString()).apply();
        } catch (Exception ignored) { }
    }

    /** Plus récent d'abord. */
    public static synchronized List<Entry> list(Context ctx) {
        List<Entry> out = new ArrayList<>();
        try {
            SimpleDateFormat f = new SimpleDateFormat(FMT, Locale.US);
            JSONArray arr = new JSONArray(ctx.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]"));
            for (int i = arr.length() - 1; i >= 0; i--) {
                JSONObject o = arr.getJSONObject(i);
                out.add(new Entry(f.parse(o.getString("start")), f.parse(o.getString("end"))));
            }
        } catch (Exception ignored) { }
        return out;
    }
}
