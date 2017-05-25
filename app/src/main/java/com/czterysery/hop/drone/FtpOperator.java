package com.czterysery.hop.drone;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.ftp.FTPUploadRequest;

import java.io.File;

/**
 * Created by tmax0 on 18.05.2017.
 */

public class FtpOperator {

    private static final String TAG = "FtpOperator";

    public FtpOperator() {}

    public void uploadFTP(final Context context, String path) {
        if (new File(path).exists()){
            Toast.makeText(context, "Jest plik", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "filePath: "+path);
        }else{
            Toast.makeText(context, "Nie ma pliku", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "filePath: "+path);
        }
        try {
            String uploadId =
                    new FTPUploadRequest(context, "136.243.46.32", 21)
                            .setUsernameAndPassword("tkadz", "cmogw1846")
                            .addFileToUpload(path, "/public_html/drone/images")
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(4)
                            .startUpload();
        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getMessage(), exc);
        }
    }
}
