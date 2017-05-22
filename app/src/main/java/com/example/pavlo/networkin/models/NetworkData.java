package com.example.pavlo.networkin.models;

import java.util.Date;

/**
 * Created by pavlo on 5/18/2017.
 */

public class NetworkData {

    private String ssid;
    private String bssid;
    private String date;
    private String time;

    public NetworkData(String ssid, String bssid) {
        this.ssid = ssid;
        this.bssid = bssid;
    }

    public NetworkData(String ssid, String bssid, String date, String time) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.date = date;
        this.time = time;
    }

    public String getSsid() {
        return this.ssid;
    }

    public String getBssid() {
        return this.bssid;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return  this.time;
    }
}
