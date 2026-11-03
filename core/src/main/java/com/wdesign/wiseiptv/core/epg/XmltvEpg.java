package com.wdesign.wiseiptv.core.epg;

import android.content.Context;
import android.util.Xml;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.util.DataUsage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import org.xmlpull.v1.XmlPullParser;

/**
 * Guide XMLTV : sources essayées dans l'ordre
 *  1. xmltv.php du serveur Xtream de l'abonnement ;
 *  2. les URL EPG fournies à l'activation ;
 *  3. les guides publics EPG Share (epgshare01.online), choisis selon le pays de la chaîne.
 * Les fichiers sont mis en cache (12 h) puis lus en flux : seul le guide de la chaîne est conservé.
 */
final class XmltvEpg {
    private XmltvEpg() {}

    private static final long TTL_MS = 12L * 3600_000L;
    private static final long MAX_BYTES = 160L * 1024 * 1024;
    private static final String PUBLIC = "https://epgshare01.online/epgshare01/epg_ripper_";

    private static final Map<String, List<EpgClient.Program>> MEM =
        new LinkedHashMap<String, List<EpgClient.Program>>(16, 0.75f, true) {
            @Override protected boolean removeEldestEntry(Map.Entry<String, List<EpgClient.Program>> e) {
                return size() > 24;
            }
        };

    private static final Map<String, String> COUNTRY = new HashMap<>();
    static {
        String[][] m = {{"FR", "FR1"}, {"US", "US2"}, {"GB", "UK1"}, {"UK", "UK1"}, {"CA", "CA2"},
            {"AU", "AU1"}, {"IN", "IN1"}, {"DE", "DE1"}, {"ES", "ES1"}, {"IT", "IT1"}, {"ZA", "ZA1"},
            {"NG", "NG1"}, {"AE", "AE1"}, {"SA", "AE1"}, {"EG", "AE1"}, {"MA", "AE1"}, {"DZ", "AE1"},
            {"TN", "AE1"}, {"QA", "AE1"}, {"IE", "IE1"}, {"NZ", "NZ1"}, {"NL", "NL1"}, {"PT", "PT1"},
            {"PL", "PL1"}, {"RO", "RO1"}, {"TR", "TR1"}, {"BR", "BR1"}, {"MX", "MX1"}, {"KE", "KE1"}};
        for (String[] e : m) COUNTRY.put(e[0], e[1]);
    }

    /** @return programmes de la chaîne (peut être vide) ; lève une exception si rien n'est joignable. */
    static List<EpgClient.Program> load(Context ctx, ChannelEntity ch, EpgClient.Source src) throws Exception {
        final String key = ch.id + ":" + (ch.epgChannelId == null ? ch.name : ch.epgChannelId);
        synchronized (MEM) {
            List<EpgClient.Program> hit = MEM.get(key);
            if (hit != null && !hit.isEmpty() && freshEnough(hit)) return hit;
        }
        Set<String> urls = new LinkedHashSet<>();
        if (src != null) urls.add(src.base + "/xmltv.php?username=" + enc(src.user) + "&password=" + enc(src.pass));
        for (String prefs : new String[]{"wise_activation", "wise_activation_tv"}) {
            String raw = ctx.getApplicationContext().getSharedPreferences(prefs, Context.MODE_PRIVATE)
                .getString("dns_epg_urls", "");
            for (String u : raw.split(",")) if (u.trim().startsWith("http")) urls.add(u.trim());
        }
        urls.addAll(publicSources(ch));

        Exception last = null;
        int tried = 0;
        for (String url : urls) {
            if (tried++ >= 5) break;
            try {
                File f = cached(ctx, url);
                List<EpgClient.Program> r = parse(f, ch, src);
                if (!r.isEmpty()) {
                    synchronized (MEM) { MEM.put(key, r); }
                    return r;
                }
            } catch (Exception e) { last = e; }
        }
        if (last != null && tried > 0) throw last;
        return new ArrayList<>();
    }

    private static boolean freshEnough(List<EpgClient.Program> l) {
        long now = System.currentTimeMillis() / 1000;
        return l.get(l.size() - 1).end > now + 1800;
    }

    private static List<String> publicSources(ChannelEntity ch) {
        List<String> out = new ArrayList<>();
        String code = ch.country == null ? "" : ch.country.trim().toUpperCase(Locale.ROOT);
        if (code.length() != 2) code = Locale.getDefault().getCountry().toUpperCase(Locale.ROOT);
        String f = COUNTRY.get(code);
        if (f != null) out.add(PUBLIC + f + ".xml.gz");
        for (String d : new String[]{"FR1", "US2", "UK1", "ES1"}) {
            String u = PUBLIC + d + ".xml.gz";
            if (!out.contains(u)) out.add(u);
            if (out.size() >= 3) break;
        }
        return out;
    }

    // ── Cache disque ──────────────────────────────────────────────

