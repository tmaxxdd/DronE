package com.czterysery.hop.drone;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.ftp.FTPUploadRequest;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tmax0 on 18.05.2017.
 */

public class FtpOperator {

    private static final String TAG = "FtpOperator";
    private Activity activity;
    private String droneImage;

    FtpOperator(Activity activty, String droneImage) {
        this.activity = activty;
        this.droneImage = droneImage;
    }

    void uploadFTP(final Context context, String path) {
        if (checkIfFileLocalExists(path)) {
            if (isOnline()) {
                if (!checkIfFileUrlExists(droneImage))//There won't be duplicate
                try {
                    String uploadId =
                            new FTPUploadRequest(context, "136.243.46.32", 21)
                                    .setUsernameAndPassword("tkadz", "cmogw1846")
                                    .addFileToUpload(path, "../public_html/drone/images/")//Images are uploaded on Tomek's own server
                                    .setNotificationConfig(new UploadNotificationConfig())
                                    .setMaxRetries(4)
                                    .startUpload();
                } catch (Exception exc) {
                    Log.e("AndroidUploadService", exc.getMessage(), exc);
                }
            }else{
                toast("No internet connection. Can't upload image.");
            }
        }else{
            toast("Can't find file to upload.");
        }
    }

    private boolean checkIfFileLocalExists(String path){
        boolean fileState;
        if (new File(path).exists()){
            fileState = true;
            Log.d(TAG, "checkIfFileLocalExists: " + "There is file to upload.");
        }else {
            fileState = false;
            Log.d(TAG, "checkIfFileLocalExists: " + "No file to upload.");
        }
        return fileState;
    }

    private boolean checkIfFileUrlExists(String URLName){
        if (URLName != null) {
            try {
                HttpURLConnection.setFollowRedirects(false);
                // note : you may also need
                //        HttpURLConnection.setInstanceFollowRedirects(false)
                HttpURLConnection con =
                        (HttpURLConnection) new URL(URLName).openConnection();
                con.setRequestMethod("HEAD");
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else{
            Log.d(TAG, "URLName: " + droneImage);
            return false;
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void toast(String message) {
        Toast.makeText(
                activity, message, Toast.LENGTH_SHORT).show();
    }

}
