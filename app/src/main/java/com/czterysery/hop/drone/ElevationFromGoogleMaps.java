package com.czterysery.hop.drone;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tmax0 on 02.10.2017.
 */

public class ElevationFromGoogleMaps extends AsyncTask<LatLng,Void,Double> {
    private static final String TAG = "ElevationFromGoogleMaps";
    public AsyncResponse delegate = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        /* Newer versions of android requires strict work with http */
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    protected Double doInBackground(LatLng... latLngs) {
        double result = Double.NaN;

        //Get primitive values
        double latitude = latLngs[0].latitude;
        double longitude = latLngs[0].longitude;

        //Connect to google map's page and put coordinates
        //https://developers.google.com/maps/documentation/elevation/intro
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String url = "http://maps.googleapis.com/maps/api/elevation/"
                + "xml?locations=" + String.valueOf(latitude)
                + "," + String.valueOf(longitude)
                + "&sensor=true";

        HttpGet httpGet = new HttpGet(url);
        try {
            //Interpret response from a page
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                int r = -1;//Means error
                StringBuffer respStr = new StringBuffer();
                while ((r = instream.read()) != -1)//Check if correct
                    respStr.append((char) r);
                String tagOpen = "<elevation>";//Html
                String tagClose = "</elevation>";//Html
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);//Final string
                    result = Double.parseDouble(value);
                    //Return double value in meters
                    //Elevation values are expressed relative to local mean sea level (LMSL).
                }
                instream.close();
            }
        } catch (ClientProtocolException e) {
            Log.e(TAG, e.getMessage());
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(Double aDouble) {
        super.onPostExecute(aDouble);

        //Return result to previous activity
        delegate.showResult(aDouble);
    }
}
