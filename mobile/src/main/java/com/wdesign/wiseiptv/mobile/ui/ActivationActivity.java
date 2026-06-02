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
 *
 * CORRECTIONS :
 *  1. Le crash "accéder au contenu" était dû au fait que goToMain() ne transmettait
 *     pas l'ActivationResult à MainActivity → celle-ci relançait un check réseau à
 *     froid, causant un double téléchargement concurrent et un deadlock Room.
 *  2. On stocke maintenant l'ActivationResult dans lastResult dès onActive() et on
 *     le passe à MainActivity via des extras Intent sérialisés (dns_urls/login/password/expires).
 *  3. btnAccess.onClick ne fait plus goToMain() directement : il déclenche d'abord
 *     un dernier check si lastResult est null (cas hors-ligne / cache), puis navigue.
 *  4. WiseApp ne refait plus checkAndSync() : MainActivity est la seule à déclencher
 *     le download au démarrage (voir MainActivity corrigée).
 */
public class ActivationActivity extends AppCompatActivity {

    private TextView    tvDeviceKey, tvStatus, tvStatusDetail;
    private TextView    tvProviderLabel, tvLogin, tvPassword, tvExpiry;
    private View        cardProvider;
    private Button      btnCheck, btnAccess, btnCopy;
    private ProgressBar progressBar;
    private String      deviceKey;

    // Résultat courant gardé en mémoire pour le transmettre à MainActivity
    private DeviceSecurity.ActivationResult lastResult = null;

    private static final String PREFS_ACTIVATION = "wise_activation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

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

        deviceKey = DeviceSecurity.getOrCreateKey(this);
        if (tvDeviceKey != null) tvDeviceKey.setText(deviceKey);

        if (btnCopy != null) {
            btnCopy.setOnClickListener(v -> {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText("device_key", deviceKey));
                    Toast.makeText(this, "Clé copiée !", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Si déjà actif en cache ET pas expiré → aller directement à MainActivity
        // (MainActivity se chargera de re-vérifier + télécharger en arrière-plan)
        String savedStatus = ActivationManager.getSavedStatus(this);
        if ("ACTIVE".equals(savedStatus)) {
            goToMain(null); // null = MainActivity utilisera ses propres prefs
            return;
        }

        if (btnCheck != null)  btnCheck.setOnClickListener(v -> checkActivation());

        // CORRECTION CRASH : btnAccess transmet lastResult à MainActivity
        // au lieu de faire goToMain() sans contexte.
        if (btnAccess != null) {
            btnAccess.setOnClickListener(v -> {
                if (lastResult != null) {
                    // Cas normal : on a un résultat frais → on l'envoie à MainActivity
                    goToMain(lastResult);
                } else {
                    // Cas hors-ligne : on navigue quand même (MainActivity utilisera le cache)
                    String st = ActivationManager.getSavedStatus(this);
                    if ("ACTIVE".equals(st)) {
                        goToMain(null);
                    } else {
                        Toast.makeText(this, "Activation requise avant d'accéder au contenu.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Vérification silencieuse automatique au lancement
        checkActivation();
    }

    private void checkActivation() {
        setLoading(true);
        ActivationManager.checkAndSync(this, new ActivationManager.OnResult() {
            @Override
            public void onActivated(DeviceSecurity.ActivationResult r) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    lastResult = r; // Stocker pour le transmettre à MainActivity
                    setLoading(false);
                    showActive(r);
                });
            }

            @Override
            public void onExpired(String status) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    lastResult = null;
                    setLoading(false);
                    showInactive(status);
                });
            }

            @Override
            public void onError(String msg) {
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    setLoading(false);
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
        if (tvStatusDetail != null) tvStatusDetail.setText("Votre abonnement est actif");
        if (cardProvider != null) cardProvider.setVisibility(View.VISIBLE);
        if (tvLogin    != null) tvLogin.setText("Login : " + r.login);
        if (tvPassword != null) tvPassword.setText("Mot de passe : " + r.password);
        if (tvExpiry   != null) tvExpiry.setText("Expire le : " + r.expiresAt);
        if (tvProviderLabel != null && r.dnsServers != null && !r.dnsServers.isEmpty())
            tvProviderLabel.setText("Provider : " + r.dnsServers.get(0).url);
        if (btnAccess != null) {
            btnAccess.setVisibility(View.VISIBLE);
            btnAccess.setEnabled(true);
        }
    }

    private void showInactive(String status) {
        if (cardProvider != null) cardProvider.setVisibility(View.GONE);
        if (btnAccess   != null) btnAccess.setVisibility(View.GONE);
        if (tvStatus == null || tvStatusDetail == null) return;
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

    private void showPending(String errMsg) {
        if (cardProvider != null) cardProvider.setVisibility(View.GONE);
        if (btnAccess   != null) btnAccess.setVisibility(View.GONE);
        if (tvStatus != null) { tvStatus.setText("⏳ EN ATTENTE"); tvStatus.setTextColor(0xFFFFEB3B); }
        if (tvStatusDetail != null)
            tvStatusDetail.setText("Communiquez votre Device Key à votre revendeur.\n\n(Erreur : " + errMsg + ")");
    }

    private void showOffline(String errMsg) {
        if (tvStatus != null) { tvStatus.setText("📡 HORS LIGNE (cache)"); tvStatus.setTextColor(0xFF9E9E9E); }
        if (tvStatusDetail != null) tvStatusDetail.setText("Connexion impossible. Accès accordé depuis le cache.");
        if (btnAccess != null) { btnAccess.setVisibility(View.VISIBLE); btnAccess.setEnabled(true); }
    }

    private void setLoading(boolean loading) {
        if (progressBar != null) progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        if (btnCheck != null) {
            btnCheck.setEnabled(!loading);
            btnCheck.setText(loading ? "Vérification…" : "Vérifier l'activation");
        }
    }

    /**
     * Navigation vers MainActivity.
     * @param r  ActivationResult frais (peut être null → MainActivity utilisera le cache prefs).
     *           Les DNS/login/password sont passés en extras pour éviter un nouveau check réseau.
     */
    private void goToMain(DeviceSecurity.ActivationResult r) {
        Intent intent = new Intent(this, MainActivity.class);
        if (r != null) {
            // Sérialisation légère : on passe login/password + la liste des DNS en String[]
            intent.putExtra(MainActivity.EXTRA_ACT_LOGIN,    r.login);
            intent.putExtra(MainActivity.EXTRA_ACT_PASSWORD, r.password);
            intent.putExtra(MainActivity.EXTRA_ACT_EXPIRES,  r.expiresAt);
            if (r.dnsServers != null && !r.dnsServers.isEmpty()) {
                String[] urls    = new String[r.dnsServers.size()];
                String[] epgUrls = new String[r.dnsServers.size()];
                for (int i = 0; i < r.dnsServers.size(); i++) {
                    urls[i]    = r.dnsServers.get(i).url;
                    epgUrls[i] = r.dnsServers.get(i).epgUrl != null ? r.dnsServers.get(i).epgUrl : "";
                }
                intent.putExtra(MainActivity.EXTRA_ACT_DNS_URLS,    urls);
                intent.putExtra(MainActivity.EXTRA_ACT_DNS_EPG_URLS, epgUrls);
            }
        }
        startActivity(intent);
        finish();
    }
}
