package com.wdesign.wiseiptv.tv.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.tv.R;
import com.wdesign.wiseiptv.tv.util.ActivationManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * TvActivationActivity — PREMIER ÉCRAN TV, affiché seulement si nécessaire.
 *
 * Skip automatique si activation valide en cache (même logique que mobile).
 * En cas d'expiration → affiche l'écran explicitement.
 * Bouton "Accéder" → télécharge toutes les playlists DNS avant d'ouvrir TvMainActivity.
 */
public class TvActivationActivity extends FragmentActivity {

    private static final String PREFS = "wise_activation_tv";

    private TextView    tvDeviceKey, tvStatus, tvStatusDetail;
    private TextView    tvLogin, tvPassword, tvExpiry, tvProvider;
    private View        cardProvider;
    private Button      btnCheck, btnAccess;
    private ProgressBar progressBar;
    private ImageView   imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ── Skip si déjà activé et non expiré ──────────────────────
        if (isActiveAndNotExpired()) {
            goToMain(false);
            return;
        }

        setContentView(R.layout.activity_tv_activation);

        imgLogo        = findViewById(R.id.img_logo);
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

        // Logo
        if (imgLogo != null) imgLogo.setImageResource(R.mipmap.ic_launcher);

        tvDeviceKey.setText(DeviceSecurity.getOrCreateKey(this));

        btnCheck.setOnClickListener(v -> checkActivation(false));
        btnAccess.setOnClickListener(v -> checkActivation(true));
        btnCheck.requestFocus();

        // Si expiré, montrer immédiatement
        String saved = getPrefs().getString("status", "");
        if ("EXPIRED".equals(saved) || "DISABLED".equals(saved)) showInactive(saved);

        // Vérification réseau silencieuse
        checkActivation(false);
    }

    private void checkActivation(boolean goOnSuccess) {
        setLoading(true);
        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override
            public void onActive(DeviceSecurity.ActivationResult r) {
                saveCache(r);
                runOnUiThread(() -> {
                    if (goOnSuccess) {
                        downloadAndGo(r);
                    } else {
                        setLoading(false);
                        showActive(r);
                    }
                });
            }

            @Override
            public void onInactive(String status, String message) {
                clearCache(status);
                runOnUiThread(() -> { setLoading(false); showInactive(status); });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    if (isActiveAndNotExpired()) showOffline();
                    else showPending(message);
                });
            }
        });
    }

    private void downloadAndGo(DeviceSecurity.ActivationResult r) {
        btnAccess.setEnabled(false);
        btnAccess.setText("Téléchargement…");
        progressBar.setVisibility(View.VISIBLE);

        AppDatabase db = AppDatabase.get(this);
        ActivationManager.upsertAndDownloadAll(this, db, r, new ActivationManager.DownloadCallback() {
            @Override public void onProgress(String name) {
                runOnUiThread(() -> btnAccess.setText("📥 " + name + "…"));
            }
            @Override public void onDone(int total) {
                runOnUiThread(() -> { progressBar.setVisibility(View.GONE); goToMain(true); });
            }
            @Override public void onError(String msg) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(TvActivationActivity.this,
                        "Téléchargement partiel : " + msg, Toast.LENGTH_LONG).show();
                    goToMain(true);
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
        btnAccess.setEnabled(true);
        btnAccess.setText("▶ Accéder au contenu");
        btnAccess.requestFocus();
    }

    private void showInactive(String status) {
        cardProvider.setVisibility(View.GONE);
        btnAccess.setVisibility(View.GONE);
        if (status.contains("EXPIRED")) {
            tvStatus.setText("⏰ EXPIRÉ"); tvStatus.setTextColor(0xFFFF9800);
            tvStatusDetail.setText("Abonnement expiré.\nContactez votre revendeur.");
        } else if (status.contains("DISABLED")) {
            tvStatus.setText("🚫 DÉSACTIVÉ"); tvStatus.setTextColor(0xFFF44336);
            tvStatusDetail.setText("Accès suspendu.\nContactez votre revendeur.");
        } else {
            tvStatus.setText("❌ NON ACTIVÉ"); tvStatus.setTextColor(0xFFF44336);
            tvStatusDetail.setText("Communiquez votre Device Key à votre revendeur.");
        }
    }

    private void showPending(String err) {
        cardProvider.setVisibility(View.GONE);
        btnAccess.setVisibility(View.GONE);
        tvStatus.setText("⏳ EN ATTENTE"); tvStatus.setTextColor(0xFFFFEB3B);
        tvStatusDetail.setText("Communiquez votre Device Key à votre revendeur.\n(" + err + ")");
    }

    private void showOffline() {
        tvStatus.setText("📡 HORS LIGNE"); tvStatus.setTextColor(0xFF9E9E9E);
        tvStatusDetail.setText("Pas de connexion. Accès via le cache.");
        btnAccess.setVisibility(View.VISIBLE);
        btnAccess.setEnabled(true);
        btnAccess.setText("▶ Continuer hors ligne");
        btnAccess.setOnClickListener(v -> goToMain(true));
        btnAccess.requestFocus();
    }

    private boolean isActiveAndNotExpired() {
        SharedPreferences p = getPrefs();
        if (!"ACTIVE".equals(p.getString("status", ""))) return false;
        String exp = p.getString("expires_at", "");
        if (exp.isEmpty()) return false;
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(exp);
            return d != null && new Date().before(d);
        } catch (Exception e) { return false; }
    }

    private void saveCache(DeviceSecurity.ActivationResult r) {
        getPrefs().edit()
            .putString("status", "ACTIVE")
            .putString("expires_at", r.expiresAt)
            .putString("login", r.login).apply();
    }

    private void clearCache(String status) {
        getPrefs().edit().putString("status", status)
            .remove("expires_at").remove("login").apply();
    }

    private SharedPreferences getPrefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private void goToMain(boolean animate) {
        startActivity(new Intent(this, TvMainActivity.class));
        if (!animate) overridePendingTransition(0, 0);
        finish();
    }

    private void setLoading(boolean on) {
        if (progressBar != null) progressBar.setVisibility(on ? View.VISIBLE : View.GONE);
        if (btnCheck != null) {
            btnCheck.setEnabled(!on);
            btnCheck.setText(on ? "Vérification…" : "🔄 Vérifier");
        }
    }

    @Override
    public boolean onKeyDown(int kc, KeyEvent e) {
        if (kc == KeyEvent.KEYCODE_DPAD_CENTER || kc == KeyEvent.KEYCODE_ENTER) {
            View f = getCurrentFocus();
            if (f != null) f.performClick();
            return true;
        }
        return super.onKeyDown(kc, e);
    }
}
