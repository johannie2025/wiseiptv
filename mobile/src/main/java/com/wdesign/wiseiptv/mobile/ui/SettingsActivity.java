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
        View rPlaylists = bindRow(R.id.row_playlists, R.drawable.ic_playlist, "Playlists",
            "Ajouter une playlist M3U / Xtream", () -> {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra(MainActivity.EXTRA_OPEN_ADD, true);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            });
        View rGrid = bindRow(R.id.row_grid, R.drawable.ic_grid, "Disposition de la grille",
            gridColumns() + " colonnes", this::showGridDialog);
        View rRefresh = bindRow(R.id.row_refresh, R.drawable.ic_refresh, "Actualiser les chaînes",
            "Retélécharger les playlists maintenant", () -> {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra(MainActivity.EXTRA_FORCE_SYNC, true);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            });
        View rSupport = bindRow(R.id.row_support, R.drawable.ic_chat, "Support WhatsApp",
            "Obtenir de l'aide rapidement", this::openWhatsApp);
        View rAbout = bindRow(R.id.row_about, R.drawable.ic_info, "À propos",
            "Wise IPTV — par W Design",
            () -> startActivity(new Intent(this, AboutActivity.class)));

        Fx.stagger(60, 55, rAccount, rPlaylists, rGrid, rRefresh, rSupport, rAbout);
        Fx.pressable(rAccount); Fx.pressable(rPlaylists); Fx.pressable(rGrid);
        Fx.pressable(rRefresh); Fx.pressable(rSupport); Fx.pressable(rAbout);
    }

    private View bindRow(int includeId, int icon, String title, String sub, Runnable action) {
        View row = findViewById(includeId);
        ((ImageView) row.findViewById(R.id.row_icon)).setImageResource(icon);
        ((TextView) row.findViewById(R.id.row_title)).setText(title);
        ((TextView) row.findViewById(R.id.row_sub)).setText(sub);
        row.setOnClickListener(v -> action.run());
        return row;
    }

    private int gridColumns() {
        int c = getSharedPreferences("wise_display", MODE_PRIVATE).getInt("grid_columns", 3);
        return Math.max(2, Math.min(5, c));
    }

    private void showGridDialog() {
        String[] items = {"2 colonnes", "3 colonnes", "4 colonnes", "5 colonnes"};
        new android.app.AlertDialog.Builder(this)
            .setTitle("Disposition de la grille")
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
