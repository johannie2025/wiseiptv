package com.wdesign.wiseiptv.tv.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.wdesign.wiseiptv.tv.R;

public class TvMainMenuActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_settings);
        startActivity(new Intent(this, TvAccountActivity.class));
        finish();
    }
}
