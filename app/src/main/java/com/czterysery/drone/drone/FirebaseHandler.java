package com.czterysery.drone.drone;

import android.app.Activity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by tmax0 on 27.05.2017.
 */

public class FirebaseHandler {
    private Activity activity;
    private DatabaseReference databaseReference;
    private static boolean isPersistenceEnabled = false;
    private static String dronesRef = "Drones";
    private static String fixedLocationB = "locationB";
    //https://stackoverflow.com/questions/37448186/setpersistenceenabledtrue-crashes-app

    public FirebaseHandler(Activity activity) {
        this.activity = activity;    // context can be used to call PreferenceManager etc.
        if (!isPersistenceEnabled) {
            //Setup for offline storing
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            isPersistenceEnabled = true;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getDronesRef() {
        return databaseReference.child(dronesRef);
    }

    public DatabaseReference getRefB() {
        return databaseReference.child(fixedLocationB);
    }
}
