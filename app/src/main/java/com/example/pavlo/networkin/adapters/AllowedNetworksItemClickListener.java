package com.example.pavlo.networkin.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.example.pavlo.networkin.content_providers.DatabaseConfig;
import com.example.pavlo.networkin.content_providers.NetworksProviderWrapper;
import com.example.pavlo.networkin.models.NetworkData;
import com.example.pavlo.networkin.utils.NetworksManager;

import java.util.ArrayList;

/**
 * Created by pavlo on 5/22/2017.
 */

public class AllowedNetworksItemClickListener implements View.OnClickListener {

    private Context context;
    private ArrayList<NetworkData> allowedNetworks;
    private int position;

    public AllowedNetworksItemClickListener(Context context, ArrayList<NetworkData> allowedNetworks, int position) {
        this.context = context;
        this.allowedNetworks = allowedNetworks;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        new AlertDialog.Builder(context)
                .setTitle("What would you like to do?")
                .setMessage("You choose network " + allowedNetworks.get(position).getSsid())
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NetworkData network = allowedNetworks.get(position);
                        if (NetworksProviderWrapper.isNetworkStored(context, DatabaseConfig.NETWORK_SSID, network.getSsid())) {
                            Toast.makeText(context, "The network " + network.getSsid() + " already stored!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (NetworksProviderWrapper.isNetworkStored(context, DatabaseConfig.NETWORK_BSSID, network.getBssid())) {
                            Toast.makeText(context, "The network with " + network.getBssid() + "already stored and will be updated with name " + network.getSsid(), Toast.LENGTH_SHORT).show();
                            NetworksProviderWrapper.update(context, network);
                            return;
                        }

                        NetworksProviderWrapper.save(context, network);
                        Toast.makeText(context, "Network " + network.getSsid() + " saved successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ssid = allowedNetworks.get(position).getSsid();
                        NetworksManager networksManager = NetworksManager.getNetworksManager(context);
                        networksManager.enableWifi(context);
                        networksManager.connectToNetwork(ssid);
                        Toast.makeText(context, "Connecting to " + ssid + " network!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "This action was canceled!", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}
