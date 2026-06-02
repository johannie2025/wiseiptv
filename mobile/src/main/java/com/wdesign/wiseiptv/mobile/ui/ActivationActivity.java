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
 * ActivationActivity — PREMIER ÉCRAN de l'APK Mobile.
 * Optimisé : Pas de réaffichage inutile et téléchargement automatique en arrière-plan.
 */
public class ActivationActivity extends AppCompatActivity {

    private TextView  tvDeviceKey, tvStatus, tvStatusDetail;
    private TextView  tvProviderLabel, tvLogin, tvPassword, tvExpiry;
    private View      cardProvider;
    private Button    btnCheck, btnAccess, btnCopy;
    private ProgressBar progressBar;
    private String    deviceKey;

    private static final String PREFS_ACTIVATION = "wise_activation_tv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation); // 🛠️ ID RESTAURÉ[cite: 5]

        // 🛠️ COMPOSANTS ET IDENTIFIANTS RESTAURÉS À LEUR ÉTAT D'ORIGINE STRICT[cite: 5]
        tvDeviceKey    = findViewById(R.id.tv_device_key);[cite: 5]
        tvStatus       = findViewById(R.id.tv_status);[cite: 5]
        tvStatusDetail = findViewById(R.id.tv_status_detail);[cite: 5]
        tvProviderLabel= findViewById(R.id.tv_provider_label);[cite: 5]
        tvLogin        = findViewById(R.id.tv_login);[cite: 5]
        tvPassword     = findViewById(R.id.tv_password);[cite: 5]
        tvExpiry       = findViewById(R.id.tv_expiry);[cite: 5]
        cardProvider   = findViewById(R.id.card_provider);[cite: 5]
        btnCheck       = findViewById(R.id.btn_check);[cite: 5]
        btnAccess      = findViewById(R.id.btn_access);[cite: 5]
        btnCopy        = findViewById(R.id.btn_copy_key);[cite: 5]
        progressBar    = findViewById(R.id.progress_bar);[cite: 5]

        // Récupération et affichage de la clé courte à 6 caractères
        deviceKey = DeviceSecurity.getOrCreateKey(this);[cite: 5]
        if (tvDeviceKey != null) {
            tvDeviceKey.setText(deviceKey);[cite: 5]
        }

