package com.wdesign.wiseiptv.mobile.ui;
import android.content.*;
import android.os.*;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.WiseApp;
import com.wdesign.wiseiptv.mobile.adapter.ChannelAdapter;
import com.wdesign.wiseiptv.mobile.util.PlaylistLoader;
import java.util.List;
public class MainActivity extends AppCompatActivity implements ChannelAdapter.OnChannelClick {
    private RecyclerView rvChannels;
    private ChannelAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TabLayout tabLayout;
    private BottomNavigationView bottomNav;
    private SearchView searchView;
    private AppDatabase db;
    private int currentTab = 0;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.get(this);
        rvChannels  = findViewById(R.id.rv_channels);
        progressBar = findViewById(R.id.progress_bar);
        tvEmpty     = findViewById(R.id.tv_empty);
        tabLayout   = findViewById(R.id.tab_layout);
        bottomNav   = findViewById(R.id.bottom_nav);
        searchView  = findViewById(R.id.search_view);
        adapter = new ChannelAdapter(this);
        rvChannels.setLayoutManager(new GridLayoutManager(this, 3));
        rvChannels.setAdapter(adapter);
        setupTabs(); setupSearch(); setupBottomNav();
        String url = WiseApp.get().getPlaylistUrl();
        if (!TextUtils.isEmpty(url)) loadChannels(); else showAddPlaylistDialog();
    }
    private void setupTabs() {
        String[] tabs = {"Tout","Live","Films","Séries","Favoris"};
        for (String t : tabs) tabLayout.addTab(tabLayout.newTab().setText(t));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab t) { currentTab = t.getPosition(); observeCurrentTab(); }
            @Override public void onTabUnselected(TabLayout.Tab t) {}
            @Override public void onTabReselected(TabLayout.Tab t) {}
        });
    }
    private void observeCurrentTab() {
        switch (currentTab) {
            case 0: db.channelDao().getAll().observe(this, this::updateList); break;
            case 1: db.channelDao().getLive().observe(this, this::updateList); break;
            case 2: db.channelDao().getFilms().observe(this, this::updateList); break;
            case 3: db.channelDao().getSeries().observe(this, this::updateList); break;
            case 4: db.channelDao().getFavorites().observe(this, this::updateList); break;
        }
    }
    private void updateList(List<ChannelEntity> list) {
        adapter.setData(list);
        tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
    }
    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { return false; }
            @Override public boolean onQueryTextChange(String q) {
                if (TextUtils.isEmpty(q)) { observeCurrentTab(); return true; }
                db.channelDao().search(q).observe(MainActivity.this, MainActivity.this::updateList);
                return true;
            }
        });
    }
    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if      (id == R.id.nav_home)    { observeCurrentTab(); return true; }
            else if (id == R.id.nav_add)     { showAddPlaylistDialog(); return true; }
            else if (id == R.id.nav_settings){ startActivity(new Intent(this, SettingsActivity.class)); return true; }
            return false;
        });
    }
    private void loadChannels() {
        String url = WiseApp.get().getPlaylistUrl();
        if (TextUtils.isEmpty(url)) return;
        progressBar.setVisibility(View.VISIBLE);
        PlaylistLoader.load(url, db, new PlaylistLoader.Callback() {
            @Override public void onDone(int count) {
                runOnUiThread(() -> { progressBar.setVisibility(View.GONE); observeCurrentTab();
                    Toast.makeText(MainActivity.this, count + " chaînes chargées", Toast.LENGTH_SHORT).show(); });
            }
            @Override public void onError(String msg) {
                runOnUiThread(() -> { progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Erreur : " + msg, Toast.LENGTH_LONG).show();
                    observeCurrentTab(); });
            }
        });
    }
    public void showAddPlaylistDialog() {
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(this);
        b.setTitle("Ajouter une playlist M3U");
        android.view.View v = getLayoutInflater().inflate(R.layout.dialog_add_playlist, null);
        EditText etName = v.findViewById(R.id.et_playlist_name);
        EditText etUrl  = v.findViewById(R.id.et_playlist_url);
        b.setView(v);
        b.setPositiveButton("Charger", (d, w) -> {
            String name = etName.getText().toString().trim();
            String url  = etUrl.getText().toString().trim();
            if (TextUtils.isEmpty(url)) return;
            WiseApp.get().savePlaylist(name.isEmpty() ? "Ma playlist" : name, url);
            loadChannels();
        });
        b.setNegativeButton("Annuler", null);
        b.show();
    }
    @Override public void onClick(ChannelEntity ch) {
        Intent i = new Intent(this, PlayerActivity.class);
        i.putExtra(PlayerActivity.EXTRA_ID, ch.id);
        i.putExtra(PlayerActivity.EXTRA_NAME, ch.name);
        i.putExtra(PlayerActivity.EXTRA_URL, ch.streamUrl);
        i.putExtra(PlayerActivity.EXTRA_TYPE, ch.contentType);
        startActivity(i);
    }
    @Override protected void onResume() { super.onResume(); observeCurrentTab(); }
}
