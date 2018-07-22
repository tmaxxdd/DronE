package com.czterysery.drone.drone.Model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by tmax0 on 15.05.2017.
 */
@IgnoreExtraProperties
public class MyDrone {
    public String image, country, name, description, telemetry, battery, motors, buzzer, bluetooth,
            wifi, gps, compass, receiver, transmitter, camera, gimbal, pwm,
            switches, battery_warner, controller;


    public MyDrone(){
        // Default constructor required for calls to DataSnapshot.getValue(MyDrone.class)
    }

    public MyDrone(String image, String country, String name, String description, String telemetry,
                   String battery, String motors, String buzzer, String bluetooth, String wifi, String gps,
                   String compass, String receiver, String transmitter, String camera, String gimbal,
                   String pwm, String switches, String battery_warner, String controller) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.telemetry = telemetry;
        this.battery = battery;
        this.motors = motors;
        this.buzzer = buzzer;
        this.bluetooth = bluetooth;
        this.wifi = wifi;
        this.gps = gps;
        this.compass = compass;
        this.receiver = receiver;
        this.transmitter = transmitter;
        this.camera = camera;
        this.gimbal = gimbal;
        this.pwm = pwm;
        this.switches = switches;
        this.battery_warner = battery_warner;
        this.controller = controller;
        this.country = country;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public String getTelemetry() {
        return telemetry;
    }

    public String getBattery() {
        return battery;
    }

    public String getMotors() {
        return motors;
    }

    public String getBuzzer() {
        return buzzer;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public String getWifi() {
        return wifi;
    }

    public String getGps() {
        return gps;
    }

    public String getCompass() {
        return compass;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getTransmitter() {
        return transmitter;
    }

    public String getCamera() {
        return camera;
    }

    public String getGimbal() {
        return gimbal;
    }

    public String getPwm() {
        return pwm;
    }

    public String getSwitches() {
        return switches;
    }

    public String getBattery_warner() {
        return battery_warner;
    }

    public String getController() {
        return controller;
    }
}
