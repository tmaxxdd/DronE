package com.czterysery.drone.drone.Model.Wifi;

import android.net.wifi.WifiManager;

import com.czterysery.drone.drone.Parameters;
import com.czterysery.drone.drone.Presenter.ControlPresenter;
import com.czterysery.drone.drone.Presenter.ControlPresenterImpl;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class WifiInteractorImpl implements WifiInteractor {
    private final String TAG = this.getClass().getSimpleName();
    private ControlPresenter controlPresenter;
    private WifiManager wifiManager;
    private WifiInteractorImpl interactor;

    public WifiInteractorImpl(ControlPresenterImpl controlPresenter,
                              WifiManager wifiManager){
        this.controlPresenter = controlPresenter;
        this.wifiManager = wifiManager;
        this.interactor = this;
    }

    @Override
    public void registerWifiReceiver() {

        WifiHelper.setWifiListener(connected -> {
            if (connected){
                controlPresenter.setWifiConnected(true);
                controlPresenter.onWifiChanged();
            }else {
                controlPresenter.setWifiConnected(false);
                controlPresenter.onWifiChanged();
            }
        });
    }

    @Override
    public void unregisterWifiReceiver() {

    }

    @Override
    public void registerWifiInterval() {

        ScheduledThreadPoolExecutor deamon = new ScheduledThreadPoolExecutor(1);

        deamon.scheduleAtFixedRate(() -> {
            WifiIntervalTask task =
                    new WifiIntervalTask(interactor);
            task.execute();
        }, 0L, 1000L, TimeUnit.MILLISECONDS);

    }

    public void tryToConnectToESP(){
        WifiTask wifiTask = new WifiTask(controlPresenter, wifiManager);
        if (wifiManager.isWifiEnabled()) {
            wifiTask.execute();
        }else {
            controlPresenter.showMessage("Please turn on wifi");
            controlPresenter.onWifiFailure("Wifi is OFF");
        }
    }

    @Override
    public void checkCurrentWifi(){
        String ssid = null;

        if (controlPresenter.isWifiConnected()) {
            ssid = wifiManager.getConnectionInfo().getSSID();
        }

        if(ssid != null) {
            //Check if device is connected to the proper wifi
            if (ssid.equals("\"" + Parameters.WIFI_NAME + "\"") && controlPresenter.isWifiConnected()) {
                controlPresenter.onWifiSuccess(ssid);
            } else {
                controlPresenter.onWifiFailure("Wrong network");
            }
        }else {
            controlPresenter.onWifiFailure("Not found");
        }
    }

}
