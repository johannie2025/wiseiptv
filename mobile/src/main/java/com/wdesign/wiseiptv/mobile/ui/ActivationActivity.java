package com.wdesign.wiseiptv.mobile.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.util.ActivationManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ActivationActivity — PREMIER ÉCRAN de l'APK Mobile (Corrigé pour contrôle fluide unique).
 */
public class ActivationActivity extends AppCompatActivity {

    private TextView  tvDeviceKey, tvStatus, tvStatusDetail;
    private TextView  tvProvider, tvLogin, tvPassword, tvExpiry;
    private View      cardProvider;
    private Button    btnCheck, btnAccess;
    private ProgressBar progressBar;
    private String    deviceKey;

    private static final String PREFS_ACTIVATION = "wise_activation_tv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        // Initialisation des vues
        tvDeviceKey    = findViewById(R.id.tvDeviceKey);
        tvStatus       = findViewById(R.id.tvStatus);
        tvStatusDetail = findViewById(R.id.tvStatusDetail);
        tvProvider     = findViewById(R.id.tvProvider);
        tvLogin        = findViewById(R.id.tvLogin);
        tvPassword     = findViewById(R.id.tvPassword);
        tvExpiry       = findViewById(R.id.tvExpiry);
        cardProvider   = findViewById(R.id.cardProvider);
        btnCheck       = findViewById(R.id.btnCheck);
        btnAccess      = findViewById(R.id.btnAccess);
        progressBar    = findViewById(R.id.progressBar);

        // Récupération ou génération de la clé courte à 6 caractères
        deviceKey = DeviceSecurity.getOrCreateKey(this);
        tvDeviceKey.setText(deviceKey);

