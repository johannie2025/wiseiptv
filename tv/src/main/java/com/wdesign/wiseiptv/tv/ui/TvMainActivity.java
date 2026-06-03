package com.wdesign.wiseiptv.tv.ui;

import android.content.*;
import android.os.*;
import android.util.Log;
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

/**
 * TvMainActivity — BrowseSupportFragment Leanback.
 *
 * CORRECTIONS :
 *  1. Logo mipmap au lieu du texte "WISEIPTVTV"
 *  2. Icône recherche → ouvre PlaylistManagerActivity (ajouter playlist)
 *  3. Grille multi-directionnelle via VerticalGridSupportFragment par onglet
 *  4. Groupes de chaînes en lignes distinctes
 *  5. Favoris et Récents gérés (Room LiveData)
 *  6. Téléchargement playlist en background non bloquant (depuis extras Activation)
 */
public class TvMainActivity extends FragmentActivity {

    public static final String EXTRA_ACT_LOGIN        = "act_login";
    public static final String EXTRA_ACT_PASSWORD     = "act_password";
    public static final String EXTRA_ACT_EXPIRES      = "act_expires";
    public static final String EXTRA_ACT_DNS_URLS     = "act_dns_urls";
    public static final String EXTRA_ACT_DNS_EPG_URLS = "act_dns_epg_urls";

    private static final String TAG = "TvMainActivity";

    private BrowseSupportFragment browseFragment;
    private ArrayObjectAdapter    rowsAdapter;
    private AppDatabase           db;

