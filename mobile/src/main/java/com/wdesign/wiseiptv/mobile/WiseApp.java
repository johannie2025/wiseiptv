package com.wdesign.wiseiptv.mobile;

import android.app.Application;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.mobile.util.ActivationManager;
import com.wdesign.wiseiptv.mobile.util.PlaylistLoader;

public class WiseApp extends Application {
    private static WiseApp instance;
    public static WiseApp get() { return instance; }

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        AppDatabase db = AppDatabase.get(this);

        // 1. Vérifier l'activation sur le panel
        ActivationManager.checkAndSync(this, new ActivationManager.OnResult() {
            @Override public void onActivated(com.wdesign.wiseiptv.core.security.DeviceSecurity.ActivationResult r) {
                // Playlist d'activation mise à jour automatiquement
            }
            @Override public void onExpired(String status) {
                // Playlists supprimées par ActivationManager
                android.util.Log.w("WiseApp", "Activation expirée: " + status);
            }
            @Override public void onError(String msg) {
                // Pas de réseau → refresh hebdomadaire classique
                PlaylistLoader.refreshStaleIfNeeded(db, null);
            }
        });
    }
}
