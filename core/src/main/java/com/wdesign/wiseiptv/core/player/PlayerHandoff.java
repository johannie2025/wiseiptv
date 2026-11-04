package com.wdesign.wiseiptv.core.player;

import android.os.Handler;
import android.os.Looper;

/**
 * Passe le lecteur de l'aperçu (accueil) au lecteur plein écran SANS recharger le flux.
 * L'écran d'accueil "offre" son moteur ; le lecteur le "prend" s'il s'agit de la même URL.
 * Si personne ne le prend sous 6 s, le moteur est libéré (aucune fuite).
 */
public final class PlayerHandoff {
    private static PlayerEngine engine;
    private static String url;
    private static final Handler H = new Handler(Looper.getMainLooper());
    private static final Runnable EXPIRE = () -> clear(true);

    private PlayerHandoff() {}

    public static synchronized void offer(PlayerEngine e, String streamUrl) {
        clear(true);
        engine = e; url = streamUrl;
        H.postDelayed(EXPIRE, 6000);
    }

    /** @return le moteur déjà en lecture pour cette URL, ou null. Le receveur en devient propriétaire. */
    public static synchronized PlayerEngine take(String streamUrl) {
        if (engine == null || url == null || !url.equals(streamUrl)) return null;
        PlayerEngine e = engine; engine = null; url = null;
        H.removeCallbacks(EXPIRE);
        return e;
    }

    private static synchronized void clear(boolean release) {
        H.removeCallbacks(EXPIRE);
        if (engine != null && release) engine.release();
        engine = null; url = null;
    }
}