        // Clic pour copier le code
        findViewById(R.id.btnCopy).setOnClickListener(v -> {
            ClipboardManager cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cb != null) {
                cb.setPrimaryClip(ClipData.newPlainText("Device Key", deviceKey));
                Toast.makeText(this, "Code copié !", Toast.LENGTH_SHORT).show();
            }
        });

        // ─── OPTIMISATION 1 : VÉRIFICATION LOCALE DIRECTE AU DÉMARRAGE ───
        SharedPreferences prefs = getSharedPreferences(PREFS_ACTIVATION, Context.MODE_PRIVATE);
        String savedStatus = prefs.getString("status", "INACTIVE");
        String expiresAt   = prefs.getString("expires_at", "");

        if ("ACTIVE".equals(savedStatus) && !isExpired(expiresAt)) {
            // Si c'est déjà actif localement et non expiré, on va au Main sans afficher l'écran !
            goToMain();
            return;
        }

        // Lancer une vérification réseau silencieuse en tâche de fond pour mettre à jour le statut au démarrage
        autoCheckSilently();

        // Événements des boutons
        btnCheck.setOnClickListener(v -> checkActivation(false));
        
        // ─── OPTIMISATION 2 : CLIC SUR ACCÉDER AU CONTENU DIRECT ET SÉCURISÉ ───
        btnAccess.setOnClickListener(v -> checkActivation(true));
    }

    private void autoCheckSilently() {
        setLoading(true);
        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override
            public void onActive(DeviceSecurity.ActivationResult r) {
                runOnUiThread(() -> {
                    setLoading(false);
                    saveActivationState(r);
                    showActive(r);
                    // Téléchargement immédiat et silencieux en arrière-plan sans action utilisateur
                    triggerSilentDownload(r);
                });
            }

            @Override
            public void onInactive(String status, String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    clearActivationState();
                    if ("EXPIRED".equals(status)) showExpired();
                    else if ("DISABLED".equals(status)) showDisabled();
                    else showPending("");
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    // Si le réseau échoue mais qu'on a un cache actif valide, on laisse passer
                    SharedPreferences prefs = getSharedPreferences(PREFS_ACTIVATION, Context.MODE_PRIVATE);
                    if ("ACTIVE".equals(prefs.getString("status", ""))) {
                        showOffline(message);
                    }
                });
            }
        });
    }

    private void checkActivation(boolean launchImmediatelyOnSuccess) {
        setLoading(true);
        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override
            public void onActive(DeviceSecurity.ActivationResult r) {
                runOnUiThread(() -> {
                    setLoading(false);
                    saveActivationState(r);
                    showActive(r);
                    
                    // Téléchargement automatique forcé dans l'appareil de l'utilisateur
                    triggerSilentDownload(r);

                    if (launchImmediatelyOnSuccess) {
                        goToMain();
                    } else {
                        Toast.makeText(ActivationActivity.this, "Activation validée ! Cliquez sur Accéder au contenu.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onInactive(String status, String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    clearActivationState();
                    if ("EXPIRED".equals(status)) showExpired();
                    else if ("DISABLED".equals(status)) showDisabled();
                    else {
                        showPending(message);
                        Toast.makeText(ActivationActivity.this, "Appareil non activé sur le panel.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(ActivationActivity.this, "Erreur réseau : " + message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // Sauvegarde l'activation pour que l'écran ne s'affiche plus jamais
    private void saveActivationState(DeviceSecurity.ActivationResult r) {
        getSharedPreferences(PREFS_ACTIVATION, Context.MODE_PRIVATE).edit()
                .putString("status", "ACTIVE")
                .putString("expires_at", r.expiresAt)
                .apply();
    }

    private void clearActivationState() {
        getSharedPreferences(PREFS_ACTIVATION, Context.MODE_PRIVATE).edit().clear().apply();
    }

    // Déclenche le téléchargement automatique et totalement silencieux des playlists dans la DB de l'appareil
    private void triggerSilentDownload(DeviceSecurity.ActivationResult r) {
        AppDatabase db = AppDatabase.get(this);
        ActivationManager.upsertActivationPlaylist(this, db, r);
    }

    private boolean isExpired(String dateStr) {
        try {
            if (dateStr == null || dateStr.isEmpty()) return true;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date expireDate = sdf.parse(dateStr);
            return new Date().after(expireDate);
        } catch (Exception e) {
            return true;
        }
    }

    private void showActive(DeviceSecurity.ActivationResult r) {
        cardProvider.setVisibility(View.VISIBLE);
        btnAccess.setVisibility(View.VISIBLE);
        btnAccess.setEnabled(true);
        tvStatus.setText("✅ ACTIF");
        tvStatus.setTextColor(0xFF4CAF50);
        tvStatusDetail.setText("Votre appareil est activé.");
        tvProvider.setText("WiseIPTV Premium");
        tvLogin.setText(r.login);
        tvPassword.setText(r.password);
        tvExpiry.setText(r.expiresAt);
    }

    private void showExpired() {
        cardProvider.setVisibility(View.GONE);
        btnAccess.setVisibility(View.GONE);
        tvStatus.setText("⌛ EXPIRÉ");
        tvStatus.setTextColor(0xFFFF9800);
        tvStatusDetail.setText("Votre abonnement a expiré.\\nRenouvelez-le auprès de votre revendeur.");
    }

    private void showDisabled() {
        cardProvider.setVisibility(View.GONE);
        btnAccess.setVisibility(View.GONE);
        tvStatus.setText("🚫 DÉSACTIVÉ");
        tvStatus.setTextColor(0xFFF44336);
        tvStatusDetail.setText("Votre accès a été suspendu.\\nContactez votre revendeur.");
    }

    private void showPending(String errMsg) {
        cardProvider.setVisibility(View.GONE);
        btnAccess.setVisibility(View.GONE);
        tvStatus.setText("⏳ EN ATTENTE");
        tvStatus.setTextColor(0xFFFFEB3B);
        tvStatusDetail.setText("Communiquez votre Device Key à votre revendeur pour activation.");
    }

    private void showOffline(String errMsg) {
        tvStatus.setText("📡 HORS LIGNE (cache)");
        tvStatus.setTextColor(0xFF9E9E9E);
        tvStatusDetail.setText("Connexion impossible. Accès accordé depuis le cache.");
        btnAccess.setVisibility(View.VISIBLE);
        btnAccess.setEnabled(true);
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnCheck.setEnabled(!loading);
        btnCheck.setText(loading ? "Vérification…" : "Vérifier l'activation");
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish(); // Détruit l'écran d'activation pour éviter le retour en arrière dessus
    }
}