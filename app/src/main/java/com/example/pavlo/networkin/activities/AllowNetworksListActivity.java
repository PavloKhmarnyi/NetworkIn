package com.example.pavlo.networkin.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.pavlo.networkin.R;
import com.example.pavlo.networkin.adapters.NetworksAdapter;
import com.example.pavlo.networkin.models.NetworkData;
import com.example.pavlo.networkin.utils.Config;
import com.example.pavlo.networkin.utils.NetworksManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavlo on 5/18/2017.
 */

public class AllowNetworksListActivity extends AppCompatActivity {

    private RecyclerView allowNetworksRecyclerView;
    private NetworksAdapter adapter;

    private NetworksManager networksManager;

    private WifiManager wifiManager;
    private WifiReciever wifiReciever;

    private List<ScanResult> scanResults;
    private final ArrayList<NetworkData> networkList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_networks_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        networksManager = NetworksManager.getNetworksManager(this);
        wifiManager = networksManager.getWifiManager();
        wifiReciever = new WifiReciever();

        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

        networksManager.enableWifi(this);

        allowNetworksRecyclerView = (RecyclerView) findViewById(R.id.network_list);
        assert allowNetworksRecyclerView != null;

        adapter = new NetworksAdapter(this, networkList, Config.ALLOWED_NETWORKS_ITEM_CLICK_LISTENER_CODE);
        allowNetworksRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReciever);
    }

    private class WifiReciever extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String ssid;
            String bssid;

            networkList.clear();
            scanResults = wifiManager.getScanResults();

            for (ScanResult scanResult: scanResults) {
                ssid = scanResult.SSID;
                bssid = scanResult.BSSID;
                NetworkData networkData = new NetworkData(ssid, bssid);
                networkList.add(networkData);
            }

            adapter.notifyDataSetChanged();
            wifiManager.startScan();
        }
    }
}
