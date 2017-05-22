package com.example.pavlo.networkin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavlo.networkin.content_providers.NetworksProviderWrapper;
import com.example.pavlo.networkin.R;
import com.example.pavlo.networkin.utils.PermissionsManager;
import com.example.pavlo.networkin.utils.Config;
import com.example.pavlo.networkin.utils.NetworksManager;

public class NetworkDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView ssidTextView;
    private TextView bssidTextView;
    private TextView dateTextView;
    private TextView timeTextView;

    private Button connectButton;
    private Button deleteButton;
    private Button cancelButton;

    private Intent intent;

    private String ssid;
    private String bssid;
    private String date;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ssidTextView = (TextView) findViewById(R.id.ssidTextView);
        bssidTextView = (TextView) findViewById(R.id.bssidTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);

        intent = getIntent();
        ssid = intent.getStringExtra("ssid");
        bssid = intent.getStringExtra("bssid");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");

        ssidTextView.setText(ssid);
        bssidTextView.setText(bssid);
        dateTextView.setText(date);
        timeTextView.setText(time);

        connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(this);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connectButton:
                PermissionsManager.requestPermissions(this);
                NetworksManager networksManager = NetworksManager.getNetworksManager(this);
                networksManager.enableWifi(this);
                networksManager.connectToNetwork(ssid);
                Toast.makeText(this, "Connecting to " + ssid + " network!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteButton:
                new AlertDialog.Builder(this)
                        .setTitle("The network " + ssid + " will be deleted!")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NetworksProviderWrapper.delete(getApplicationContext(), ssid);
                                Toast.makeText(getApplicationContext(), "Network " + ssid + " was deleted!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
            case R.id.cancelButton:
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grandResult);

        String message =
                requestCode == Config.PERMISSION_REQUEST_CODE &&
                        grandResult.length == 2 &&
                        grandResult[0] == PackageManager.PERMISSION_GRANTED &&
                        grandResult[1] == PackageManager.PERMISSION_GRANTED ?
                        getString(R.string.allow_location_permission_message) :
                        getString(R.string.not_allow_location_permission_message);

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
