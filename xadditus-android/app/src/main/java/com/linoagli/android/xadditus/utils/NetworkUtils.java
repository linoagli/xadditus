/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.utils
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtils {

    public static boolean isConnectedToWifi(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        boolean isConnected = ni != null && ni.isConnected();
        boolean isWifi = ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI;

        return isConnected && isWifi;
    }

    public static boolean isConnectedToBluetooth()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public static String getConnectionSSID(Context context)
    {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        String ssid = wi.getSSID();
        return (ssid != null)? ssid.replace("\"", "") : null;
    }

    public static String getLocalIpAddress(Context context)
    {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        int ip = wi.getIpAddress();
        return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
    }

    public static String getBroadcastIpAddress(Context context)
    {
        String ipString = getLocalIpAddress(context);
        return ipString.replaceAll("(\\d)+$", "255");
    }

    public static String getLocalMacAddress(Context context)
    {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        return wi.getMacAddress();
    }
}