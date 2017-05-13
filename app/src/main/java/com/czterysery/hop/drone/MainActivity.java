package com.czterysery.hop.drone;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.rey.material.widget.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import static android.R.attr.onClick;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = "MainActivity";
    TabPageIndicator tabPageIndicator;
    ViewPager viewPager;
    AdapterStron adapterStron;
    List<Fragment> main_strony;
    @BindView(R.id.view) View view;
    TabPageIndicator mainTabindicator;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup app layout by color mode
        /*
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("pref_dark_theme"),false)){
            setTheme(R.style.AppTheme_Dark);
            */
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toolbar_switch_layout, null, false);
        ButterKnife.bind(this, view);

        toast( "Witaj w aplikacji!");

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ///
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MapsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
