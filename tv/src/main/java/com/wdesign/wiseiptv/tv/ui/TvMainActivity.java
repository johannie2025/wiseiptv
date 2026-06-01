package com.wdesign.wiseiptv.tv.ui;
import android.app.AlertDialog;
import android.content.*;
import android.os.*;
import android.text.TextUtils;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.*;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.tv.util.PlaylistLoader;
import com.wdesign.wiseiptv.tv.R;
import com.wdesign.wiseiptv.tv.WiseIptvTvApp;
import com.wdesign.wiseiptv.tv.adapter.TvCardPresenter;
public class TvMainActivity extends FragmentActivity {
    private BrowseSupportFragment browseFragment;
    private ArrayObjectAdapter rowsAdapter;
    private AppDatabase db;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_main);
        db = AppDatabase.get(this);
        setupBrowseFragment();
        String url = WiseIptvTvApp.get().getPlaylistUrl();
        if (TextUtils.isEmpty(url)) showAddPlaylistDialog(); else loadRows();
    }
    private void setupBrowseFragment() {
        browseFragment = (BrowseSupportFragment) getSupportFragmentManager().findFragmentById(R.id.browse_fragment);
        if (browseFragment == null) {
            browseFragment = new BrowseSupportFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.browse_fragment, browseFragment).commit();
        }
        browseFragment.setTitle("Wise IPTV");
        browseFragment.setBrandColor(getResources().getColor(R.color.wise_blue_dark, null));
        browseFragment.setSearchAffordanceColor(getResources().getColor(R.color.wise_red, null));
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        browseFragment.setAdapter(rowsAdapter);
        browseFragment.setOnItemViewClickedListener((ivh, item, rvh, row) -> {
            if (item instanceof ChannelEntity) openPlayer((ChannelEntity) item);
        });
        browseFragment.setOnSearchClickedListener(v -> showAddPlaylistDialog());
    }
    private void loadRows() {
        TvCardPresenter p = new TvCardPresenter();
        rowsAdapter.clear();
        db.channelDao().getLive().observe(this, list -> { if (list != null && !list.isEmpty()) addRow("Live", 0, p, list); });
        db.channelDao().getFilms().observe(this, list -> { if (list != null && !list.isEmpty()) addRow("Films", 1, p, list); });
        db.channelDao().getSeries().observe(this, list -> { if (list != null && !list.isEmpty()) addRow("Series", 2, p, list); });
        db.channelDao().getFavorites().observe(this, list -> { if (list != null && !list.isEmpty()) addRow("Favoris", 3, p, list); });
    }
    private void addRow(String title, int id, TvCardPresenter p, java.util.List<ChannelEntity> list) {
        ArrayObjectAdapter rowAdapter = new ArrayObjectAdapter(p);
        for (ChannelEntity ch : list) rowAdapter.add(ch);
        for (int i = 0; i < rowsAdapter.size(); i++) {
            ListRow r = (ListRow) rowsAdapter.get(i);
            if (r.getHeaderItem().getId() == id) { rowsAdapter.replace(i, new ListRow(new HeaderItem(id, title), rowAdapter)); return; }
        }
        rowsAdapter.add(new ListRow(new HeaderItem(id, title), rowAdapter));
    }
    private void openPlayer(ChannelEntity ch) {
        Intent i = new Intent(this, TvPlayerActivity.class);
        i.putExtra(TvPlayerActivity.EXTRA_ID, ch.id);
        i.putExtra(TvPlayerActivity.EXTRA_NAME, ch.name);
        i.putExtra(TvPlayerActivity.EXTRA_URL, ch.streamUrl);
        startActivity(i);
    }
    private void showAddPlaylistDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Playlist M3U");
        EditText et = new EditText(this);
        et.setHint("URL M3U : http://..."); et.setPadding(32, 24, 32, 24);
        String cur = WiseIptvTvApp.get().getPlaylistUrl();
        if (!TextUtils.isEmpty(cur)) et.setText(cur);
        b.setView(et);
        b.setPositiveButton("Charger", (d, w) -> {
            String url = et.getText().toString().trim();
            if (TextUtils.isEmpty(url)) return;
            WiseIptvTvApp.get().savePlaylistUrl(url);
            PlaylistLoader.load(url, db, new PlaylistLoader.Callback() {
                @Override public void onDone(int count) {
                    runOnUiThread(() -> { loadRows(); Toast.makeText(TvMainActivity.this, count+" chaînes", Toast.LENGTH_SHORT).show(); });
                }
                @Override public void onError(String msg) {
                    runOnUiThread(() -> Toast.makeText(TvMainActivity.this, "Erreur: "+msg, Toast.LENGTH_LONG).show());
                }
            });
        });
        b.setNegativeButton("Annuler", null);
        b.show();
    }
}
