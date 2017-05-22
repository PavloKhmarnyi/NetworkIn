package com.example.pavlo.networkin.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.example.pavlo.networkin.activities.AllowNetworksListActivity;

import java.util.List;

/**
 * Created by pavlo on 5/21/2017.
 */

public class NetworksManager {

    private static NetworksManager networksManager;

    private WifiManager wifiManager;
    private List<ScanResult> scanResults;
    private List<WifiConfiguration> configurations;
    private WifiConfiguration configuration;

    private NetworksManager(Context context) {
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public static NetworksManager getNetworksManager(Context context) {
        if (networksManager == null)
            networksManager = new NetworksManager(context);

        return networksManager;
    }

    public WifiManager getWifiManager() {
        return wifiManager;
    }

    public void enableWifi(Context context) {
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(context, "Wifi is disabled. Enabled wifi..", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
        }
    }

    public void connectToNetwork(String ssid) {
        int networkId = -1;
        scanResults = wifiManager.getScanResults();
        configurations = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration configuration: configurations) {
            if (configuration.SSID != null && configuration.SSID.equals("\"" + ssid + "\"")) {
                networkId = configuration.networkId;
                break;
            }
        }
        configuration = new WifiConfiguration();
        if (networkId == -1) {
            for (ScanResult scanResult:scanResults) {
                if (scanResult.SSID.equals(ssid)) {
                    configuration.SSID = String.format("\"%s\"", ssid);
                    configuration.BSSID = scanResult.BSSID;
                    configuration.priority = 1;
                    configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                    configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    configuration.status = WifiConfiguration.Status.ENABLED;
                    networkId = wifiManager.addNetwork(configuration);
                    break;
                }
            }
        }
        wifiManager.saveConfiguration();
        wifiManager.enableNetwork(networkId, true);
        wifiManager.reconnect();
    }
}
