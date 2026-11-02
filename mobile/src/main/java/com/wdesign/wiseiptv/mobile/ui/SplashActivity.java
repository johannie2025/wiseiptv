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
import com.wdesign.wiseiptv.mobile.R;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_MS = 2000;
    private android.animation.ObjectAnimator progressAnim;
    private boolean left = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View logo = findViewById(R.id.splash_logo);
        View title = findViewById(R.id.splash_title);
        View tagline = findViewById(R.id.splash_tagline);
        View footer = findViewById(R.id.splash_footer);
        android.widget.ProgressBar pb = findViewById(R.id.splash_progress);
        TextView ver = findViewById(R.id.splash_version);

        ver.setText("Wise IPTV v" + versionName());
        logo.setAlpha(0f);
        logo.setScaleX(0.55f);
        logo.setScaleY(0.55f);
        logo.animate().alpha(1f).scaleX(1f).scaleY(1f)
            .setDuration(700).setInterpolator(new android.view.animation.OvershootInterpolator(1.4f)).start();

        Fx.stagger(450, 160, title, tagline, footer);
        progressAnim = android.animation.ObjectAnimator.ofInt(pb, "progress", 0, 100);
        progressAnim.setDuration(SPLASH_MS);
        progressAnim.setInterpolator(new android.view.animation.DecelerateInterpolator(1.2f));
        progressAnim.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(android.animation.Animator animation) {
                if (!left) goNext();
            }
        });
        progressAnim.setStartDelay(300);
        progressAnim.start();
    }

    private void goNext() {
        if (isFinishing() || isDestroyed()) return;
        startActivity(new Intent(this, ActivationActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private String versionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "2.0.0";
        }
    }

    @Override
    protected void onDestroy() {
        left = true;
        if (progressAnim != null) progressAnim.cancel();
        super.onDestroy();
    }
}
