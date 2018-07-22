package com.czterysery.drone.drone.Model.Wifi;

import android.os.AsyncTask;

import com.czterysery.drone.drone.Model.Wifi.WifiInteractorImpl;

public class WifiIntervalTask extends AsyncTask<Void, Void, Void> {
    private WifiInteractorImpl interactor;

    WifiIntervalTask(WifiInteractorImpl wifiInteractor){
        this.interactor = wifiInteractor;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        interactor.checkCurrentWifi();
    }
}
