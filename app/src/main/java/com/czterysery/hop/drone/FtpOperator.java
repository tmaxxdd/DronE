package com.czterysery.hop.drone;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.ftp.FTPUploadRequest;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tmax0 on 18.05.2017.
 */

class FtpOperator {

    private static final String TAG = "FtpOperator";
    private Activity activity;
    private String droneImage;
    private ConnectionManager connectionManager;
    private LayoutWorker layoutWorker;

    FtpOperator(Activity activty, String droneImage) {
        this.activity = activty;
        this.droneImage = droneImage;
        connectionManager = new ConnectionManager(activity);
        layoutWorker = new LayoutWorker(activity);
    }

    void uploadFTP(final Context context, String path) {
        if (checkIfFileLocalExists(path)) {
            if (connectionManager.isOnline()) {
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
                layoutWorker.toast("No internet connection. Can't upload image.");
            }
        }else{
            layoutWorker.toast("Can't find file to upload.");
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

}
