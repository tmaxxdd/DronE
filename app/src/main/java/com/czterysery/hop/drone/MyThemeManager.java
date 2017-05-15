package com.czterysery.hop.drone;
import android.app.Activity;

/**
 * Created by tmax0 on 14.05.2017.
 */

public class MyThemeManager {
    public final static boolean LIGHT_THEME = false;
    public final static boolean DARK_THEME = true;
    private static boolean theme;
    private Activity activity;
    private TinyDB tinyDB;

    public MyThemeManager(Activity activity) {
        this.activity = activity;
        tinyDB = new TinyDB(activity);
    }

    public void setTheme(boolean theme) {
        MyThemeManager.theme = theme;
        tinyDB.putBoolean("viewMode", theme);
    }

    public boolean getTheme(){
        return tinyDB.getBoolean("viewMode");
    }

    //Used to decode boolean value on human-readable string
    public String toText(boolean state){
        if (state == LIGHT_THEME){
            return "LIGHT_THEME";
        }else{
            return "DARK_THEME";
        }
    }

    public void chooseTheme(){
        if (getTheme() == MyThemeManager.LIGHT_THEME){
            activity.setTheme(R.style.AppTheme);
        }else{
            activity.setTheme(R.style.AppTheme_Dark);
        }
    }

}
