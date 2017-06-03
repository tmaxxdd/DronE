package com.czterysery.drone.drone.Drone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.czterysery.drone.drone.MyThemeManager;
import com.czterysery.drone.drone.R;
import com.czterysery.drone.drone.R2;

import butterknife.BindView;

/**
 * Created by tmax0 on 15.05.2017.
 */

public class ControlActivity extends AppCompatActivity {
    private MyThemeManager myThemeManager;
    @BindView(R2.id.control_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myThemeManager = new MyThemeManager(this);
        myThemeManager.chooseTheme();
        setContentView(R.layout.control_layout);
        initializeToolbar();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeToolbar() {
        ActionBar actionBar;
        if (toolbar != null) {
            toolbar.bringToFront();
            setSupportActionBar(toolbar);

            actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Control");
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
                if (myThemeManager.getTheme() == MyThemeManager.DARK_THEME)
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
                else actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            }
        }
    }
}
