package com.czterysery.drone.drone.Model.Socket;

import com.czterysery.drone.drone.Commands;
import com.czterysery.drone.drone.Model.Socket.SocketInteractor;
import com.czterysery.drone.drone.Parameters;
import com.czterysery.drone.drone.Presenter.ControlPresenter;
import com.czterysery.drone.drone.Presenter.ControlPresenterImpl;
import com.czterysery.drone.drone.Model.Socket.SocketTaskImpl;

public class SocketInteractorImpl implements SocketInteractor {
    private ControlPresenter controlPresenter;
    private SocketTaskImpl socketTask;


    public SocketInteractorImpl(ControlPresenterImpl controlPresenter){
        this.controlPresenter = controlPresenter;
    }

    @Override
    public void connectToTheSocket() {

        try {
            // Start the asynchronous task thread
            controlPresenter.setStatus("Attempting to connect...");
            socketTask = new SocketTaskImpl(controlPresenter,
                    Parameters.HOST_ADDRESS, Parameters.PORT);
            socketTask.run();
        } catch (Exception e) {
            e.printStackTrace();
            controlPresenter.setStatus("Can't connect to the socket!");
        }
    }

    @Override
    public void setFlightMode(String flightMode) {
        try {
            switch (flightMode){
                case "AltHold":
                    socketTask.sendMessage(Commands.SET_FLIGHT_MODE_ALTHOLD);
                    break;
                case "Loiter":
                    socketTask.sendMessage(Commands.SET_FLIGHT_MODE_LOITER);
                    break;
                case "Stabilize":
                    socketTask.sendMessage(Commands.SET_FLIGHT_MODE_STABILIZE);
                    break;
                case "AutoReturn":
                    socketTask.sendMessage(Commands.SET_FLIGHT_MODE_AUTO_RETURN);
                    break;

                default:
                    socketTask.sendMessage("Undefined flight mode");
                    break;

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setArmed() {
        try {
            socketTask.sendMessage(Commands.SET_ARM);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setDisarmed() {
        try {
            socketTask.sendMessage(Commands.SET_DISARM);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setRollValue(String rollValue) {
        try {
            socketTask.sendMessage(Commands.SET_ROLL + rollValue);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setPitchValue(String pitchValue) {
        try {
            socketTask.sendMessage(Commands.SET_PITCH + pitchValue);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setThrottleValue(String throttleValue) {
        try {
            socketTask.sendMessage(Commands.SET_THROTTLE + throttleValue);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setYawValue(String yawValue) {
        try {
            socketTask.sendMessage(Commands.SET_YAW + yawValue);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void disconnectFromSocket() {
        try {
            socketTask.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
