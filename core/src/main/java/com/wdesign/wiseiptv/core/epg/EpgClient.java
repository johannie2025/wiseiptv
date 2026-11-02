package com.wdesign.wiseiptv.core.epg;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Guide des programmes (EPG) et Replay via l'API Xtream Codes du serveur.
 * Les identifiants sont déduits de l'URL du flux : http://hôte/[live/]user/pass/id[.ext]
 */
public final class EpgClient {
    private EpgClient() {}

    private static final Pattern STREAM = Pattern.compile(
        "^(https?://[^/]+)/(?:live/)?([^/]+)/([^/]+)/(\\d+)(?:\\.\\w+)?(?:\\?.*)?$");
    private static final ExecutorService EXEC = Executors.newFixedThreadPool(2);
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    public static final class Source {
        public String base, user, pass, id;
    }

    public static final class Program {
        public String title = "", desc = "", startStr = "";
        public long start, end;      // secondes epoch
        public boolean archive;      // replay disponible
        public boolean isNow(long nowSec) { return nowSec >= start && nowSec < end; }
        public boolean isPast(long nowSec) { return end <= nowSec; }
        public int progress(long nowSec) {
            if (end <= start) return 0;
            return (int) Math.max(0, Math.min(100, (nowSec - start) * 100 / (end - start)));
        }
        public String time() { return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(start * 1000)); }
    }

    public interface Callback {
        void onResult(List<Program> programs, Source source);
        void onError(String message);
    }

    public static Source parse(String streamUrl) {
        if (streamUrl == null) return null;
        Matcher m = STREAM.matcher(streamUrl.trim());
        if (!m.matches()) return null;
        Source s = new Source();
        s.base = m.group(1); s.user = m.group(2); s.pass = m.group(3); s.id = m.group(4);
        return s;
    }

    /** Charge le guide de la chaîne (résultat renvoyé sur le thread principal). */
    public static void load(String streamUrl, Callback cb) {
        final Source src = parse(streamUrl);
        if (src == null) { post(() -> cb.onError("Guide indisponible pour cette source")); return; }
        EXEC.execute(() -> {
            try {
                List<Program> list = fetch(src, "get_simple_data_table");
                if (list.isEmpty()) list = fetch(src, "get_short_epg&limit=24");
                if (list.isEmpty()) { post(() -> cb.onError("Programme indisponible pour cette chaîne")); return; }
                final List<Program> res = window(list);
                post(() -> cb.onResult(res, src));
            } catch (Exception e) {
                post(() -> cb.onError("Programme indisponible (réseau)"));
            }
        });
    }

    /** Garde de 6 h dans le passé à 18 h dans le futur, trié par heure. */
    private static List<Program> window(List<Program> in) {
        long now = System.currentTimeMillis() / 1000;
        List<Program> out = new ArrayList<>();
        for (Program p : in) if (p.end > now - 6 * 3600 && p.start < now + 18 * 3600) out.add(p);
        Collections.sort(out, (a, b) -> Long.compare(a.start, b.start));
        return out;
    }

    private static List<Program> fetch(Source s, String action) throws Exception {
        String url = s.base + "/player_api.php?username=" + enc(s.user) + "&password=" + enc(s.pass)
            + "&action=" + action + "&stream_id=" + s.id;
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setConnectTimeout(10000); c.setReadTimeout(12000);
        c.setRequestProperty("User-Agent", "WiseIPTV");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) sb.append(line);
        } finally { c.disconnect(); }
        List<Program> out = new ArrayList<>();
        JSONObject root = new JSONObject(sb.toString());
        JSONArray arr = root.optJSONArray("epg_listings");
        if (arr == null) return out;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            if (o == null) continue;
            Program p = new Program();
            p.title = decode(o.optString("title"));
            p.desc = decode(o.optString("description"));
            p.startStr = o.optString("start");
            p.start = o.optLong("start_timestamp", 0);
            p.end = o.optLong("stop_timestamp", 0);
            if (p.start == 0 || p.end == 0) continue;
            p.archive = "1".equals(o.optString("has_archive", "0"));
            out.add(p);
        }
        return out;
    }

    /** URL de replay (timeshift Xtream) d'un programme passé. */
    public static String replayUrl(Source s, Program p) {
        long minutes = Math.max(1, (p.end - p.start) / 60);
        String ts;
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(p.startStr);
            ts = new SimpleDateFormat("yyyy-MM-dd:HH-mm", Locale.US).format(d);
        } catch (Exception e) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd:HH-mm", Locale.US);
            f.setTimeZone(TimeZone.getTimeZone("UTC"));
            ts = f.format(new Date(p.start * 1000));
        }
        return s.base + "/timeshift/" + s.user + "/" + s.pass + "/" + minutes + "/" + ts + "/" + s.id + ".ts";
    }

    private static String decode(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        if (raw.matches("^[A-Za-z0-9+/=\\s]+$") && raw.length() % 4 == 0) {
            try {
                String d = new String(Base64.decode(raw, Base64.DEFAULT), StandardCharsets.UTF_8).trim();
                if (!d.isEmpty() && d.indexOf('�') < 0) return d;
            } catch (Exception ignored) { }
        }
        return raw.trim();
    }

    private static String enc(String s) {
        try { return java.net.URLEncoder.encode(s, "UTF-8"); } catch (Exception e) { return s; }
    }

    private static void post(Runnable r) { MAIN.post(r); }
}
