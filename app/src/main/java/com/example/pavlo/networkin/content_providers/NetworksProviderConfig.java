package com.example.pavlo.networkin.content_providers;

import android.net.Uri;

/**
 * Created by pavlo on 5/19/2017.
 */

public class NetworksProviderConfig {
    public static final String AUTHORITY = "com.example.pavlo.networkin.StoredNetworks";
    public static final String  NETWORKS_PATH = "networks";
    public static final Uri NETWORKS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NETWORKS_PATH);

    public static final int URI_NETWORKS = 1;
    public static final int URI_NETWORKS_ID = 2;

    public static final String NETWORK_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + " ." + NETWORKS_PATH;
    public static final String NETWORK_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + " ." + NETWORKS_PATH;
}
