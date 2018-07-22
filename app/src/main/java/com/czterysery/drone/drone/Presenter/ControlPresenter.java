package com.czterysery.drone.drone.Presenter;

import android.support.design.widget.BottomSheetBehavior;

public interface ControlPresenter {

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void showMessage(String message);

    void onWifiSuccess(String ssid);

    void onSocketSuccess(String message);

    void onSocketFailure(String message);

    void onWifiFailure(String response);

    void onProcessStart(String message);

    void setStatus(String status);

    void gotMessage(String msg);

    void setFlightMode(String flightMode);

    void setRotorParam(int id, int newValue);

    void onButtonClick(int view);

    void setBottomSheetBehavior(BottomSheetBehavior bmb);

    void onWifiChanged();

    void setWifiConnected(boolean connected);

    void setWifiDisconnect();

    Boolean isWifiConnected();

    void setSocketConnected(boolean connected);

    Boolean isSocketConnected();

    void setSocketPing(boolean pinging);
}
