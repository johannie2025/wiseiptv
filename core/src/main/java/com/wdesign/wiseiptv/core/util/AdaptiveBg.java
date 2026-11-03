package com.wdesign.wiseiptv.core.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Fond adaptatif (comme dans VLC) : la couleur dominante du logo de la chaîne sélectionnée
 * teinte le fond de l'écran avec un dégradé animé.
 */
public final class AdaptiveBg {
    private AdaptiveBg() {}

    private static final int BASE_TOP = 0xFF0C1E42, BASE_BOTTOM = 0xFF040814;
    private static final Map<View, String> LAST = new WeakHashMap<>();
    private static final Map<View, int[]> CUR = new WeakHashMap<>();

    public static void apply(final View target, final String logoUrl) {
        if (target == null) return;
        if (logoUrl == null || logoUrl.isEmpty()) { reset(target); return; }
        LAST.put(target, logoUrl);
        Context app = target.getContext().getApplicationContext();
        Glide.with(app).asBitmap().load(logoUrl).override(48, 48).into(new CustomTarget<Bitmap>() {
            @Override public void onResourceReady(@NonNull Bitmap b, @Nullable Transition<? super Bitmap> t) {
                if (!logoUrl.equals(LAST.get(target))) return;
                int[] c = colors(b);
                animate(target, c[0], c[1]);
            }
            @Override public void onLoadCleared(@Nullable Drawable d) { }
            @Override public void onLoadFailed(@Nullable Drawable d) {
                if (logoUrl.equals(LAST.get(target))) animate(target, BASE_TOP, BASE_BOTTOM);
            }
        });
    }

    public static void reset(View target) {
        if (target == null) return;
        LAST.remove(target);
        animate(target, BASE_TOP, BASE_BOTTOM);
    }

    private static int[] colors(Bitmap b) {
        float[] hsv = new float[3];
        double sr = 0, sg = 0, sb = 0, sw = 0;
        for (int y = 0; y < b.getHeight(); y++) {
            for (int x = 0; x < b.getWidth(); x++) {
                int px = b.getPixel(x, y);
                if (Color.alpha(px) < 128) continue;
                int r = Color.red(px), g = Color.green(px), bl = Color.blue(px);
                Color.RGBToHSV(r, g, bl, hsv);
                if (hsv[2] < 0.12f) continue;
                double w = 0.04 + hsv[1] * hsv[1] * hsv[2];
                sr += r * w; sg += g * w; sb += bl * w; sw += w;
            }
        }
        if (sw <= 0) return new int[]{BASE_TOP, BASE_BOTTOM};
        Color.RGBToHSV((int) (sr / sw), (int) (sg / sw), (int) (sb / sw), hsv);
        if (hsv[1] < 0.14f) return new int[]{BASE_TOP, BASE_BOTTOM};   // logo blanc / gris
        float h = hsv[0], s = Math.min(1f, hsv[1] * 0.9f);
        int top = Color.HSVToColor(new float[]{h, s, 0.46f});
        int bottom = Color.HSVToColor(new float[]{h, Math.min(1f, s * 0.95f), 0.08f});
        return new int[]{top, bottom};
    }

    private static void animate(final View target, final int top, final int bottom) {
        final int[] from = CUR.containsKey(target) ? CUR.get(target) : new int[]{BASE_TOP, BASE_BOTTOM};
        final int[] to = {top, bottom};
        CUR.put(target, to);
        final ArgbEvaluator ev = new ArgbEvaluator();
        final GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
            new int[]{from[0], from[1]});
        target.setBackground(gd);
        ValueAnimator va = ValueAnimator.ofFloat(0f, 1f);
        va.setDuration(450);
        va.addUpdateListener(a -> {
            float f = (float) a.getAnimatedValue();
            gd.setColors(new int[]{(Integer) ev.evaluate(f, from[0], to[0]),
                (Integer) ev.evaluate(f, from[1], to[1])});
        });
        va.start();
    }
}
