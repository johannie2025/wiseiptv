package com.wdesign.wiseiptv.mobile.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.core.util.SubscriptionInfo;
import com.wdesign.wiseiptv.mobile.R;

public class SettingsActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_settings);

        NavHelper.bind(this, (BottomNavigationView) findViewById(R.id.bottom_nav), R.id.nav_settings);

        View rAccount = bindRow(R.id.row_account, R.drawable.ic_person, "Mon compte",
            "Abonnement et clé de l'appareil",
            () -> startActivity(new Intent(this, AccountActivity.class)));
        View rAppearance = bindRow(R.id.row_appearance, R.drawable.ic_palette, "Apparence",
            currentSetting("appearance", "Sombre"), () -> showChoiceDialog(R.id.row_appearance, "Apparence",
                "appearance", new String[]{"Sombre","Clair","Auto"}, new String[]{"Sombre","Clair","Auto"}, "Sombre"));
        View rLanguage = bindRow(R.id.row_language, R.drawable.ic_language, "Langue",
            currentSetting("language", "Français"), () -> showChoiceDialog(R.id.row_language, "Langue",
                "language", new String[]{"Français","English","العربية"}, new String[]{"Français","English","العربية"}, "Français"));
        View rParental = bindRow(R.id.row_parental, R.drawable.ic_shield, "Contrôle parental",
            currentSetting("parental", "Désactivé"), () -> showChoiceDialog(R.id.row_parental, "Contrôle parental",
                "parental", new String[]{"Désactivé","Actif","Enfant"}, new String[]{"Désactivé","Actif","Enfant"}, "Désactivé"));
        View rNetwork = bindRow(R.id.row_network, R.drawable.ic_signal, "Qualité réseau",
            currentSetting("network", "Automatique"), () -> showChoiceDialog(R.id.row_network, "Qualité réseau",
                "network", new String[]{"Automatique","Faible","Moyenne","Haute"}, new String[]{"Automatique","Faible","Moyenne","Haute"}, "Automatique"));
        View rVideo = bindRow(R.id.row_video, R.drawable.ic_play, "Lecteur vidéo",
            currentSetting("video", "Complet"), () -> showChoiceDialog(R.id.row_video, "Lecteur vidéo",
                "video", new String[]{"Complet","Compact","HD uniquement"}, new String[]{"Complet","Compact","HD uniquement"}, "Complet"));
        View rGrid = bindRow(R.id.row_grid, R.drawable.ic_grid, "Disposition",
            gridColumns() + " colonnes", this::showGridDialog);
        View rRefresh = bindRow(R.id.row_refresh, R.drawable.ic_refresh, "Actualiser les chaînes",
            "Rafraîchir manuellement", () -> {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra(MainActivity.EXTRA_FORCE_SYNC, true);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            });
        View rSupport = bindRow(R.id.row_support, R.drawable.ic_chat, "Support WhatsApp",
            "Obtenir de l'aide rapidement", this::openWhatsApp);
        View rAbout = bindRow(R.id.row_about, R.drawable.ic_info, "À propos",
            "Wise IPTV — Solutions Premium OTT & Panel Revendeur",
            () -> startActivity(new Intent(this, AboutActivity.class)));

        Fx.stagger(60, 55, rAccount, rAppearance, rLanguage, rParental, rNetwork, rVideo, rGrid, rRefresh, rSupport, rAbout);
        Fx.pressable(rAccount); Fx.pressable(rAppearance); Fx.pressable(rLanguage); Fx.pressable(rParental);
        Fx.pressable(rNetwork); Fx.pressable(rVideo); Fx.pressable(rGrid); Fx.pressable(rRefresh);
        Fx.pressable(rSupport); Fx.pressable(rAbout);
    }

    private View bindRow(int includeId, int icon, String title, String sub, Runnable action) {
        View row = findViewById(includeId);
        ((ImageView) row.findViewById(R.id.row_icon)).setImageResource(icon);
        ((TextView) row.findViewById(R.id.row_title)).setText(title);
        ((TextView) row.findViewById(R.id.row_sub)).setText(sub);
        row.setOnClickListener(v -> action.run());
        return row;
    }

    private String currentSetting(String key, String defaultValue) {
        String value = getSharedPreferences("wise_settings", MODE_PRIVATE).getString(key, defaultValue);
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }

    private void showChoiceDialog(int rowId, String title, String prefKey, String[] values, String[] labels, String defaultValue) {
        SharedPreferences prefs = getSharedPreferences("wise_settings", MODE_PRIVATE);
        String current = prefs.getString(prefKey, defaultValue);
        int selected = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(current)) {
                selected = i;
                break;
            }
        }
        new android.app.AlertDialog.Builder(this)
            .setTitle(title)
            .setSingleChoiceItems(labels, selected, (d, which) -> {
                prefs.edit().putString(prefKey, values[which]).apply();
                ((TextView) findViewById(rowId).findViewById(R.id.row_sub)).setText(labels[which]);
                d.dismiss();
            }).show();
    }

    private int gridColumns() {
        int c = getSharedPreferences("wise_display", MODE_PRIVATE).getInt("grid_columns", 3);
        return Math.max(2, Math.min(5, c));
    }

    private void showGridDialog() {
        String[] items = {"2 colonnes", "3 colonnes", "4 colonnes", "5 colonnes"};
        new android.app.AlertDialog.Builder(this)
            .setTitle("Disposition")
            .setSingleChoiceItems(items, gridColumns() - 2, (d, which) -> {
                SharedPreferences p = getSharedPreferences("wise_display", MODE_PRIVATE);
                p.edit().putInt("grid_columns", which + 2).apply();
                ((TextView) findViewById(R.id.row_grid).findViewById(R.id.row_sub))
                    .setText((which + 2) + " colonnes");
                d.dismiss();
            }).show();
    }

    private void openWhatsApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/" + SubscriptionInfo.WHATSAPP)));
        } catch (Exception ignored) { }
    }
}
