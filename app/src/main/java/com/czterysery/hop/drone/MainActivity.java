package com.czterysery.hop.drone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.czterysery.hop.drone.Adapters.MyDroneAdapter;
import com.czterysery.hop.drone.Countries.CountryActivity;
import com.czterysery.hop.drone.Drone.ControlActivity;
import com.czterysery.hop.drone.Drone.MapsActivity;
import com.czterysery.hop.drone.Models.MyDrone;
import com.czterysery.hop.drone.More.AboutActivity;
import com.czterysery.hop.drone.More.ContactActivity;
import com.czterysery.hop.drone.More.SettingsActivity;
import com.github.fabtransitionactivity.SheetLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.rey.material.widget.Switch;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener {
    public final static String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;
    private MyThemeManager myThemeManager;
    private Toolbar toolbar;//Problem with menu icon with butterknife
    private View toolbarLayout;//Contains logo and switch
    private Drawer result;//Navigation drawer
    private RecyclerView.Adapter adapter;
    private ArrayList<MyDrone> drones = new ArrayList<>();
    private FirebaseHandler firebaseHandler;
    private DatabaseReference dronesDatabase;
    @BindView(R2.id.main_recyclerview)
    RecyclerView recyclerView;
    @BindView(R2.id.main_bottom_sheet)
    SheetLayout sheetLayout;
    @BindView(R2.id.main_fab)
    FloatingActionButton fab;
    @BindView(R2.id.main_refreshlayout)
    SwipyRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup app layout by color mode
        //Order methods below is very important!
        myThemeManager = new MyThemeManager(this);
        myThemeManager.chooseTheme();//Automatically set light or dark
        setContentView(R.layout.activity_main);

        initializeToolbarLayout();
        initializeToolbar();
        initializeNavigationDrawer();
        ButterKnife.bind(this);
        initializeRefreshLayout();
        //Butterknife things under bind
        sheetLayout.setFab(fab);
        sheetLayout.setFabAnimationEndListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Create adapter passing data about drones from DB
        adapter = new MyDroneAdapter(this, drones);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize handler and set listener
        initializeFirebase();

        initializeToolbarSwitch();
    }

    private void initializeFirebase() {
        firebaseHandler = new FirebaseHandler(this);
        dronesDatabase = firebaseHandler.getDronesRef();

        //TODO Divide on refresh and initialization
        dronesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                drones.clear();//Remove old info
                if (drones.size() == 0) {//Don't repeat drones
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        //Add all now
                        drones.add(data.getValue(MyDrone.class));
                    }
                }
                adapter.notifyDataSetChanged();//Show new data
                refreshLayout.setRefreshing(false);//Interrupt animation
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ", databaseError.toException());
            }
        });
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //Show drawer
            if (result.isDrawerOpen())
                result.closeDrawer();
                else result.openDrawer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void restartActivity(Activity act){
        Intent intent=new Intent();
        intent.setClass(act, act.getClass());
        ((Activity)act).startActivity(intent);
        ((Activity)act).finish();
    }

    private void toast(String message) {
        Toast.makeText(
                this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.main_fab)
    void onFabClick() {
        sheetLayout.expandFab();
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(this, AddDroneActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            sheetLayout.contractFab();
        }
    }

    private void initializeRefreshLayout(){
        refreshLayout.setOnRefreshListener(direction -> {
            initializeFirebase();//Refresh data
        });
    }

    private void initializeNavigationDrawer() {
        //Full documentation https://github.com/mikepenz/MaterialDrawer
        new DrawerBuilder().withActivity(this).build();
        //create the drawer and remember the `Drawer` result object
        PrimaryDrawerItem home = new PrimaryDrawerItem()
                .withIdentifier(1).withName("Home").withIcon(R.drawable.ic_home_green_24dp);
        PrimaryDrawerItem maps = new PrimaryDrawerItem()
                .withIdentifier(2).withName("Maps").withIcon(R.drawable.ic_map_green_24dp);
        PrimaryDrawerItem control = new PrimaryDrawerItem()
                .withIdentifier(3).withName("Control").withIcon(R.drawable.ic_control_green_24dp);
        PrimaryDrawerItem statistics = new PrimaryDrawerItem()
                .withIdentifier(4).withName("Checklist").withIcon(R.drawable.ic_done_all_green_24dp);

        SecondaryDrawerItem settings = new SecondaryDrawerItem()
                .withIdentifier(7).withName("Settings").withIcon(R.drawable.ic_settings_green_24dp);
        SecondaryDrawerItem open_source = new SecondaryDrawerItem()
                .withIdentifier(8).withName("Open Source").withIcon(R.drawable.ic_code_green_24dp);
        SecondaryDrawerItem contact = new SecondaryDrawerItem()
                .withIdentifier(9).withName("Contact").withIcon(R.drawable.ic_phone_green_24dp);
        SecondaryDrawerItem about = new SecondaryDrawerItem()
                .withIdentifier(10).withName("About").withIcon(R.drawable.ic_more_green_24dp);

        SecondaryDrawerItem croatia = new SecondaryDrawerItem()
                .withIdentifier(13).withName("Croatia").withIcon(R.drawable.ic_croatia_24dp);
        SecondaryDrawerItem poland = new SecondaryDrawerItem()
                .withIdentifier(14).withName("Poland").withIcon(R.drawable.ic_poland_24dp);
        SecondaryDrawerItem slovenia = new SecondaryDrawerItem()
                .withIdentifier(15).withName("Slovenia").withIcon(R.drawable.ic_slovenia_24dp);
        SecondaryDrawerItem spain = new SecondaryDrawerItem()
                .withIdentifier(16).withName("Spain").withIcon(R.drawable.ic_spain_24dp);

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new SecondaryDrawerItem()
                                .withName("Drone")
                                .withSelectable(false),
                        home,
                        maps,
                        control,
                        statistics,
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName("Extras")
                                .withSelectable(false),
                        settings,
                        open_source,
                        contact,
                        about,
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName("Countries")
                                .withSelectable(false),
                        croatia,
                        poland,
                        slovenia,
                        spain
                )
                .withFooter(R.layout.footer_layout)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    // do something with the clicked item :D
                    switch (position){
                        case 1:
                            //startActivity(new Intent(this, MainActivity.class));
                            toast("Welcome in project!");
                            break;
                        case 2:
                            startActivity(new Intent(this, MapsActivity.class));
                            break;
                        case 3:
                            startActivity(new Intent(this, ControlActivity.class));
                            break;
                        case 4:
                            startActivity(new Intent(this, CheckListActivity.class));
                            break;
                        case 7:
                            startActivity(new Intent(this, SettingsActivity.class));
                            break;
                        case 8:
                            openGithub();
                            break;
                        case 9:
                            startActivity(new Intent(this, ContactActivity.class));
                            break;
                        case 10:
                            startActivity(new Intent(this, AboutActivity.class));
                            break;
                        case 13:
                            Intent croatiaIntent = new Intent(this, CountryActivity.class);
                            croatiaIntent.putExtra("countryName", "Croatia");
                            startActivity(croatiaIntent);
                            break;
                        case 14:
                            Intent polandIntent = new Intent(this, CountryActivity.class);
                            polandIntent.putExtra("countryName", "Poland");
                            startActivity(polandIntent);
                            break;
                        case 15:
                            Intent sloveniaIntent = new Intent(this, CountryActivity.class);
                            sloveniaIntent.putExtra("countryName", "Slovenia");
                            startActivity(sloveniaIntent);
                            break;
                        case 16:
                            Intent spainIntent = new Intent(this, CountryActivity.class);
                            spainIntent.putExtra("countryName", "Spain");
                            startActivity(spainIntent);
                            break;
                    }
                    return true;
                })
                .withSelectedItem(2)
                .build();

        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        if (myThemeManager.getTheme() == MyThemeManager.LIGHT_THEME)
            result.getActionBarDrawerToggle().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        else result.getActionBarDrawerToggle().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }

    private void initializeToolbarLayout() {
        toolbarLayout = getLayoutInflater()
                .inflate(R.layout.toolbar_layout, null);
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.addView(toolbarLayout);
        ActionBar actionBar;
        if (toolbar != null) {
            toolbar.bringToFront();
            setSupportActionBar(toolbar);

            actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    private void initializeToolbarSwitch() {
        Switch modeSwitch = (Switch) toolbarLayout
                .findViewById(R.id.toolbar_layout_switch);

        //LIGHT is false
        //DARK is true
        modeSwitch.setChecked(myThemeManager.getTheme());

        modeSwitch.setOnCheckedChangeListener((view, checked) -> {
            if (checked == MyThemeManager.DARK_THEME){
                myThemeManager.setTheme(MyThemeManager.DARK_THEME);
                toast("set dark theme");
                restartActivity(this);
            }else{
                myThemeManager.setTheme(MyThemeManager.LIGHT_THEME);
                toast("set light theme");
                restartActivity(this);
            }
        });
    }

    private void openGithub() {
        String github = getResources().getString(R.string.github_link);
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(github));
        startActivity(intent);
    }
}
