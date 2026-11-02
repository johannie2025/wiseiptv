package com.wdesign.wiseiptv.tv.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.core.util.SubscriptionInfo;
import com.wdesign.wiseiptv.tv.R;

public class TvSettingsActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_settings);

        View acc = findViewById(R.id.row_account);
        View appearance = findViewById(R.id.row_appearance);
        View language = findViewById(R.id.row_language);
        View parental = findViewById(R.id.row_parental);
        View network = findViewById(R.id.row_network);
        View video = findViewById(R.id.row_video);
        View grid = findViewById(R.id.row_grid);
        View refresh = findViewById(R.id.row_refresh);
        View about = findViewById(R.id.row_about);

        Fx.focusZoom(acc, 1.04f); Fx.focusZoom(appearance, 1.04f); Fx.focusZoom(language, 1.04f);
        Fx.focusZoom(parental, 1.04f); Fx.focusZoom(network, 1.04f); Fx.focusZoom(video, 1.04f);
        Fx.focusZoom(grid, 1.04f); Fx.focusZoom(refresh, 1.04f); Fx.focusZoom(about, 1.04f);

        acc.setOnClickListener(v -> startActivity(new Intent(this, TvAccountActivity.class)));
        about.setOnClickListener(v -> startActivity(new Intent(this, TvAboutActivity.class)));
        ((TextView) findViewById(R.id.tv_settings_version)).setText("v" + SubscriptionInfo.VERSION_NAME);
    }
}
