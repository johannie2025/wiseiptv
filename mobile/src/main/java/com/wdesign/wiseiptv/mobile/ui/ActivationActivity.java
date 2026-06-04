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
 * ActivationActivity — PREMIER ÉCRAN, seulement si nécessaire.
 *
 * Flux :
 *  1. Cache valide (ACTIVE + non expiré) → goToMainFromCache() directement
 *  2. btnCheck → checkActivation(false)  : affiche l'état seulement
 *  3. btnAccess → cache ACTIVE           : startDownloadAndGo() depuis prefs
 *               → pas encore vérifié    : checkActivation(true) → startDownloadAndGo(r)
 *  4. Hors-ligne + cache valide          : goToMainFromCache() (pas de DL)
 *
 * saveCache() stocke : status, expires_at, login, password, dns_urls, dns_epg_urls
 * MainActivity reçoit tous ces extras pour construire les PlaylistEntity.
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

        // Cache valide → MainActivity directement, sans passer par l'UI
        if (isActiveAndNotExpired()) {
            goToMainFromCache();
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

        // Vérification manuelle : affiche l'état uniquement, ne navigue pas
        btnCheck.setOnClickListener(v -> checkActivation(false));

        // Accès : cache ACTIVE → passe les credentials depuis prefs
        //         sinon → vérifie réseau puis navigue avec résultat frais
        btnAccess.setOnClickListener(v -> {
            if (isCheckInProgress) return;
            String saved = getPrefs().getString("status", "");
            if ("ACTIVE".equals(saved)) {
                goToMainFromCache();
            } else {
                checkActivation(true);
            }
        });

        // Afficher état expiré/désactivé depuis cache
        String saved = getPrefs().getString("status", "");
        if ("EXPIRED".equals(saved) || "DISABLED".equals(saved)) {
            showInactive(saved);
        }

        // Vérification réseau silencieuse au démarrage (affichage seulement)
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
                saveCache(r); // stocke login + password + dns_urls + dns_epg_urls
                runOnUiThread(() -> {
                    isCheckInProgress = false;
                    if (isFinishing() || isDestroyed()) return;
                    setLoading(false);
                    if (goOnSuccess) {
                        startDownloadAndGo(r); // résultat frais → extras complets
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

    // ── Navigation ─────────────────────────────────────────────────

    /**
     * Navigation avec résultat frais (après vérification réseau réussie).
     * Tous les extras sont présents → MainActivity télécharge les playlists.
     */
    private void startDownloadAndGo(DeviceSecurity.ActivationResult r) {
        if (isFinishing() || isDestroyed()) return;
        Intent intent = new Intent(this, MainActivity.class);
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
        startActivity(intent);
        finish();
    }

    /**
     * Navigation depuis le cache SharedPreferences.
     * Utilisé quand : cache valide au démarrage, btnAccess sur ACTIVE en cache,
     * ou mode hors-ligne.
     * MainActivity recevra les extras et fera un refresh stale si nécessaire.
     */
    private void goToMainFromCache() {
        if (isFinishing() || isDestroyed()) return;
        SharedPreferences p = getPrefs();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_ACT_LOGIN,    p.getString("login",      ""));
        intent.putExtra(MainActivity.EXTRA_ACT_PASSWORD, p.getString("password",   ""));
        intent.putExtra(MainActivity.EXTRA_ACT_EXPIRES,  p.getString("expires_at", ""));
        String dnsRaw = p.getString("dns_urls",     "");
        String epgRaw = p.getString("dns_epg_urls", "");
        if (!dnsRaw.isEmpty()) {
            intent.putExtra(MainActivity.EXTRA_ACT_DNS_URLS,     dnsRaw.split(","));
            intent.putExtra(MainActivity.EXTRA_ACT_DNS_EPG_URLS, epgRaw.split(","));
        }
        startActivity(intent);
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
        // Hors-ligne → cache uniquement, pas de téléchargement
        btnAccess.setOnClickListener(v -> goToMainFromCache());
    }

    // ── Cache ───────────────────────────────────────────────────────

    /**
     * Vérifie si le cache local indique un abonnement ACTIVE non expiré.
     */
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

    /**
     * Sauvegarde TOUS les champs nécessaires à goToMainFromCache() :
     * login, password, expires_at, dns_urls (CSV), dns_epg_urls (CSV).
     */
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

    /**
     * Efface tous les credentials du cache et enregistre le nouveau statut.
     */
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

    private void setLoading(boolean on) {
        if (progressBar == null || btnCheck == null) return;
        progressBar.setVisibility(on ? View.VISIBLE : View.GONE);
        btnCheck.setEnabled(!on);
        btnCheck.setText(on ? "Vérification…" : "🔄 Vérifier l'activation");
    }
}