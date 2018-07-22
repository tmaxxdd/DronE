package com.czterysery.drone.drone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.czterysery.drone.drone.Model.Wifi.WifiHelper;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (netInfo != null && netInfo.isConnected())
        {
            Log.d("WifiReceiver", "Connection changed");
            WifiHelper.setWifiConnected(true);
        }

        if (netInfo != null && (!netInfo.isConnected() || netInfo.isFailover())) {
            Log.d("WifiReceiver", "Lost connection");
            WifiHelper.setWifiConnected(false);
        }
    }

}
