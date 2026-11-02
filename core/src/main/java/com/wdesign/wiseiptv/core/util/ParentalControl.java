package com.wdesign.wiseiptv.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Contrôle parental : PIN à 4 chiffres + masquage des contenus adultes (par mots-clés). */
public final class ParentalControl {
    private ParentalControl() {}

    private static final String PREFS = "wise_parental";
    private static final String[] KEYWORDS = {"adult", "xxx", "porn", "erotic", "érotic", "sex",
        "18+", "playboy", "hustler", "brazzers", "hot club", "for adults"};

    private static SharedPreferences p(Context c) {
        return c.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static boolean isEnabled(Context c) {
        return p(c).getBoolean("enabled", false) && !p(c).getString("pin", "").isEmpty();
    }

    public static void enable(Context c, String pin) {
        p(c).edit().putString("pin", hash(pin)).putBoolean("enabled", true).apply();
    }

    public static void disable(Context c) {
        p(c).edit().putBoolean("enabled", false).remove("pin").apply();
    }

    public static boolean checkPin(Context c, String pin) {
        String saved = p(c).getString("pin", "");
        return !saved.isEmpty() && saved.equals(hash(pin));
    }

    public static boolean isRestricted(Context c, ChannelEntity ch) {
        if (!isEnabled(c) || ch == null) return false;
        String hay = ((ch.groupTitle == null ? "" : ch.groupTitle) + " " + ch.name
            + " " + (ch.category == null ? "" : ch.category)).toLowerCase(Locale.ROOT);
        for (String k : KEYWORDS) if (hay.contains(k)) return true;
        return false;
    }

    public static List<ChannelEntity> filter(Context c, List<ChannelEntity> in) {
        if (in == null || !isEnabled(c)) return in;
        List<ChannelEntity> out = new ArrayList<>(in.size());
        for (ChannelEntity e : in) if (!isRestricted(c, e)) out.add(e);
        return out;
    }

    private static String hash(String pin) {
        try {
            byte[] h = MessageDigest.getInstance("SHA-256").digest(("wise|" + pin).getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : h) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { return pin; }
    }
}
