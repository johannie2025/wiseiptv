package com.wdesign.wiseiptv.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wdesign.wiseiptv.mobile.R;

/** Navigation commune (barre du bas) pour les écrans Compte / Paramètres / À propos. */
final class NavHelper {
    private NavHelper() {}

    static void bind(Activity a, BottomNavigationView nav, int selectedId) {
        bind(a, nav, selectedId, false);
    }

    /** reselectOpens=true : un tap sur l'onglet déjà actif ouvre quand même son écran (sous-écrans). */
    static void bind(final Activity a, BottomNavigationView nav, final int selectedId,
                     final boolean reselectOpens) {
        if (nav == null) return;
        Menu m = nav.getMenu();
        MenuItem current = m.findItem(selectedId);
        if (current != null) current.setChecked(true);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedId && !reselectOpens) return true;
            Intent i;
            if (id == R.id.nav_account) {
                i = new Intent(a, AccountActivity.class);
            } else if (id == R.id.nav_settings) {
                i = new Intent(a, SettingsActivity.class);
            } else {
                i = new Intent(a, MainActivity.class);
                i.putExtra(MainActivity.EXTRA_NAV, id);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
            a.startActivity(i);
            a.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            a.finish();
            return true;
        });
    }
}
