package com.czterysery.drone.drone.Model.Wifi;

interface WifiInteractor {

    void registerWifiReceiver();

    void unregisterWifiReceiver();

    void checkCurrentWifi();

    void registerWifiInterval();

    void tryToConnectToESP();
}
