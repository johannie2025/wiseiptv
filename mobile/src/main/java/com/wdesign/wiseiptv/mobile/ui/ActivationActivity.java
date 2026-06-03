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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivationActivity extends AppCompatActivity {

    private static final String PREFS = "wise_activation";

    private TextView    tvDeviceKey, tvStatus, tvStatusDetail;
    private TextView    tvProviderLabel, tvLogin, tvPassword, tvExpiry;
    private View        cardProvider;
    private Button      btnCheck, btnAccess, btnCopy;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isActiveAndNotExpired()) {
            goToMain(false, null);
            return;
        }

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

        String key = DeviceSecurity.getOrCreateKey(this);
        tvDeviceKey.setText(key);

        btnCopy.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) cm.setPrimaryClip(ClipData.newPlainText("device_key", key));
            Toast.makeText(this, "Clé copiée !", Toast.LENGTH_SHORT).show();
        });

        btnCheck.setOnClickListener(v -> checkActivation(false));
        btnAccess.setOnClickListener(v -> checkActivation(true));

        String saved = getPrefs().getString("status", "");
        if ("EXPIRED".equals(saved) || "DISABLED".equals(saved)) showInactive(saved);

        checkActivation(false);
    }

    private void checkActivation(boolean goOnSuccess) {
        setLoading(true);
        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override
            public void onActive(DeviceSecurity.ActivationResult r) {
                saveCache(r);
                runOnUiThread(() -> {
                    setLoading(false);
                    if (goOnSuccess) {
                        // Aller directement à MainActivity avec les extras — pas de téléchargement ici
                        goToMain(true, r);
                    } else {
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

    private void showActive(DeviceSecurity.ActivationResult r) {
        tvStatus.setText("✅ ACTIVÉ");
        tvStatus.setTextColor(0xFF4CAF50);
        tvStatusDetail.setText("Votre abonnement est actif");
        cardProvider.setVisibility(View.VISIBLE);
        tvLogin.setText("Login : " + r.login);
        tvPassword.setText("Mot de passe : " + r.password);
        tvExpiry.setText("Expire le : " + r.expiresAt);
        if (!r.dnsServers.isEmpty())
            tvProviderLabel.setText("Provider : " + r.dnsServers.get(0).url);
        btnAccess.setVisibility(View.VISIBLE);
        btnAccess.setEnabled(true);
        btnAccess.setText("▶ Accéder au contenu");
    }

    private void showInactive(String status) {
        cardProvider.setVisibility(View.GONE);
        btnAccess.setVisibility(View.GONE);
        if (status.contains("EXPIRED")) {
            tvStatus.setText("⏰ EXPIRÉ");
            tvStatus.setTextColor(0xFFFF9800);
            tvStatusDetail.setText("Abonnement expiré.\nContactez votre revendeur pour renouveler.");
        } else if (status.contains("DISABLED")) {
            tvStatus.setText("🚫 DÉSACTIVÉ");
            tvStatus.setTextColor(0xFFF44336);
            tvStatusDetail.setText("Accès suspendu.\nContactez votre revendeur.");
        } else {
            tvStatus.setText("❌ NON ACTIVÉ");
            tvStatus.setTextColor(0xFFF44336);
            tvStatusDetail.setText("Communiquez votre Device Key à votre revendeur pour activation.");
        }
    }

    private void showPending(String err) {
        cardProvider.setVisibility(View.GONE);
        btnAccess.setVisibility(View.GONE);
        tvStatus.setText("⏳ EN ATTENTE");
        tvStatus.setTextColor(0xFFFFEB3B);
        tvStatusDetail.setText("Communiquez votre Device Key à votre revendeur.\n(" + err + ")");
    }

    private void showOffline() {
        tvStatus.setText("📡 HORS LIGNE");
        tvStatus.setTextColor(0xFF9E9E9E);
        tvStatusDetail.setText("Pas de connexion. Accès via le cache.");
        btnAccess.setVisibility(View.VISIBLE);
        btnAccess.setEnabled(true);
        btnAccess.setText("▶ Continuer hors ligne");
        btnAccess.setOnClickListener(v -> goToMain(true, null));
    }

    private boolean isActiveAndNotExpired() {
        SharedPreferences p = getPrefs();
        if (!"ACTIVE".equals(p.getString("status", ""))) return false;
        String exp = p.getString("expires_at", "");
        if (exp.isEmpty()) return false;
        try {
            Date expDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(exp);
            return expDate != null && new Date().before(expDate);
        } catch (Exception e) { return false; }
    }

    private void saveCache(DeviceSecurity.ActivationResult r) {
        SharedPreferences.Editor ed = getPrefs().edit()
            .putString("status", "ACTIVE")
            .putString("expires_at", r.expiresAt)
            .putString("login", r.login)
            .putString("password", r.password);
        // Sauvegarder les DNS urls pour MainActivity
        StringBuilder urls = new StringBuilder();
        StringBuilder epgs = new StringBuilder();
        for (int i = 0; i < r.dnsServers.size(); i++) {
            if (i > 0) { urls.append(","); epgs.append(","); }
            urls.append(r.dnsServers.get(i).url);
            epgs.append(r.dnsServers.get(i).epgUrl != null ? r.dnsServers.get(i).epgUrl : "");
        }
        ed.putString("dns_urls", urls.toString())
          .putString("dns_epg_urls", epgs.toString())
          .apply();
    }

    private void clearCache(String status) {
        getPrefs().edit()
            .putString("status", status)
            .remove("expires_at")
            .remove("login")
            .remove("password")
            .remove("dns_urls")
            .remove("dns_epg_urls")
            .apply();
    }

    private SharedPreferences getPrefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /**
     * Navigue vers MainActivity en passant les infos d'activation en extras.
     * Le téléchargement des playlists se fait dans MainActivity.
     */
    private void goToMain(boolean animate, DeviceSecurity.ActivationResult r) {
        Intent intent = new Intent(this, MainActivity.class);
        if (r != null) {
            intent.putExtra(MainActivity.EXTRA_ACT_LOGIN,    r.login);
            intent.putExtra(MainActivity.EXTRA_ACT_PASSWORD, r.password);
            intent.putExtra(MainActivity.EXTRA_ACT_EXPIRES,  r.expiresAt);
            String[] dnsUrls = new String[r.dnsServers.size()];
            String[] epgUrls = new String[r.dnsServers.size()];
            for (int i = 0; i < r.dnsServers.size(); i++) {
                dnsUrls[i] = r.dnsServers.get(i).url;
                epgUrls[i] = r.dnsServers.get(i).epgUrl != null ? r.dnsServers.get(i).epgUrl : "";
            }
            intent.putExtra(MainActivity.EXTRA_ACT_DNS_URLS,     dnsUrls);
            intent.putExtra(MainActivity.EXTRA_ACT_DNS_EPG_URLS, epgUrls);
        }
        startActivity(intent);
        if (!animate) overridePendingTransition(0, 0);
        finish();
    }

    private void setLoading(boolean on) {
        progressBar.setVisibility(on ? View.VISIBLE : View.GONE);
        btnCheck.setEnabled(!on);
        btnCheck.setText(on ? "Vérification…" : "🔄 Vérifier l'activation");
    }
}
