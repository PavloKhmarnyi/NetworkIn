package com.example.pavlo.networkin.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pavlo.networkin.adapters.NetworksAdapter;
import com.example.pavlo.networkin.content_providers.NetworksProviderWrapper;
import com.example.pavlo.networkin.R;
import com.example.pavlo.networkin.models.NetworkData;
import com.example.pavlo.networkin.utils.PermissionsManager;
import com.example.pavlo.networkin.utils.Config;
import com.example.pavlo.networkin.utils.NetworksManager;

import java.util.ArrayList;

public class NetworkListActivity extends AppCompatActivity {

    public static final String LOG_TAG = "Network list activity log";

    private final ArrayList<NetworkData> storedNetworksList = new ArrayList<>();

    private Button allowNetworksButton;
    private RecyclerView storedNetworksRecyclerView;
    private NetworksAdapter adapter;

    private NetworksManager networksManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        storedNetworksRecyclerView = (RecyclerView) findViewById(R.id.network_list);
        assert storedNetworksRecyclerView != null;

        adapter = new NetworksAdapter(this, storedNetworksList, Config.STORED_NETWORKS_ITEM_CLICK_LISTENER_CODE);
        storedNetworksRecyclerView.setAdapter(adapter);

        allowNetworksButton = (Button) findViewById(R.id.allowNetworksButton);
        allowNetworksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionsManager.isPermissions(NetworkListActivity.this)) {
                    PermissionsManager.requestPermissions(NetworkListActivity.this);
                } else {
                    Intent intent = new Intent(NetworkListActivity.this, AllowNetworksListActivity.class);
                    startActivity(intent);
                }
            }
        });

        networksManager = NetworksManager.getNetworksManager(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (storedNetworksList.size() != 0)
            storedNetworksList.clear();

        NetworksProviderWrapper.getStoredNetworks(this, storedNetworksList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grandResult);

        if (requestCode == Config.PERMISSION_REQUEST_CODE &&
                grandResult.length == 2 &&
                grandResult[0] == PackageManager.PERMISSION_GRANTED &&
                grandResult[1] == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(this, AllowNetworksListActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.not_allow_location_permission_message), Toast.LENGTH_SHORT).show();
        }
    }
}
