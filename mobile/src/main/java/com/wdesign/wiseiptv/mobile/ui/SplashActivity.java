package com.wdesign.wiseiptv.mobile.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.mobile.R;

/**
 * SplashActivity — écran de démarrage animé (logo, halo, barre de chargement),
 * puis ouverture de ActivationActivity (qui bascule vers MainActivity si déjà activé).
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_MS = 2000;

    private android.animation.AnimatorSet logoAnim;
    private ObjectAnimator progressAnim;
    private boolean left = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View logo      = findViewById(R.id.splash_logo);
        View title     = findViewById(R.id.splash_title);
        View tagline   = findViewById(R.id.splash_tagline);
        View footer    = findViewById(R.id.splash_footer);
        ProgressBar pb = findViewById(R.id.splash_progress);
        TextView ver   = findViewById(R.id.splash_version);

        ver.setText("Wise IPTV v" + versionName());

        // Logo : zoom + fondu avec léger rebond
        logo.setAlpha(0f);
        logo.setScaleX(0.55f);
        logo.setScaleY(0.55f);
        logo.animate().alpha(1f).scaleX(1f).scaleY(1f)
            .setDuration(700).setInterpolator(new OvershootInterpolator(1.4f)).start();

        // Le logo lui-même s'anime (respiration + léger balancement), plus de cercle en fond
        logo.postDelayed(() -> { if (!left) logoAnim = Fx.logoLife(logo); }, 800);

        Fx.stagger(450, 160, title, tagline, footer);

        // Barre de chargement 0 → 100 %, puis passage à l'activation
        progressAnim = ObjectAnimator.ofInt(pb, "progress", 0, 100);
        progressAnim.setDuration(SPLASH_MS);
        progressAnim.setInterpolator(new DecelerateInterpolator(1.2f));
        progressAnim.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
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
        if (logoAnim != null) logoAnim.cancel();
        if (progressAnim != null) progressAnim.cancel();
        super.onDestroy();
    }
}
