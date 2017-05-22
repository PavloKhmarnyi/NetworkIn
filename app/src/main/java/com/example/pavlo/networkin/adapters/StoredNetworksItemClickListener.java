package com.example.pavlo.networkin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.pavlo.networkin.activities.NetworkDetailActivity;
import com.example.pavlo.networkin.models.NetworkData;

import java.util.ArrayList;

/**
 * Created by pavlo on 5/22/2017.
 */

public class StoredNetworksItemClickListener implements View.OnClickListener {

    private Context context;
    private ArrayList<NetworkData> storedNetworks;
    private int position;

    public StoredNetworksItemClickListener(Context context, ArrayList<NetworkData> storedNetworks, int position) {
        this.context = context;
        this.storedNetworks = storedNetworks;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        NetworkData network = storedNetworks.get(position);
        Intent intent = new Intent(context, NetworkDetailActivity.class);
        intent.putExtra("ssid", network.getSsid());
        intent.putExtra("bssid", network.getBssid());
        intent.putExtra("date", network.getDate());
        intent.putExtra("time", network.getTime());
        context.startActivity(intent);
    }
}
