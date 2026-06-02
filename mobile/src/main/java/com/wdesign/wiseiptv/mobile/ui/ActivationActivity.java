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
 * Corrigé : Attend la fin du téléchargement avant de rediriger vers l'accueil.
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
        setContentView(R.layout.activity_activation);

        // Liaison des composants
        tvDeviceKey     = findViewById(R.id.tv_device_key);
        tvStatus        = findViewById(R.id.tv_status);
        tvStatusDetail  = findViewById(R.id.tv_status_detail);
        tvProviderLabel = findViewById(R.id.tv_provider_label);
        tvLogin         = findViewById(R.id.tv_login);
        tvPassword      = findViewById(R.id.tv_password);
        tvExpiry        = findViewById(R.id.tv_expiry);
        cardProvider    = findViewById(R.id.card_provider);
        btnCheck        = findViewById(R.id.btn_check);
        btnAccess       = findViewById(R.id.btn_access);
        btnCopy         = findViewById(R.id.btn_copy_key);
        progressBar     = findViewById(R.id.progress_bar);

        // Clé unique de l'appareil
        deviceKey = DeviceSecurity.getOrCreateKey(this);
        if (tvDeviceKey != null) {
            tvDeviceKey.setText(deviceKey);
        }

        if (btnCopy != null) {
            btnCopy.setOnClickListener(v -> {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText("device_key", deviceKey));
                    Toast.makeText(this, "Clé copiée !", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Éviter le réaffichage si déjà actif
        SharedPreferences prefs = getSharedPreferences(PREFS_ACTIVATION, Context.MODE_PRIVATE);
        String savedStatus = prefs.getString("status", "INACTIVE");
        String expiresAt   = prefs.getString("expires_at", "");

        if ("ACTIVE".equals(savedStatus) && !isExpired(expiresAt)) {
            goToMain();
            return;
        }

        // Vérification silencieuse automatique au démarrage
        autoCheckSilently();

        if (btnCheck != null) {
            btnCheck.setOnClickListener(v -> checkActivation(false));
        }

        if (btnAccess != null) {
            btnAccess.setOnClickListener(v -> checkActivation(true));
        }
    }

    private void autoCheckSilently() {
        setLoading(true, "Vérification…");
        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override
            public void onActive(DeviceSecurity.ActivationResult r) {
                runOnUiThread(() -> {
                    saveActivationState(r);
                    showActive(r);
                    // Au démarrage, on télécharge tranquillement en tâche de fond sans forcer la redirection
                    triggerDownloadAndRedirect(r, false);
                });
            }

            @Override
            public void onInactive(String status, String message) {
                runOnUiThread(() -> {
                    setLoading(false, "");
                    clearActivationState();
                    showInactive(status);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false, "");
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
        setLoading(true, launchImmediatelyOnSuccess ? "Téléchargement de la playlist…" : "Vérification…");
        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override
            public void onActive(DeviceSecurity.ActivationResult r) {
                runOnUiThread(() -> {
                    saveActivationState(r);
                    showActive(r);
                    
                    // On passe le flag pour savoir s'il faut rediriger à la fin du téléchargement
                    triggerDownloadAndRedirect(r, launchImmediatelyOnSuccess);
                });
            }

            @Override
            public void onInactive(String status, String message) {
                runOnUiThread(() -> {
                    setLoading(false, "");
                    clearActivationState();
                    showInactive(status);
                    Toast.makeText(ActivationActivity.this, "Appareil non activé sur le panel.", Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false, "");
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

    /**
     * Gère le téléchargement de manière sécurisée et n'ouvre l'application que si tout est prêt localement.
     */
private void triggerDownloadAndRedirect(DeviceSecurity.ActivationResult r, boolean redirectOnSuccess) {
        AppDatabase db = AppDatabase.get(this);
        
        // Exécute proprement le téléchargement de TOUS les DNS sans bloquer le fil principal de l'APK
        ActivationManager.downloadAllPlaylistsAsync(this, db, r, new ActivationManager.OnDownloadCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    setLoading(false, "");
                    if (redirectOnSuccess) {
                        goToMain();
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                runOnUiThread(() -> {
                    setLoading(false, "");
                    Toast.makeText(ActivationActivity.this, "Note: Erreur sur certains flux, accès accordé.", Toast.LENGTH_SHORT).show();
                    if (redirectOnSuccess) {
                        goToMain();
                    }
                });
            }
        });
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
            tvStatus.setText("✅ ACTIVÉ");
            tvStatus.setTextColor(0xFF4CAF50);
        }
        if (tvStatusDetail != null) {
            tvStatusDetail.setText("Votre abonnement est actif");
        }

        if (cardProvider != null) cardProvider.setVisibility(View.VISIBLE);
        if (tvLogin != null) tvLogin.setText("Login : " + r.login);
        if (tvPassword != null) tvPassword.setText("Mot de passe : " + r.password);
        if (tvExpiry != null) tvExpiry.setText("Expire le : " + r.expiresAt);

        if (tvProviderLabel != null && r.dnsServers != null && !r.dnsServers.isEmpty()) {
            tvProviderLabel.setText("Provider : " + r.dnsServers.get(0).url);
        }

        if (btnAccess != null) {
            btnAccess.setVisibility(View.VISIBLE);
            btnAccess.setEnabled(true);
        }
    }

    private void showInactive(String status) {
        if (cardProvider != null) cardProvider.setVisibility(View.GONE);
        if (btnAccess != null) btnAccess.setVisibility(View.GONE);

        if (tvStatus != null && tvStatusDetail != null) {
            if (status != null && status.contains("EXPIRED")) {
                tvStatus.setText("⏰ EXPIRÉ");
                tvStatus.setTextColor(0xFFFF9800);
                tvStatusDetail.setText("Votre abonnement a expiré.\nContactez votre revendeur pour renouveler.");
            } else if (status != null && status.contains("DISABLED")) {
                tvStatus.setText("🚫 DÉSACTIVÉ");
                tvStatus.setTextColor(0xFFF44336);
                tvStatusDetail.setText("Votre accès a été suspendu.\nContactez votre revendeur.");
            } else {
                tvStatus.setText("❌ NON ENREGISTRÉ");
                tvStatus.setTextColor(0xFFF44336);
                tvStatusDetail.setText("Ce device n'est pas encore activé.\nCommuniquez votre Device Key à votre revendeur.");
            }
        }
    }

    private void showPending(String errMsg) {
        if (cardProvider != null) cardProvider.setVisibility(View.GONE);
        if (btnAccess != null) btnAccess.setVisibility(View.GONE);
        if (tvStatus != null) {
            tvStatus.setText("⏳ EN ATTENTE");
            tvStatus.setTextColor(0xFFFFEB3B);
        }
        if (tvStatusDetail != null) {
            tvStatusDetail.setText("Communiquez votre Device Key à votre revendeur pour activation.\n\n(Erreur : " + errMsg + ")");
        }
    }

    private void showOffline(String errMsg) {
        if (tvStatus != null) {
            tvStatus.setText("📡 HORS LIGNE (cache)");
            tvStatus.setTextColor(0xFF9E9E9E);
        }
        if (tvStatusDetail != null) {
            tvStatusDetail.setText("Connexion impossible. Accès accordé depuis le cache.");
        }
        if (btnAccess != null) {
            btnAccess.setVisibility(View.VISIBLE);
            btnAccess.setEnabled(true);
        }
    }

    private void setLoading(boolean loading, String message) {
        if (progressBar != null) progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        if (btnCheck != null) {
            btnCheck.setEnabled(!loading);
            btnCheck.setText(loading ? message : "Vérifier l'activation");
        }
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}