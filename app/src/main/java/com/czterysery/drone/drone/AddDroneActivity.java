package com.czterysery.drone.drone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.czterysery.drone.drone.Models.MyDrone;
import com.github.chuross.library.ExpandableLayout;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.Spinner;

import net.gotev.uploadservice.UploadService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tmax0 on 16.05.2017.
 */

public class AddDroneActivity extends AppCompatActivity {
    private static final String TAG = "AddDroneActivity";
    private static final String IMAGES_DIRECTORY = "http://hoptimist.pl/drone/images/";
    private MyThemeManager myThemeManager;
    private PhoneInfo info;
    private String imageName;
    private String droneImage;
    //Temporary image
    @BindView(R2.id.add_drone_toolbar)
    Toolbar toolbar;
    @BindView(R2.id.add_drone_imageview)
    ImageView imageView;
    @BindView(R2.id.add_drone_telemetry_edittext)
    MaterialEditText telemetryEditText;
    @BindView(R2.id.add_drone_battery_edittext)
    MaterialEditText batteryEditText;
    @BindView(R2.id.add_drone_motors_edittext)
    MaterialEditText motorsEditText;
    @BindView(R2.id.add_drone_buzzer_edittext)
    MaterialEditText buzzerEditText;
    @BindView(R2.id.add_drone_bluetooth_edittext)
    MaterialEditText bluetoothEditText;
    @BindView(R2.id.add_drone_wifi_edittext)
    MaterialEditText wifiEditText;
    @BindView(R2.id.add_drone_gps_edittext)
    MaterialEditText gpsEditText;
    @BindView(R2.id.add_drone_compass_edittext)
    MaterialEditText compassEditText;
    @BindView(R2.id.add_drone_receiver_edittext)
    MaterialEditText receiverEditText;
    @BindView(R2.id.add_drone_transmitter_edittext)
    MaterialEditText transmitterEditText;
    @BindView(R2.id.add_drone_camera_edittext)
    MaterialEditText cameraEditText;
    @BindView(R2.id.add_drone_gimbal_edittext)
    MaterialEditText gimbalEditText;
    @BindView(R2.id.add_drone_pwm_edittext)
    MaterialEditText pwmEditText;
    @BindView(R2.id.add_drone_switch_edittext)
    MaterialEditText switchEditText;
    @BindView(R2.id.add_drone_battery_warner_edittext)
    MaterialEditText batterywarnerEditText;
    @BindView(R2.id.add_drone_controller_edittext)
    MaterialEditText controllerEditText;
    @BindView(R2.id.add_drone_name_edittext)
    MaterialEditText nameEditText;
    @BindView(R2.id.add_drone_description_edittext)
    MaterialEditText descriptionEditText;
    @BindView(R2.id.add_drone_files_button)
    Button filesButton;
    @BindView(R2.id.add_drone_camera_button)
    Button cameraButton;
    @BindView(R2.id.add_drone_specification_button)
    Button specificationButton;
    @BindView(R2.id.add_drone_image_layout)
    RelativeLayout imageLayout;
    @BindView(R2.id.add_drone_country_spinner)
    Spinner countrySpinner;
    @BindView(R2.id.add_drone_expandable_view)
    ExpandableLayout expandableLayout;
    private String countryName, droneName, droneDescription, droneTransmitter, droneTelemetry, droneBattery,
            droneMotors, droneBuzzer, droneBluetooth, droneWifi, droneGps, droneCompass, droneReceiver,
            droneCamera, droneGimbal, dronePwm, droneSwitch, droneBatteryWarner, droneController;
    private ArrayList<String> emptyFields = new ArrayList<>();
    private FirebaseHandler firebase;
    private DatabaseReference dronesDatabase;
    private ImageWorker worker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setup the broadcast action namespace string which will
        // be used to notify upload status.
        // Gradle automatically generates proper variable as below.
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Or, you can define it manually.
        UploadService.NAMESPACE = "com.czterysery.hop.drone";
        myThemeManager = new MyThemeManager(this);
        myThemeManager.chooseTheme();//Automatically set light or dark
        setContentView(R.layout.add_drone_layout);
        info = new PhoneInfo(this);
        ButterKnife.bind(this);
        initializeToolbar();
        initializeSpinner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        worker = new ImageWorker(this);//Run only once

