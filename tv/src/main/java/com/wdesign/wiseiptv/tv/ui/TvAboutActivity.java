package com.wdesign.wiseiptv.tv.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.tv.R;

public class TvAboutActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_about);

        TextView version = findViewById(R.id.tv_about_version);
        String v = "2.0.0";
        try { v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName; }
        catch (Exception ignored) {}
        version.setText("v" + v);

        View web = findViewById(R.id.row_web);
        View legal = findViewById(R.id.row_legal);
        View privacy = findViewById(R.id.row_privacy);
        View contact = findViewById(R.id.row_contact);
        Fx.focusZoom(web, 1.04f);
        Fx.focusZoom(legal, 1.04f);
        Fx.focusZoom(privacy, 1.04f);
        Fx.focusZoom(contact, 1.04f);

        web.setOnClickListener(v -> openUrl("https://www.wiseiptv.com"));
        legal.setOnClickListener(v -> openUrl("https://www.wiseiptv.com/mentions-legales"));
        privacy.setOnClickListener(v -> openUrl("https://www.wiseiptv.com/politique-de-confidentialite"));
        contact.setOnClickListener(v -> openUrl("https://wa.me/240222886007"));
    }

    private void openUrl(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)));
        } catch (Exception ignored) {}
    }
}
