package com.restaurantfinder.manager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.restaurantfinder.R;
import com.restaurantfinder.services.FetchAddressIntentService;
import com.restaurantfinder.startup.RestaurantApplication;
import com.restaurantfinder.utils.Constants;

/**
 * Created by Aditya on 14-Apr-16.
 */
public class LocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public interface LocationAddressListener{
        public void locationFound(String address);
    }

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    private Context mContext;
    private AddressResultReceiver mResultReceiver;


    private static volatile LocationManager _instance = null;

    private LocationManager(){

    }

    public void setContext(Context context){
        mContext = context;
        mResultReceiver = new AddressResultReceiver(new Handler());
        buildGoogleApiClient();
    }

    public static LocationManager getInstance() {
        if (_instance == null) {
            synchronized (LocationManager.class) {
                if (_instance == null) {
                    _instance = new LocationManager();
                }
            }
        }
        return _instance;
    }

    @Override
    public void onConnected(Bundle bundle) {
        final int REQUEST_LOCATION = 2;

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // Update location every second

        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if(mLastLocation == null){
            mLastLocation = new Location("");
            mLastLocation.setLatitude(12.932909d);//your coords of course
            mLastLocation.setLongitude(77.602859d);
            startIntentService();
        }
        if (mLastLocation != null) {
            mLastLocation.setLatitude(12.932909d);//your coords of course
            mLastLocation.setLongitude(77.602859d);
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
            startIntentService();
        }
        logLatLong();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /*@Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        startIntentService();
        logLatLong();
    }*/

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(RestaurantApplication.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    void logLatLong() {
        Log.i("hello", "latitide : " + lat);
        Log.i("hello", "longitude : " + lon);
    }

    public void start(){
        mGoogleApiClient.connect();
    }

    public void stop(){
        mGoogleApiClient.disconnect();
    }

    public String getLatitude() {
        if (mLastLocation != null) {
            return Double.toString(mLastLocation.getLatitude());
        }
        return "0";
    }

    public String getLongitude() {
        if (mLastLocation != null) {
            return Double.toString(mLastLocation.getLongitude());
        }
        return "0";
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(mContext, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        mContext.startService(intent);
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            Log.i("hello", "Address = " + resultData.getString(Constants.RESULT_DATA_KEY));
            //displayAddressOutput();
            if(mContext!=null){
                ((LocationAddressListener)mContext).locationFound(resultData.getString(Constants.RESULT_DATA_KEY));
            }
            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                //showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            //mAddressRequested = false;
            //updateUIWidgets();
        }
    }
}
