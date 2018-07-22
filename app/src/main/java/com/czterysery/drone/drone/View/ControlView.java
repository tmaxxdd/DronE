package com.czterysery.drone.drone.View;

public interface ControlView {

    void showSSID(String ssid);

    void showSocket(String socket);

    void showEspStatus(String status);

    void showDroneStatus(String status);

    void showMessage(String message);

    void showConsoleLog(String message);

    void showProcessingDialog(String message);

    void hideProcessingDialog(String message);

    void setButtonDisconnect();

    void setButtonConnect();

    void setButtonSocketOn();

    void setButtonSocketOff();

    void setButtonSocketEnabled(boolean state);

}
