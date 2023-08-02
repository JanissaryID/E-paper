package com.example.e_paper.classes

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log
import com.example.e_paper.MainActivity

class WifiConnector(private val context: Context) {

    private val wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private var mainActivityVar: MainActivity? = null

    private val _ssid = "EPD APK"

    fun getMainActivity(mainActivity: MainActivity){
        mainActivityVar = mainActivity
    }

    fun CheckWifiState(): Boolean{
        Log.i("Network", "Wifi Status = ${wifiManager.isWifiEnabled}")
        return wifiManager.isWifiEnabled
    }

    fun GetInfoWifi(): Boolean{
        val wifiInfo: WifiInfo? = wifiManager.connectionInfo
        var ssid: String = wifiInfo!!.ssid
        Log.i("Network", "SSID = ${ssid.length} -- Pass = \"${_ssid.length}\"")

        return if(ssid == "\"$_ssid\"") true else false
    }
}