    private static File cached(Context ctx, String url) throws Exception {
        File dir = new File(ctx.getApplicationContext().getCacheDir(), "epg");
        if (!dir.exists()) dir.mkdirs();
        File[] old = dir.listFiles();
        if (old != null) for (File o : old)
            if (System.currentTimeMillis() - o.lastModified() > 3L * 24 * 3600_000L) o.delete();
        File f = new File(dir, Integer.toHexString(url.hashCode()) + ".xml");
        if (f.exists() && f.length() > 0 && System.currentTimeMillis() - f.lastModified() < TTL_MS) return f;

        File tmp = new File(dir, f.getName() + ".part");
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setConnectTimeout(15_000);
        c.setReadTimeout(60_000);
        c.setRequestProperty("User-Agent", "WiseIPTV/2.0");
        c.setRequestProperty("Accept-Encoding", "identity");
        try {
            if (c.getResponseCode() != 200) throw new Exception("EPG HTTP " + c.getResponseCode());
            long total = 0;
            try (InputStream in = c.getInputStream(); FileOutputStream out = new FileOutputStream(tmp)) {
                byte[] buf = new byte[32 * 1024];
                int n;
                while ((n = in.read(buf)) > 0) {
                    out.write(buf, 0, n);
                    total += n;
                    if (total > MAX_BYTES) throw new Exception("Guide trop volumineux");
                }
            }
            DataUsage.add(ctx.getApplicationContext(), total);
        } catch (Exception e) {
            tmp.delete();
            throw e;
        } finally { c.disconnect(); }
        if (f.exists()) f.delete();
        if (!tmp.renameTo(f)) throw new Exception("Cache EPG indisponible");
        return f;
    }

    // ── Lecture XMLTV en flux ─────────────────────────────────────

    private static List<EpgClient.Program> parse(File file, ChannelEntity ch, EpgClient.Source src) throws Exception {
        String wantId = ch.epgChannelId == null ? "" : ch.epgChannelId.trim();
        String wantName = norm(ch.name);
        if (wantId.isEmpty() && wantName.isEmpty()) return new ArrayList<>();
        long now = System.currentTimeMillis() / 1000;
        long from = now - 24 * 3600, to = now + 30 * 3600;
        List<EpgClient.Program> out = new ArrayList<>();
        Set<String> ids = new HashSet<>();

        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file), 64 * 1024);
        bin.mark(4);
        int b1 = bin.read(), b2 = bin.read();
        bin.reset();
        InputStream is = (b1 == 0x1f && b2 == 0x8b) ? new GZIPInputStream(bin, 64 * 1024) : bin;
        try {
            XmlPullParser p = Xml.newPullParser();
            p.setInput(is, null);
            String curChannel = null;
            boolean inChannel = false;
            int ev;
            while ((ev = p.next()) != XmlPullParser.END_DOCUMENT) {
                if (ev == XmlPullParser.START_TAG) {
                    String n = p.getName();
                    if ("channel".equals(n)) {
                        curChannel = p.getAttributeValue(null, "id");
                        inChannel = true;
                        if (curChannel != null && !wantId.isEmpty() && curChannel.equalsIgnoreCase(wantId))
                            ids.add(curChannel);
                    } else if (inChannel && "display-name".equals(n)) {
                        String dn = p.nextText();
                        if (curChannel != null && !wantName.isEmpty() && norm(dn).equals(wantName))
                            ids.add(curChannel);
                    } else if ("programme".equals(n)) {
                        String chn = p.getAttributeValue(null, "channel");
                        if (chn != null && ids.contains(chn)) readProgramme(p, out, from, to, now, src != null);
                        else skipTag(p);
                    }
                } else if (ev == XmlPullParser.END_TAG && "channel".equals(p.getName())) {
                    inChannel = false;
                }
            }
        } finally { is.close(); }
        Collections.sort(out, (a, c) -> Long.compare(a.start, c.start));
        return out;
    }

    private static void skipTag(XmlPullParser p) throws Exception {
        int depth = 1;
        while (depth > 0) {
            int ev = p.next();
            if (ev == XmlPullParser.START_TAG) depth++;
            else if (ev == XmlPullParser.END_TAG) depth--;
            else if (ev == XmlPullParser.END_DOCUMENT) return;
        }
    }

    private static void readProgramme(XmlPullParser p, List<EpgClient.Program> out, long from, long to,
                                      long now, boolean xtream) throws Exception {
        long start = time(p.getAttributeValue(null, "start"));
        long stop = time(p.getAttributeValue(null, "stop"));
        EpgClient.Program pr = new EpgClient.Program();
        int depth = 1;
        while (depth > 0) {
            int ev = p.next();
            if (ev == XmlPullParser.START_TAG) {
                String n = p.getName();
                if ("title".equals(n) && pr.title.isEmpty()) pr.title = p.nextText().trim();
                else if ("desc".equals(n) && pr.desc.isEmpty()) pr.desc = p.nextText().trim();
                else depth++;
            } else if (ev == XmlPullParser.END_TAG) depth--;
            else if (ev == XmlPullParser.END_DOCUMENT) break;
        }
        if (start <= 0 || stop <= start || stop < from || start > to) return;
        pr.start = start;
        pr.end = stop;
        pr.startStr = "";   // le replay utilisera l'heure UTC
        pr.archive = xtream && stop < now && start > now - 3L * 24 * 3600;
        out.add(pr);
    }

    private static long time(String s) {
        if (s == null || s.length() < 14) return 0;
        try {
            String v = s.trim();
            if (v.length() == 14) v += " +0000";
            else if (v.length() > 14 && v.charAt(14) != ' ') v = v.substring(0, 14) + " " + v.substring(14);
            Date d = new SimpleDateFormat("yyyyMMddHHmmss Z", Locale.US).parse(v);
            return d == null ? 0 : d.getTime() / 1000;
        } catch (Exception e) { return 0; }
    }

    private static String norm(String s) {
        if (s == null) return "";
        String t = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return t.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    private static String enc(String s) {
        try { return URLEncoder.encode(s == null ? "" : s, "UTF-8"); } catch (Exception e) { return s; }
    }
}
