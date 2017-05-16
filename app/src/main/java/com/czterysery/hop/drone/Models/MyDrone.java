package com.czterysery.hop.drone.Models;

/**
 * Created by tmax0 on 15.05.2017.
 */

public class MyDrone {
    private String image, header, description;

    public MyDrone(String image, String header, String description) {
        this.image = image;
        this.header = header;
        this.description = description;
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
