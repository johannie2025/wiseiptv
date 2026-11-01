package com.wdesign.wiseiptv.mobile.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.mobile.R;

public class AboutActivity extends AppCompatActivity {

    private android.animation.ObjectAnimator glowPulse;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_about);

        // Sous-écran de Paramètres : on garde "Paramètres" actif dans la barre
        NavHelper.bind(this, (BottomNavigationView) findViewById(R.id.bottom_nav), R.id.nav_settings, true);

        String v = "2.0.0";
        try { v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName; }
        catch (Exception ignored) { }
        ((TextView) findViewById(R.id.tv_about_version)).setText("v" + v);

        View dev = bindRow(R.id.row_dev, R.drawable.ic_info, "Développé par W Design",
            "Agence d'Architecture · Design · Innovation", null);
        View web = bindRow(R.id.row_web, R.drawable.ic_link, "Site web", "www.wdesign.ga", () ->
            open(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wdesign.ga"))));
        View contact = bindRow(R.id.row_contact, R.drawable.ic_phone, "Nous contacter",
            "+240 222 886 007", () ->
            open(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+240222886007"))));

        glowPulse = Fx.pulse(findViewById(R.id.about_glow), 1.1f, 1600);
        Fx.pop(findViewById(R.id.about_logo));
        Fx.stagger(150, 80, dev, web, contact);
        Fx.pressable(web); Fx.pressable(contact);
    }

    private View bindRow(int includeId, int icon, String title, String sub, Runnable action) {
        View row = findViewById(includeId);
        ((ImageView) row.findViewById(R.id.row_icon)).setImageResource(icon);
        ((TextView) row.findViewById(R.id.row_title)).setText(title);
        ((TextView) row.findViewById(R.id.row_sub)).setText(sub);
        if (action != null) row.setOnClickListener(v -> action.run());
        else { row.setClickable(false); row.setFocusable(false); }
        return row;
    }

    private void open(Intent i) {
        try { startActivity(i); } catch (Exception ignored) { }
    }

    @Override protected void onDestroy() {
        if (glowPulse != null) glowPulse.cancel();
        super.onDestroy();
    }
}
