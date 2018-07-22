package com.czterysery.drone.drone.Model.Socket;

interface SocketTask {

    void run();

    void sendMessage(String message);

    void disconnect();


}
