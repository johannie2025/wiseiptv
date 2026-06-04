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

/**
 * ActivationActivity — adapté de TvActivationActivity (version stable).
 *
 * Flux identique à la TV :
 *  1. Cache valide → goToMain(false, null) directement
 *  2. btnCheck    → checkActivation(false) : affiche seulement
 *  3. btnAccess   → checkActivation(true)  : vérifie + navigue avec résultat frais
 *  4. Hors-ligne  → goToMain(false, null)  : cache uniquement
 *
 * goToMain() passe TOUJOURS les extras complets à MainActivity.
 * saveCache() stocke : status, expires_at, login, password, dns_urls, dns_epg_urls
 */
public class ActivationActivity extends AppCompatActivity {

    private static final String PREFS = "wise_activation";

    private TextView    tvDeviceKey, tvStatus, tvStatusDetail;
    private TextView    tvProviderLabel, tvLogin, tvPassword, tvExpiry;
    private View        cardProvider;
    private Button      btnCheck, btnAccess, btnCopy;
    private ProgressBar progressBar;

    private boolean isCheckInProgress = false;

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
        // Identique à TV : btnAccess déclenche toujours checkActivation(true)
        btnAccess.setOnClickListener(v -> checkActivation(true));

        String saved = getPrefs().getString("status", "");
        if ("EXPIRED".equals(saved) || "DISABLED".equals(saved)) showInactive(saved);

        checkActivation(false);
    }

    // ── Vérification ───────────────────────────────────────────────

    private void checkActivation(boolean goOnSuccess) {
        if (isCheckInProgress) return;
        isCheckInProgress = true;
        setLoading(true);

        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override
            public void onActive(DeviceSecurity.ActivationResult r) {
                saveCache(r);
                runOnUiThread(() -> {
                    isCheckInProgress = false;
                    if (isFinishing() || isDestroyed()) return;
                    setLoading(false);
                    if (goOnSuccess) {
                        goToMain(true, r); // résultat frais → extras complets
                    } else {
                        showActive(r);
                    }
                });
            }

            @Override
            public void onInactive(String status, String message) {
                clearCache(status);
                runOnUiThread(() -> {
                    isCheckInProgress = false;
                    if (isFinishing() || isDestroyed()) return;
                    setLoading(false);
                    showInactive(status);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    isCheckInProgress = false;
                    if (isFinishing() || isDestroyed()) return;
                    setLoading(false);
                    if (isActiveAndNotExpired()) showOffline();
                    else showPending(message);
                });
            }
        });
    }

    // ── Navigation — identique à TvActivationActivity.goToMain() ───

    /**
     * @param animate  true = transition normale, false = sans animation (retour cache)
     * @param r        résultat frais (null = navigation depuis cache)
     */
    private void goToMain(boolean animate, DeviceSecurity.ActivationResult r) {
        if (isFinishing() || isDestroyed()) return;
        Intent intent = new Intent(this, MainActivity.class);

        if (r != null) {
            // Résultat frais : extras complets depuis le serveur
            intent.putExtra(MainActivity.EXTRA_ACT_LOGIN,    r.login    != null ? r.login    : "");
            intent.putExtra(MainActivity.EXTRA_ACT_PASSWORD, r.password != null ? r.password : "");
            intent.putExtra(MainActivity.EXTRA_ACT_EXPIRES,  r.expiresAt != null ? r.expiresAt : "");
            if (r.dnsServers != null && !r.dnsServers.isEmpty()) {
                String[] urls    = new String[r.dnsServers.size()];
                String[] epgUrls = new String[r.dnsServers.size()];
                for (int i = 0; i < r.dnsServers.size(); i++) {
                    urls[i]    = r.dnsServers.get(i).url;
                    epgUrls[i] = r.dnsServers.get(i).epgUrl != null
                                 ? r.dnsServers.get(i).epgUrl : "";
                }
                intent.putExtra(MainActivity.EXTRA_ACT_DNS_URLS,     urls);
                intent.putExtra(MainActivity.EXTRA_ACT_DNS_EPG_URLS, epgUrls);
            }
        } else {
            // Cache : reconstruire les extras depuis SharedPreferences
            SharedPreferences p = getPrefs();
            intent.putExtra(MainActivity.EXTRA_ACT_LOGIN,    p.getString("login",      ""));
            intent.putExtra(MainActivity.EXTRA_ACT_PASSWORD, p.getString("password",   ""));
            intent.putExtra(MainActivity.EXTRA_ACT_EXPIRES,  p.getString("expires_at", ""));
            String dnsRaw = p.getString("dns_urls",     "");
            String epgRaw = p.getString("dns_epg_urls", "");
            if (!dnsRaw.isEmpty()) {
                intent.putExtra(MainActivity.EXTRA_ACT_DNS_URLS,     dnsRaw.split(","));
                intent.putExtra(MainActivity.EXTRA_ACT_DNS_EPG_URLS, epgRaw.split(","));
            }
        }

        startActivity(intent);
        if (!animate) overridePendingTransition(0, 0);
        finish();
    }

    // ── Affichage état ──────────────────────────────────────────────

    private void showActive(DeviceSecurity.ActivationResult r) {
        tvStatus.setText("✅ ACTIVÉ");
        tvStatus.setTextColor(0xFF4CAF50);
        tvStatusDetail.setText("Votre abonnement est actif");
        cardProvider.setVisibility(View.VISIBLE);
        tvLogin.setText("Login : " + r.login);
        tvPassword.setText("Mot de passe : " + r.password);
        tvExpiry.setText("Expire le : " + r.expiresAt);
        if (r.dnsServers != null && !r.dnsServers.isEmpty())
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
        btnAccess.setOnClickListener(v -> goToMain(false, null));
    }

    // ── Cache ───────────────────────────────────────────────────────

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
        if (r == null) return;
        SharedPreferences.Editor ed = getPrefs().edit()
            .putString("status",     "ACTIVE")
            .putString("expires_at", r.expiresAt != null ? r.expiresAt : "")
            .putString("login",      r.login     != null ? r.login     : "")
            .putString("password",   r.password  != null ? r.password  : "");
        if (r.dnsServers != null && !r.dnsServers.isEmpty()) {
            StringBuilder urls = new StringBuilder();
            StringBuilder epgs = new StringBuilder();
            for (int i = 0; i < r.dnsServers.size(); i++) {
                if (i > 0) { urls.append(","); epgs.append(","); }
                urls.append(r.dnsServers.get(i).url);
                String epg = r.dnsServers.get(i).epgUrl;
                epgs.append(epg != null ? epg : "");
            }
            ed.putString("dns_urls",     urls.toString())
              .putString("dns_epg_urls", epgs.toString());
        }
        ed.apply();
    }

    private void clearCache(String status) {
        getPrefs().edit()
            .putString("status", status)
            .remove("expires_at").remove("login").remove("password")
            .remove("dns_urls").remove("dns_epg_urls")
            .apply();
    }

    private SharedPreferences getPrefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private void setLoading(boolean on) {
        if (progressBar == null || btnCheck == null) return;
        progressBar.setVisibility(on ? View.VISIBLE : View.GONE);
        btnCheck.setEnabled(!on);
        btnCheck.setText(on ? "Vérification…" : "🔄 Vérifier l'activation");
    }
}