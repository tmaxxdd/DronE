package com.czterysery.hop.drone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.czterysery.hop.drone.Models.MyDrone;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tmax0 on 30.05.2017.
 */

public class ReadDroneActivity extends AppCompatActivity {
    private static final String TAG = "ReadDroneActivity";
    private MyThemeManager myThemeManager;
    private FirebaseHandler firebaseHandler;
    private DatabaseReference dronesDatabase;
    private LayoutWorker layoutWorker;
    private String droneName;
    @BindView(R2.id.read_drone_toolbar)
    Toolbar toolbar;
    @BindView(R2.id.read_drone_imageview)
    ImageView imageView;
    @BindView(R2.id.read_drone_header_name)
    TextView nameView;
    @BindView(R2.id.read_drone_header_description)
    TextView descriptionView;
    @BindView(R2.id.read_drone_telemetry_textview)
    TextView telemetryView;
    @BindView(R2.id.read_drone_battery_textview)
    TextView batteryView;
    @BindView(R2.id.read_drone_motors_textview)
    TextView motorsView;
    @BindView(R2.id.read_drone_buzzer_textview)
    TextView buzzerView;
    @BindView(R2.id.read_drone_bluetooth_textview)
    TextView bluetoothView;
    @BindView(R2.id.read_drone_wifi_textview)
    TextView wifiView;
    @BindView(R2.id.read_drone_gps_textview)
    TextView gpsView;
    @BindView(R2.id.read_drone_compass_textview)
    TextView compassView;
    @BindView(R2.id.read_drone_receiver_textview)
    TextView receiverView;
    @BindView(R2.id.read_drone_transmitter_textview)
    TextView transmitterView;
    @BindView(R2.id.read_drone_camera_textview)
    TextView cameraView;
    @BindView(R2.id.read_drone_pwm_textview)
    TextView pwmView;
    @BindView(R2.id.read_drone_switches_textview)
    TextView switchesView;
    @BindView(R2.id.read_drone_battery_warner_textview)
    TextView batteryWarnerView;
    @BindView(R2.id.read_drone_gimbal_textview)
    TextView gimbalView;
    @BindView(R2.id.read_drone_controller_textview)
    TextView controllerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup app layout by color mode
        //Order methods below is very important!
        myThemeManager = new MyThemeManager(this);
        myThemeManager.chooseTheme();//Automatically set light or dark
        setContentView(R.layout.read_drone_activity);
        //Get clicked drone
        Bundle extras = getIntent().getExtras();
        droneName = extras.getString("droneName");
        ButterKnife.bind(this);

        layoutWorker = new LayoutWorker(this);
        //layoutWorker.calculateImageViewHeight(imageView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializeFirebase();
    }

    private void initializeFirebase() {
        firebaseHandler = new FirebaseHandler(this);
        dronesDatabase = firebaseHandler.getDronesRef().child(droneName);

        dronesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyDrone myDrone = dataSnapshot.getValue(MyDrone.class);
                loadValues(myDrone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ", databaseError.toException());
            }
        });
    }

    private void loadValues(MyDrone myDrone){
        Picasso.with(this).load(myDrone.getImage()).into(imageView);
        nameView.setText(myDrone.getName());
        descriptionView.setText(myDrone.getDescription());
        telemetryView.setText(myDrone.getTelemetry());
        batteryView.setText(myDrone.getBattery());
        motorsView.setText(myDrone.getMotors());
        buzzerView.setText(myDrone.getBuzzer());
        bluetoothView.setText(myDrone.getBluetooth());
        wifiView.setText(myDrone.getWifi());
        gpsView.setText(myDrone.getGps());
        compassView.setText(myDrone.getCompass());
        receiverView.setText(myDrone.getReceiver());
        transmitterView.setText(myDrone.getTransmitter());
        cameraView.setText(myDrone.getCamera());
        pwmView.setText(myDrone.getPwm());
        switchesView.setText(myDrone.getSwitches());
        batteryWarnerView.setText(myDrone.getBattery_warner());
        gimbalView.setText(myDrone.getGimbal());
        controllerView.setText(myDrone.getController());
    }
}
