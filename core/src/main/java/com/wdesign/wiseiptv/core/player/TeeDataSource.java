package com.wdesign.wiseiptv.core.player;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * DataSource « tee » : laisse le lecteur lire normalement et recopie les octets de média dans
 * l'enregistrement en cours. Une seule connexion au serveur (beaucoup d'abonnements IPTV refusent
 * une seconde connexion simultanée, ce qui rendait l'enregistrement vide).
 */
@UnstableApi
final class TeeDataSource implements DataSource {
    private final DataSource upstream;
    private final PlayerEngine engine;
    private boolean record;

    TeeDataSource(DataSource upstream, PlayerEngine engine) {
        this.upstream = upstream;
        this.engine = engine;
    }

    @Override public void addTransferListener(@NonNull TransferListener l) { upstream.addTransferListener(l); }

    @Override public long open(@NonNull DataSpec spec) throws IOException {
        String p = spec.uri.getPath() == null ? "" : spec.uri.getPath().toLowerCase(Locale.ROOT);
        record = !(p.endsWith(".m3u8") || p.endsWith(".m3u") || p.endsWith(".mpd") || p.endsWith(".key")
            || p.endsWith(".vtt") || p.endsWith(".webvtt") || p.endsWith(".xml"));
        return upstream.open(spec);
    }

    @Override public int read(@NonNull byte[] buffer, int offset, int length) throws IOException {
        int n = upstream.read(buffer, offset, length);
        if (n > 0 && record) engine.onMediaBytes(buffer, offset, n);
        return n;
    }

    @Nullable @Override public Uri getUri() { return upstream.getUri(); }

    @NonNull @Override public Map<String, List<String>> getResponseHeaders() { return upstream.getResponseHeaders(); }

    @Override public void close() throws IOException { upstream.close(); }
}
