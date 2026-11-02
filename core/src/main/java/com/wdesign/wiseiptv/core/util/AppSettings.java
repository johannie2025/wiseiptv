package com.wdesign.wiseiptv.core.util;

import android.content.Context;
import android.content.SharedPreferences;

/** Réglages utilisateur persistants (partagés entre l'UI et le lecteur). */
public final class AppSettings {
    private AppSettings() {}

    public static final String PREFS = "wise_settings";
    public static final String DISPLAY_PREFS = "wise_display";

    public static final int NET_AUTO = 0, NET_SAVER = 1, NET_MAX = 2;
    /** Plafond de débit en mode « Économie de données » (≈ 480p). */
    public static final int SAVER_MAX_BITRATE = 1_000_000;

    public static final int REFRESH_MANUAL = 0, REFRESH_EVERY_OPEN = 1, REFRESH_DAILY = 2,
        REFRESH_3_DAYS = 3, REFRESH_WEEKLY = 4;
    public static final String[] REFRESH_LABELS = {
        "Manuellement seulement", "À chaque ouverture de l'app", "Toutes les 24 heures",
        "Tous les 3 jours", "Chaque semaine"};

    public static final String[] AUDIO_CODES  = {"", "fr", "en", "es", "pt", "ar"};
    public static final String[] AUDIO_LABELS = {"Automatique", "Français", "English", "Español",
        "Português", "العربية"};

    private static SharedPreferences p(Context c) {
        return c.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // Disposition (grille / liste)
    public static boolean isListLayout(Context c) {
        return "list".equals(c.getSharedPreferences(DISPLAY_PREFS, Context.MODE_PRIVATE)
            .getString("layout_mode", "grid"));
    }
    public static void setListLayout(Context c, boolean list) {
        c.getSharedPreferences(DISPLAY_PREFS, Context.MODE_PRIVATE).edit()
            .putString("layout_mode", list ? "list" : "grid").apply();
    }
    public static int gridColumns(Context c) {
        int v = c.getSharedPreferences(DISPLAY_PREFS, Context.MODE_PRIVATE).getInt("grid_columns", 3);
        return Math.max(2, Math.min(5, v));
    }
    public static void setGridColumns(Context c, int n) {
        c.getSharedPreferences(DISPLAY_PREFS, Context.MODE_PRIVATE).edit()
            .putInt("grid_columns", Math.max(2, Math.min(5, n))).apply();
    }

    // Apparence
    public static boolean animations(Context c)  { return p(c).getBoolean("animations", true); }
    public static void setAnimations(Context c, boolean v) { p(c).edit().putBoolean("animations", v).apply(); }
    public static boolean liveBadges(Context c)  { return p(c).getBoolean("live_badges", true); }
    public static void setLiveBadges(Context c, boolean v) { p(c).edit().putBoolean("live_badges", v).apply(); }

    // Lecteur
    public static int defaultQuality(Context c) { return p(c).getInt("quality_default", 0); }
    public static void setDefaultQuality(Context c, int i) { p(c).edit().putInt("quality_default", i).apply(); }
    public static boolean subtitles(Context c) { return p(c).getBoolean("subtitles", false); }
    public static void setSubtitles(Context c, boolean v) { p(c).edit().putBoolean("subtitles", v).apply(); }
    public static int audioLangIndex(Context c) {
        int i = p(c).getInt("audio_lang", 0);
        return i >= 0 && i < AUDIO_CODES.length ? i : 0;
    }
    public static void setAudioLangIndex(Context c, int i) { p(c).edit().putInt("audio_lang", i).apply(); }

    // Réseau
    public static int networkMode(Context c) { return p(c).getInt("net_mode", NET_AUTO); }
    public static void setNetworkMode(Context c, int m) { p(c).edit().putInt("net_mode", m).apply(); }

    // Actualisation des chaînes
    public static int refreshFreq(Context c) { return p(c).getInt("refresh_freq", REFRESH_WEEKLY); }
    public static void setRefreshFreq(Context c, int f) { p(c).edit().putInt("refresh_freq", f).apply(); }
    public static boolean refreshWifiOnly(Context c) { return p(c).getBoolean("refresh_wifi", false); }
    public static void setRefreshWifiOnly(Context c, boolean v) { p(c).edit().putBoolean("refresh_wifi", v).apply(); }

    /** Délai (ms) au-delà duquel une actualisation automatique est due ; -1 = jamais (manuel). */
    public static long refreshIntervalMs(Context c) {
        switch (refreshFreq(c)) {
            case REFRESH_MANUAL:     return -1;
            case REFRESH_EVERY_OPEN: return 0;
            case REFRESH_DAILY:      return 24L * 3600_000L;
            case REFRESH_3_DAYS:     return 3L * 24 * 3600_000L;
            default:                 return 7L * 24 * 3600_000L;
        }
    }

    public static boolean isOnWifi(Context c) {
        try {
            android.net.ConnectivityManager cm = (android.net.ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) return false;
            return !cm.isActiveNetworkMetered();
        } catch (Exception e) { return false; }
    }
}
