package com.czterysery.drone.drone.Model.Socket;

public interface SocketInteractor {

    void connectToTheSocket();

    void setFlightMode(String flightMode);

    void setArmed();

    void setDisarmed();

    void setRollValue(String rollValue);

    void setPitchValue(String pitchValue);

    void setThrottleValue(String throttleValue);

    void setYawValue(String yawValue);

    void disconnectFromSocket();
}
