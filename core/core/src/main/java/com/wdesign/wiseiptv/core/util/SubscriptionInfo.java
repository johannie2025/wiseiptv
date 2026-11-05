package com.wdesign.wiseiptv.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/** Lit l'état d'abonnement mis en cache par l'écran d'activation. */
public final class SubscriptionInfo {
    public static final String PREFS_MOBILE = "wise_activation";
    public static final String PREFS_TV     = "wise_activation_tv";
    public static final String WHATSAPP     = "240222886007";

    private static final String[] MONTHS = {"Janv.", "Févr.", "Mars", "Avr.", "Mai", "Juin",
        "Juil.", "Août", "Sept.", "Oct.", "Nov.", "Déc."};

    public final String login;
    public final Date   expiresAt;
    public final long   daysLeft;   // négatif si expiré
    public final boolean active;

    private SubscriptionInfo(String login, Date exp, long days, boolean active) {
        this.login = login; this.expiresAt = exp; this.daysLeft = days; this.active = active;
    }

    /** @return null si aucune activation en cache. */
    public static SubscriptionInfo read(Context ctx, String prefsName) {
        SharedPreferences p = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        String status = p.getString("status", "");
        String exp    = p.getString("expires_at", "");
        String login  = p.getString("login", "");
        if (!"ACTIVE".equals(status) || exp.isEmpty()) return null;
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(exp);
            if (d == null) return null;
            long ms = d.getTime() - System.currentTimeMillis();
            long days = (long) Math.ceil(ms / 86400000.0);
            return new SubscriptionInfo(login, d, days, ms > 0);
        } catch (Exception e) {
            return null;
        }
    }

    /** Ex : "31 Déc. 2026" */
    public String formattedExpiry() {
        Calendar c = Calendar.getInstance();
        c.setTime(expiresAt);
        return c.get(Calendar.DAY_OF_MONTH) + " " + MONTHS[c.get(Calendar.MONTH)]
            + " " + c.get(Calendar.YEAR);
    }

    public boolean isExpiringSoon(int withinDays) {
        return daysLeft <= withinDays;
    }

    /** Part de jours restants sur une base de 365 j (0..100) pour la barre de progression. */
    public int remainingPercent() {
        long d = Math.max(0, Math.min(365, daysLeft));
        return (int) (d * 100 / 365);
    }

    /** Nom d'affichage : premier caractère en majuscule. */
    public String displayName() {
        if (login == null || login.isEmpty()) return "";
        return login.substring(0, 1).toUpperCase(Locale.getDefault()) + login.substring(1);
    }

    public String initial() {
        String n = displayName();
        return n.isEmpty() ? "W" : n.substring(0, 1);
    }
}
