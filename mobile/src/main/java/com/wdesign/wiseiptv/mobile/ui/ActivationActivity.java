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
 * ActivationActivity — PREMIER ÉCRAN de l'APK Mobile (Corrigé avec les ID exacts du layout).
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
        // Utilisation du layout mobile d'origine
        setContentView(R.layout.activity_activation_mobile); 

        // 🛠️ CORRECTION DES ID : Alignement strict sur les composants réels de votre layout mobile
        tvDeviceKey    = findViewById(R.id.txt_key);          // Réaligné
        tvStatus       = findViewById(R.id.txt_status);       // Réaligné
        tvStatusDetail = findViewById(R.id.txt_status_desc);  // Réaligné
        tvProvider     = findViewById(R.id.txt_provider);     // Réaligné
        tvLogin        = findViewById(R.id.txt_login);        // Réaligné
        tvPassword     = findViewById(R.id.txt_password);     // Réaligné
        tvExpiry       = findViewById(R.id.txt_expiry);       // Réaligné
        cardProvider   = findViewById(R.id.layout_details);   // Réaligné
        btnCheck       = findViewById(R.id.btn_check);        // Réaligné
        btnAccess      = findViewById(R.id.btn_access);       // Réaligné
        progressBar    = findViewById(R.id.progress_loading); // Réaligné

        // Récupération ou génération de la clé courte à 6 caractères
        deviceKey = DeviceSecurity.getOrCreateKey(this);
        if (tvDeviceKey != null) {
            tvDeviceKey.setText(deviceKey);
        }

        // Clic pour copier le code (Réaligné sur btn_copy)
        View btnCopy = findViewById(R.id.btn_copy);
        if (btnCopy != null) {
            btnCopy.setOnClickListener(v -> {
                ClipboardManager cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cb != null) {
                    cb.setPrimaryClip(ClipData.newPlainText("Device Key", deviceKey));
                    Toast.makeText(this, "Code copié !", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // ─── OPTIMISATION 1 : VÉRIFICATION LOCALE DIRECTE AU DÉMARRAGE ───
        SharedPreferences prefs = getSharedPreferences(PREFS_ACTIVATION, Context.MODE_PRIVATE);
        String savedStatus = prefs.getString("status", "INACTIVE");
        String expiresAt   = prefs.getString("expires_at", "");

        if ("ACTIVE".equals(savedStatus) && !isExpired(expiresAt)) {
            goToMain();
            return;
        }

        // Lancer une vérification réseau silencieuse au démarrage
        autoCheckSilently();

        // Événements des boutons
        if (btnCheck != null) {
            btnCheck.setOnClickListener(v -> checkActivation(false));
        }
        if (btnAccess != null) {
            btnAccess.setOnClickListener(v -> checkActivation(true));
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
        // Si upsertActivationPlaylist refuse de compiler, utilisez directement l'appel unifié du package core ou passez la méthode en public dans ActivationManager
        try {
            com.wdesign.wiseiptv.mobile.util.ActivationManager.upsertActivationPlaylist(this, db, r);
        } catch (Exception e) {
            android.util.Log.e("ActivationActivity", "Erreur d'appel au manager : " + e.getMessage());
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
        if (cardProvider != null) cardProvider.setVisibility(View.VISIBLE);
        if (btnAccess != null) {
            btnAccess.setVisibility(View.VISIBLE);
            btnAccess.setEnabled(true);
        }
        if (tvStatus != null) {
            tvStatus.setText("✅ ACTIF");
            tvStatus.setTextColor(0xFF4CAF50);
        }
        if (tvStatusDetail != null) tvStatusDetail.setText("Votre appareil est activé.");
        if (tvProvider != null) tvProvider.setText("WiseIPTV Premium");
        if (tvLogin != null) tvLogin.setText(r.login);
        if (tvPassword != null) tvPassword.setText(r.password);
        if (tvExpiry != null) tvExpiry.setText(r.expiresAt);
    }

    private void showExpired() {
        if (cardProvider != null) cardProvider.setVisibility(View.GONE);
        if (btnAccess != null) btnAccess.setVisibility(View.GONE);
        if (tvStatus != null) {
            tvStatus.setText("⌛ EXPIRÉ");
            tvStatus.setTextColor(0xFFFF9800);
        }
        if (tvStatusDetail != null) tvStatusDetail.setText("Votre abonnement a expiré.");
    }

    private void showDisabled() {
        if (cardProvider != null) cardProvider.setVisibility(View.GONE);
        if (btnAccess != null) btnAccess.setVisibility(View.GONE);
        if (tvStatus != null) {
            tvStatus.setText("🚫 DÉSACTIVÉ");
            tvStatus.setTextColor(0xFFF44336);
        }
        if (tvStatusDetail != null) tvStatusDetail.setText("Votre accès a été suspendu.");
    }

    private void showPending(String errMsg) {
        if (cardProvider != null) cardProvider.setVisibility(View.GONE);
        if (btnAccess != null) btnAccess.setVisibility(View.GONE);
        if (tvStatus != null) {
            tvStatus.setText("⏳ EN ATTENTE");
            tvStatus.setTextColor(0xFFFFEB3B);
        }
        if (tvStatusDetail != null) tvStatusDetail.setText("Communiquez votre Code à votre revendeur.");
    }

    private void showOffline(String errMsg) {
        if (tvStatus != null) {
            tvStatus.setText("📡 HORS LIGNE (cache)");
            tvStatus.setTextColor(0xFF9E9E9E);
        }
        if (tvStatusDetail != null) tvStatusDetail.setText("Accès accordé depuis le cache hors-ligne.");
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
        finish();
    }
}