/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.helpers
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.helpers

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import org.apache.commons.codec.binary.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object Utils {
    fun isConnectedToWifi(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        val isConnected = ni != null && ni.isConnected
        val isWifi = ni != null && ni.type == ConnectivityManager.TYPE_WIFI

        return isConnected && isWifi
    }

    fun isConnectedToBluetooth(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled
    }

    fun getConnectionSSID(context: Context): String? {
        val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wi = wm.connectionInfo
        val ssid = wi.ssid
        return ssid?.replace("\"", "")
    }

    fun getLocalIpAddress(context: Context): String {
        val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wi = wm.connectionInfo
        val ip = wi.ipAddress
        return String.format("%d.%d.%d.%d", ip and 0xff, ip shr 8 and 0xff, ip shr 16 and 0xff, ip shr 24 and 0xff)
    }

    fun getBroadcastIpAddress(context: Context): String {
        val ipString = getLocalIpAddress(context)
        return ipString.replace("(\\d)+$".toRegex(), "255")
    }

    fun getLocalMacAddress(context: Context): String {
        val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wi = wm.connectionInfo
        return wi.macAddress
    }

    fun encodeObject(obj: Any): String? {
        try {
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            oos.writeObject(obj)
            oos.flush()
            oos.close()

            return Base64.encodeBase64String(baos.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun decodeObject(data: String): Any? {
        try {
            val byteArray = Base64.decodeBase64(data)
            val bais = ByteArrayInputStream(byteArray)
            val ois = ObjectInputStream(bais)
            return ois.readObject()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }
}