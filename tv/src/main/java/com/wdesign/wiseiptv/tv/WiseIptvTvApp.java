package com.wdesign.wiseiptv.tv;

import android.app.Application;
import android.preference.PreferenceManager;
import android.util.Log;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.tv.util.PlaylistLoader; // Assurez-vous que le loader est partagé ou adaptez le package
import com.wdesign.wiseiptv.tv.util.ActivationManager;

public class WiseIptvTvApp extends Application {
    private static final String TAG = "WiseIptvTvApp";
    public static final String PREF_PLAYLIST_URL = "playlist_url";
    private static WiseIptvTvApp instance;
    
    public static WiseIptvTvApp get() { return instance; }

    @Override 
    public void onCreate() { 
        super.onCreate(); 
        instance = this; 
        
        // 1. Initialiser la base de données de l'application TV
        AppDatabase db = AppDatabase.get(this);

        // 2. Lancer la vérification de l'activation sur le serveur mutualisé PHP
        ActivationManager.checkAndSync(this, new ActivationManager.OnResult() {
            @Override 
            public void onActivated(DeviceSecurity.ActivationResult r) {
                Log.d(TAG, "Téléviseur/Box activé avec succès. URL: " + r.url);
                // La playlist d'activation est gérée et injectée de manière transparente
            }

            @Override 
            public void onExpired(String status) {
                // Coupe l'accès et purge les chaînes locales si l'abonnement IPTV est expiré
                Log.w(TAG, "Statut d'activation non valide sur ce téléviseur : " + status);
            }

            @Override 
            public void onError(String msg) {
                // Erreur réseau ou serveur inaccessible -> mode hors-ligne ou rafraîchissement classique
                Log.e(TAG, "Impossible de joindre le panel d'activation : " + msg);
                // PlaylistLoader.refreshStaleIfNeeded(db, null); // Activer si disponible dans le core
            }
        });
    }

    public String getPlaylistUrl() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_PLAYLIST_URL, "");
    }

    public void savePlaylistUrl(String url) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(PREF_PLAYLIST_URL, url).apply();
    }
}