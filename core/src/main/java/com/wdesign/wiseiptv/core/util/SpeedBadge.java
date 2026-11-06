package com.wdesign.wiseiptv.core.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.TrafficStats;
import android.os.Process;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.Locale;

/** Petit badge « ↓ 2,4 Mb/s » : débit entrant de l'app, rafraîchi chaque seconde. */
public class SpeedBadge extends TextView {
    private long lastBytes = -1, lastTime;
    private final Runnable tick = new Runnable() {
        @Override public void run() {
            long now = SystemClock.elapsedRealtime();
            long bytes = TrafficStats.getUidRxBytes(Process.myUid());
            if (bytes == TrafficStats.UNSUPPORTED) bytes = TrafficStats.getTotalRxBytes();
            if (lastBytes >= 0 && now > lastTime && bytes >= lastBytes) {
                double bps = (bytes - lastBytes) * 8000.0 / (now - lastTime);
                setText("↓ " + format(bps));
            }
            lastBytes = bytes; lastTime = now;
            postDelayed(this, 1000);
        }
    };

    public SpeedBadge(Context c) {
        super(c);
        setTextColor(Color.WHITE);
        setTextSize(11);
        setTypeface(getTypeface(), android.graphics.Typeface.BOLD);
        setText("↓ 0 kb/s");
        float d = c.getResources().getDisplayMetrics().density;
        setPadding((int) (8 * d), (int) (3 * d), (int) (8 * d), (int) (3 * d));
        GradientDrawable g = new GradientDrawable();
        g.setColor(0x66000000);
        g.setCornerRadius(12 * d);
        setBackground(g);
        setFocusable(false);
    }

    @Override protected void onAttachedToWindow() { super.onAttachedToWindow(); lastBytes = -1; post(tick); }
    @Override protected void onDetachedFromWindow() { removeCallbacks(tick); super.onDetachedFromWindow(); }

    static String format(double bps) {
        if (bps >= 1_000_000) return String.format(Locale.getDefault(), "%.1f Mb/s", bps / 1_000_000);
        return String.format(Locale.getDefault(), "%.0f kb/s", bps / 1000);
    }

    /** Ajoute le badge dans un coin de l'écran de l'activité. */
    public static SpeedBadge attach(Activity a, int gravity, int marginStartEndDp, int marginTopBottomDp) {
        ViewGroup root = a.findViewById(android.R.id.content);
        SpeedBadge b = new SpeedBadge(a);
        float d = a.getResources().getDisplayMetrics().density;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, gravity);
        int mh = (int) (marginStartEndDp * d), mv = (int) (marginTopBottomDp * d);
        lp.setMargins(mh, mv, mh, mv);
        b.setLayoutParams(lp);
        b.setElevation(20 * d);
        root.addView(b);
        return b;
    }
}
