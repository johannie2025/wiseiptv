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
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.util.ActivationManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ActivationActivity — PREMIER ÉCRAN, mais seulement si nécessaire.
 *
 * Logique de routage au démarrage :
 *  ┌─ ACTIVE en cache + non expiré  →  skip directement → MainActivity (invisible)
 *  ├─ ACTIVE en cache + expiré      →  afficher page expiration
 *  └─ INACTIF / INCONNU             →  afficher page activation
 *
 * La page ne s'affiche JAMAIS si l'activation est valide en cache.
 * La vérification réseau se fait en background depuis WiseApp.
 */
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

        // ── DÉCISION INSTANTANÉE : pas besoin d'afficher la page ? ──
        if (isActiveAndNotExpired()) {
            goToMain(false);  // transparent, pas d'animation
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

        // Bouton vérification manuelle
        btnCheck.setOnClickListener(v -> checkActivation(false));

        // Bouton accès : vérifie + télécharge + ouvre
        btnAccess.setOnClickListener(v -> checkActivation(true));

        // Si expiré, montrer l'état expiré immédiatement
        String saved = getPrefs().getString("status", "");
        if ("EXPIRED".equals(saved) || "DISABLED".equals(saved)) {
            showInactive(saved);
        }

        // Vérification réseau silencieuse
        checkActivation(false);
    }

    // ── Vérification activation ─────────────────────────────────────

    private void checkActivation(boolean goOnSuccess) {
        setLoading(true);
        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override
            public void onActive(DeviceSecurity.ActivationResult r) {
                saveCache(r);
                runOnUiThread(() -> {
                    if (goOnSuccess) {
                        // Télécharger les playlists puis rediriger
                        downloadAndGo(r);
                    } else {
                        // Juste afficher l'état actif
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
                    // Mode offline : si cache ACTIVE, montrer bouton accès
                    if (isActiveAndNotExpired()) {
                        showOffline();
                    } else {
                        showPending(message);
                    }
                });
            }
        });
    }

    /**
     * Télécharge toutes les playlists en background puis redirige vers MainActivity.
     * L'UI montre "Téléchargement…" pendant le process.
     */
    private void downloadAndGo(DeviceSecurity.ActivationResult r) {
        btnAccess.setEnabled(false);
        btnAccess.setText("Téléchargement…");
        progressBar.setVisibility(View.VISIBLE);

        AppDatabase db = AppDatabase.get(this);
        ActivationManager.upsertAndDownloadAll(this, db, r, new ActivationManager.DownloadCallback() {
            @Override
            public void onProgress(String playlistName) {
                runOnUiThread(() -> btnAccess.setText("📥 " + playlistName + "…"));
            }
            @Override
            public void onDone(int totalChannels) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    goToMain(true);
                });
            }
            @Override
            public void onError(String msg) {
                runOnUiThread(() -> {
                    // Même en cas d'erreur de téléchargement on laisse accéder
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ActivationActivity.this,
                        "Téléchargement partiel : " + msg, Toast.LENGTH_LONG).show();
                    goToMain(true);
                });
            }
        });
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
        // En mode offline le clic va directement à MainActivity sans retélécharger
        btnAccess.setOnClickListener(v -> goToMain(true));
    }

    // ── Cache ──────────────────────────────────────────────────────

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
        getPrefs().edit()
            .putString("status", "ACTIVE")
            .putString("expires_at", r.expiresAt)
            .putString("login", r.login)
            .apply();
    }

    private void clearCache(String status) {
        getPrefs().edit()
            .putString("status", status)
            .remove("expires_at")
            .remove("login")
            .apply();
    }

    private SharedPreferences getPrefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // ── Navigation ─────────────────────────────────────────────────

    private void goToMain(boolean animate) {
        startActivity(new Intent(this, MainActivity.class));
        if (!animate) overridePendingTransition(0, 0);
        finish();
    }

    private void setLoading(boolean on) {
        progressBar.setVisibility(on ? View.VISIBLE : View.GONE);
        btnCheck.setEnabled(!on);
        btnCheck.setText(on ? "Vérification…" : "🔄 Vérifier l'activation");
    }
}
