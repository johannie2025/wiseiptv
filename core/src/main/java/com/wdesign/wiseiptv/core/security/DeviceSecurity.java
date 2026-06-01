package com.wdesign.wiseiptv.core.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * DeviceSecurity — Génère une device_key unique + interroge le panel d'activation.
 *
 * Utilisation :
 *   DeviceSecurity.check(context, new DeviceSecurity.Callback() {
 *       public void onActive(ActivationResult r) { // lancer la playlist }
 *       public void onInactive(String status, String message) { // afficher erreur }
 *       public void onError(String message) { // réseau / timeout }
 *   });
 */
public final class DeviceSecurity {

    private static final String TAG        = "DeviceSecurity";
    private static final String PANEL_URL = "http://wisedesign.pro/wiseiptvpanel/api/index.php";
    private static final String PREFS_NAME = "wise_device";
    private static final String KEY_DEVICE = "device_key";
    private static final int    TIMEOUT_MS = 15_000;

    private static final Executor EXEC = Executors.newSingleThreadExecutor();

    // ── Résultat d'activation ─────────────────────────────────────
    public static class ActivationResult {
        public final String deviceKey;
        public final String login;
        public final String password;
        public final String expiresAt;
        public final List<DnsEntry> dnsServers;

        ActivationResult(String dk, String l, String p, String exp, List<DnsEntry> dns) {
            deviceKey = dk; login = l; password = p; expiresAt = exp; dnsServers = dns;
        }
        /** DNS principal (priorité 0) */
        public String primaryDns() {
            return dnsServers.isEmpty() ? "" : dnsServers.get(0).url;
        }
        /** URL M3U Xtream construite à partir du premier DNS */
        public String buildM3uUrl() {
            String dns = primaryDns();
            if (dns.isEmpty()) return "";
            String base = dns.endsWith("/") ? dns : dns + "/";
            return base + "get.php?username=" + login + "&password=" + password + "&type=m3u_plus&output=ts";
        }
    }

    public static class DnsEntry {
        public final String url;
        public final String epgUrl;
        public final int    priority;
        DnsEntry(String u, String e, int p) { url=u; epgUrl=e; priority=p; }
    }

    // ── Callback ──────────────────────────────────────────────────
    public interface Callback {
        /** Device ACTIVE → accès autorisé */
        void onActive(ActivationResult result);
        /** Device DISABLED / EXPIRED / NOT_FOUND */
        void onInactive(String status, String message);
        /** Erreur réseau ou serveur */
        void onError(String message);
    }

    // ── Point d'entrée principal ───────────────────────────────────
    public static void check(Context ctx, Callback cb) {
        final String key = getOrCreateKey(ctx);
        EXEC.execute(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("device_key", key);
                String response = postJson(PANEL_URL, payload.toString());
                JSONObject json = new JSONObject(response);
                String status = json.optString("status", "ERROR");

                if ("ACTIVE".equals(status)) {
                    List<DnsEntry> dns = new ArrayList<>();
                    if (json.has("dns_servers")) {
                        JSONArray arr = json.getJSONArray("dns_servers");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject e = arr.getJSONObject(i);
                            dns.add(new DnsEntry(
                                e.optString("url",""),
                                e.optString("epg_url",""),
                                e.optInt("priority", i)
                            ));
                    }}
                    ActivationResult r = new ActivationResult(
                        key,
                        json.optString("login",""),
                        json.optString("password",""),
                        json.optString("expires_at",""),
                        dns
                    );
                    cb.onActive(r);
                } else {
                    cb.onInactive(status, json.optString("message", status));
                }
            } catch (IOException e) {
                Log.w(TAG, "Network error: " + e.getMessage());
                cb.onError("Connexion impossible. Vérifiez votre réseau.");
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage(), e);
                cb.onError("Erreur inattendue: " + e.getMessage());
            }
        });
    }

    // ── Récupérer ou générer la device_key ────────────────────────
    public static String getOrCreateKey(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String stored = prefs.getString(KEY_DEVICE, null);
        if (stored != null && stored.length() == 64) return stored;
        String key = generateKey(ctx);
        prefs.edit().putString(KEY_DEVICE, key).apply();
        return key;
    }

    /**
     * Génère une clé SHA-256 déterministe basée sur le matériel.
     * Combine ANDROID_ID + Build.FINGERPRINT + Build.SERIAL
     * pour maximiser l'unicité sur TV boxes, téléphones et tablettes.
     */
    private static String generateKey(Context ctx) {
        String androidId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        String fingerprint = Build.FINGERPRINT;
        @SuppressWarnings("deprecation")
        String serial = Build.SERIAL != null && !Build.SERIAL.equals(Build.UNKNOWN) ? Build.SERIAL : "";
        String raw = "WISE:" + androidId + "|" + fingerprint + "|" + serial;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(64);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString(); // 64 hex chars
        } catch (NoSuchAlgorithmException e) {
            // Fallback UUID déterministe (très rare)
            return String.format("%064x", Math.abs(raw.hashCode()));
        }
    }

    // ── HTTP POST JSON minimaliste (pas de dépendance OkHttp) ──────
    private static String postJson(String url, String json) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("User-Agent", "WiseIPTV-APK/2.0");
        conn.setConnectTimeout(TIMEOUT_MS);
        conn.setReadTimeout(TIMEOUT_MS);
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        int code = conn.getResponseCode();
        InputStream is = code >= 400 ? conn.getErrorStream() : conn.getInputStream();
        if (is == null) throw new IOException("Réponse vide (HTTP " + code + ")");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }
}
