package com.czterysery.drone.drone;

import android.app.Activity;

import com.rey.material.app.Dialog;

/**
 * Created by tmax0 on 28.05.2017.
 */

public class Dialogs {
    private Activity activity;
    private Dialog cancelAddDrone;

    public Dialogs(Activity activity) {
        this.activity = activity;


        cancelAddDrone = new Dialog(activity)
                .title("Do you want to leave?")
                .positiveAction("YES")
                .negativeAction("NO")
                .positiveActionClickListener(v -> activity.finish())
                .negativeActionClickListener(v -> cancelAddDrone.dismiss())
                .cancelable(false);
    }


    public Dialog getCancelAddDrone() {
        return cancelAddDrone;
    }
}
