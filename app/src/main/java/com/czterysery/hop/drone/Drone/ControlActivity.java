package com.czterysery.hop.drone.Drone;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.czterysery.hop.drone.MyThemeManager;
import com.czterysery.hop.drone.R;

/**
 * Created by tmax0 on 15.05.2017.
 */

public class ControlActivity extends Activity {
    private MyThemeManager themeManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeManager = new MyThemeManager(this);
        themeManager.chooseTheme();
        setContentView(R.layout.control_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
