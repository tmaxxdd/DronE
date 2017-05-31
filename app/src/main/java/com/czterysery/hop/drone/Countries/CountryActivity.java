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

import com.czterysery.hop.drone.LayoutWorker;
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
    private String countryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myThemeManager = new MyThemeManager(this);
        myThemeManager.chooseTheme();//Automatically set light or dark
        setContentView(R.layout.country_layout);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        countryName = bundle.getString("countryName");

        phoneInfo = new PhoneInfo(this);//Returns important data about smartphone
        LayoutWorker layoutWorker = new LayoutWorker(this);
        initializeToolbar();
        imageView.setMinimumHeight(
                layoutWorker.calculateImageViewHeight(imageView));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Picasso.with(this).load(R.drawable.poland_photo).fit().into(imageView);
        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        switch (countryName){
            case "Poland":
                englishTextView.setText(getString(R.string.poland_english_description));
                break;
            case "Croatia":
                englishTextView.setText(getString(R.string.croatia_english_description));
                break;
            case "Slovenia":
                englishTextView.setText(getString(R.string.slovenia_english_description));
                break;
            case "Spain":
                englishTextView.setText(getString(R.string.spain_english_description));
                break;
        }
    }

    public void initializeToolbar() {
        ActionBar actionBar;
        if (toolbar != null) {
            toolbar.bringToFront();
            setSupportActionBar(toolbar);

            actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(countryName);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Clear memory
        System.gc();
    }
}
