package com.wdesign.wiseiptv.core.util;

import android.app.UiModeManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import java.util.Locale;
import org.json.JSONObject;

/** Informations sur l'appareil connecté (modèle, Android, écran, réseau…) envoyées au panel. */
public final class DeviceInfo {
    private DeviceInfo() {}

    public static String model() {
        String m = Build.MODEL == null ? "" : Build.MODEL.trim();
        String maker = Build.MANUFACTURER == null ? "" : Build.MANUFACTURER.trim();
        if (m.toLowerCase(Locale.ROOT).startsWith(maker.toLowerCase(Locale.ROOT))) return m;
        return (maker.isEmpty() ? "" : cap(maker) + " ") + m;
    }

    private static String cap(String s) { return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1); }

    public static boolean isTv(Context c) {
        UiModeManager um = (UiModeManager) c.getSystemService(Context.UI_MODE_SERVICE);
        return um != null && um.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    private static String network(Context c) {
        try {
            ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm != null ? cm.getActiveNetworkInfo() : null;
            if (ni == null || !ni.isConnected()) return "offline";
            return ni.getType() == ConnectivityManager.TYPE_WIFI ? "wifi"
                : ni.getType() == ConnectivityManager.TYPE_ETHERNET ? "ethernet" : "mobile";
        } catch (Exception e) { return "unknown"; }
    }

    public static JSONObject toJson(Context ctx) {
        Context c = ctx.getApplicationContext();
        JSONObject o = new JSONObject();
        try {
            o.put("model", model());
            o.put("manufacturer", Build.MANUFACTURER);
            o.put("brand", Build.BRAND);
            o.put("device", Build.DEVICE);
            o.put("android_version", Build.VERSION.RELEASE);
            o.put("sdk", Build.VERSION.SDK_INT);
            o.put("type", isTv(c) ? "tv" : "mobile");
            o.put("locale", Locale.getDefault().toString());
            o.put("network", network(c));
            DisplayMetrics dm = c.getResources().getDisplayMetrics();
            o.put("screen", dm.widthPixels + "x" + dm.heightPixels);
            o.put("package", c.getPackageName());
            try {
                PackageInfo pi = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
                o.put("app_version", pi.versionName);
            } catch (Exception ignored) { }
        } catch (Exception ignored) { }
        return o;
    }
}
