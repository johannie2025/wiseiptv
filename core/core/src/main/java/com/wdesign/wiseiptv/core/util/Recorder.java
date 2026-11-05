package com.wdesign.wiseiptv.core.util;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Enregistre le flux en cours dans un fichier (Films/WiseIPTV du stockage de l'app).
 * Flux direct (.ts, .mp4…) : copie des octets. HLS (.m3u8) : téléchargement des segments au fil de l'eau.
 */
public final class Recorder {
    public interface Listener {
        void onStarted(File file);
        void onFinished(File file, long bytes, String error);   // error == null si arrêt volontaire
    }

    private static final OkHttpClient HTTP = new OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true).build();
    private static final long MIN_FREE = 150L * 1024 * 1024;

    private volatile boolean running;
    private Thread thread;
    private File file;

    public boolean isRecording() { return running; }
    public File getFile() { return file; }

    public synchronized void start(final Context ctx, final String url, String name, final Listener l) {
        if (running || url == null || url.isEmpty()) return;
        final Context app = ctx.getApplicationContext();
        File dir = app.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if (dir == null) dir = app.getFilesDir();
        dir = new File(dir, "WiseIPTV");
        if (!dir.exists()) dir.mkdirs();
        String clean = (name == null ? "enregistrement" : name).replaceAll("[^A-Za-z0-9._-]+", "_");
        String stamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        file = new File(dir, clean + "_" + stamp + extension(url));
        final File out = file;
        running = true;
        thread = new Thread(() -> {
            long total = 0;
            String error = null;
            try {
                if (l != null) l.onStarted(out);
                try (OutputStream os = new FileOutputStream(out)) {
                    if (url.toLowerCase(Locale.ROOT).contains(".m3u8")) total = recordHls(app, url, os, out);
                    else total = recordDirect(app, url, os, out);
                }
            } catch (Exception e) {
                if (running) error = e.getMessage() != null ? e.getMessage() : "Erreur d'enregistrement";
            } finally {
                running = false;
                DataUsage.flush(app);
            }
            if (total <= 0) out.delete();
            if (l != null) l.onFinished(out, total, error);
        }, "wise-recorder");
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        if (thread != null) thread.interrupt();
    }

    private static String extension(String url) {
        String u = url.toLowerCase(Locale.ROOT);
        int q = u.indexOf('?');
        if (q > 0) u = u.substring(0, q);
        if (u.endsWith(".mp4")) return ".mp4";
        if (u.endsWith(".mkv")) return ".mkv";
        if (u.endsWith(".avi")) return ".avi";
        return ".ts";
    }

    private long recordDirect(Context app, String url, OutputStream os, File out) throws Exception {
        Request rq = new Request.Builder().url(url).header("User-Agent", "WiseIPTV/2.0").build();
        long total = 0;
        try (Response r = HTTP.newCall(rq).execute()) {
            if (!r.isSuccessful() || r.body() == null) throw new Exception("Flux indisponible (HTTP " + r.code() + ")");
            try (InputStream in = r.body().byteStream()) {
                byte[] buf = new byte[64 * 1024];
                int n;
                while (running && (n = in.read(buf)) > 0) {
                    os.write(buf, 0, n);
                    total += n;
                    DataUsage.add(app, n);
                    if (out.getUsableSpace() < MIN_FREE) throw new Exception("Espace de stockage insuffisant");
                }
            }
        }
        return total;
    }

    private long recordHls(Context app, String url, OutputStream os, File out) throws Exception {
        String media = url;
        String text = get(app, media);
        if (text.contains("#EXT-X-STREAM-INF")) {          // playlist maître → meilleure variante
            String best = null;
            long bestBw = -1;
            String[] lines = text.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String ln = lines[i].trim();
                if (ln.startsWith("#EXT-X-STREAM-INF")) {
                    long bw = 0;
                    int k = ln.indexOf("BANDWIDTH=");
                    if (k >= 0) {
                        String num = ln.substring(k + 10).split("[,\\s]")[0];
                        try { bw = Long.parseLong(num); } catch (Exception ignored) { }
                    }
                    for (int j = i + 1; j < lines.length; j++) {
                        String cand = lines[j].trim();
                        if (!cand.isEmpty() && !cand.startsWith("#")) {
                            if (bw > bestBw) { bestBw = bw; best = cand; }
                            break;
                        }
                    }
                }
            }
            if (best == null) throw new Exception("Playlist HLS invalide");
            media = URI.create(url).resolve(best).toString();
        }
        LinkedHashSet<String> done = new LinkedHashSet<>();
        long total = 0;
        while (running) {
            text = get(app, media);
            double target = 6;
            boolean end = text.contains("#EXT-X-ENDLIST");
            List<String> segs = new ArrayList<>();
            for (String ln : text.split("\n")) {
                String t = ln.trim();
                if (t.startsWith("#EXT-X-TARGETDURATION:")) {
                    try { target = Double.parseDouble(t.substring(22).trim()); } catch (Exception ignored) { }
                } else if (!t.isEmpty() && !t.startsWith("#")) segs.add(URI.create(media).resolve(t).toString());
            }
            for (String seg : segs) {
                if (!running) break;
                if (done.contains(seg)) continue;
                Request rq = new Request.Builder().url(seg).header("User-Agent", "WiseIPTV/2.0").build();
                try (Response r = HTTP.newCall(rq).execute()) {
                    if (!r.isSuccessful() || r.body() == null) continue;
                    byte[] data = r.body().bytes();
                    os.write(data);
                    total += data.length;
                    DataUsage.add(app, data.length);
                }
                done.add(seg);
                if (done.size() > 400) { String first = done.iterator().next(); done.remove(first); }
                if (out.getUsableSpace() < MIN_FREE) throw new Exception("Espace de stockage insuffisant");
            }
            if (end) break;
            try { Thread.sleep((long) (Math.max(1.0, target / 2) * 1000)); }
            catch (InterruptedException e) { break; }
        }
        return total;
    }

    private String get(Context app, String url) throws Exception {
        Request rq = new Request.Builder().url(url).header("User-Agent", "WiseIPTV/2.0").build();
        try (Response r = HTTP.newCall(rq).execute()) {
            if (!r.isSuccessful() || r.body() == null) throw new Exception("Flux indisponible (HTTP " + r.code() + ")");
            String s = r.body().string();
            DataUsage.add(app, s.length());
            return s;
        }
    }
}
