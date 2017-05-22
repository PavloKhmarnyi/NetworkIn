package com.example.pavlo.networkin.content_providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.pavlo.networkin.models.NetworkData;
import com.example.pavlo.networkin.utils.Config;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pavlo on 5/19/2017.
 */

public class NetworksProviderWrapper {

    public static void getStoredNetworks(Context context, ArrayList<NetworkData> networks) {
        Cursor cursor = context.getContentResolver().query(NetworksProviderConfig.NETWORKS_CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst() && cursor != null) {
            int ssidId = cursor.getColumnIndex(DatabaseConfig.NETWORK_SSID);
            int bssidId = cursor.getColumnIndex(DatabaseConfig.NETWORK_BSSID);
            int dateId = cursor.getColumnIndex(DatabaseConfig.NETWORK_DATE);
            int timeId = cursor.getColumnIndex(DatabaseConfig.NETWORK_TIME);
            do {
                String ssid = cursor.getString(ssidId);
                String bssid = cursor.getString(bssidId);
                String date = cursor.getString(dateId);
                String time = cursor.getString(timeId);
                NetworkData networkData = new NetworkData(ssid, bssid, date, time);
                networks.add(networkData);
            } while (cursor.moveToNext());
        }
    }

    public static boolean isNetworkStored(Context context, String paramName, String paramValue) {
        Cursor cursor = context.getContentResolver().query(NetworksProviderConfig.NETWORKS_CONTENT_URI,
                null,
                paramName + " = '" + paramValue + "'",
                null,
                null);

        return cursor.moveToFirst() ? true : false;
    }

    public static void save(Context context, NetworkData network) {
        String ssid = network.getSsid();
        String bssid = network.getBssid();
        String date = Config.DATE_FORMAT.format(new Date());
        String time = Config.TIME_FORMAT.format(new Date());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConfig.NETWORK_SSID, ssid);
        contentValues.put(DatabaseConfig.NETWORK_BSSID, bssid);
        contentValues.put(DatabaseConfig.NETWORK_DATE, date);
        contentValues.put(DatabaseConfig.NETWORK_TIME, time);
        Uri uri = context.getContentResolver().insert(NetworksProviderConfig.NETWORKS_CONTENT_URI, contentValues);
    }

    public static int update(Context context, NetworkData network) {
        String ssid = network.getSsid();
        String bssid = network.getBssid();
        String date = Config.DATE_FORMAT.format(new Date());
        String time = Config.TIME_FORMAT.format(new Date());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConfig.NETWORK_SSID, ssid);
        contentValues.put(DatabaseConfig.NETWORK_DATE, date);
        contentValues.put(DatabaseConfig.NETWORK_TIME, time);
        int id = context.getContentResolver().update(NetworksProviderConfig.NETWORKS_CONTENT_URI,
                contentValues,
                DatabaseConfig.NETWORK_BSSID + " = '" + bssid + "'",
                null);

        return id;
    }

    public static void delete(Context context, String ssid) {
        context.getContentResolver().delete(NetworksProviderConfig.NETWORKS_CONTENT_URI,
                DatabaseConfig.NETWORK_SSID + " = '" + ssid + "'",
                null);
    }
}
