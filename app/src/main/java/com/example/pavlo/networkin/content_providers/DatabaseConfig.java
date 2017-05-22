package com.example.pavlo.networkin.content_providers;

/**
 * Created by pavlo on 5/19/2017.
 */

public class DatabaseConfig {
    public static final String DB_NAME = "NetworksDb";
    public static final int DB_VERSION = 1;

    public static final String NETWORKS_TABLE = "Networks";
    public static final String NETWORK_ID = "id";
    public static final String NETWORK_SSID = "ssid";
    public static final String NETWORK_BSSID = "bssid";
    public static final String NETWORK_DATE = "date";
    public static final String NETWORK_TIME = "time";

    public static final String DB_CREATE = "CREATE TABLE " + NETWORKS_TABLE + " ("
            + NETWORK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NETWORK_SSID + " TEXT, "
            + NETWORK_BSSID + " TEXT,"
            + NETWORK_DATE + " TEXT,"
            + NETWORK_TIME + " TEXT"
            + ");";
}
