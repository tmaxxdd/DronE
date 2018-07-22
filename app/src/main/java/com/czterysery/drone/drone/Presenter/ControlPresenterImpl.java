package com.czterysery.drone.drone.Presenter;

import android.net.wifi.WifiManager;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import com.czterysery.drone.drone.Commands;
import com.czterysery.drone.drone.Model.Socket.SocketInteractor;
import com.czterysery.drone.drone.Model.Socket.SocketInteractorImpl;
import com.czterysery.drone.drone.Parameters;
import com.czterysery.drone.drone.R;
import com.czterysery.drone.drone.View.ControlView;
import com.czterysery.drone.drone.Model.Wifi.WifiInteractorImpl;

public class ControlPresenterImpl implements ControlPresenter {
    private String TAG = this.getClass().getSimpleName();
    private ControlView controlView;
    private BottomSheetBehavior bmb;
    private WifiManager wifiManager;
    private WifiInteractorImpl wifiInteractor;
    private SocketInteractor socketInteractor;
    private Boolean buttonConnected = false;
    private Boolean serialConnected = false;
    private Boolean wifiConnected = false;

    public ControlPresenterImpl(ControlView controlView, WifiManager wifiManager) {
        this.controlView = controlView;
        this.wifiManager = wifiManager;
    }

    @Override
    public void onStart() {
        wifiInteractor = new WifiInteractorImpl(this, wifiManager);
        socketInteractor = new SocketInteractorImpl(this);
        if (controlView != null) {
            setStatus("***Console started***");
            //Start looking for a ESP
            wifiInteractor.registerWifiInterval();
        }
    }

    @Override
    public void onResume() {
        wifiInteractor.registerWifiReceiver();
    }

    @Override
    public void onPause() {
        wifiInteractor.unregisterWifiReceiver();
    }

    @Override
    public void onStop() {
        if (controlView != null) {
            setStatus("***Console stopped***");
        }
    }

    /**
     * Shows a Toast with the message
     */
    @Override
    public void showMessage(String message) {
        if (controlView != null) {
            controlView.showMessage(message);
        }
    }

    /**
     * Functions that react on the WifiTask
     */

    @Override
    public void onWifiSuccess(String ssid) {
        if (controlView != null) {
            wifiInteractor.registerWifiReceiver();
            if (wifiConnected) {
                controlView.showSSID(ssid);
                controlView.setButtonDisconnect();
                buttonConnected = true;
                setSocketButtonEnabled(true);
            }else {
                onWifiFailure("Not connected");
            }
        }
    }

    @Override
    public void onWifiFailure(String response) {
        if (controlView != null) {
            controlView.showSSID(response);
            controlView.setButtonConnect();
            buttonConnected = false;
            setSocketButtonEnabled(false);
        }
    }

    @Override
    public void onSocketSuccess(String message){
        if (controlView != null){
            setSocketName(message); //Socket host name (ip address)
            controlView.setButtonSocketOn();
            setSocketConnected(true);
        }
    }

    @Override
    public void onSocketFailure(String message) {
        if (controlView != null){
            setSocketName(message); //Socket host name (ip address)
            controlView.setButtonSocketOff();
            setSocketConnected(false);
        }
    }

    @Override
    public void onProcessStart(String message) {
        if (controlView != null) {
            //controlView.showProcessingDialog(message);
        }
    }


    /**
     * Helper function, print a status to both the UI and program log.
     */
    public void setStatus(String status) {
        if (controlView != null) {
            Log.v(TAG, status);
            controlView.showConsoleLog(status);
        }
    }

    /**
     * Invoked by the AsyncTask when a newline-delimited message is received.
     */
    @Override
    public void gotMessage(String msg) {
        if (controlView != null) {
            controlView.showConsoleLog(msg);
            Log.v(TAG, "[RX] " + msg);
        }
    }

    @Override
    public void setFlightMode(String flightMode) {
        if (controlView != null) {
            socketInteractor.setFlightMode(flightMode);
        }
    }

    @Override
    public void setRotorParam(int id, int newValue) {
        if (controlView != null) {
            //Switch between different sliders
            switch (id) {
                case R.id.control_controller_roll_slider:
                    //ROLL slider
                    controlView.showMessage("Roll: " + newValue);
                    String rollValue = Commands.SET_ROLL + String.valueOf(newValue);
                    socketInteractor.setRollValue(rollValue);
                    break;
                case R.id.control_controller_pitch_slider:
                    //PITCH slider
                    controlView.showMessage("Pitch: " + newValue);
                    String pitchValue = Commands.SET_PITCH + String.valueOf(newValue);
                    socketInteractor.setPitchValue(pitchValue);
                    break;
                case R.id.control_controller_throttle_slider:
                    //THROTTLE slider
                    controlView.showMessage("Throttle: " + newValue);
                    String throttleValue = Commands.SET_THROTTLE + String.valueOf(newValue);
                    socketInteractor.setThrottleValue(throttleValue);
                    break;
                case R.id.control_controller_yaw_slider:
                    //YAW slider
                    controlView.showMessage("Yaw: " + newValue);
                    String yawValue = Commands.SET_YAW + String.valueOf(newValue);
                    socketInteractor.setYawValue(yawValue);
                    break;
            }
        }
    }

    @Override
    public void onButtonClick(int view) {
        if (controlView != null) {
            switch (view) {
                case R.id.control_joystick_btn:
                    //JOYSTICK
                    controlView.showMessage("Joystick");
                    break;
                case R.id.control_arm_btn:
                    //ARM
                    socketInteractor.setArmed();
                    break;
                case R.id.control_logs_btn:
                    //LOGS
                    if (bmb.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        bmb.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    break;
                case R.id.control_connect_btn:
                    //CONNECT
                    //Start connecting
                    if (isWifiConnected()){
                        setWifiDisconnect();
                    }else{
                        wifiInteractor.tryToConnectToESP();
                    }
                    break;
                case R.id.control_serial_btn:
                    if (isSocketConnected()){
                        socketInteractor.disconnectFromSocket();
                    }else {
                        if (isWifiConnected()) {
                            socketInteractor.connectToTheSocket();
                        }else {
                            showMessage("Not connected to the ESP");
                        }
                    }
            }
        }
    }

    @Override
    public void setBottomSheetBehavior(BottomSheetBehavior bmb) {
        this.bmb = bmb;
    }

    @Override
    public void onWifiChanged() {
        wifiInteractor.registerWifiInterval();
    }

    @Override
    public void setWifiConnected(boolean connected) {
        wifiConnected = connected;
    }

    @Override
    public void setWifiDisconnect() {
        buttonConnected = false;
        wifiManager.disconnect();
    }

    @Override
    public Boolean isWifiConnected(){
        return wifiConnected;
    }

    @Override
    public void setSocketConnected(boolean connected){
        serialConnected = connected;
    }

    @Override
    public Boolean isSocketConnected() {
        return serialConnected;
    }

    @Override
    public void setSocketPing(boolean pinging) {
        if (controlView != null) {
            if (pinging) {
                controlView.showEspStatus(Commands.PINGING);
            } else {
                controlView.showEspStatus("Lost connection");
            }
        }
    }

    private void setSocketName(String name){
        if (controlView != null) {
            if (name.equals(Parameters.HOST_ADDRESS)) {
                controlView.showSocket(name);
            } else {
                controlView.showSocket(name);
            }
        }
    }

    private void setSocketButtonEnabled(boolean state){
        if (controlView != null) {
            controlView.setButtonSocketEnabled(state);
        }
    }
}