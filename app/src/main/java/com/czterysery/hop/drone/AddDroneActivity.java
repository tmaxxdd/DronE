package com.czterysery.hop.drone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.czterysery.hop.drone.Models.MyDrone;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.rey.material.widget.Spinner;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.UploadService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tmax0 on 16.05.2017.
 */

public class AddDroneActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_GALLERY = 1;
    private static final int PICK_IMAGE_CAMERA = 2;
    private static final String TAG = "AddDroneActivity";
    private MyThemeManager myThemeManager;
    private PhoneInfo info;
    private String pathFromCamera;
    private String droneImage =
            "https://pisces.bbystatic.com/BestBuy_US/images/products/5621/5621780_sd.jpg;maxHeight=460;maxWidth=460";
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
    @BindView(R2.id.add_drone_image_layout)
    RelativeLayout imageLayout;
    @BindView(R2.id.add_drone_country_spinner)
    Spinner countrySpinner;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

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
        //TODO Add camera feature in future
        //https://developer.android.com/training/camera/photobasics.html
        imageLayout.setVisibility(View.GONE);//Hided
    }

    private void initializeSpinner() {
        String[] countriesArray =
                getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.row_spn, countriesArray);
        adapter.setDropDownViewResource(R.layout.);
    }

    @Override
    protected void onStart() {
        super.onStart();

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MyDrone myDrone = dataSnapshot.getValue(MyDrone.class);
                toast(myDrone.getName());
                finish();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_drone, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.gc();
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

    //TODO Protect with null values!
    private void trySave() {

        getAndPushDataToFirebase();

        String countryName = "Poland";



        toast("Save");
        //finish();
    }

    private void tryClose() {
        toast("Close");
        finish();
    }

    private void toast(String message) {
        Toast.makeText(
                this, message, Toast.LENGTH_SHORT).show();
    }

    private int checkCameraPermission(){
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
    }

    private void getAndPushDataToFirebase(){

        String droneName = nameEditText
                .getEditableText().toString();
        String droneDescription = descriptionEditText
                .getEditableText().toString();
        String droneTransmitter = transmitterEditText
                .getEditableText().toString();
        String droneBattery = batteryEditText
                .getEditableText().toString();
        String droneMotors = motorsEditText
                .getEditableText().toString();
        String droneBluetooth = bluetoothEditText
                .getEditableText().toString();
        String droneBuzzer = buzzerEditText
                .getEditableText().toString();
        String droneWifi = wifiEditText
                .getEditableText().toString();
        String droneGps = gpsEditText
                .getEditableText().toString();
        String droneCompass = compassEditText
                .getEditableText().toString();
        String droneReceiver = receiverEditText
                .getEditableText().toString();
        String droneTelemetry = telemetryEditText
                .getEditableText().toString();
        String droneCamera = cameraEditText
                .getEditableText().toString();
        String droneGimbal = gimbalEditText
                .getEditableText().toString();
        String dronePwm = pwmEditText
                .getEditableText().toString();
        String droneSwitch = switchEditText
                .getEditableText().toString();
        String droneBatteryWarner = batterywarnerEditText
                .getEditableText().toString();
        String droneController = controllerEditText
                .getEditableText().toString();

        MyDrone myDrone = new MyDrone(droneImage, countryName, droneName, droneDescription, droneTelemetry,
                droneBattery, droneMotors, droneBuzzer, droneBluetooth, droneWifi, droneGps,
                droneCompass, droneReceiver, droneTransmitter, droneCamera, droneGimbal, dronePwm,
                droneSwitch, droneBatteryWarner, droneController);

        myRef.child("Drones").child(countryName).child(droneName).setValue(myDrone);

    }

    @OnClick(R2.id.add_drone_files_button)
    public void runGalleryIntent(){
        //http://codetheory.in/android-pick-select-image-from-gallery-with-intents/
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
    }

    //TODO Add log.d's and exceptions to check if smth work
    @OnClick(R2.id.add_drone_camera_button)
    public void runCameraIntent(){
        //https://androidkennel.org/android-camera-access-tutorial/

        //check if user allowed permission

        //TODO Divide and make method
        if (info.getApi() >= 23){
            int state = checkCameraPermission();
            if (state == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pathFromCamera = generateFilePathBySdk();
                startActivityForResult(intent, PICK_IMAGE_CAMERA);
            }else{
                ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
            }
        }else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pathFromCamera = generateFilePathBySdk();
            startActivityForResult(intent, PICK_IMAGE_CAMERA);
        }
    }

    private String generateFilePathBySdk(){
        String path = null;
        if (info.getApi() >= 23) {
            toast("New SDK filepath");

            path = getOutputMediaFile().getAbsolutePath();
            Log.d(TAG, "generateFilePathBySdk SDK 7.0.0: "+path);
        }else{
            toast("Old SDK filepath");
            try {
                path = getOutputMediaFile().getAbsolutePath();
            }catch (NullPointerException e){
                e.printStackTrace();
                toast("Cannot create path for file");
            }
        }
        return path;
    }

    @Nullable
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory().getPath() + "/DronE");/** The right destination*/

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(mediaStorageDir + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        Log.d(TAG, "getOutputMediaFile: "+ file.getAbsolutePath());
        return file;
    }

    private void storeImage(Bitmap image, String path) {
        File pictureFile = new File(path);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (info.getApi() >= 23) {
            //Ask for permission
            if (requestCode == 0) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    toast("PERMISSION_GRANTED");
                    runCameraIntent();
                } else {
                    toast("PERMISSION_DENIED");
                    toast("Can't use camera");
                }
            }
        }else{
            //Older versions
            runCameraIntent();
        }
    }

    //TODO Divide code on smaller methods and wrap delicately places
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_IMAGE_GALLERY:
                if (resultCode == RESULT_OK &&
                        data != null && data.getData() != null){
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        // Log.d(TAG, String.valueOf(bitmap));
                        storeImage(bitmap, uri.getPath());
                        FtpOperator ftpOperator = new FtpOperator();
                        ftpOperator.uploadFTP(this, uri.getPath());
                        Picasso.with(this).load(uri).fit().into(imageView);
                        toast("Success on gallery");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    toast("Something went wrong. Can't make image");
                    Log.d(TAG, "onActivityResult: Error gallery");
                }
                break;
            case PICK_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    //TODO Image may be saved twice, try avoid on 7.0.0
                    createBitmapFromFile(pathFromCamera);
                    FtpOperator ftpOperator = new FtpOperator();
                    ftpOperator.uploadFTP(this, pathFromCamera);
                    Picasso.with(this).load(pathFromCamera).fit().into(imageView);
                    toast("Success on camera");
                }else{
                    toast("Something went wrong. Can't make image");
                    Log.d(TAG, "onActivityResult: Error camera");
                }
                break;
        }
    }


    private void createBitmapFromFile(String path){
        File image = new File(path);
        if (!image.exists()){
            try {
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path,bmOptions);
        Bitmap.createScaledBitmap(bitmap, 1280, 720, true);
    }

}
