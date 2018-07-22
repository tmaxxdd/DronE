package com.czterysery.drone.drone.Drone;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.czterysery.drone.drone.Commands;
import com.czterysery.drone.drone.MyThemeManager;
import com.czterysery.drone.drone.Parameters;
import com.czterysery.drone.drone.Presenter.ControlPresenter;
import com.czterysery.drone.drone.Presenter.ControlPresenterImpl;
import com.czterysery.drone.drone.R;
import com.czterysery.drone.drone.R2;
import com.czterysery.drone.drone.View.ControlView;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.Slider;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by tmax0 on 15.05.2017.
 */

public class ControlActivity extends AppCompatActivity implements ControlView,
        Slider.OnPositionChangeListener, View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();
    private MyThemeManager myThemeManager;
    private ControlPresenter controlPresenter;
    private Dialog processingDialog;
    private Dialog messageDialog;
    @BindView(R2.id.control_toolbar)
    Toolbar toolbar;
    /* TextView */
    @BindView(R2.id.control_console)
    TextView consoleLog;
    @BindView(R2.id.control_ssid)
    TextView ssid_tv;
    @BindView(R2.id.control_socket)
    TextView socket_tv;
    @BindView(R2.id.control_esp_status)
    TextView esp_status_tv;
    @BindView(R2.id.control_drone_status)
    TextView drone_status_tv;
    /* Spinner */
    @BindView(R2.id.control_mode_spinner)
    MaterialSpinner flight_mode_spinner;
    /* Slider */
    @BindView(R2.id.control_controller_roll_slider)
    Slider roll_slider;
    @BindView(R2.id.control_controller_pitch_slider)
    Slider pitch_slider;
    @BindView(R2.id.control_controller_throttle_slider)
    Slider throttle_slider;
    @BindView(R2.id.control_controller_yaw_slider)
    Slider yaw_slider;
    /* Button */
    @BindView(R2.id.control_joystick_btn)
    Button joystick_btn;
    @BindView(R2.id.control_arm_btn)
    Button arm_btn;
    @BindView(R2.id.control_logs_btn)
    Button logs_btn;
    @BindView(R2.id.control_connect_btn)
    Button connect_btn;
    @BindView(R2.id.control_serial_btn)
    Button socket_btn;
    @BindView(R2.id.bottom_sheet1)
    View bottomSheet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myThemeManager = new MyThemeManager(this);
        myThemeManager.chooseTheme();
        setContentView(R.layout.control_layout);
        initializeToolbar();
        ButterKnife.bind(this);

        //Declare the phone's wifi manager
        WifiManager wifiManager =
                (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        controlPresenter = new ControlPresenterImpl(this, wifiManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showConsoleLog("=================");
        controlPresenter.onStart();

        initializeSpinner();
        initializeBottomSheet();

        //Init the sliders listeners
        roll_slider.setOnPositionChangeListener(this);
        pitch_slider.setOnPositionChangeListener(this);
        throttle_slider.setOnPositionChangeListener(this);
        yaw_slider.setOnPositionChangeListener(this);
        //Init the buttons listeners
        joystick_btn.setOnClickListener(this);
        arm_btn.setOnClickListener(this);
        logs_btn.setOnClickListener(this);
        connect_btn.setOnClickListener(this);
        socket_btn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        controlPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        controlPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        controlPresenter.onStop();
        showConsoleLog("=================");
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
                actionBar.setTitle("Control");
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

    private void initializeSpinner(){
        //Get an array with the flight modes
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                R.layout.row_spn, getResources().getStringArray(R.array.flight_modes_array));

        //Adjust the spinner (dropdown list)
        spinnerAdapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        flight_mode_spinner.setAdapter(spinnerAdapter);
        flight_mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String flightMode = parent.getItemAtPosition(position).toString();
                controlPresenter.setFlightMode(flightMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeBottomSheet(){
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        //It is collapsed for default
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        controlPresenter.setBottomSheetBehavior(bottomSheetBehavior);
    }

    @Override
    public void onPositionChanged(Slider view, boolean fromUser,
                                  float oldPos, float newPos, int oldValue, int newValue) {
        controlPresenter.setRotorParam(view.getId(), newValue);
    }

    @Override
    public void onClick(View view) {
        controlPresenter.onButtonClick(view.getId());
    }

    @Override
    public void showSSID(String ssid) {
        if (ssid.equals("\"" + Parameters.WIFI_NAME + "\"")){
            ssid_tv.setTextColor(getResources().getColor(R.color.colorAccent));
            ssid_tv.setText(ssid);
        }else {
            ssid_tv.setTextColor(getResources().getColor(R.color.md_red_500));
            ssid_tv.setText(ssid);
        }
    }

    @Override
    public void showSocket(String socket) {
        if (socket.equals(Parameters.HOST_ADDRESS)){
            String fullName = Parameters.HOST_ADDRESS + ":" + Parameters.PORT;
            socket_tv.setTextColor(getResources().getColor(R.color.colorAccent));
            socket_tv.setText(fullName);
        }else {
            socket_tv.setTextColor(getResources().getColor(R.color.md_red_500));
            socket_tv.setText(socket);
        }
    }

    @Override
    public void showEspStatus(String status) {
        if (status.equals(Commands.PINGING)){
            esp_status_tv.setTextColor(getResources().getColor(R.color.colorAccent));
            esp_status_tv.setText(status);
        }else {
            esp_status_tv.setTextColor(getResources().getColor(R.color.md_red_500));
            esp_status_tv.setText(status);
        }
    }

    @Override
    public void showDroneStatus(String status) {
        drone_status_tv.setText(status);
    }

    @Override
    public void showMessage(String message) {
        toast(message);
    }

    @Override
    public void showConsoleLog(String message) {
        String oldMessage = consoleLog.getText().toString();
        String updatedLog = oldMessage + "\n" + message;
        consoleLog.setText(updatedLog);
    }

    public void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showProcessingDialog(String message){
        Dialog processingDialog = getProcessingDialog(message);
        processingDialog.show();
    }

    public void hideProcessingDialog(String message){
        Dialog processingDialog = getProcessingDialog(message);
        processingDialog.dismiss();
    }

    @Override
    public void setButtonDisconnect() {
        connect_btn.setText("Disconnect");
        connect_btn.setTextColor(getResources().getColor(R.color.md_red_500));
    }

    @Override
    public void setButtonConnect() {
        connect_btn.setText("Connect");
        connect_btn.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void setButtonSocketOn() {
        socket_btn.setText("SOCKET_OFF");
        socket_btn.setTextColor(getResources().getColor(R.color.md_red_500));
    }

    @Override
    public void setButtonSocketOff() {
        socket_btn.setText("SOCKET_ON");
        socket_btn.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void setButtonSocketEnabled(boolean enabled) {
        if (enabled){
            socket_btn.setEnabled(true);
            socket_btn.setAlpha((float) 1);
        }else {
            socket_btn.setEnabled(false);
            socket_btn.setAlpha((float) 0.5);
        }
    }

    private Dialog getProcessingDialog(String message){
        View dialogLayout = LayoutInflater
                .from(this)
                .inflate(R.layout.dialog_layout, null);

        TextView dialogText = dialogLayout.findViewById(R.id.dialog_text);
        dialogText.setText(message);

         if (processingDialog != null){
             processingDialog.setContentView(dialogLayout);
             return processingDialog;
         }else {
             processingDialog = new Dialog(this)
                     .title("Processing")
                     .contentView(dialogLayout)
                     .cancelable(false);
         }
         return processingDialog;
    }

}
