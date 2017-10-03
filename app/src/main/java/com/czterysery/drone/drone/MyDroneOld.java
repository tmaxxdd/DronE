package com.czterysery.drone.drone;

/**
 * Created by tmax0 on 20.05.2017.
 */

public class MyDroneOld {
    private String image, header, description;
    public MyDroneOld(String image, String header, String description) {
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
