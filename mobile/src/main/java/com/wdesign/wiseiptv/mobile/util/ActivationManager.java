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
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.util.ActivationManager;

/**
 * ActivationActivity — PREMIER ÉCRAN de l'APK Mobile.
 */
public class ActivationActivity extends AppCompatActivity {

    private TextView  tvDeviceKey, tvStatus, tvStatusDetail;
    private TextView  tvProviderLabel, tvLogin, tvPassword, tvExpiry;
    private View      cardProvider;
    private Button    btnCheck, btnAccess, btnCopy;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        tvDeviceKey    = findViewById(R.id.tv_device_key);
        tvStatus       = findViewById(R.id.tv_status);
        tvStatusDetail = findViewById(R.id.tv_status_detail);
        tvProviderLabel= findViewById(R.id.tv_provider_label);
        tvLogin        = findViewById(R.id.tv_login);
        tvPassword     = findViewById(R.id.tv_password);
        tvExpiry       = findViewById(R.id.tv_expiry);
        cardProvider   = findViewById(R.id.card_provider);
        btnCheck       = findViewById(R.id.btn_check);
        btnAccess      = findViewById(R.id.btn_access);
        btnCopy        = findViewById(R.id.btn_copy_key);
        progressBar    = findViewById(R.id.progress_bar);

        // Afficher la device_key
        String key = DeviceSecurity.getOrCreateKey(this);
        if (tvDeviceKey != null) {
            tvDeviceKey.setText(key);
        }

        // Copier la clé dans le presse-papier
        if (btnCopy != null) {
            btnCopy.setOnClickListener(v -> {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText("device_key", key));
                    Toast.makeText(this, "Clé copiée !", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Si l'application a déjà été activée et validée, on passe directement à l'application
        String saved = ActivationManager.getSavedStatus(this);
        if ("ACTIVE".equals(saved)) {
            goToMain();
            return;
        }

        // Bouton Vérifier l'activation
        if (btnCheck != null) {
            btnCheck.setOnClickListener(v -> checkActivation());
        }

        // Accéder au contenu : redirige immédiatement vers la MainActivity
        if (btnAccess != null) {
            btnAccess.setOnClickListener(v -> goToMain());
        }

        // Vérifier automatiquement l'état de l'appareil de manière transparente au lancement
        checkActivation();
    }

    private void checkActivation() {
        setLoading(true);
        ActivationManager.checkAndSync(this, new ActivationManager.OnResult() {
            @Override
            public void onActivated(DeviceSecurity.ActivationResult r) {
                runOnUiThread(() -> {
                    // Sécurité anti-crash : Vérifie si l'activité n'est pas fermée
                    if (isFinishing() || isDestroyed()) return;
                    setLoading(false);
                    showActive(r);
                });
            }

            @Override
            public void onExpired(String status) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    setLoading(false);
                    showInactive(status);
                });
            }

            @Override
            public void onError(String msg) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    setLoading(false);
                    
                    // Vérifier le statut sauvegardé localement en cache
                    String saved = ActivationManager.getSavedStatus(ActivationActivity.this);
                    if ("ACTIVE".equals(saved)) {
                        showOffline(msg);
                    } else {
                        showPending(msg);
                    }
                });
            }
        });
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
        
        if (tvStatus != null && tvStatusDetail != null && status != null) {
            if (status.contains("EXPIRED")) {
                tvStatus.setText("⏰ EXPIRÉ");
                tvStatus.setTextColor(0xFFFF9800);
                tvStatusDetail.setText("Votre abonnement a expiré.\nContactez votre revendeur pour renouveler.");
            } else if (status.contains("DISABLED")) {
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

    private void setLoading(boolean loading) {
        if (progressBar != null) progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        if (btnCheck != null) {
            btnCheck.setEnabled(!loading);
            btnCheck.setText(loading ? "Vérification…" : "Vérifier l'activation");
        }
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish(); // Termine proprement l'activité d'activation pour libérer la mémoire
    }
}