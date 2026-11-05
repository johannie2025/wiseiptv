package com.wdesign.wiseiptv.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;

/** Mémorise où l'utilisateur s'est arrêté pour reprendre à la réouverture de l'app. */
public final class ResumeStore {
    private ResumeStore() {}

    public static final String SCREEN_MAIN = "main", SCREEN_ACCOUNT = "account",
        SCREEN_SETTINGS = "settings", SCREEN_ABOUT = "about", SCREEN_PLAYER = "player";

    private static SharedPreferences p(Context c) {
        return c.getApplicationContext().getSharedPreferences("wise_resume", Context.MODE_PRIVATE);
    }

    public static void setScreen(Context c, String screen) { p(c).edit().putString("screen", screen).apply(); }
    public static String screen(Context c) { return p(c).getString("screen", SCREEN_MAIN); }

    /** tab : -1 = accueil, 0 Tout, 1 Live, 2 Films, 3 Séries, 4 Favoris. */
    public static void setTab(Context c, int tab) { p(c).edit().putInt("tab", tab).apply(); }
    public static int tab(Context c) { return p(c).getInt("tab", -1); }

    public static void setPlayer(Context c, long channelId, long positionMs, boolean fullscreen) {
        p(c).edit().putLong("ch_id", channelId).putLong("ch_pos", positionMs)
            .putBoolean("ch_full", fullscreen).apply();
    }
    public static long channelId(Context c) { return p(c).getLong("ch_id", -1); }
    public static long position(Context c)  { return p(c).getLong("ch_pos", 0); }
    public static boolean fullscreen(Context c) { return p(c).getBoolean("ch_full", false); }
}
