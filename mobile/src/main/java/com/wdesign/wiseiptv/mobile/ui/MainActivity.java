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
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.adapter.ChannelAdapter;
import com.wdesign.wiseiptv.mobile.util.PlaylistLoader;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ChannelAdapter.OnChannelClick {

    private RecyclerView rvChannels;
    private ChannelAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TabLayout tabLayout;
    private BottomNavigationView bottomNav;
    private SearchView searchView;
    private Spinner spinnerGroup;
    private AppDatabase db;
    private int currentTab = 0;         // 0=All,1=Live,2=Films,3=Series,4=Fav
    private String currentGroup = null; // null = tous les groupes

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
        spinnerGroup= findViewById(R.id.spinner_group);

        adapter = new ChannelAdapter(this);
        rvChannels.setLayoutManager(new GridLayoutManager(this, 3));
        rvChannels.setAdapter(adapter);

        setupTabs(); setupSearch(); setupBottomNav(); setupGroupSpinner();

        // Première ouverture sans playlist → propose d'en ajouter une
        Executors.newSingleThreadExecutor().execute(() -> {
            if (db.playlistDao().getAllSync().isEmpty()) {
                runOnUiThread(this::showAddPlaylistDialog);
            } else {
                runOnUiThread(this::observeCurrentTab);
            }
        });
    }

    private void setupTabs() {
        String[] tabs = {"Tout","Live","Films","Séries","Favoris"};
        for (String t : tabs) tabLayout.addTab(tabLayout.newTab().setText(t));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab t) {
                currentTab = t.getPosition(); currentGroup = null;
                loadGroupsForCurrentTab(); observeCurrentTab();
            }
            @Override public void onTabUnselected(TabLayout.Tab t) {}
            @Override public void onTabReselected(TabLayout.Tab t) {}
        });
    }

    private void loadGroupsForCurrentTab() {
        switch (currentTab) {
            case 1: db.channelDao().getLiveGroups().observe(this, this::updateGroupSpinner); break;
            case 2: db.channelDao().getFilmGroups().observe(this, this::updateGroupSpinner); break;
            case 3: db.channelDao().getSeriesGroups().observe(this, this::updateGroupSpinner); break;
            default: updateGroupSpinner(null); break;
        }
    }

    private void updateGroupSpinner(List<String> groups) {
        if (spinnerGroup == null) return;
        if (groups == null || groups.isEmpty()) {
            spinnerGroup.setVisibility(View.GONE); return;
        }
        spinnerGroup.setVisibility(View.VISIBLE);
        java.util.ArrayList<String> items = new java.util.ArrayList<>();
        items.add("Tous les groupes");
        items.addAll(groups);
        ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(a);
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                currentGroup = pos == 0 ? null : items.get(pos);
                observeCurrentTab();
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });
    }

    private void setupGroupSpinner() { loadGroupsForCurrentTab(); }

    private void observeCurrentTab() {
        if (currentGroup != null) {
            // Filtre par groupe
            switch (currentTab) {
                case 1: db.channelDao().getLiveByGroup(currentGroup).observe(this, this::updateList); return;
                case 2: db.channelDao().getFilmsByGroup(currentGroup).observe(this, this::updateList); return;
                case 3: db.channelDao().getSeriesByGroup(currentGroup).observe(this, this::updateList); return;
            }
        }
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

    public void showAddPlaylistDialog() {
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(this);
        b.setTitle("Ajouter une playlist");
        android.view.View v = getLayoutInflater().inflate(R.layout.dialog_add_playlist, null);
        EditText etName   = v.findViewById(R.id.et_playlist_name);
        EditText etUrl    = v.findViewById(R.id.et_playlist_url);
        EditText etServer = v.findViewById(R.id.et_xtream_server);
        EditText etUser   = v.findViewById(R.id.et_xtream_user);
        EditText etPass   = v.findViewById(R.id.et_xtream_pass);
        RadioGroup rgType = v.findViewById(R.id.rg_type);
        android.view.View layoutXtream = v.findViewById(R.id.layout_xtream);

        rgType.setOnCheckedChangeListener((g, checked) -> {
            boolean isXtream = checked == R.id.rb_xtream;
            layoutXtream.setVisibility(isXtream ? android.view.View.VISIBLE : android.view.View.GONE);
            etUrl.setVisibility(isXtream ? android.view.View.GONE : android.view.View.VISIBLE);
        });

        b.setView(v);
        b.setPositiveButton("Charger", (d, w) -> {
            String name = etName.getText().toString().trim();
            int checkedId = rgType.getCheckedRadioButtonId();
            PlaylistEntity pl = new PlaylistEntity();
            pl.name = name.isEmpty() ? "Playlist" : name;
            pl.lastUpdated = 0; // Force le premier chargement

            if (checkedId == R.id.rb_xtream) {
                String server = etServer.getText().toString().trim();
                String user   = etUser.getText().toString().trim();
                String pass   = etPass.getText().toString().trim();
                if (server.isEmpty() || user.isEmpty()) return;
                pl.type = PlaylistEntity.TYPE_XTREAM;
                pl.url = server; pl.username = user; pl.password = pass;
            } else {
                String url = etUrl.getText().toString().trim();
                if (TextUtils.isEmpty(url)) return;
                pl.type = checkedId == R.id.rb_m3u_file
                        ? PlaylistEntity.TYPE_M3U_FILE : PlaylistEntity.TYPE_M3U_URL;
                pl.url = url;
            }

            progressBar.setVisibility(android.view.View.VISIBLE);
            Executors.newSingleThreadExecutor().execute(() -> {
                pl.id = db.playlistDao().insert(pl);
                PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                    @Override public void onDone(int count) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(android.view.View.GONE);
                            observeCurrentTab();
                            Toast.makeText(MainActivity.this, count + " chaînes chargées", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override public void onError(String msg) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(android.view.View.GONE);
                            Toast.makeText(MainActivity.this, "Erreur : " + msg, Toast.LENGTH_LONG).show();
                        });
                    }
                });
            });
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
        i.putExtra(PlayerActivity.EXTRA_GROUP, ch.groupTitle);
        i.putExtra(PlayerActivity.EXTRA_ORDER, ch.sortOrder);
        startActivity(i);
    }

    @Override protected void onResume() { super.onResume(); observeCurrentTab(); }
}
