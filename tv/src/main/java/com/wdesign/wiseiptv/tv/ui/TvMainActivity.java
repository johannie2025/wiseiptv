package com.wdesign.wiseiptv.tv.ui;

import android.app.AlertDialog;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.*;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.tv.R;
import com.wdesign.wiseiptv.tv.adapter.TvCardPresenter;
import com.wdesign.wiseiptv.tv.util.ActivationManager;
import com.wdesign.wiseiptv.tv.util.PlaylistLoader;
import java.util.*;
import java.util.concurrent.Executors;

public class TvMainActivity extends FragmentActivity {

    public static final String EXTRA_ACT_LOGIN        = "act_login";
    public static final String EXTRA_ACT_PASSWORD     = "act_password";
    public static final String EXTRA_ACT_EXPIRES      = "act_expires";
    public static final String EXTRA_ACT_DNS_URLS     = "act_dns_urls";
    public static final String EXTRA_ACT_DNS_EPG_URLS = "act_dns_epg_urls";

    private static final String TAG           = "TvMainActivity";
    private static final String PREFS_RECENTS = "wise_tv_recents";
    private static final int    MAX_RECENTS   = 30;

    private BrowseSupportFragment browseFragment;
    private ArrayObjectAdapter    rowsAdapter;
    private AppDatabase           db;
    private ProgressBar           progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_main);
        db = AppDatabase.get(this);
        progressBar = findViewById(R.id.progress_bar);
        setupBrowseFragment();
        observeAllRows();
        startBackgroundSync();
    }

    // ── BrowseFragment ────────────────────────────────────────────────────────

    private void setupBrowseFragment() {
        browseFragment = (BrowseSupportFragment)
            getSupportFragmentManager().findFragmentById(R.id.browse_fragment);
        if (browseFragment == null) {
            browseFragment = new BrowseSupportFragment();
            getSupportFragmentManager().beginTransaction()
                .add(R.id.browse_fragment, browseFragment).commit();
        }

        // Logo mipmap via badge
        browseFragment.setTitle("");
        try {
            browseFragment.setBadgeDrawable(
                getResources().getDrawable(R.mipmap.ic_launcher, getTheme()));
        } catch (Exception e) {
            browseFragment.setTitle("Wise IPTV");
        }

        // Couleurs immersives
        browseFragment.setBrandColor(getResources().getColor(R.color.wise_brand, getTheme()));
        browseFragment.setSearchAffordanceColor(getResources().getColor(R.color.wise_red, getTheme()));

        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        browseFragment.setAdapter(rowsAdapter);

        // FIX CRASH : icône recherche = dialog inline (pas d'Activity externe incompatible)
        browseFragment.setOnSearchClickedListener(v -> showAddPlaylistDialog());

        // Clic chaîne → lecture
        browseFragment.setOnItemViewClickedListener((ivh, item, rvh, row) -> {
            if (item instanceof ChannelEntity) openPlayer((ChannelEntity) item);
            else if (item instanceof ActionItem) ((ActionItem) item).run();
        });
    }

    // ── Ligne d'actions (Ajouter playlist) ───────────────────────────────────

    /**
     * Ligne "Actions" en haut avec un bouton "+ Ajouter une playlist".
     * Utilise un ArrayObjectAdapter<ActionItem> présenté via un Presenter custom.
     */
    private void addActionsRow() {
        ArrayObjectAdapter actionsAdapter = new ArrayObjectAdapter(new ActionPresenter());
        actionsAdapter.add(new ActionItem("➕  Ajouter une playlist", this::showAddPlaylistDialog));
        actionsAdapter.add(new ActionItem("🔄  Synchroniser", this::syncNow));
        ListRow actionsRow = new ListRow(new HeaderItem(0, "Actions"), actionsAdapter);
        // S'assurer qu'elle est en position 0
        if (rowsAdapter.size() == 0 || ((ListRow)rowsAdapter.get(0)).getHeaderItem().getId() != 0) {
            rowsAdapter.add(0, actionsRow);
        } else {
            rowsAdapter.replace(0, actionsRow);
        }
    }

    /** Simple objet action pour la ligne d'actions */
    private static class ActionItem {
        final String label;
        final Runnable action;
        ActionItem(String label, Runnable action) { this.label = label; this.action = action; }
        void run() { action.run(); }
    }

    /** Presenter pour afficher un ActionItem comme bouton focusable */
    private static class ActionPresenter extends Presenter {
        @Override public ViewHolder onCreateViewHolder(android.view.ViewGroup parent) {
            TextView tv = new TextView(parent.getContext());
            tv.setPadding(48, 28, 48, 28);
            tv.setTextSize(16f);
            tv.setTextColor(0xFFFFFFFF);
            tv.setBackgroundResource(android.R.drawable.btn_default_small);
            tv.setBackground(buildBg(parent.getContext()));
            tv.setFocusable(true);
            tv.setFocusableInTouchMode(true);
            tv.setClickable(true);
            android.view.ViewGroup.LayoutParams lp =
                new android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            return new ViewHolder(tv);
        }
        @Override public void onBindViewHolder(ViewHolder vh, Object item) {
            ((TextView) vh.view).setText(((ActionItem) item).label);
        }
        @Override public void onUnbindViewHolder(ViewHolder vh) {}

        private android.graphics.drawable.Drawable buildBg(android.content.Context ctx) {
            android.graphics.drawable.StateListDrawable sl = new android.graphics.drawable.StateListDrawable();
            android.graphics.drawable.GradientDrawable focused = new android.graphics.drawable.GradientDrawable();
            focused.setColor(0xFFE50914); // rouge focus
            focused.setCornerRadius(8f);
            android.graphics.drawable.GradientDrawable normal = new android.graphics.drawable.GradientDrawable();
            normal.setColor(0xFF1565C0); // bleu normal
            normal.setCornerRadius(8f);
            sl.addState(new int[]{android.R.attr.state_focused}, focused);
            sl.addState(new int[]{}, normal);
            return sl;
        }
    }

    // ── Rows LiveData ─────────────────────────────────────────────────────────

    private void observeAllRows() {
        TvCardPresenter p = new TvCardPresenter();

        // Ligne d'actions toujours en tête
        addActionsRow();

        // Live par groupes
        db.channelDao().getLiveGroups().observe(this, groups -> {
            if (groups == null) return;
            removeRowsWithIdRange(1000, 1999);
            int rowId = 1000;
            for (String grp : groups) {
                final int fId = rowId++;
                db.channelDao().getLiveByGroup(grp).observe(this, list -> {
                    if (list != null && !list.isEmpty())
                        upsertRow("📺 " + grp, fId, p, list);
                    else
                        removeRow(fId);
                });
            }
        });

        // Films par groupes
        db.channelDao().getFilmGroups().observe(this, groups -> {
            if (groups == null) return;
            removeRowsWithIdRange(2000, 2999);
            int rowId = 2000;
            for (String grp : groups) {
                final int fId = rowId++;
                db.channelDao().getFilmsByGroup(grp).observe(this, list -> {
                    if (list != null && !list.isEmpty())
                        upsertRow("🎬 " + grp, fId, p, list);
                    else
                        removeRow(fId);
                });
            }
        });

        // Séries par groupes
        db.channelDao().getSeriesGroups().observe(this, groups -> {
            if (groups == null) return;
            removeRowsWithIdRange(3000, 3999);
            int rowId = 3000;
            for (String grp : groups) {
                final int fId = rowId++;
                db.channelDao().getSeriesByGroup(grp).observe(this, list -> {
                    if (list != null && !list.isEmpty())
                        upsertRow("📺 " + grp, fId, p, list);
                    else
                        removeRow(fId);
                });
            }
        });

        // Favoris
        db.channelDao().getFavorites().observe(this, list -> {
            if (list != null && !list.isEmpty()) upsertRow("❤️ Favoris", 9001, p, list);
            else removeRow(9001);
        });

        // Récents via SharedPreferences
        loadRecentsRow();
    }

    // ── Gestion des lignes ────────────────────────────────────────────────────

    private void upsertRow(String title, int id, TvCardPresenter p, List<ChannelEntity> list) {
        ArrayObjectAdapter rowAdapter = new ArrayObjectAdapter(p);
        for (ChannelEntity ch : list) rowAdapter.add(ch);
        ListRow newRow = new ListRow(new HeaderItem(id, title), rowAdapter);
        for (int i = 0; i < rowsAdapter.size(); i++) {
            if (((ListRow) rowsAdapter.get(i)).getHeaderItem().getId() == id) {
                rowsAdapter.replace(i, newRow);
                return;
            }
        }
        int insertAt = rowsAdapter.size();
        for (int i = 0; i < rowsAdapter.size(); i++) {
            long rid = ((ListRow) rowsAdapter.get(i)).getHeaderItem().getId();
            if (rid > id && rid != 0) { insertAt = i; break; }
        }
        rowsAdapter.add(insertAt, newRow);
    }

    private void removeRow(int id) {
        for (int i = 0; i < rowsAdapter.size(); i++) {
            if (((ListRow) rowsAdapter.get(i)).getHeaderItem().getId() == id) {
                rowsAdapter.remove(rowsAdapter.get(i)); return;
            }
        }
    }

    private void removeRowsWithIdRange(int from, int to) {
        for (int i = rowsAdapter.size() - 1; i >= 0; i--) {
            long rid = ((ListRow) rowsAdapter.get(i)).getHeaderItem().getId();
            if (rid >= from && rid <= to) rowsAdapter.remove(rowsAdapter.get(i));
        }
    }

    // ── Récents (SharedPreferences, sans DAO custom) ──────────────────────────

    private void saveRecent(long channelId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_RECENTS, Context.MODE_PRIVATE);
        String raw = prefs.getString("ids", "");
        List<String> ids = new ArrayList<>(
            Arrays.asList(raw.isEmpty() ? new String[0] : raw.split(",")));
        String sid = String.valueOf(channelId);
        ids.remove(sid);
        ids.add(0, sid);
        if (ids.size() > MAX_RECENTS) ids = ids.subList(0, MAX_RECENTS);
        prefs.edit().putString("ids", android.text.TextUtils.join(",", ids)).apply();
        loadRecentsRow();
    }

    private void loadRecentsRow() {
        Executors.newSingleThreadExecutor().execute(() -> {
            SharedPreferences prefs = getSharedPreferences(PREFS_RECENTS, Context.MODE_PRIVATE);
            String raw = prefs.getString("ids", "");
            if (raw.isEmpty()) { runOnUiThread(() -> removeRow(9002)); return; }
            List<ChannelEntity> recents = new ArrayList<>();
            for (String sid : raw.split(",")) {
                try {
                    ChannelEntity ch = db.channelDao().findById(Long.parseLong(sid.trim()));
                    if (ch != null) recents.add(ch);
                } catch (NumberFormatException ignored) {}
            }
            final List<ChannelEntity> fr = recents;
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                if (!fr.isEmpty()) upsertRow("🕐 Récents", 9002, new TvCardPresenter(), fr);
                else removeRow(9002);
            });
        });
    }

    // ── Téléchargement background ─────────────────────────────────────────────

    /**
     * FIX DOWNLOAD :
     * Chemin 1 : extras depuis TvActivationActivity (premier lancement).
     * Chemin 2 : reconstruction depuis prefs "wise_activation_tv" (reboot/rotation).
     *            LIT les dns_urls sauvegardées par TvActivationActivity.saveCache().
     * Chemin 3 : playlists manuelles stale.
     */
    private void startBackgroundSync() {
        Intent intent = getIntent();
        String login    = intent.getStringExtra(EXTRA_ACT_LOGIN);
        String password = intent.getStringExtra(EXTRA_ACT_PASSWORD);
        String expires  = intent.getStringExtra(EXTRA_ACT_EXPIRES);
        String[] dnsUrls    = intent.getStringArrayExtra(EXTRA_ACT_DNS_URLS);
        String[] dnsEpgUrls = intent.getStringArrayExtra(EXTRA_ACT_DNS_EPG_URLS);

        // Chemin 1 : extras frais
        if (login != null && dnsUrls != null && dnsUrls.length > 0) {
            Log.d(TAG, "Sync chemin 1: extras, DNS=" + dnsUrls.length);
            List<DeviceSecurity.DnsEntry> entries = new ArrayList<>();
            for (int i = 0; i < dnsUrls.length; i++) {
                String epg = (dnsEpgUrls != null && i < dnsEpgUrls.length) ? dnsEpgUrls[i] : "";
                entries.add(new DeviceSecurity.DnsEntry(dnsUrls[i], epg, i));
            }
            launchDownload(new DeviceSecurity.ActivationResult(
                DeviceSecurity.getOrCreateKey(this),
                login, password != null ? password : "",
                expires != null ? expires : "", entries));
            return;
        }

        // Chemin 2 : prefs — lire les dns_urls sauvegardés par TvActivationActivity
        SharedPreferences prefs = getSharedPreferences("wise_activation_tv", Context.MODE_PRIVATE);
        String savedStatus = prefs.getString("status",   "UNKNOWN");
        String savedLogin  = prefs.getString("login",    null);
        String savedPass   = prefs.getString("password", "");
        String savedExp    = prefs.getString("expires_at", "");
        String savedDnsRaw = prefs.getString("dns_urls",   "");

        if ("ACTIVE".equals(savedStatus) && savedLogin != null && !savedDnsRaw.isEmpty()) {
            Log.d(TAG, "Sync chemin 2: prefs, dns=" + savedDnsRaw);
            String[] urls = savedDnsRaw.split(",");
            String[] epgs = prefs.getString("dns_epg_urls", "").split(",");
            List<DeviceSecurity.DnsEntry> entries = new ArrayList<>();
            for (int i = 0; i < urls.length; i++) {
                if (urls[i].trim().isEmpty()) continue;
                String epg = i < epgs.length ? epgs[i].trim() : "";
                entries.add(new DeviceSecurity.DnsEntry(urls[i].trim(), epg, i));
            }
            if (!entries.isEmpty()) {
                launchDownload(new DeviceSecurity.ActivationResult(
                    DeviceSecurity.getOrCreateKey(this),
                    savedLogin, savedPass, savedExp, entries));
                return;
            }
        }

        // Chemin 3 : playlists manuelles stale
        Log.d(TAG, "Sync chemin 3: playlists stale");
        PlaylistLoader.refreshStaleIfNeeded(db, new PlaylistLoader.Callback() {
            @Override public void onDone(int count) {
                runOnUiThread(() -> Toast.makeText(TvMainActivity.this,
                    "✅ " + count + " chaînes", Toast.LENGTH_SHORT).show());
            }
            @Override public void onError(String msg) {
                android.util.Log.w(TAG, "refreshStale: " + msg);
            }
        });
    }

    private void launchDownload(DeviceSecurity.ActivationResult result) {
        showProgress(true);
        ActivationManager.upsertAndDownloadAll(
            getApplicationContext(), db, result,
            new ActivationManager.DownloadCallback() {
                @Override public void onProgress(String name) {
                    Log.d(TAG, "⬇ " + name);
                }
                @Override public void onDone(int total) {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        showProgress(false);
                        Toast.makeText(TvMainActivity.this,
                            "✅ " + total + " chaînes chargées", Toast.LENGTH_SHORT).show();
                    });
                }
                @Override public void onError(String msg) {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        showProgress(false);
                        Log.w(TAG, "Download non-fatal: " + msg);
                        // Proposer ajout manuel si aucune chaîne
                        Executors.newSingleThreadExecutor().execute(() -> {
                            if (db.channelDao().count() == 0)
                                runOnUiThread(() -> showAddPlaylistDialog());
                        });
                    });
                }
            });
    }

    private void showProgress(boolean show) {
        if (progressBar != null)
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // ── Dialog ajout playlist (inline, pas d'Activity externe) ───────────────

    private void showAddPlaylistDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog);
        b.setTitle("Ajouter une playlist");

        // Layout simple inline
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 16);

        RadioGroup rgType = new RadioGroup(this);
        rgType.setOrientation(RadioGroup.HORIZONTAL);
        RadioButton rbUrl   = new RadioButton(this); rbUrl.setText("URL M3U");   rbUrl.setId(1);
        RadioButton rbXt    = new RadioButton(this); rbXt.setText("Xtream");     rbXt.setId(2);
        rbUrl.setChecked(true);
        rgType.addView(rbUrl); rgType.addView(rbXt);
        layout.addView(rgType);

        EditText etUrl  = new EditText(this); etUrl.setHint("URL M3U : http://...");
        EditText etName = new EditText(this); etName.setHint("Nom (optionnel)");
        EditText etSrv  = new EditText(this); etSrv.setHint("Serveur Xtream"); etSrv.setVisibility(View.GONE);
        EditText etUser = new EditText(this); etUser.setHint("Login Xtream");  etUser.setVisibility(View.GONE);
        EditText etPass = new EditText(this); etPass.setHint("Mot de passe");  etPass.setVisibility(View.GONE);
        etPass.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
            android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        layout.addView(etName); layout.addView(etUrl);
        layout.addView(etSrv); layout.addView(etUser); layout.addView(etPass);

        rgType.setOnCheckedChangeListener((g, id) -> {
            boolean isXt = id == 2;
            etUrl.setVisibility(isXt ? View.GONE : View.VISIBLE);
            etSrv.setVisibility(isXt ? View.VISIBLE : View.GONE);
            etUser.setVisibility(isXt ? View.VISIBLE : View.GONE);
            etPass.setVisibility(isXt ? View.VISIBLE : View.GONE);
        });

        b.setView(layout);
        b.setPositiveButton("Charger", (d, w) -> {
            String name = etName.getText().toString().trim();
            int cid = rgType.getCheckedRadioButtonId();
            PlaylistEntity pl = new PlaylistEntity();
            pl.name = name.isEmpty() ? "Playlist" : name;
            pl.lastUpdated = 0;
            pl.isActive = true;
            if (cid == 2) {
                pl.type     = PlaylistEntity.TYPE_XTREAM;
                pl.url      = etSrv.getText().toString().trim();
                pl.username = etUser.getText().toString().trim();
                pl.password = etPass.getText().toString().trim();
                if (pl.url.isEmpty() || pl.username.isEmpty()) return;
            } else {
                pl.type = PlaylistEntity.TYPE_M3U_URL;
                pl.url  = etUrl.getText().toString().trim();
                if (pl.url.isEmpty()) return;
            }
            showProgress(true);
            Executors.newSingleThreadExecutor().execute(() -> {
                pl.id = db.playlistDao().insert(pl);
                PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                    @Override public void onDone(int count) {
                        runOnUiThread(() -> {
                            showProgress(false);
                            Toast.makeText(TvMainActivity.this,
                                count + " chaînes chargées ✅", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override public void onError(String msg) {
                        runOnUiThread(() -> {
                            showProgress(false);
                            Toast.makeText(TvMainActivity.this,
                                "Erreur : " + msg, Toast.LENGTH_LONG).show();
                        });
                    }
                });
            });
        });
        b.setNegativeButton("Annuler", null);
        b.show();
    }

    // ── Sync manuelle ─────────────────────────────────────────────────────────

    private void syncNow() {
        SharedPreferences prefs = getSharedPreferences("wise_activation_tv", Context.MODE_PRIVATE);
        String savedDnsRaw = prefs.getString("dns_urls", "");
        String savedLogin  = prefs.getString("login", null);
        String savedPass   = prefs.getString("password", "");
        String savedExp    = prefs.getString("expires_at", "");
        if (savedLogin != null && !savedDnsRaw.isEmpty()) {
            String[] urls = savedDnsRaw.split(",");
            String[] epgs = prefs.getString("dns_epg_urls", "").split(",");
            List<DeviceSecurity.DnsEntry> entries = new ArrayList<>();
            for (int i = 0; i < urls.length; i++) {
                if (urls[i].trim().isEmpty()) continue;
                entries.add(new DeviceSecurity.DnsEntry(urls[i].trim(),
                    i < epgs.length ? epgs[i].trim() : "", i));
            }
            if (!entries.isEmpty()) {
                launchDownload(new DeviceSecurity.ActivationResult(
                    DeviceSecurity.getOrCreateKey(this), savedLogin, savedPass, savedExp, entries));
                return;
            }
        }
        Toast.makeText(this, "Aucun abonnement actif", Toast.LENGTH_SHORT).show();
    }

    // ── Player ────────────────────────────────────────────────────────────────

    private void openPlayer(ChannelEntity ch) {
        saveRecent(ch.id);
        Intent i = new Intent(this, TvPlayerActivity.class);
        i.putExtra(TvPlayerActivity.EXTRA_ID,   ch.id);
        i.putExtra(TvPlayerActivity.EXTRA_NAME, ch.name);
        i.putExtra(TvPlayerActivity.EXTRA_URL,  ch.streamUrl);
        startActivity(i);
    }
}
