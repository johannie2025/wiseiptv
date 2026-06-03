package com.wdesign.wiseiptv.mobile.ui;

import android.content.*;
import android.os.*;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.wdesign.wiseiptv.core.db.AppDatabase;
import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import com.wdesign.wiseiptv.core.db.entity.PlaylistEntity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.mobile.R;
import com.wdesign.wiseiptv.mobile.adapter.ChannelAdapter;
import com.wdesign.wiseiptv.mobile.util.ActivationManager;
import com.wdesign.wiseiptv.mobile.util.PlaylistLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ChannelAdapter.OnChannelClick {

    public static final String EXTRA_ACT_LOGIN        = "act_login";
    public static final String EXTRA_ACT_PASSWORD     = "act_password";
    public static final String EXTRA_ACT_EXPIRES      = "act_expires";
    public static final String EXTRA_ACT_DNS_URLS     = "act_dns_urls";
    public static final String EXTRA_ACT_DNS_EPG_URLS = "act_dns_epg_urls";

    private AppDatabase db;
    private RecyclerView rvChannels;
    private ChannelAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TabLayout tabLayout;
    private BottomNavigationView bottomNav;
    private SearchView searchView;
    private Spinner spinnerGroup;

    private String currentContentType = ChannelEntity.TYPE_LIVE;
    private String currentSearchQuery = "";
    private String currentGroup       = "";

    private LiveData<List<ChannelEntity>> currentLiveData;
    private Observer<List<ChannelEntity>> currentObserver;

    private LiveData<List<String>> currentGroupLiveData;
    private Observer<List<String>> currentGroupObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db          = AppDatabase.get(this);
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

        setupTabs();
        setupSearch();
        setupBottomNav();
        setupGroupSpinner();

        // 1. Charger immédiatement le cache local hors-ligne existant
        observeCurrentTab();

        // 2. Lancement asynchrone non-bloquant de la mise à jour des playlists d'activation
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        DeviceSecurity.check(this, new DeviceSecurity.Callback() {
            @Override
            public void onActive(DeviceSecurity.ActivationResult r) {
                // CORRECTION : Utilisation de OnDownloadCallback avec onSuccess et onFailure
                ActivationManager.downloadAllPlaylistsAsync(getApplicationContext(), db, r, new ActivationManager.OnDownloadCallback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(() -> {
                            if (progressBar != null) progressBar.setVisibility(View.GONE);
                            observeCurrentTab(); // Rafraîchit l'affichage graphique
                            Toast.makeText(MainActivity.this, "Chaînes mises à jour !", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onFailure(String msg) {
                        runOnUiThread(() -> {
                            if (progressBar != null) progressBar.setVisibility(View.GONE);
                            Log.e("MainActivity", "Erreur sync arrière-plan : " + msg);
                        });
                    }
                });
            }

            @Override
            public void onInactive(String status, String message) {
                runOnUiThread(() -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                });
            }
        });
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("EN DIRECT").setTag(ChannelEntity.TYPE_LIVE));
        tabLayout.addTab(tabLayout.newTab().setText("FILMS").setTag(ChannelEntity.TYPE_VOD));
        tabLayout.addTab(tabLayout.newTab().setText("SÉRIES").setTag(ChannelEntity.TYPE_SERIES));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                currentContentType = (String) tab.getTag();
                currentGroup = ""; 
                observeCurrentTab();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { return false; }
            @Override public boolean onQueryTextChange(String q) {
                currentSearchQuery = q;
                observeCurrentTab();
                return true;
            }
        });
    }

    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_playlists) {
                startActivity(new Intent(this, PlaylistManagerActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupGroupSpinner() {
        if (currentGroupLiveData != null && currentGroupObserver != null) {
            currentGroupLiveData.removeObserver(currentGroupObserver);
        }

        currentGroupLiveData = db.channelDao().getGroupsByType(currentContentType);
        currentGroupObserver = groups -> {
            List<String> list = new ArrayList<>();
            list.add("Tous les groupes");
            if (groups != null) {
                list.addAll(groups);
            }

            ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerGroup.setAdapter(adapterSpinner);

            spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                    if (position == 0) {
                        currentGroup = "";
                    } else {
                        currentGroup = list.get(position);
                    }
                    observeCurrentTab();
                }
                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });
        };
        currentGroupLiveData.observe(this, currentGroupObserver);
    }

    private void observeCurrentTab() {
        if (currentLiveData != null && currentObserver != null) {
            currentLiveData.removeObserver(currentObserver);
        }

        boolean hasSearch = !TextUtils.isEmpty(currentSearchQuery);
        boolean hasGroup  = !TextUtils.isEmpty(currentGroup);

        if (hasSearch && hasGroup) {
            currentLiveData = db.channelDao().searchInGroup(currentContentType, currentGroup, "%" + currentSearchQuery + "%");
        } else if (hasSearch) {
            currentLiveData = db.channelDao().searchGlobal(currentContentType, "%" + currentSearchQuery + "%");
        } else if (hasGroup) {
            currentLiveData = db.channelDao().getByGroup(currentContentType, currentGroup);
        } else {
            currentLiveData = db.channelDao().getAllByType(currentContentType);
        }

        currentObserver = channels -> {
            if (channels == null || channels.isEmpty()) {
                rvChannels.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
            } else {
                rvChannels.setVisibility(View.VISIBLE);
                tvEmpty.setVisibility(View.GONE);
                adapter.setChannels(channels);
            }
        };

        currentLiveData.observe(this, currentObserver);
    }

    private void showAddPlaylistDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Ajouter une playlist Xtream");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_playlist, null);
        EditText etName = view.findViewById(R.id.et_name);
        EditText etUrl  = view.findViewById(R.id.et_url);
        EditText etUser = view.findViewById(R.id.et_username);
        EditText etPass = view.findViewById(R.id.et_password);
        b.setView(view);

        b.setPositiveButton("Ajouter", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String url  = etUrl.getText().toString().trim();
            String user = etUser.getText().toString().trim();
            String pass = etPass.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(url)) {
                Toast.makeText(this, "Nom et URL obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            PlaylistEntity pl = new PlaylistEntity();
            pl.name = name;
            pl.type = PlaylistEntity.TYPE_XTREAM;
            pl.url = url;
            pl.username = user;
            pl.password = pass;
            pl.isActive = true;

            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

            Executors.newSingleThreadExecutor().execute(() -> {
                long id = db.playlistDao().insert(pl);
                pl.id = id;

                PlaylistLoader.load(pl, db, new PlaylistLoader.Callback() {
                    @Override public void onDone(int count) {
                        runOnUiThread(() -> {
                            if (progressBar != null) progressBar.setVisibility(View.GONE);
                            setupGroupSpinner();
                            observeCurrentTab();
                            Toast.makeText(MainActivity.this, "Playlist ajoutée : " + count + " chaînes.", Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override public void onError(String msg) {
                        runOnUiThread(() -> {
                            if (progressBar != null) progressBar.setVisibility(View.GONE);
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
        i.putExtra(PlayerActivity.EXTRA_ID,    ch.id);
        i.putExtra(PlayerActivity.EXTRA_NAME,  ch.name);
        i.putExtra(PlayerActivity.EXTRA_URL,   ch.streamUrl);
        i.putExtra(PlayerActivity.EXTRA_TYPE,  ch.contentType);
        i.putExtra(PlayerActivity.EXTRA_GROUP, ch.groupTitle);
        i.putExtra(PlayerActivity.EXTRA_ORDER, ch.sortOrder);
        startActivity(i);
    }

    @Override protected void onResume() {
        super.onResume();
        observeCurrentTab();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (currentLiveData != null && currentObserver != null)
            currentLiveData.removeObserver(currentObserver);
        if (currentGroupLiveData != null && currentGroupObserver != null)
            currentGroupLiveData.removeObserver(currentGroupObserver);
    }
}