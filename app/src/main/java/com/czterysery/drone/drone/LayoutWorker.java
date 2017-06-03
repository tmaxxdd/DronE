package com.czterysery.drone.drone;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by tmax0 on 30.05.2017.
 */

/*
    Special class with methods excluded from normal activities.
    You can find methods that works with layout's elements.
    It is highly recommended to use this code.
    Generally if some action is for many layouts, it will be added here.
 */
public class LayoutWorker extends AppCompatActivity {
    public static final int ORIENTATION_PORTRAIT = 1;
    public static final int ORIENTATION_LANDSCAPE = 2;
    private Activity activity;

    public LayoutWorker(Activity activity){
        this.activity = activity;
    }

    //In xml value is wrap_content so dynamically set height
    /*
        Without changing height, ImageView is too high.
        ImageView in CollapsingToolbarLayout is fit to image.
     */
    public int calculateImageViewHeight(ImageView imageView) {
        PhoneInfo phoneInfo = new PhoneInfo(activity);
        int imageViewHeight = 300;//Default
        int screenHeight = phoneInfo.getScreenHeight();//In px's
        int orientation = phoneInfo.getOrientation();
        if (orientation == ORIENTATION_LANDSCAPE) {
            imageViewHeight = (int) (screenHeight / 1.5);
        }else if(orientation == ORIENTATION_PORTRAIT){
            imageViewHeight = screenHeight / 3;
        }
        return imageViewHeight;
    }

}
