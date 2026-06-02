package com.wdesign.wiseiptv.tv.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.tv.R;
import com.wdesign.wiseiptv.tv.util.ActivationManager;

/**
 * TvActivationActivity — PREMIER ÉCRAN APK TV.
 * Affiche device_key, statut activation, infos provider.
 * Navigation D-Pad : OK=Vérifier, ►=Accéder au contenu (si actif)
 */
public class TvActivationActivity extends FragmentActivity {

    private TextView  tvDeviceKey, tvStatus, tvStatusDetail;
    private TextView  tvLogin, tvPassword, tvExpiry, tvProvider;
    private View      cardProvider;
    private Button    btnCheck, btnAccess;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_activation);

        tvDeviceKey    = findViewById(R.id.tv_device_key);
        tvStatus       = findViewById(R.id.tv_status);
        tvStatusDetail = findViewById(R.id.tv_status_detail);
        tvLogin        = findViewById(R.id.tv_login);
        tvPassword     = findViewById(R.id.tv_password);
        tvExpiry       = findViewById(R.id.tv_expiry);
        tvProvider     = findViewById(R.id.tv_provider_label);
        cardProvider   = findViewById(R.id.card_provider);
        btnCheck       = findViewById(R.id.btn_check);
        btnAccess      = findViewById(R.id.btn_access);
        progressBar    = findViewById(R.id.progress_bar);

        tvDeviceKey.setText(DeviceSecurity.getOrCreateKey(this));

        btnCheck.setOnClickListener(v -> checkActivation());
        btnAccess.setOnClickListener(v -> goToMain());
        btnCheck.requestFocus();

        checkActivation();
    }

    private void checkActivation() {
        setLoading(true);
        ActivationManager.checkAndSync(this, new ActivationManager.OnResult() {
            @Override public void onActivated(DeviceSecurity.ActivationResult r) {
                runOnUiThread(() -> { setLoading(false); showActive(r); });
            }
            @Override public void onExpired(String status) {
                runOnUiThread(() -> { setLoading(false); showInactive(status); });
            }
            @Override public void onError(String msg) {
                runOnUiThread(() -> {
                    setLoading(false);
                    String saved = ActivationManager.getSavedStatus(TvActivationActivity.this);
                    if ("ACTIVE".equals(saved)) { tvStatus.setText("📡 HORS LIGNE (cache)"); tvStatus.setTextColor(0xFF9E9E9E);
                        tvStatusDetail.setText("Mode hors ligne."); btnAccess.setVisibility(View.VISIBLE); }
                    else { tvStatus.setText("⏳ EN ATTENTE"); tvStatus.setTextColor(0xFFFFEB3B);
                        tvStatusDetail.setText("Communiquez votre Device Key à votre revendeur.\n(" + msg + ")"); }
                });
            }
        });
    }

    private void showActive(DeviceSecurity.ActivationResult r) {
        tvStatus.setText("✅ ACTIVÉ"); tvStatus.setTextColor(0xFF4CAF50);
        tvStatusDetail.setText("Abonnement actif");
        cardProvider.setVisibility(View.VISIBLE);
        if (!r.dnsServers.isEmpty()) tvProvider.setText("Provider : " + r.dnsServers.get(0).url);
        tvLogin.setText("Login : " + r.login);
        tvPassword.setText("Mot de passe : " + r.password);
        tvExpiry.setText("Expire : " + r.expiresAt);
        btnAccess.setVisibility(View.VISIBLE);
        btnAccess.requestFocus();
    }

    private void showInactive(String status) {
        cardProvider.setVisibility(View.GONE); btnAccess.setVisibility(View.GONE);
        if (status.contains("EXPIRED")) { tvStatus.setText("⏰ EXPIRÉ"); tvStatus.setTextColor(0xFFFF9800); }
        else if (status.contains("DISABLED")) { tvStatus.setText("🚫 DÉSACTIVÉ"); tvStatus.setTextColor(0xFFF44336); }
        else { tvStatus.setText("❌ NON ENREGISTRÉ"); tvStatus.setTextColor(0xFFF44336); }
        tvStatusDetail.setText("Contactez votre revendeur.");
    }

    private void setLoading(boolean l) {
        progressBar.setVisibility(l ? View.VISIBLE : View.GONE);
        btnCheck.setEnabled(!l);
    }

    private void goToMain() {
        startActivity(new Intent(this, TvMainActivity.class));
    }

    @Override public boolean onKeyDown(int kc, KeyEvent e) {
        if (kc == KeyEvent.KEYCODE_DPAD_CENTER || kc == KeyEvent.KEYCODE_ENTER) {
            View focused = getCurrentFocus();
            if (focused != null) focused.performClick();
            return true;
        }
        return super.onKeyDown(kc, e);
    }
}