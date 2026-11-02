package com.wdesign.wiseiptv.core.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

/**
 * Fx — petits effets d'animation partagés (mobile + TV).
 * Uniquement des animations natives (aucune dépendance externe) → compatible GitHub Actions.
 */
public final class Fx {
    private Fx() {}

    private static float dp(View v, float value) {
        return value * v.getResources().getDisplayMetrics().density;
    }

    /** Entrée : fondu + glissement vers le haut. */
    public static void enter(View v, long delayMs) {
        if (v == null) return;
        if (!AppSettings.animations(v.getContext())) { v.setAlpha(1f); v.setTranslationY(0f); return; }
        v.animate().cancel();
        v.setAlpha(0f);
        v.setTranslationY(dp(v, 22));
        v.animate().alpha(1f).translationY(0f)
            .setStartDelay(delayMs).setDuration(420)
            .setInterpolator(new DecelerateInterpolator(1.6f)).start();
    }

    /** Entrée en cascade de plusieurs vues. */
    public static void stagger(long startDelayMs, long stepMs, View... views) {
        long d = startDelayMs;
        for (View v : views) {
            if (v != null) enter(v, d);
            d += stepMs;
        }
    }

    /** Apparition "pop" (échelle + fondu), pour les cartes qui s'affichent après un état. */
    public static void pop(View v) {
        if (v == null) return;
        if (!AppSettings.animations(v.getContext())) return;
        v.animate().cancel();
        v.setAlpha(0f);
        v.setScaleX(0.92f);
        v.setScaleY(0.92f);
        v.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(320)
            .setInterpolator(new OvershootInterpolator(1.2f)).start();
    }

    /** Pulsation infinie (halo, statut). À annuler avec animator.cancel() dans onDestroy. */
    public static ObjectAnimator pulse(View v, float toScale, long durationMs) {
        if (v == null) return null;
        android.animation.PropertyValuesHolder sx =
            android.animation.PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, toScale);
        android.animation.PropertyValuesHolder sy =
            android.animation.PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, toScale);
        android.animation.PropertyValuesHolder a =
            android.animation.PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0.75f);
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(v, sx, sy, a);
        anim.setDuration(durationMs);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
        return anim;
    }

    /** Effet de pression tactile (mobile) : réduit légèrement la vue pendant le doigt posé. */
    public static void pressable(View v) {
        if (v == null) return;
        v.setOnTouchListener((view, e) -> {
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    view.animate().scaleX(0.95f).scaleY(0.95f).setDuration(90).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    view.animate().scaleX(1f).scaleY(1f).setDuration(160)
                        .setInterpolator(new OvershootInterpolator(2f)).start();
                    break;
                default: break;
            }
            return false; // laisse passer le clic
        });
    }

    /** Zoom au focus (TV / télécommande). */
    public static void focusZoom(View v, float scale) {
        if (v == null) return;
        v.setOnFocusChangeListener((view, hasFocus) ->
            view.animate().scaleX(hasFocus ? scale : 1f).scaleY(hasFocus ? scale : 1f)
                .setDuration(160).setInterpolator(new DecelerateInterpolator()).start());
    }

    /** Compteur animé 0 → valeur. */
    public static void countUp(TextView tv, int target, long durationMs) {
        if (tv == null) return;
        ValueAnimator va = ValueAnimator.ofInt(0, Math.max(0, target));
        va.setDuration(durationMs);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(a -> tv.setText(String.valueOf((int) a.getAnimatedValue())));
        va.start();
    }

    /** Barre de progression animée. */
    public static void progressTo(android.widget.ProgressBar pb, int value, long durationMs) {
        if (pb == null) return;
        ObjectAnimator a = ObjectAnimator.ofInt(pb, "progress", 0, value);
        a.setDuration(durationMs);
        a.setInterpolator(new DecelerateInterpolator());
        a.start();
    }

    /** Animation du logo lui-même (respiration + léger balancement), sans halo. À annuler dans onDestroy. */
    public static android.animation.AnimatorSet logoLife(View logo) {
        if (logo == null) return null;
        android.animation.PropertyValuesHolder sx = android.animation.PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.07f);
        android.animation.PropertyValuesHolder sy = android.animation.PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.07f);
        ObjectAnimator breathe = ObjectAnimator.ofPropertyValuesHolder(logo, sx, sy);
        breathe.setDuration(1100);
        breathe.setRepeatCount(ValueAnimator.INFINITE);
        breathe.setRepeatMode(ValueAnimator.REVERSE);
        breathe.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator sway = ObjectAnimator.ofFloat(logo, View.ROTATION, -3f, 3f);
        sway.setDuration(1700);
        sway.setRepeatCount(ValueAnimator.INFINITE);
        sway.setRepeatMode(ValueAnimator.REVERSE);
        sway.setInterpolator(new AccelerateDecelerateInterpolator());
        android.animation.AnimatorSet set = new android.animation.AnimatorSet();
        set.playTogether(breathe, sway);
        set.start();
        return set;
    }
}
