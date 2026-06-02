package com.wdesign.wiseiptv.mobile.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.util.ActivationManager;

/**
 * ActivationActivity — PREMIER ÉCRAN de l'APK Mobile.
 *
 * Affiche :
 *  • Device Key (à communiquer à l'admin/revendeur pour activation)
 *  • Statut : EN ATTENTE / ACTIF / EXPIRÉ / DÉSACTIVÉ
 *  • Provider, Login, Password (renseignés automatiquement après activation serveur)
 *  • Bouton [Vérifier l'activation] → interroge le panel
 *  • Bouton [Accéder au contenu] → visible seulement si ACTIF
 *
 * Flux :
 *  1. User installe l'APK → voit sa device_key → la communique à admin/revendeur
 *  2. Admin/Revendeur active le device dans le panel web
 *  3. User appuie sur "Vérifier" → le serveur renvoie login/password/DNS
 *  4. Les playlists sont téléchargées automatiquement
 *  5. Bouton "Accéder au contenu" s'active → MainActivity
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
        tvDeviceKey.setText(key);

        // Copier la clé dans le presse-papier
        btnCopy.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("device_key", key));
            Toast.makeText(this, "Clé copiée !", Toast.LENGTH_SHORT).show();
        });

        // Vérifier l'activation
        btnCheck.setOnClickListener(v -> checkActivation());

        // Accéder au contenu (si déjà activé)
        btnAccess.setOnClickListener(v -> goToMain());

        // Vérifier automatiquement au démarrage
        checkActivation();
    }

    private void checkActivation() {
        setLoading(true);
        ActivationManager.checkAndSync(this, new ActivationManager.OnResult() {
            @Override
            public void onActivated(DeviceSecurity.ActivationResult r) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showActive(r);
                });
            }
            @Override
            public void onExpired(String status) {
                runOnUiThread(() -> {
                    setLoading(false);
                    showInactive(status);
                });
            }
            @Override
            public void onError(String msg) {
                runOnUiThread(() -> {
                    setLoading(false);
                    // Vérifier le statut sauvegardé localement
                    String saved = ActivationManager.getSavedStatus(ActivationActivity.this);
                    if ("ACTIVE".equals(saved)) {
                        // Mode offline : on autorise l'accès avec les données en cache
                        showOffline(msg);
                    } else {
                        showPending(msg);
                    }
                });
            }
        });
    }

    private void showActive(DeviceSecurity.ActivationResult r) {
        tvStatus.setText("✅ ACTIVÉ");
        tvStatus.setTextColor(0xFF4CAF50);
        tvStatusDetail.setText("Votre abonnement est actif");

        // Afficher les infos provider
        cardProvider.setVisibility(View.VISIBLE);
        tvLogin.setText("Login : " + r.login);
        tvPassword.setText("Mot de passe : " + r.password);
        tvExpiry.setText("Expire le : " + r.expiresAt);
        if (!r.dnsServers.isEmpty()) {
            tvProviderLabel.setText("Provider : " + r.dnsServers.get(0).url);
        }

        btnAccess.setVisibility(View.VISIBLE);
        btnAccess.setEnabled(true);
    }

    private void showInactive(String status) {
        cardProvider.setVisibility(View.GONE);
        btnAccess.setVisibility(View.GONE);
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

    private void showPending(String errMsg) {
        cardProvider.setVisibility(View.GONE);
        btnAccess.setVisibility(View.GONE);
        tvStatus.setText("⏳ EN ATTENTE");
        tvStatus.setTextColor(0xFFFFEB3B);
        tvStatusDetail.setText("Communiquez votre Device Key à votre revendeur pour activation.\n\n(Erreur : " + errMsg + ")");
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
        // Ne pas finish() → retour possible depuis MainActivity
    }
}