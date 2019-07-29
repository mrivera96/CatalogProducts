package com.diadema.catalogproducts.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.diadema.catalogproducts.R;

public class NetworkStatusManager {
    private static String statusResponse;
    public static String status(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("NetworkStatusManager", "Online");
            Log.d("NetworkStatusManager", " Estado actual: " + networkInfo.getState());
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    // connected to wifi get SSID
                    WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    Log.d("NetworkStatusManager", " SSID iguales: " + wifiInfo.getSSID().equals(context.getString(R.string.ssid)));
                    Log.d("NetworkStatusManager", " SSID: " + wifiInfo.getSSID());
                    //if (wifiInfo.getSSID().equals(context.getString(R.string.ssid))){
                        statusResponse = "SSID Correct";
                   // }else
                    //    statusResponse = "SSID Incorrect";

                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    // connected to mobile data
                    statusResponse = "Mobile data";
                    break;
                default:
                    break;
            }
        } else {
            Log.d("NetworkStatusManager", "Offline");
            statusResponse = "Offline";
        }
        return statusResponse;
    }
}
