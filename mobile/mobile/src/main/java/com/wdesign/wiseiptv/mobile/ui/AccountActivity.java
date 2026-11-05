package com.wdesign.wiseiptv.mobile.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.core.util.SubscriptionInfo;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.core.util.DataUsage;
import com.wdesign.wiseiptv.core.util.SubscriptionHistory;
import com.wdesign.wiseiptv.mobile.util.ResumeStore;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

/** Mon Compte : profil, abonnement, statistiques, clé appareil, alerte d'expiration. */
public class AccountActivity extends AppCompatActivity {

    private static final int ALERT_DAYS = 5;
    private android.animation.ObjectAnimator renewPulse;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_account);
        NavHelper.bind(this, (BottomNavigationView) findViewById(R.id.bottom_nav), R.id.nav_account);

        final String key = DeviceSecurity.getOrCreateKey(this);
        ((TextView) findViewById(R.id.tv_acc_key)).setText(key);
        findViewById(R.id.btn_acc_copy).setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) cm.setPrimaryClip(ClipData.newPlainText("device_key", key));
            Toast.makeText(this, "Clé copiée !", Toast.LENGTH_SHORT).show();
        });

        SubscriptionInfo si = SubscriptionInfo.read(this, SubscriptionInfo.PREFS_MOBILE);
        // Abonnements déjà actifs avant cette version : on enregistre la date en cache
        SubscriptionHistory.record(this, getSharedPreferences(SubscriptionInfo.PREFS_MOBILE, MODE_PRIVATE)
            .getString("expires_at", ""));
        bindSubscription(si, key);
        bindHistory();
        bindDataUsage();
        findViewById(R.id.btn_data_reset).setOnClickListener(v ->
            new android.app.AlertDialog.Builder(this).setTitle("Réinitialiser les statistiques ?")
                .setMessage("Le compteur de consommation de données sera remis à zéro.")
                .setPositiveButton("Réinitialiser", (d, w) -> { DataUsage.reset(this); bindDataUsage(); })
                .setNegativeButton("Annuler", null).show());
        bindHelp(key);
        loadStats();

        Fx.stagger(40, 70,
            findViewById(R.id.acc_title), findViewById(R.id.banner_expiry),
            findViewById(R.id.card_profile), findViewById(R.id.card_sub),
            findViewById(R.id.stats_row), findViewById(R.id.card_history), findViewById(R.id.card_data),
            findViewById(R.id.card_key), findViewById(R.id.row_help));
        Fx.pressable(findViewById(R.id.btn_renew));
        Fx.pressable(findViewById(R.id.row_help));
    }

    private void bindSubscription(SubscriptionInfo si, String key) {
        TextView tvName   = findViewById(R.id.tv_name);
        TextView tvAvatar = findViewById(R.id.tv_avatar);
        TextView badge    = findViewById(R.id.tv_status_badge);
        TextView status   = findViewById(R.id.tv_acc_status);
        TextView expiry   = findViewById(R.id.tv_acc_expiry);
        TextView days     = findViewById(R.id.tv_acc_days);
        ImageView state   = findViewById(R.id.img_sub_state);
        ProgressBar bar   = findViewById(R.id.acc_progress);
        View banner       = findViewById(R.id.banner_expiry);

        if (si == null) {
            status.setText("Aucun abonnement actif");
            state.setImageResource(R.drawable.ic_warning);
            state.setColorFilter(0xFFFFD54F);
            expiry.setText("Communiquez votre Device Key à votre revendeur.");
            days.setVisibility(View.GONE);
            bar.setVisibility(View.GONE);
            badge.setText("Non activé");
            return;
        }

        String name = si.displayName();
        tvName.setText(name.isEmpty() ? "Appareil Wise IPTV" : name);
        tvAvatar.setText(si.initial());
        expiry.setText("Expire le : " + si.formattedExpiry());

        if (si.active) {
            badge.setText("Actif");
            badge.setBackgroundResource(R.drawable.bg_badge_green);
            status.setText("Abonnement Actif");
            days.setText("Jours restants : " + si.daysLeft + (si.daysLeft > 1 ? " jours" : " jour"));
            Fx.progressTo(bar, si.remainingPercent(), 900);
        } else {
            badge.setText("Expiré");
            badge.setBackgroundResource(R.drawable.bg_badge_live);
            status.setText("Abonnement expiré");
            state.setImageResource(R.drawable.ic_warning);
            state.setColorFilter(0xFFFFD54F);
            days.setText("Jours restants : 0");
            bar.setProgress(0);
        }

        if (si.isExpiringSoon(ALERT_DAYS)) {
            banner.setVisibility(View.VISIBLE);
            TextView t = findViewById(R.id.tv_alert_text);
            t.setText(si.active
                ? "Votre abonnement expire dans " + si.daysLeft + (si.daysLeft > 1 ? " jours !" : " jour !")
                : "Votre abonnement a expiré !");
            renewPulse = Fx.pulse(findViewById(R.id.btn_renew), 1.03f, 900);
            findViewById(R.id.btn_renew).setOnClickListener(v -> openWhatsApp(
                "Bonjour, je souhaite renouveler mon abonnement Wise IPTV. Device Key : " + key));
        }
    }

    private void bindHistory() {
        android.widget.LinearLayout box = findViewById(R.id.history_container);
        box.removeAllViews();
        List<SubscriptionHistory.Entry> list = SubscriptionHistory.list(this);
        findViewById(R.id.history_empty).setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        for (SubscriptionHistory.Entry e : list) {
            View row = getLayoutInflater().inflate(R.layout.item_history, box, false);
            ((TextView) row.findViewById(R.id.h_title)).setText(e.title());
            ((TextView) row.findViewById(R.id.h_range)).setText(e.range());
            TextView badge = row.findViewById(R.id.h_badge);
            boolean act = e.active();
            badge.setText(act ? "Actif" : "Expiré");
            badge.setBackgroundResource(act ? R.drawable.bg_badge_green : R.drawable.bg_badge_live);
            row.findViewById(R.id.h_dot).setBackgroundResource(act ? R.drawable.dot_on : R.drawable.dot_off);
            box.addView(row);
        }
    }

    private void bindDataUsage() {
        final android.content.Context ctx = getApplicationContext();
        Executors.newSingleThreadExecutor().execute(() -> {
            final long today = DataUsage.forDay(ctx, 0), week = DataUsage.lastDays(ctx, 7),
                month = DataUsage.thisMonth(ctx), total = DataUsage.total(ctx);
            final long[] days = new long[7];
            for (int i = 0; i < 7; i++) days[6 - i] = DataUsage.forDay(ctx, i);
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                ((TextView) findViewById(R.id.data_today)).setText(DataUsage.format(today));
                ((TextView) findViewById(R.id.data_week)).setText(DataUsage.format(week));
                ((TextView) findViewById(R.id.data_month)).setText(DataUsage.format(month));
                ((TextView) findViewById(R.id.data_total)).setText(DataUsage.format(total));
                drawChart(days);
            });
        });
    }

    /** Histogramme des 7 derniers jours (barres simples, sans dépendance). */
    private void drawChart(long[] days) {
        android.widget.LinearLayout chart = findViewById(R.id.data_chart);
        android.widget.LinearLayout labels = findViewById(R.id.data_chart_labels);
        chart.removeAllViews(); labels.removeAllViews();
        long max = 1;
        for (long d : days) max = Math.max(max, d);
        float density = getResources().getDisplayMetrics().density;
        String[] names = {"D", "L", "M", "M", "J", "V", "S"};
        for (int i = 0; i < 7; i++) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, -(6 - i));
            android.widget.LinearLayout col = new android.widget.LinearLayout(this);
            col.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            col.setGravity(android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL);
            View bar = new View(this);
            int h = days[i] <= 0 ? Math.round(3 * density)
                : Math.max(Math.round(4 * density), Math.round(80 * density * days[i] / max));
            android.widget.LinearLayout.LayoutParams lp =
                new android.widget.LinearLayout.LayoutParams(Math.round(14 * density), h);
            bar.setLayoutParams(lp);
            bar.setBackgroundColor(i == 6 ? 0xFF4DA3FF : 0xFF2A4C8F);
            col.addView(bar);
            chart.addView(col);
            TextView l = new TextView(this);
            l.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            l.setGravity(android.view.Gravity.CENTER);
            l.setTextSize(10); l.setTextColor(0xFF8FA6CC);
            l.setText(names[c.get(Calendar.DAY_OF_WEEK) - 1]);
            labels.addView(l);
        }
    }

    @Override protected void onResume() {
        super.onResume();
        ResumeStore.setScreen(this, ResumeStore.SCREEN_ACCOUNT);
        bindDataUsage();
    }

    private void bindHelp(String key) {
        View row = findViewById(R.id.row_help);
        ((ImageView) row.findViewById(R.id.row_icon)).setImageResource(R.drawable.ic_chat);
        ((TextView) row.findViewById(R.id.row_title)).setText("Besoin d'aide ?");
        ((TextView) row.findViewById(R.id.row_sub)).setText("Contactez notre support WhatsApp");
        row.setOnClickListener(v -> openWhatsApp("Bonjour, j'ai besoin d'aide avec Wise IPTV. Device Key : " + key));
    }

    private void loadStats() {
        final AppDatabase db = AppDatabase.get(this);
        Executors.newSingleThreadExecutor().execute(() -> {
            final int live   = db.channelDao().countByType(ChannelEntity.TYPE_LIVE);
            final int films  = db.channelDao().countByType(ChannelEntity.TYPE_FILM);
            final int series = db.channelDao().countByType(ChannelEntity.TYPE_SERIES);
            final int favs   = db.channelDao().countFavorites();
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                Fx.countUp((TextView) findViewById(R.id.stat_live),   live,   800);
                Fx.countUp((TextView) findViewById(R.id.stat_films),  films,  800);
                Fx.countUp((TextView) findViewById(R.id.stat_series), series, 800);
                Fx.countUp((TextView) findViewById(R.id.stat_favs),   favs,   800);
            });
        });
    }

    @Override protected void onDestroy() {
        if (renewPulse != null) renewPulse.cancel();
        super.onDestroy();
    }

    private void openWhatsApp(String message) {
        try {
            Uri u = Uri.parse("https://wa.me/" + SubscriptionInfo.WHATSAPP
                + "?text=" + Uri.encode(message));
            startActivity(new Intent(Intent.ACTION_VIEW, u));
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp indisponible", Toast.LENGTH_SHORT).show();
        }
    }
}