    // Observers LiveData — un seul actif par catégorie pour éviter l'accumulation
    private final Map<Integer, androidx.lifecycle.Observer<List<ChannelEntity>>> observers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_main);
        db = AppDatabase.get(this);
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

        // FIX 1 : logo mipmap
        browseFragment.setTitle(""); // texte vide — on met le logo via badge
        try {
            browseFragment.setBadgeDrawable(getResources().getDrawable(R.mipmap.ic_launcher, null));
        } catch (Exception e) {
            browseFragment.setTitle("Wise IPTV");
        }

        browseFragment.setBrandColor(getResources().getColor(R.color.wise_blue_dark, null));
        browseFragment.setSearchAffordanceColor(getResources().getColor(R.color.wise_red, null));

        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        browseFragment.setAdapter(rowsAdapter);

        // FIX 2 : icône recherche → ouvre PlaylistManager (ajouter/gérer playlists)
        browseFragment.setOnSearchClickedListener(v ->
            startActivity(new Intent(this, PlaylistManagerActivity.class))
        );

        // Clic sur une chaîne → lecture
        browseFragment.setOnItemViewClickedListener((ivh, item, rvh, row) -> {
            if (item instanceof ChannelEntity) openPlayer((ChannelEntity) item);
        });
    }

    // ── Rows LiveData — groupes + catégories ─────────────────────────────────

    /**
     * FIX 3+4 : chaque groupe de chaînes devient une ligne horizontale.
     * Navigation D-Pad : gauche/droite dans la ligne, haut/bas entre les lignes.
     * Structure : [Live - Groupe1] [Live - Groupe2] … [Films] [Séries] [Favoris] [Récents]
     */
    private void observeAllRows() {
        TvCardPresenter p = new TvCardPresenter();
        rowsAdapter.clear();

        // ── Live par groupes ──────────────────────────────────────────────────
        db.channelDao().getLiveGroups().observe(this, groups -> {
            if (groups == null) return;
            // Supprimer les anciennes lignes Live (id 1000+)
            removeRowsWithIdRange(1000, 1999);
            int rowId = 1000;
            for (String grp : groups) {
                final int fId = rowId++;
                final String fGrp = grp;
                db.channelDao().getLiveByGroup(grp).observe(this, list -> {
                    if (list != null && !list.isEmpty())
                        upsertRow("📺 " + fGrp, fId, p, list);
                    else
                        removeRow(fId);
                });
            }
        });

        // ── Films par groupes ─────────────────────────────────────────────────
        db.channelDao().getFilmGroups().observe(this, groups -> {
            if (groups == null) return;
            removeRowsWithIdRange(2000, 2999);
            int rowId = 2000;
            for (String grp : groups) {
                final int fId = rowId++;
                final String fGrp = grp;
                db.channelDao().getFilmsByGroup(grp).observe(this, list -> {
                    if (list != null && !list.isEmpty())
                        upsertRow("🎬 " + fGrp, fId, p, list);
                    else
                        removeRow(fId);
                });
            }
        });

        // ── Séries par groupes ────────────────────────────────────────────────
        db.channelDao().getSeriesGroups().observe(this, groups -> {
            if (groups == null) return;
            removeRowsWithIdRange(3000, 3999);
            int rowId = 3000;
            for (String grp : groups) {
                final int fId = rowId++;
                final String fGrp = grp;
                db.channelDao().getSeriesByGroup(grp).observe(this, list -> {
                    if (list != null && !list.isEmpty())
                        upsertRow("📺 " + fGrp, fId, p, list);
                    else
                        removeRow(fId);
                });
            }
        });

        // FIX 5 : Favoris
        db.channelDao().getFavorites().observe(this, list -> {
            if (list != null && !list.isEmpty()) upsertRow("❤️ Favoris", 9001, p, list);
            else removeRow(9001);
        });

        // Récents : chargés manuellement depuis recentIds (pas de DAO requis)
        loadRecentsRow(p);
    }

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
        // Insérer à la bonne position (ordre par id)
        int insertAt = rowsAdapter.size();
        for (int i = 0; i < rowsAdapter.size(); i++) {
            if (((ListRow) rowsAdapter.get(i)).getHeaderItem().getId() > id) {
                insertAt = i; break;
            }
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

    // ── Téléchargement background ─────────────────────────────────────────────

    private void startBackgroundSync() {
        Intent intent = getIntent();
        String login    = intent.getStringExtra(EXTRA_ACT_LOGIN);
        String password = intent.getStringExtra(EXTRA_ACT_PASSWORD);
        String expires  = intent.getStringExtra(EXTRA_ACT_EXPIRES);
        String[] dnsUrls    = intent.getStringArrayExtra(EXTRA_ACT_DNS_URLS);
        String[] dnsEpgUrls = intent.getStringArrayExtra(EXTRA_ACT_DNS_EPG_URLS);

        // Chemin 1 : extras frais depuis TvActivationActivity
        if (login != null && dnsUrls != null && dnsUrls.length > 0) {
            List<DeviceSecurity.DnsEntry> entries = new ArrayList<>();
            for (int i = 0; i < dnsUrls.length; i++) {
                String epg = (dnsEpgUrls != null && i < dnsEpgUrls.length) ? dnsEpgUrls[i] : "";
                entries.add(new DeviceSecurity.DnsEntry(dnsUrls[i], epg, i));
            }
            DeviceSecurity.ActivationResult result = new DeviceSecurity.ActivationResult(
                DeviceSecurity.getOrCreateKey(this),
                login, password != null ? password : "",
                expires != null ? expires : "", entries);
            launchDownload(result);
            return;
        }

        // Chemin 2 : reconstruction depuis prefs (retour arrière / reboot)
        SharedPreferences prefs = getSharedPreferences("wise_activation_tv", Context.MODE_PRIVATE);
        String savedStatus = prefs.getString("status", "UNKNOWN");
        String savedLogin  = prefs.getString("login", null);
        String savedPass   = prefs.getString("password", "");
        String savedExp    = prefs.getString("expires_at", "");

        if ("ACTIVE".equals(savedStatus) && savedLogin != null) {
            final String fLogin = savedLogin, fPass = savedPass, fExp = savedExp;
            Executors.newSingleThreadExecutor().execute(() -> {
                List<PlaylistEntity> all = db.playlistDao().getAllSync();
                List<DeviceSecurity.DnsEntry> entries = new ArrayList<>();
                boolean anyStale = false;
                for (PlaylistEntity pl : all) {
                    if (pl.isActive && pl.type == PlaylistEntity.TYPE_XTREAM) {
                        entries.add(new DeviceSecurity.DnsEntry(pl.url, "", entries.size()));
                        if (PlaylistLoader.needsRefresh(pl)) anyStale = true;
                    }
                }
                if (!entries.isEmpty() && anyStale) {
                    DeviceSecurity.ActivationResult result = new DeviceSecurity.ActivationResult(
                        DeviceSecurity.getOrCreateKey(this), fLogin, fPass, fExp, entries);
                    runOnUiThread(() -> launchDownload(result));
                }
            });
            return;
        }

        // Chemin 3 : playlists manuelles
        PlaylistLoader.refreshStaleIfNeeded(db, null);
    }

    private void launchDownload(DeviceSecurity.ActivationResult result) {
        ActivationManager.upsertAndDownloadAll(
            getApplicationContext(), db, result,
            new ActivationManager.DownloadCallback() {
                @Override public void onProgress(String name) {
                    Log.d(TAG, "Downloading: " + name);
                }
                @Override public void onDone(int total) {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        Toast.makeText(TvMainActivity.this,
                            "✅ " + total + " chaînes", Toast.LENGTH_SHORT).show();
                    });
                }
                @Override public void onError(String msg) {
                    // Non bloquant — log discret, l'UI reste accessible
                    Log.w(TAG, "Download non-fatal: " + msg);
                }
            });
    }

    private static final String PREFS_RECENTS = "wise_tv_recents";
    private static final int    MAX_RECENTS   = 30;

    /** Sauvegarde l'id de la chaîne dans les récents (SharedPreferences). */
    private void saveRecent(long channelId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_RECENTS, Context.MODE_PRIVATE);
        String raw = prefs.getString("ids", "");
        // Reconstruire la liste en mettant cet id en tête
        List<String> ids = new ArrayList<>(Arrays.asList(raw.isEmpty() ? new String[0] : raw.split(",")));
        String sid = String.valueOf(channelId);
        ids.remove(sid);
        ids.add(0, sid);
        if (ids.size() > MAX_RECENTS) ids = ids.subList(0, MAX_RECENTS);
        prefs.edit().putString("ids", android.text.TextUtils.join(",", ids)).apply();
        loadRecentsRow(null); // rafraîchir la ligne Récents
    }

    /** Charge la ligne Récents depuis les ids sauvegardés en prefs. */
    private void loadRecentsRow(TvCardPresenter presenterArg) {
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

    private void openPlayer(ChannelEntity ch) {
        saveRecent(ch.id); // SharedPreferences, pas de DAO
        Intent i = new Intent(this, TvPlayerActivity.class);
        i.putExtra(TvPlayerActivity.EXTRA_ID,   ch.id);
        i.putExtra(TvPlayerActivity.EXTRA_NAME, ch.name);
        i.putExtra(TvPlayerActivity.EXTRA_URL,  ch.streamUrl);
        startActivity(i);
    }
}
