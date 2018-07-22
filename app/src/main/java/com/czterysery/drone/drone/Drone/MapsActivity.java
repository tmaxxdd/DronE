package com.czterysery.drone.drone.Drone;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.czterysery.drone.drone.AsyncResponse;
import com.czterysery.drone.drone.ConnectionManager;
import com.czterysery.drone.drone.Coordinates;
import com.czterysery.drone.drone.Dialogs;
import com.czterysery.drone.drone.ElevationFromGoogleMaps;
import com.czterysery.drone.drone.LayoutWorker;
import com.czterysery.drone.drone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AsyncResponse {

    private GoogleMap mMap;
    private LayoutWorker layoutWorker;
    private ConnectionManager connectionManager;
    private Activity activity;
    private Dialog countriesDialog;
    private LatLng poland = Coordinates.Poland;
    private LatLng croatia = Coordinates.Croatia;
    private LatLng slovenia = Coordinates.Slovenia;
    private LatLng spain = Coordinates.Spain;
    private double currentElevation; //Keep value calculated elevation


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_layout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        activity = this;

        //Initialize util classes
        countriesDialog = new Dialogs(this).getSelectCountry();
        layoutWorker = new LayoutWorker(this);
        connectionManager = new ConnectionManager(this);

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Define map's style
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Handle clicks on map. Every click will return altitude
        mMap.setOnMapClickListener(point -> {
            //From asyncTask will be returned result to method
            calculateElevationForPoint(point.longitude, point.latitude);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showSchool();
    }

    /* This function will execute task in background to get elevation from web */
    private void calculateElevationForPoint(double longitude, double latitude) {

        //Check if is internet connection
        if (connectionManager.isOnline()) {

            //Notify user
            layoutWorker.toast("Calculating your elevation...");

            ElevationFromGoogleMaps elevationAsyncTask = new ElevationFromGoogleMaps();

            //This point activity where asyncTask
            // returns value
            elevationAsyncTask.delegate = this;

            //Put data to asyncTask
            LatLng input = new LatLng(latitude, longitude);
            elevationAsyncTask.execute(input);

        } else {
            //There is no internet connection
            layoutWorker.toast("No internet connection. Cannot get elevation.");
        }
    }

    /* Show dialog with human-readable parameters (induced from interface) */
    @Override
    public void showResult(Double elevation){
        double tempElevation = currentElevation;//Needed for comparing

        Dialog elevationDialog = new Dialogs(activity).getElevationDialog();

        //Current elevation
        TextView currentElevationView = (TextView) elevationDialog.findViewById(R.id.current_elevation);
        currentElevationView.setText(String.valueOf(elevation));

        //Temp elevation
        TextView previousElevationView = (TextView) elevationDialog.findViewById(R.id.previous_elevation);
        previousElevationView.setText(String.valueOf(tempElevation));

        elevationDialog.show();

        //Replace value
        currentElevation = elevation;
    }

    //TODO Try to simplify code below
    private void showSchool() {
        /* Move map's pointer to school */

        //Reaction for Poland
        countriesDialog.findViewById(R.id.select_country_poland)
                .setOnClickListener( v -> {
                    moveCamera(poland, "ZS 10 Mikulczyce");
                    countriesDialog.dismiss();
                });

        //Reaction for Poland
        countriesDialog.findViewById(R.id.select_country_croatia)
                .setOnClickListener( v -> {
                    moveCamera(croatia, "Technicka Skola Sisak");
                    countriesDialog.dismiss();
                });

        //Reaction for Poland
        countriesDialog.findViewById(R.id.select_country_slovenia)
                .setOnClickListener( v -> {
                    moveCamera(slovenia, "Solski center Krsko-Sevnica");
                    countriesDialog.dismiss();
                });

        //Reaction for Poland
        countriesDialog.findViewById(R.id.select_country_spain)
                .setOnClickListener( v -> {
                    moveCamera(spain, "IES la FOIA");
                    countriesDialog.dismiss();
                });


        countriesDialog.show();
    }

    private void moveCamera(LatLng latLng, String title){
        mMap.addMarker(new MarkerOptions().position(latLng).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
    }

}
