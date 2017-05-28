package com.czterysery.hop.drone;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tmax0 on 28.05.2017.
 */

/*
    All method working with images are excluded here.
    It allows operate on bitmaps from all code.
    This class should be continuously optimized for
    newest android versions (permissions, ram usage).
    Class based on official sources:
    https://developer.android.com/topic/performance/graphics/load-bitmap.html
    https://developer.android.com/topic/performance/graphics/index.html
    https://developer.android.com/training/camera/photobasics.html
 */

public class ImageWorker extends Activity{
    private static final String TAG = "ImageWorker";
    private static final int PICK_IMAGE_GALLERY = 1;
    private static final int PICK_IMAGE_CAMERA = 2;
    private Activity activity;
    private ImageView imageView;
    private PhoneInfo info;
    private String pathFromCamera;

    public ImageWorker(Activity activity, ImageView imageView){
        super();
        this.activity = activity;
        //Use activity instead this or context in methods.
        //You should also use static reference activity.[activityMethod]
        this.imageView = imageView;
        info = new PhoneInfo(activity);
    }

    private int checkCameraPermission(){
        return ContextCompat.checkSelfPermission(
                activity, android.Manifest.permission.CAMERA);
    }

    public void runGalleryIntent(){
        //http://codetheory.in/android-pick-select-image-from-gallery-with-intents/
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
    }

    //TODO Add log.d's and exceptions to check if smth work
    public void runCameraIntent(){
        //https://androidkennel.org/android-camera-access-tutorial/

        //check if user allowed permission
        //TODO Divide and make method
        if (info.getApi() >= 23){
            int state = checkCameraPermission();
            if (state == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pathFromCamera = generateFilePathBySdk();
                activity.startActivityForResult(intent, PICK_IMAGE_CAMERA);
            }else{
                ActivityCompat.requestPermissions(activity, new String[] {
                        android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
            }
        }else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pathFromCamera = generateFilePathBySdk();
            activity.startActivityForResult(intent, PICK_IMAGE_CAMERA);
        }
    }

    private String generateFilePathBySdk(){
        String path = null;
        if (info.getApi() >= 23) {
            toast("New SDK filepath");

            path = getOutputMediaFile().getAbsolutePath();
            Log.d(TAG, "generateFilePathBySdk SDK 7.0.0: " + path);
        }else{
            toast("Old SDK filepath");
            try {
                path = getOutputMediaFile().getAbsolutePath();
            }catch (NullPointerException e){
                e.printStackTrace();
                toast("Cannot create path for file");
            }
        }
        return path;
    }

    @Nullable
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory().getPath() + "/DronE");/** The right destination*/

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(mediaStorageDir + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        Log.d(TAG, "getOutputMediaFile: "+ file.getAbsolutePath());
        return file;
    }

    private void storeImage(Bitmap image, String path) {
        File pictureFile = new File(path);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (info.getApi() >= 23) {
            //Ask for permission
            if (requestCode == 0) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    toast("PERMISSION_GRANTED");
                    runCameraIntent();
                } else {
                    toast("PERMISSION_DENIED");
                    toast("Can't use camera");
                }
            }
        }else{
            //Older versions
            runCameraIntent();
        }
    }

    //TODO Divide code on smaller methods and wrap delicately places
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_IMAGE_GALLERY:
                if (resultCode == RESULT_OK &&
                        data != null && data.getData() != null){
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        // Log.d(TAG, String.valueOf(bitmap));
                        storeImage(bitmap, uri.getPath());
                        FtpOperator ftpOperator = new FtpOperator();
                        ftpOperator.uploadFTP(activity, uri.getPath());
                        Picasso.with(activity).load(uri).fit().into(imageView);
                        toast("Success on gallery");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    toast("Something went wrong. Can't make image");
                    Log.d(TAG, "onActivityResult: Error gallery");
                }
                break;
            case PICK_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    //TODO Image may be saved twice, try avoid on 7.0.0
                    createBitmapFromFile(pathFromCamera);
                    FtpOperator ftpOperator = new FtpOperator();
                    ftpOperator.uploadFTP(activity, pathFromCamera);
                    Picasso.with(activity).load(pathFromCamera).fit().into(imageView);
                    toast("Success on camera");
                }else{
                    toast("Something went wrong. Can't make image");
                    Log.d(TAG, "onActivityResult: Error camera");
                }
                break;
        }
    }

    private void createBitmapFromFile(String path){
        File image = new File(path);
        if (!image.exists()){
            try {
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path,bmOptions);
        Bitmap.createScaledBitmap(bitmap, 1280, 720, true);
    }

    private void toast(String message) {
        Toast.makeText(
                activity, message, Toast.LENGTH_SHORT).show();
    }

    public String getPathFromCamera() {
        return pathFromCamera;
    }
}
