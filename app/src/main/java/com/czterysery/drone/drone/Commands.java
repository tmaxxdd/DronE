package com.czterysery.drone.drone;

public final class Commands {
    //Reflects connection with the ESP32
    public final static String PINGING = "PINGING";
    //Reflects connection with the drone
    public final static String HEARTBEATING = "HEARTBEATING";
    //Shows drone's state
    public final static String ARMED = "ARMED";
    public final static String DISARMED = "DISARMED";
    //Sets current flight mode
    public final static String SET_FLIGHT_MODE_ALTHOLD = "SET_FLIGHT_MODE_ALTHOLD";
    public final static String SET_FLIGHT_MODE_LOITER = "SET_FLIGHT_MODE_LOITER";
    public final static String SET_FLIGHT_MODE_STABILIZE = "SET_FLIGHT_MODE_STABILIZE";
    public final static String SET_FLIGHT_MODE_AUTO_RETURN = "SET_FLIGHT_MODE_AUTO";
    public final static String SET_FLIGHT_MODE_CIRCLE = "SET_FLIGHT_MODE_CIRCLE";
    //Sets rotors values
    public final static String SET_ROLL = "SET_ROLL_";//+ int
    public final static String SET_PITCH = "SET_PITCH_";//+ int
    public final static String SET_THROTTLE = "SET_THROTTLE_";//+ int
    public final static String SET_YAW = "SET_YAW_";//+ int
    //Sets elementary actions
    public final static String SET_ARM = "SET_ARM";
    public final static String SET_DISARM = "SET_DISARM";
}
