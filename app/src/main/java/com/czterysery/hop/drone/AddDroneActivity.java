package com.czterysery.hop.drone;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by tmax0 on 16.05.2017.
 */

public class AddDroneActivity extends Activity {
    private MyThemeManager myThemeManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myThemeManager = new MyThemeManager(this);
        myThemeManager.chooseTheme();//Automatically set light or dark
        setContentView(R.layout.add_drone_layout);
    }
}
