package com.czterysery.drone.drone;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ImageView;

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
    public static final int PICK_IMAGE_GALLERY = 1;
    public static final int PICK_IMAGE_CAMERA = 2;
    private Activity activity;
    private PhoneInfo info;
    private String generatedPath;
    private String imageFileName;

    public ImageWorker(Activity activity){
        super();
        this.activity = activity;
        //Use activity instead this or context in methods.
        //You should also use static reference activity.[activityMethod]
        info = new PhoneInfo(activity);
    }

    private int checkCameraPermission(){
        return ContextCompat.checkSelfPermission(
                activity, android.Manifest.permission.CAMERA);
    }

    public void runGalleryIntent(){
        //http://codetheory.in/android-pick-select-image-from-gallery-with-intents/
        Intent takeImageFromGallery = new Intent();
        try {
            createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Show only images, no videos or anything else
        takeImageFromGallery.setType("image/*");
        takeImageFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        activity.startActivityForResult(Intent.createChooser(takeImageFromGallery,
                "Select Picture"), PICK_IMAGE_GALLERY);
    }

    public void runCameraIntent(){
        //https://androidkennel.org/android-camera-access-tutorial/
        int state = checkCameraPermission();
        if (state == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, generateUriForImage());
            activity.startActivityForResult(intent, PICK_IMAGE_CAMERA);
        }else{
            ActivityCompat.requestPermissions(activity, new String[] {
                    android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }

    private Uri generateUriForImage(){
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Continue only if the File was successfully created
        Uri photoURI = null;
        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(activity,
                    "com.czterysery.hop.drone.fileprovider",//authorities in xml
                    photoFile);
        }
        return photoURI;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        generatedPath = image.getAbsolutePath();
        return image;
    }

    //You can find images in Android/data/com.czterysery.hop.drone/files/Pictures
    public void storeImage(Bitmap image, String path) {
        File pictureFile = new File(path);
        //Use for overwriting images
        if (pictureFile.exists()) {
            pictureFile.delete();
            pictureFile = new File(path);
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public Bitmap getBitmapFromUri(Uri uri){
        try {
            return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getGeneratedPath() {
        return generatedPath;
    }

    //TODO Try simplify this
    public String getImageFileName() {
        String base = getGeneratedPath();
        String photoDirectory =
                activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath();
        String onlyFileName = base.replace(photoDirectory, "");
        return onlyFileName;
    }

    public void uploadImage(String finalPath, String droneImage) {
        FtpOperator ftpOperator = new FtpOperator(activity, droneImage);
        ftpOperator.uploadFTP(activity, finalPath);
    }

    public void loadImageToImageView(String finalPath, ImageView imageView){
        File imageFile = new File(finalPath);
        Picasso.with(activity).load(imageFile).fit().into(imageView);
    }

    public void loadImageToImageView(int image, ImageView imageView){
        Picasso.with(activity).load(image).fit().into(imageView);
    }

    public void cropImageToImageView(String imagePath, ImageView imageView) {
        //Decoded dimensions
        int photoW = imageView.getWidth();
        int photoH = imageView.getHeight();

        //TODO In future scale bitmaps dynamically
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, photoW, photoH, false);//Resized Bitmap
        storeImage(finalBitmap, imagePath);
    }

    public void uploadAndShowImage(String finalPath, ImageView imageView, String droneImage){
        cropImageToImageView(finalPath, imageView);//Compress and resize bitmap for upload
        uploadImage(finalPath, droneImage);
        loadImageToImageView(finalPath, imageView);
    }

}
