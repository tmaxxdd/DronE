package com.czterysery.drone.drone.Model.Wifi;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import com.czterysery.drone.drone.Parameters;
import com.czterysery.drone.drone.Presenter.ControlPresenter;
import com.czterysery.drone.drone.Presenter.ControlPresenterImpl;

import java.util.List;

public class WifiTask extends AsyncTask<Void, String, String> {
    private WifiManager wifiManager;
    private ControlPresenter controlPresenter;

    public WifiTask(ControlPresenter controlPresenter, WifiManager wifiManager) {
        this.wifiManager = wifiManager;
        this.controlPresenter = controlPresenter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        controlPresenter.onProcessStart("Initiating task...");
    }

    @Override
    protected String doInBackground(Void... voids) {

        /* Get list with the configured(saved) networks */
        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();

        /* Create wifi object */
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + Parameters.WIFI_NAME + "\"";   // Please note the quotes. String should contain ssid in quotes
        conf.preSharedKey = "\""+ Parameters.WIFI_PASSWORD +"\"";
        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN); //Needed to connect via android

        /* Don't allow to create duplicates */
        if (configExist(configs, conf)) {
            publishProgress("Found config");
        }else{
            publishProgress("Creating network config...");
            wifiManager.addNetwork(conf);
            //Refresh the configurations list
            configs = wifiManager.getConfiguredNetworks();
        }

        for(WifiConfiguration i : configs) {

            if (i.SSID != null && i.SSID.equals("\"" + Parameters.WIFI_NAME + "\"")) {

                wifiManager.disconnect();
                publishProgress("Attempting to connect...");
                wifiManager.enableNetwork(i.networkId, true);
                break;
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        controlPresenter.showMessage(values[0]);
    }

    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);

    }

    private Boolean configExist(List<WifiConfiguration> configs, WifiConfiguration conf){
        for(WifiConfiguration i : configs ) {
            if (i.SSID.equals(conf.SSID))
                return true;
        }
        return false;
    }

}
