package com.czterysery.hop.drone;

import android.app.Activity;

import com.rey.material.app.Dialog;

/**
 * Created by tmax0 on 28.05.2017.
 */

public class Dialogs {
    private Activity activity;
    private Dialog cancelAddDrone;
    private Dialog selectCountry;
    private android.app.Dialog elevationDialog;

    public Dialogs(Activity activity) {
        this.activity = activity;


        cancelAddDrone = new Dialog(activity)
                .title("Do you want to leave?")
                .positiveAction("YES")
                .negativeAction("NO")
                .positiveActionClickListener(v -> activity.finish())
                .negativeActionClickListener(v -> cancelAddDrone.dismiss())
                .cancelable(false);

        selectCountry = new Dialog(activity)
                .title("Select country")
                .contentView(R.layout.select_country_layout)
                .cancelable(false);

        elevationDialog = new Dialog(activity)
                .title("Elevation")
                .contentView(R.layout.elevation_layout)
                .cancelable(true)
                .positiveAction("OK")
                .positiveActionClickListener(v -> {
                   elevationDialog.dismiss();
                });
    }


    public Dialog getCancelAddDrone() {
        return cancelAddDrone;
    }

    public Dialog getSelectCountry() {
        return selectCountry;
    }

    public android.app.Dialog getElevationDialog() {
        return elevationDialog;
    }
}