        firebase = new FirebaseHandler(this);
        dronesDatabase = firebase.getDronesRef();
        expandableLayout.expand();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_drone, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                tryClose();
                break;
            case R.id.save_button:
                trySave();
                break;
        }
        return true;
    }

    private void trySave() {
        if (isOnline()){
            toast("Uploading to database...");
            pushObjectToFirebase();
        }else{
            toast("No internet connection. Saving to offline...");
            pushObjectToOffline();
        }
    }

    private void tryClose() {
        //Ask user if sure to exit
        Dialogs dialogs = new Dialogs(this);
        Dialog cancelDialog =
                dialogs.getCancelAddDrone();
        cancelDialog.show();
    }

    private void pushObjectToOffline() {
        boolean formFilled = checkIfFormIsCorrect();

        if (formFilled) {//Important fields are filled
            MyDrone myDrone = new MyDrone(droneImage, countryName, droneName, droneDescription, droneTelemetry,
                    droneBattery, droneMotors, droneBuzzer, droneBluetooth, droneWifi, droneGps,
                    droneCompass, droneReceiver, droneTransmitter, droneCamera, droneGimbal, dronePwm,
                    droneSwitch, droneBatteryWarner, droneController);

            dronesDatabase.child(droneName).setValue(myDrone).addOnSuccessListener(task -> {
                toast("Success!");
                finish();
            });
        }else{
            toast("Complete: " + emptyFields + " and please again.");
        }
    }

    private void pushObjectToFirebase(){
        boolean formFilled = checkIfFormIsCorrect();

        if (formFilled){//Important fileds filled
            MyDrone myDrone = new MyDrone(droneImage, countryName, droneName, droneDescription, droneTelemetry,
                    droneBattery, droneMotors, droneBuzzer, droneBluetooth, droneWifi, droneGps,
                    droneCompass, droneReceiver, droneTransmitter, droneCamera, droneGimbal, dronePwm,
                    droneSwitch, droneBatteryWarner, droneController);

            dronesDatabase.child(droneName).setValue(myDrone)
                    .addOnCompleteListener(task -> {
                        toast("Added successful.");
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        toast("Problem. Not added drone.");
                        Log.d(TAG, "pushObjectToFirebase: " + e.getMessage());
                    });
        }else{
            toast("Complete: " + emptyFields + " and please again.");
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void toast(String message) {
        Toast.makeText(
                this, message, Toast.LENGTH_SHORT).show();
    }

    /*
    User have to input drone's:
    Name
    Country
    Description
    Other fields are not mandatory and can be empty.
     */
    private boolean checkIfFormIsCorrect(){

        //Country
        countryName = (String) countrySpinner.getSelectedItem();
        ///Strings only allowed from adapter
        if (countryName.equals("None")){
            emptyFields.add("Country");
            return false;
        }

        //Name
        droneName = nameEditText
                .getEditableText().toString();
        if (droneName.equals("")){
            emptyFields.add("Name");
            return false;
        }

        //Description
        droneDescription = descriptionEditText
                .getEditableText().toString();

        if (droneDescription.equals("")){
            emptyFields.add("Description");
            return false;
        }

        //Transmitter
        droneTransmitter = transmitterEditText
                .getEditableText().toString();
        if (droneTransmitter.equals("")){
            droneTransmitter = "None";
        }

        //Battery
        droneBattery = batteryEditText
                .getEditableText().toString();
        if (droneBattery.equals("")){
            droneBattery = "None";
        }

        //Motors
        droneMotors = motorsEditText
                .getEditableText().toString();
        if (droneMotors.equals("")){
            droneMotors = "None";
        }

        //Bluetooth
        droneBluetooth = bluetoothEditText
                .getEditableText().toString();
        if (droneBluetooth.equals("")){
            droneBluetooth = "None";
        }

        //Buzzer
        droneBuzzer = buzzerEditText
                .getEditableText().toString();
        if (droneBuzzer.equals("")){
            droneBuzzer = "None";
        }

        //WiFi
        droneWifi = wifiEditText
                .getEditableText().toString();
        if (droneWifi.equals("")){
            droneWifi = "None";
        }

        //GPS
        droneGps = gpsEditText
                .getEditableText().toString();
        if (droneGps.equals("")){
            droneGps = "None";
        }

        //Compass
        droneCompass = compassEditText
                .getEditableText().toString();
        if (droneCompass.equals("")){
            droneCompass = "None";
        }

        //Receiver
        droneReceiver = receiverEditText
                .getEditableText().toString();
        if (droneReceiver.equals("")){
            droneReceiver = "None";
        }

        //Telemetry
        droneTelemetry = telemetryEditText
                .getEditableText().toString();
        if (droneTelemetry.equals("")){
            droneTelemetry = "None";
        }

        //Camera
        droneCamera = cameraEditText
                .getEditableText().toString();
        if (droneCamera.equals("")){
            droneCamera = "None";
        }

        //Gimbal
        droneGimbal = gimbalEditText
                .getEditableText().toString();
        if (droneGimbal.equals("")){
            droneGimbal = "None";
        }

        //PWM
        dronePwm = pwmEditText
                .getEditableText().toString();
        if (dronePwm.equals("")){
            dronePwm = "None";
        }

        //Switch
        droneSwitch = switchEditText
                .getEditableText().toString();
        if (droneSwitch.equals("")){
            droneSwitch = "None";
        }

        //BatteryWarner
        droneBatteryWarner = batterywarnerEditText
                .getEditableText().toString();
        if (droneBatteryWarner.equals("")){
            droneBatteryWarner = "None";
        }

        //Controller
        droneController = controllerEditText
                .getEditableText().toString();
        if (droneController.equals("")){
            droneController = "None";
        }

        //Function fields are ok, can continue
        return true;
    }

    private void initializeSpinner() {
        String[] countriesArray =
                getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.row_spn, countriesArray);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        countrySpinner.setAdapter(adapter);
    }

    private void initializeToolbar() {
        ActionBar actionBar;
        if (toolbar != null) {
            toolbar.bringToFront();
            setSupportActionBar(toolbar);

            actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Add drone");
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

    @OnClick(R2.id.add_drone_specification_button)
    public void changeLayoutExpand(){
        if (expandableLayout.isExpanded()){
            //Visible
            expandableLayout.collapse();
            toast("Hide specific data");
        }else{
            //Hided
            expandableLayout.expand();
            toast("Show specific data");
        }
    }

    @OnClick(R2.id.add_drone_files_button)
    public void selectFromFiles(){
        worker.runGalleryIntent();
    }

    @OnClick(R2.id.add_drone_camera_button)
    public void selectFromCamera(){
        worker.runCameraIntent();
    }

    //This onActivityResult must be in main activity that ImageWorker is invoked.
    //The cycle is like Activity -> ImageWorker -> onActivityResult -> ImageWorker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ImageWorker.PICK_IMAGE_GALLERY:
                if (resultCode == RESULT_OK &&
                        data != null && data.getData() != null){
                    Uri fileUri = data.getData();
                    String finalPath = worker.getGeneratedPath();

                    Bitmap bitmap = worker.getBitmapFromUri(fileUri);
                    worker.storeImage(bitmap, finalPath);//Save bitmap in generated path
                    makeImageUrl();//To make droneImage
                    worker.uploadAndShowImage(finalPath, imageView, droneImage);

                    toast("Success on gallery");
                }else{
                    toast("Something went wrong. Can't make image");
                    Log.d(TAG, "onActivityResult: Error gallery");
                }
                break;
            case ImageWorker.PICK_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    //Don't store image. Photo was saved by intent
                    String finalPath = worker.getGeneratedPath();//Get raw path
                    makeImageUrl();//To make droneImage
                    worker.uploadAndShowImage(finalPath, imageView, droneImage);
                    toast("Success on camera");
                }else{
                    toast("Something went wrong. Can't make image");
                    Log.d(TAG, "onActivityResult: Error camera");
                }
                break;
        }
    }

    private void makeImageUrl() {
        droneImage = null;
        imageName = worker.getImageFileName();
        if (imageName != null) {
            droneImage = IMAGES_DIRECTORY + imageName;
            Log.d(TAG, "droneImage: " + droneImage);
        }else{
            toast("Error. Cannot make URL for image.");
        }
    }

    //TODO Try simplify this
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (info.getApi() >= 23) {
            //Ask for permission
            if (requestCode == 0) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    toast("PERMISSION_GRANTED");
                    selectFromCamera();
                } else {
                    toast("PERMISSION_DENIED");
                    toast("Can't use camera");
                }
            }
        }else{
            //Older versions
            selectFromCamera();
        }
    }

}
