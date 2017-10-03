package com.czterysery.hop.drone;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by Tomasz Kądziołka on 08.05.2016.
 */
public class PhoneInfo {
    private int ID;
    private Activity activity;
    private TinyDB tinyDB;
    private Display display;

    public PhoneInfo(Activity activity) {
        this.activity = activity;

        tinyDB = new TinyDB(activity);
        display = activity.getWindowManager().getDefaultDisplay();
    }

    public int getID(){
        ID = tinyDB.getInt("deviceID");
        return ID;
    }

    public int getApi(){
        return android.os.Build.VERSION.SDK_INT;
    }


    public int getScreenHeight() {
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public int getScreenWidth() {
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getOrientation(){
        //0 = undefined
        //1 = landscape
        //2 = portrait
        return activity.getResources().getConfiguration().orientation;
    }
}
