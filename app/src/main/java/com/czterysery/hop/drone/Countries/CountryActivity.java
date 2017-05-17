package com.czterysery.hop.drone.Countries;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.czterysery.hop.drone.MyThemeManager;
import com.czterysery.hop.drone.PhoneInfo;
import com.czterysery.hop.drone.R;
import com.czterysery.hop.drone.R2;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tmax0 on 17.05.2017.
 */

public class CountryActivity extends AppCompatActivity {
    public static final int ORIENTATION_PORTRAIT = 1;
    public static final int ORIENTATION_LANDSCAPE = 2;
    MyThemeManager myThemeManager;
    @BindView(R2.id.country_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R2.id.country_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R2.id.country_image)
    ImageView imageView;
    @BindView(R2.id.country_toolbar)
    Toolbar toolbar;
    @BindView(R2.id.country_english_expandableview)
    ExpandableTextView englishTextView;
    @BindView(R2.id.country_polish_expandableview)
    ExpandableTextView polishTextView;
    private PhoneInfo phoneInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myThemeManager = new MyThemeManager(this);
        myThemeManager.chooseTheme();//Automatically set light or dark
        setContentView(R.layout.country_layout);
        ButterKnife.bind(this);
        phoneInfo = new PhoneInfo(this);//Returns important data about smartphone
        configureImageView();
        initializeToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Picasso.with(this).load(R.drawable.poland_photo).fit().into(imageView);
        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        englishTextView.setText(getString(R.string.lorem_ipsum_long));
        polishTextView.setText(getString(R.string.lorem_ipsum_long));
    }

    //In xml value is wrap_content so dynamically set height
    private void configureImageView() {
        int imageViewHeight = 300;//Default
        int screenHeight = phoneInfo.getScreenHeight();//In px's
        int orientation = phoneInfo.getOrientation();
        if (orientation == ORIENTATION_LANDSCAPE) {
            imageViewHeight = (int) (screenHeight / 1.5);
        }else if(orientation == ORIENTATION_PORTRAIT){
            imageViewHeight = screenHeight / 3;
        }
        imageView.setMinimumHeight(imageViewHeight);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeToolbar() {
        ActionBar actionBar;
        if (toolbar != null) {
            toolbar.bringToFront();
            setSupportActionBar(toolbar);

            actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("School");
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

    @Override
    protected void onStop() {
        super.onStop();
        //Clear memory
        System.gc();
    }
}
