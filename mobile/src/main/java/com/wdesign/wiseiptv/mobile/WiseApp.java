package com.wdesign.wiseiptv.mobile;

import android.app.Application;
import com.wdesign.wiseiptv.core.db.AppDatabase;

/**
 * WiseApp — Application singleton.
 *
 * CORRECTION : checkAndSync() a été retiré d'ici.
 * Il causait un double appel réseau + double transaction Room en parallèle avec
 * celui de MainActivity, provoquant des deadlocks et des crashes aléatoires.
 *
 * Le seul déclencheur du téléchargement est désormais MainActivity.startBackgroundSync(),
 * qui reçoit l'ActivationResult directement depuis ActivationActivity via extras Intent.
 * WiseApp se contente d'initialiser la BDD pour qu'elle soit prête dès le démarrage.
 */
public class WiseApp extends Application {
    private static WiseApp instance;
    public static WiseApp get() { return instance; }

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        // Pré-initialiser la BDD en arrière-plan pour réduire la latence au premier accès
        AppDatabase.get(this);
    }
}
