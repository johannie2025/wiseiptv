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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ActivationActivity — PREMIER ÉCRAN, seulement si nécessaire.
 *
 * CORRECTIONS :
 *  1. CRASH "Activity détruite" — double DeviceSecurity.check() simultané.
 *     checkActivation(false) au démarrage + clic btnAccess → checkActivation(true)
 *     lancent deux check() sur le même EXEC singleton. Quand le 1er termine
 *     goToMain() → finish(), le 2e callback arrive sur une Activity détruite
 *     → NullPointerException sur btnAccess/progressBar.
 *     FIX : flag isCheckInProgress + guard isFinishing()/isDestroyed() dans
 *     TOUS les callbacks avant tout accès aux vues.
 *
 *  2. CRASH "cesse de fonctionner" — upsertAndDownloadAll() appelé 2x
 *     (une fois depuis checkActivation silencieux, une fois depuis btnAccess)
 *     → double transaction Room en parallèle → deadlock.
 *     FIX : le check silencieux au démarrage ne télécharge PAS (goOnSuccess=false),
 *     il met juste à jour l'état UI. Seul le clic btnAccess déclenche le DL.
 *
 *  3. Mode offline : btnAccess en mode offline navigue directement sans check réseau.
 */
public class ActivationActivity extends AppCompatActivity {

    private static final String PREFS = "wise_activation";

    private TextView    tvDeviceKey, tvStatus, tvStatusDetail;
    private TextView    tvProviderLabel, tvLogin, tvPassword, tvExpiry;
    private View        cardProvider;
    private Button      btnCheck, btnAccess, btnCopy;
    private ProgressBar progressBar;

    // FIX : empêche deux check() simultanés
    private boolean isCheckInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Décision instantanée : cache valide → aller directement à MainActivity
        if (isActiveAndNotExpired()) {
            goToMain();
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

        // Vérification manuelle : affiche l'état, ne télécharge pas
        btnCheck.setOnClickListener(v -> checkActivation(false));

        // Accès : vérifie + télécharge + ouvre MainActivity
        btnAccess.setOnClickListener(v -> {
            String saved = getPrefs().getString("status", "");
            if ("ACTIVE".equals(saved) && !isCheckInProgress) {
                // On a déjà un résultat actif → télécharger directement
                startDownloadAndGo();
            } else if (!isCheckInProgress) {
                // Pas encore vérifié → vérifier d'abord puis télécharger
                checkActivation(true);
            }
        });

        // Afficher état expiré/désactivé s'il est en cache
        String saved = getPrefs().getString("status", "");
        if ("EXPIRED".equals(saved) || "DISABLED".equals(saved)) {
            showInactive(saved);
        }

        // Vérification réseau silencieuse au démarrage (affichage seulement, pas de DL)
        checkActivation(false);
    }

    // ── Vérification ───────────────────────────────────────────────

    private void checkActivation(boolean goOnSuccess) {
        // FIX : un seul check à la fois
        if (isCheckInProgress) return;
        isCheckInProgress = true;
        setLoading(true);

        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override
            public void onActive(DeviceSecurity.ActivationResult r) {
                saveCache(r);
                runOnUiThread(() -> {
                    isCheckInProgress = false;
                    // FIX : guard — Activity peut être détruite si goToMain() déjà appelé
                    if (isFinishing() || isDestroyed()) return;
                    setLoading(false);
                    if (goOnSuccess) {
                        startDownloadAndGo(r);
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

    // ── Téléchargement + navigation ────────────────────────────────

    /**
     * Version sans ActivationResult : reconstruit depuis le cache prefs.
     * Appelée quand btnAccess est cliqué alors qu'on a déjà un état ACTIVE en cache.
     */
    private void startDownloadAndGo() {
        // Récupérer les DNS depuis les playlists existantes en BDD
        AppDatabase db = AppDatabase.get(this);
        btnAccess.setEnabled(false);
        btnAccess.setText("Téléchargement…");
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            // Chercher les playlists d'activation existantes
            java.util.List<com.wdesign.wiseiptv.core.db.entity.PlaylistEntity> pls =
                db.playlistDao().getAllSync();
            boolean hasActivation = false;
            for (com.wdesign.wiseiptv.core.db.entity.PlaylistEntity pl : pls) {
                if (pl.isActive && pl.type ==
                    com.wdesign.wiseiptv.core.db.entity.PlaylistEntity.TYPE_XTREAM) {
                    hasActivation = true;
                    break;
                }
            }
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                progressBar.setVisibility(View.GONE);
                if (hasActivation) {
                    // Playlists déjà en BDD → aller directement, MainActivity téléchargera si stale
                    goToMain();
                } else {
                    // Aucune playlist → relancer une vérification complète
                    btnAccess.setEnabled(true);
                    btnAccess.setText("▶ Accéder au contenu");
                    checkActivation(true);
                }
            });
        }).start();
    }

    /**
     * Version avec ActivationResult frais depuis le serveur.
     * Ouvre MainActivity IMMÉDIATEMENT via les extras Intent.
     * MainActivity reçoit login/password/dns et lance le téléchargement en background.
     */
    private void startDownloadAndGo(DeviceSecurity.ActivationResult r) {
        Intent intent = new Intent(this, MainActivity.class);
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
            intent.putExtra(MainActivity.EXTRA_ACT_DNS_URLS,     urls);
            intent.putExtra(MainActivity.EXTRA_ACT_DNS_EPG_URLS, epgUrls);
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
        // En mode offline : aller directement sans télécharger
        btnAccess.setOnClickListener(v -> goToMain());
    }

    // ── Cache ───────────────────────────────────────────────────────

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

    // ── Navigation ──────────────────────────────────────────────────

    private void goToMain() {
        if (isFinishing() || isDestroyed()) return;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void setLoading(boolean on) {
        if (progressBar == null || btnCheck == null) return;
        progressBar.setVisibility(on ? View.VISIBLE : View.GONE);
        btnCheck.setEnabled(!on);
        btnCheck.setText(on ? "Vérification…" : "🔄 Vérifier l'activation");
    }
}