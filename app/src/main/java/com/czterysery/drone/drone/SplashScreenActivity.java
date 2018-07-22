package com.czterysery.drone.drone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        /* Show drone's welcome page for 2 sec. */
       new Handler().postDelayed(() -> {
           startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
           finish();
       }, 2000);
    }

}
