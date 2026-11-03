package com.wdesign.wiseiptv.core.player;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.PlayerView;
import java.lang.ref.WeakReference;

/**
 * Passage de relais « aperçu → lecteur plein écran ».
 *
 * L'écran d'accueil (aperçu animé) dépose ici son lecteur déjà en train de jouer ; l'écran du lecteur
 * le récupère et continue la lecture à l'endroit exact : le flux n'est ni rechargé ni reconnecté
 * (important : beaucoup d'abonnements IPTV limitent le nombre de connexions simultanées).
 *
 * Sécurité : un lecteur déposé mais jamais repris est libéré par {@link #discard()}.
 */
@OptIn(markerClass = UnstableApi.class)
public final class PlayerHandoff {
    private PlayerHandoff() {}

    private static PlayerEngine engine;
    private static String url;
    private static WeakReference<PlayerView> fromView;

    /** Dépose un lecteur en cours de lecture (l'ancien dépôt non repris est libéré). */
    public static synchronized void offer(PlayerEngine e, String streamUrl, PlayerView from) {
        if (engine != null && engine != e) engine.release();
        engine = e;
        url = streamUrl;
        fromView = from != null ? new WeakReference<>(from) : null;
    }

    /** Récupère le lecteur déposé si c'est bien le même flux ; sinon renvoie null (rien n'est perdu). */
    public static synchronized PlayerEngine take(String streamUrl) {
        if (engine == null) return null;
        if (streamUrl == null || !streamUrl.equals(url)) { discard(); return null; }
        PlayerEngine e = engine;
        engine = null;
        url = null;
        return e;
    }

    /** Vue d'aperçu d'où vient le lecteur (à passer à PlayerView.switchTargetView). À appeler après take(). */
    public static synchronized PlayerView takeView() {
        PlayerView v = fromView != null ? fromView.get() : null;
        fromView = null;
        return v;
    }

    /** Libère un lecteur déposé mais jamais repris. */
    public static synchronized void discard() {
        if (engine != null) { try { engine.release(); } catch (Exception ignored) { } }
        engine = null; url = null; fromView = null;
    }

    public static synchronized boolean hasPending() { return engine != null; }
}
