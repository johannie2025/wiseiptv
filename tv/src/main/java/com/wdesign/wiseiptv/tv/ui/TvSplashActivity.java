package com.wdesign.wiseiptv.tv.ui;

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
import androidx.fragment.app.FragmentActivity;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.tv.R;

/** Splash TV animé → TvActivationActivity (qui bascule vers TvMainActivity si déjà activé). */
public class TvSplashActivity extends FragmentActivity {

    private ObjectAnimator glowPulse, progressAnim;
    private boolean left = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_splash);

        View logo = findViewById(R.id.splash_logo);
        View glow = findViewById(R.id.splash_glow);
        ProgressBar pb = findViewById(R.id.splash_progress);
        TextView ver = findViewById(R.id.splash_version);
        ver.setText("Wise IPTV v" + versionName());

        logo.setAlpha(0f); logo.setScaleX(0.55f); logo.setScaleY(0.55f);
        logo.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(750)
            .setInterpolator(new OvershootInterpolator(1.4f)).start();
        glow.setAlpha(0f);
        glow.animate().alpha(1f).setDuration(900).start();
        glowPulse = Fx.pulse(glow, 1.1f, 1500);
        Fx.stagger(450, 160, findViewById(R.id.splash_title),
            findViewById(R.id.splash_tagline), findViewById(R.id.splash_footer));

        progressAnim = ObjectAnimator.ofInt(pb, "progress", 0, 100);
        progressAnim.setDuration(2000);
        progressAnim.setStartDelay(300);
        progressAnim.setInterpolator(new DecelerateInterpolator(1.2f));
        progressAnim.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator a) { if (!left) goNext(); }
        });
        progressAnim.start();
    }

    private void goNext() {
        if (isFinishing() || isDestroyed()) return;
        startActivity(new Intent(this, TvActivationActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private String versionName() {
        try { return getPackageManager().getPackageInfo(getPackageName(), 0).versionName; }
        catch (Exception e) { return "2.0.0"; }
    }

    @Override protected void onDestroy() {
        left = true;
        if (glowPulse != null) glowPulse.cancel();
        if (progressAnim != null) progressAnim.cancel();
        super.onDestroy();
    }
}
