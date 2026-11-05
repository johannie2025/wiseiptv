package com.wdesign.wiseiptv.mobile.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wdesign.wiseiptv.core.player.PlayerEngine;
import com.wdesign.wiseiptv.core.util.AppSettings;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.core.util.SubscriptionInfo;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.util.ParentalControl;
import com.wdesign.wiseiptv.mobile.util.ResumeStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private static final String[] NET_LABELS = {"Automatique", "Économie de données (≈ 480p max)", "Qualité maximale"};

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_settings);
        NavHelper.bind(this, (BottomNavigationView) findViewById(R.id.bottom_nav), R.id.nav_settings);

        View[] rows = {
            bindRow(R.id.row_account, R.drawable.ic_person, "Mon compte", "Abonnement et clé de l'appareil",
                () -> startActivity(new Intent(this, AccountActivity.class))),
            bindRow(R.id.row_appearance, R.drawable.ic_tune, "Apparence", "", this::showAppearance),
            bindRow(R.id.row_language, R.drawable.ic_chat, "Langue", "", this::showLanguage),
            bindRow(R.id.row_player, R.drawable.ic_play, "Lecteur vidéo", "", this::showPlayerOptions),
            bindRow(R.id.row_parental, R.drawable.ic_warning, "Contrôle parental", "", this::showParental),
            bindRow(R.id.row_network, R.drawable.ic_link, "Qualité réseau", "", this::showNetwork),
            bindRow(R.id.row_layout, R.drawable.ic_grid, "Disposition", "", this::showLayoutDialog),
            bindRow(R.id.row_refresh, R.drawable.ic_refresh, "Actualiser les chaînes", "", this::showRefreshDialog),
            bindRow(R.id.row_support, R.drawable.ic_chat, "Support WhatsApp", "Obtenir de l'aide rapidement", this::openWhatsApp),
            bindRow(R.id.row_rate, R.drawable.ic_favorite, "Noter l'application", "Votre avis compte !", this::rateApp),
            bindRow(R.id.row_about, R.drawable.ic_info, "À propos", "Wise IPTV — par W Design",
                () -> startActivity(new Intent(this, AboutActivity.class)))
        };
        Fx.stagger(60, 45, rows);
        for (View r : rows) Fx.pressable(r);
        refreshSubs();
    }

    @Override protected void onResume() {
        super.onResume();
        ResumeStore.setScreen(this, ResumeStore.SCREEN_SETTINGS);
        refreshSubs();
    }

    private View bindRow(int includeId, int icon, String title, String sub, Runnable action) {
        View row = findViewById(includeId);
        ((ImageView) row.findViewById(R.id.row_icon)).setImageResource(icon);
        ((TextView) row.findViewById(R.id.row_title)).setText(title);
        ((TextView) row.findViewById(R.id.row_sub)).setText(sub);
        row.setOnClickListener(v -> action.run());
        return row;
    }

    private void sub(int rowId, String text) {
        ((TextView) findViewById(rowId).findViewById(R.id.row_sub)).setText(text);
    }

    private void refreshSubs() {
        sub(R.id.row_appearance, "Animations " + (AppSettings.animations(this) ? "activées" : "désactivées")
            + " · Badges " + (AppSettings.liveBadges(this) ? "visibles" : "masqués"));
        sub(R.id.row_language, "Interface : Français · Audio : "
            + AppSettings.AUDIO_LABELS[AppSettings.audioLangIndex(this)]);
        sub(R.id.row_player, "Qualité : " + PlayerEngine.QUALITY_LABELS[AppSettings.defaultQuality(this)]
            + " · Sous-titres " + (AppSettings.subtitles(this) ? "activés" : "désactivés"));
        sub(R.id.row_parental, ParentalControl.isEnabled(this) ? "Activé · contenus adultes masqués" : "Désactivé");
        sub(R.id.row_network, NET_LABELS[AppSettings.networkMode(this)]);
        sub(R.id.row_layout, AppSettings.isListLayout(this) ? "Liste"
            : "Grille · " + AppSettings.gridColumns(this) + " colonnes");
        long last = getSharedPreferences(SubscriptionInfo.PREFS_MOBILE, MODE_PRIVATE).getLong("last_sync_ts", 0);
        sub(R.id.row_refresh, AppSettings.REFRESH_LABELS[AppSettings.refreshFreq(this)]
            + (AppSettings.refreshWifiOnly(this) ? " · Wi-Fi" : "")
            + (last > 0 ? " · Dernière : " + new SimpleDateFormat("dd/MM HH:mm", Locale.FRANCE).format(new Date(last)) : ""));
    }

    // ── Dialogues ────────────────────────────────────────────────

    private void showAppearance() {
        final boolean[] on = {AppSettings.animations(this), AppSettings.liveBadges(this)};
        new AlertDialog.Builder(this).setTitle("Apparence")
            .setMultiChoiceItems(new String[]{"Animations de l'interface", "Badges « EN DIRECT »"}, on,
                (d, which, checked) -> on[which] = checked)
            .setPositiveButton("OK", (d, w) -> {
                AppSettings.setAnimations(this, on[0]);
                AppSettings.setLiveBadges(this, on[1]);
                refreshSubs();
            }).setNegativeButton("Annuler", null).show();
    }

    private void showLanguage() {
        new AlertDialog.Builder(this).setTitle("Langue audio / sous-titres préférée")
            .setSingleChoiceItems(AppSettings.AUDIO_LABELS, AppSettings.audioLangIndex(this), (d, which) -> {
                AppSettings.setAudioLangIndex(this, which); d.dismiss(); refreshSubs();
            }).setNegativeButton("Fermer", null).show();
        Toast.makeText(this, "Langue de l'interface : Français", Toast.LENGTH_SHORT).show();
    }

    private void showPlayerOptions() {
        String[] items = {
            "Qualité par défaut : " + PlayerEngine.QUALITY_LABELS[AppSettings.defaultQuality(this)],
            "Sous-titres : " + (AppSettings.subtitles(this) ? "activés" : "désactivés")};
        new AlertDialog.Builder(this).setTitle("Lecteur vidéo")
            .setItems(items, (d, which) -> {
                if (which == 0) {
                    new AlertDialog.Builder(this).setTitle("Qualité par défaut")
                        .setSingleChoiceItems(PlayerEngine.QUALITY_LABELS, AppSettings.defaultQuality(this), (d2, i) -> {
                            AppSettings.setDefaultQuality(this, i); d2.dismiss(); refreshSubs();
                        }).show();
                } else {
                    AppSettings.setSubtitles(this, !AppSettings.subtitles(this));
                    refreshSubs();
                    showPlayerOptions();
                }
            }).setNegativeButton("Fermer", null).show();
    }

    private void showNetwork() {
        new AlertDialog.Builder(this).setTitle("Qualité réseau")
            .setSingleChoiceItems(NET_LABELS, AppSettings.networkMode(this), (d, which) -> {
                AppSettings.setNetworkMode(this, which); d.dismiss(); refreshSubs();
            }).setNegativeButton("Fermer", null).show();
    }

    private void showLayoutDialog() {
        String[] items = {"Grille · 2 colonnes", "Grille · 3 colonnes", "Grille · 4 colonnes",
            "Grille · 5 colonnes", "Liste"};
        int checked = AppSettings.isListLayout(this) ? 4 : AppSettings.gridColumns(this) - 2;
        new AlertDialog.Builder(this).setTitle("Disposition")
            .setSingleChoiceItems(items, checked, (d, which) -> {
                if (which == 4) AppSettings.setListLayout(this, true);
                else { AppSettings.setListLayout(this, false); AppSettings.setGridColumns(this, which + 2); }
                d.dismiss(); refreshSubs();
            }).setNegativeButton("Fermer", null).show();
    }

    private void showRefreshDialog() {
        float dp = getResources().getDisplayMetrics().density;
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(Math.round(20 * dp), Math.round(8 * dp), Math.round(20 * dp), 0);
        TextView hint = new TextView(this);
        hint.setText("Quand actualiser automatiquement la liste des chaînes ?");
        hint.setTextColor(0xFF8FA6CC);
        hint.setPadding(0, 0, 0, Math.round(8 * dp));
        box.addView(hint);
        final RadioGroup group = new RadioGroup(this);
        for (int i = 0; i < AppSettings.REFRESH_LABELS.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setId(1000 + i);
            rb.setText(AppSettings.REFRESH_LABELS[i]);
            rb.setTextColor(0xFFFFFFFF);
            group.addView(rb);
        }
        group.check(1000 + AppSettings.refreshFreq(this));
        box.addView(group);
        final CheckBox wifi = new CheckBox(this);
        wifi.setText("Seulement en Wi-Fi / réseau non limité");
        wifi.setTextColor(0xFFFFFFFF);
        wifi.setChecked(AppSettings.refreshWifiOnly(this));
        box.addView(wifi);
        new AlertDialog.Builder(this).setTitle("Actualisation des chaînes")
            .setView(box)
            .setPositiveButton("Enregistrer", (d, w) -> {
                AppSettings.setRefreshFreq(this, Math.max(0, group.getCheckedRadioButtonId() - 1000));
                AppSettings.setRefreshWifiOnly(this, wifi.isChecked());
                refreshSubs();
            })
            .setNeutralButton("Actualiser maintenant", (d, w) -> {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra(MainActivity.EXTRA_FORCE_SYNC, true);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            })
            .setNegativeButton("Annuler", null).show();
    }

    // ── Contrôle parental ───────────────────────────────────────

    private void showParental() {
        if (!ParentalControl.isEnabled(this)) {
            askPin("Créer un code PIN (4 chiffres)", pin -> {
                if (pin.length() != 4) { toast("Le code doit comporter 4 chiffres."); return; }
                ParentalControl.enable(this, pin);
                toast("Contrôle parental activé.");
                refreshSubs();
            });
            return;
        }
        askPin("Entrez votre code PIN", pin -> {
            if (!ParentalControl.checkPin(this, pin)) { toast("Code PIN incorrect."); return; }
            new AlertDialog.Builder(this).setTitle("Contrôle parental")
                .setItems(new String[]{"Changer le code PIN", "Désactiver le contrôle parental"}, (d, which) -> {
                    if (which == 0) {
                        askPin("Nouveau code PIN (4 chiffres)", np -> {
                            if (np.length() != 4) { toast("Le code doit comporter 4 chiffres."); return; }
                            ParentalControl.enable(this, np);
                            toast("Code PIN modifié.");
                        });
                    } else {
                        ParentalControl.disable(this);
                        toast("Contrôle parental désactivé.");
                        refreshSubs();
                    }
                }).setNegativeButton("Fermer", null).show();
        });
    }

    private interface PinCallback { void onPin(String pin); }

    private void askPin(String title, PinCallback cb) {
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        et.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(4)});
        et.setTextColor(0xFFFFFFFF);
        et.setGravity(android.view.Gravity.CENTER);
        et.setTextSize(24);
        new AlertDialog.Builder(this).setTitle(title).setView(et)
            .setPositiveButton("OK", (d, w) -> cb.onPin(et.getText().toString()))
            .setNegativeButton("Annuler", null).show();
    }

    private void toast(String m) { Toast.makeText(this, m, Toast.LENGTH_SHORT).show(); }

    // ── Liens ────────────────────────────────────────────────────

    private void openWhatsApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/" + SubscriptionInfo.WHATSAPP)));
        } catch (Exception ignored) { }
    }

    private void rateApp() {
        String pkg = getPackageName().replace(".debug", "");
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg)));
        } catch (Exception e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + pkg)));
            } catch (Exception ignored) { toast("Impossible d'ouvrir la page de l'application."); }
        }
    }
}
