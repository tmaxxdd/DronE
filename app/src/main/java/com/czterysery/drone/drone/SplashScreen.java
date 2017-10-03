package com.czterysery.drone.drone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        //Run after 2 secs
       new Handler().postDelayed(() -> {
           startActivity(new Intent(SplashScreen.this, MainActivity.class));
           finish();
       }, 2000);
    }

}