        // Copie de la clé dans le presse-papier[cite: 5]
        if (btnCopy != null) {
            btnCopy.setOnClickListener(v -> {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);[cite: 5]
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText("device_key", deviceKey));[cite: 5]
                    Toast.makeText(this, "Clé copiée !", Toast.LENGTH_SHORT).show();[cite: 5]
                }
            });
        }

        // ─── OPTIMISATION 1 : VÉRIFICATION DU CACHE AU DÉMARRAGE ───
        SharedPreferences prefs = getSharedPreferences(PREFS_ACTIVATION, Context.MODE_PRIVATE);
        String savedStatus = prefs.getString("status", "INACTIVE");
        String expiresAt   = prefs.getString("expires_at", "");

        if ("ACTIVE".equals(savedStatus) && !isExpired(expiresAt)) {
            // Déjà actif et valide -> On saute directement l'écran
            goToMain();
            return;
        }

        // Lance une vérification réseau silencieuse en arrière-plan au démarrage
        autoCheckSilently();

        // Événements des boutons principaux
        if (btnCheck != null) {
            btnCheck.setOnClickListener(v -> checkActivation(false));[cite: 5]
        }
        if (btnAccess != null) {
            btnAccess.setOnClickListener(v -> checkActivation(true));[cite: 5]
        }
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
                    // ─── OPTIMISATION 2 : TÉLÉCHARGEMENT AUTOMATIQUE EN ARRIÈRE-PLAN ───
                    triggerSilentDownload(r);
                });
            }

            @Override
            public void onInactive(String status, String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    clearActivationState();
                    showInactive(status);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    SharedPreferences prefs = getSharedPreferences(PREFS_ACTIVATION, Context.MODE_PRIVATE);
                    if ("ACTIVE".equals(prefs.getString("status", ""))) {
                        showOffline(message);
                    } else {
                        showPending(message);
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
                    triggerSilentDownload(r);

                    if (launchImmediatelyOnSuccess) {
                        goToMain();
                    } else {
                        Toast.makeText(ActivationActivity.this, "Activation validée !", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onInactive(String status, String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    clearActivationState();
                    showInactive(status);
                    Toast.makeText(ActivationActivity.this, "Appareil non activé sur le panel.", Toast.LENGTH_LONG).show();
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

    private void saveActivationState(DeviceSecurity.ActivationResult r) {
        getSharedPreferences(PREFS_ACTIVATION, Context.MODE_PRIVATE).edit()
                .putString("status", "ACTIVE")
                .putString("expires_at", r.expiresAt)
                .apply();
    }

    private void clearActivationState() {
        getSharedPreferences(PREFS_ACTIVATION, Context.MODE_PRIVATE).edit().clear().apply();
    }

    private void triggerSilentDownload(DeviceSecurity.ActivationResult r) {
        AppDatabase db = AppDatabase.get(this);
        try {
            // Appel au manager mobile pour mettre à jour la playlist et lancer le téléchargement local
            ActivationManager.upsertActivationPlaylist(this, db, r);
        } catch (Exception e) {
            android.util.Log.e("ActivationActivity", "Erreur lors du téléchargement : " + e.getMessage());
        }
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
        if (tvStatus != null) {
            tvStatus.setText("✅ ACTIVÉ");[cite: 5]
            tvStatus.setTextColor(0xFF4CAF50);[cite: 5]
        }
        if (tvStatusDetail != null) tvStatusDetail.setText("Votre abonnement est actif");[cite: 5]

        if (cardProvider != null) cardProvider.setVisibility(View.VISIBLE);[cite: 5]
        if (tvLogin != null) tvLogin.setText("Login : " + r.login);[cite: 5]
        if (tvPassword != null) tvPassword.setText("Mot de passe : " + r.password);[cite: 5]
        if (tvExpiry != null) tvExpiry.setText("Expire le : " + r.expiresAt);[cite: 5]
        
        if (tvProviderLabel != null && !r.dnsServers.isEmpty()) {
            tvProviderLabel.setText("Provider : " + r.dnsServers.get(0).url);[cite: 5]
        }

        if (btnAccess != null) {
            btnAccess.setVisibility(View.VISIBLE);[cite: 5]
            btnAccess.setEnabled(true);[cite: 5]
        }
    }

    private void showInactive(String status) {
        if (cardProvider != null) cardProvider.setVisibility(View.GONE);[cite: 5]
        if (btnAccess != null) btnAccess.setVisibility(View.GONE);[cite: 5]
        
        if (tvStatus != null && tvStatusDetail != null) {
            if (status != null && status.contains("EXPIRED")) {
                tvStatus.setText("⏰ EXPIRÉ");[cite: 5]
                tvStatus.setTextColor(0xFFFF9800);[cite: 5]
                tvStatusDetail.setText("Votre abonnement a expiré.\nContactez votre revendeur pour renouveler.");[cite: 5]
            } else if (status != null && status.contains("DISABLED")) {
                tvStatus.setText("🚫 DÉSACTIVÉ");[cite: 5]
                tvStatus.setTextColor(0xFFF44336);[cite: 5]
                tvStatusDetail.setText("Votre accès a été suspendu.\nContactez votre revendeur.");[cite: 5]
            } else {
                tvStatus.setText("❌ NON ENREGISTRÉ");[cite: 5]
                tvStatus.setTextColor(0xFFF44336);[cite: 5]
                tvStatusDetail.setText("Ce device n'est pas encore activé.\nCommuniquez votre Device Key à votre revendeur.");[cite: 5]
            }
        }
    }

    private void showPending(String errMsg) {
        if (cardProvider != null) cardProvider.setVisibility(View.GONE);[cite: 5]
        if (btnAccess != null) btnAccess.setVisibility(View.GONE);[cite: 5]
        if (tvStatus != null) {
            tvStatus.setText("⏳ EN ATTENTE");[cite: 5]
            tvStatus.setTextColor(0xFFFFEB3B);[cite: 5]
        }
        if (tvStatusDetail != null) {
            tvStatusDetail.setText("Communiquez votre Device Key à votre revendeur pour activation.\n\n(Erreur : " + errMsg + ")");[cite: 5]
        }
    }

    private void showOffline(String errMsg) {
        if (tvStatus != null) {
            tvStatus.setText("📡 HORS LIGNE (cache)");[cite: 5]
            tvStatus.setTextColor(0xFF9E9E9E);[cite: 5]
        }
        if (tvStatusDetail != null) {
            tvStatusDetail.setText("Connexion impossible. Accès accordé depuis le cache.");[cite: 5]
        }
        if (btnAccess != null) {
            btnAccess.setVisibility(View.VISIBLE);[cite: 5]
            btnAccess.setEnabled(true);[cite: 5]
        }
    }

    private void setLoading(boolean loading) {
        if (progressBar != null) progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);[cite: 5]
        if (btnCheck != null) {
            btnCheck.setEnabled(!loading);[cite: 5]
            btnCheck.setText(loading ? "Vérification…" : "Vérifier l'activation");[cite: 5]
        }
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));[cite: 5]
        finish(); // Ajout du finish() pour empêcher l'utilisateur d'y retourner par accident
    }
}