package com.wdesign.wiseiptv.mobile;

import android.app.Application;
import com.wdesign.wiseiptv.core.db.AppDatabase;

/**
 * WiseApp — pré-initialise la BDD uniquement.
 * Le téléchargement des playlists est déclenché exclusivement
 * par MainActivity.startBackgroundDownload() via les extras d'ActivationActivity.
 */
public class WiseApp extends Application {
    private static WiseApp instance;
    public static WiseApp get() { return instance; }

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        AppDatabase.get(this); // pré-init pour réduire la latence au premier accès
    }
}
