package com.czterysery.hop.drone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Ekran_ladowania extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekran_ladowania);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               startActivity(new Intent(Ekran_ladowania.this, MainActivity.class));
               finish();
           }
       }, 2000);
    }

}
