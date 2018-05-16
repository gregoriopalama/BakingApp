package com.gregoriopalama.udacity.bakingapp.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Utility class to check if the device is connected through a wifi connection
 *
 * @author Gregorio Palamà
 */

public class ConnectivityUtils {
    public static boolean isConnectedThroughWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if( wifiInfo.getNetworkId() < 0 ) {
                return false;
            }
            return true;
        }

        return false;
    }
}
