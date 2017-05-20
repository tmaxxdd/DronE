package com.czterysery.hop.drone.Models;

/**
 * Created by tmax0 on 15.05.2017.
 */

public class MyDrone {
    private String image, header, description, telemetry, battery, motors, buzzer, bluetooth,
            wifi, gps, compass, receiver, transmitter, camera, gimbal, pwm,
            switches, battery_warner, controller;

    public MyDrone(String image, String header, String description, String telemetry, String battery,
                   String motors, String buzzer, String bluetooth, String wifi, String gps,
                   String compass, String receiver, String transmitter, String camera, String gimbal,
                   String pwm, String switches, String battery_warner, String controller) {
        this.image = image;
        this.header = header;
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
    }

    public String getImage() {
        return image;
    }

    public String getHeader() {
        return header;
    }

    public String getDescription() {
        return description;
    }
}
