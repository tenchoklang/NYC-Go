package com.android.tenchoklang.nycgo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by tench on 5/6/2018.
 */

public class UserLocationListener {

    private final OnLocationListener locationListener;
    private Context context;

    private double lat;
    private double lon;

    private boolean gpsEnabled =false;
    private boolean networkEnabled = false;

    private LocationManager mLocationManager;

    private static final String TAG = "UserLocationListener";

    interface OnLocationListener{
        void locationUpdate(double lat, double lon);
    }

    //listener for when users location changes
    private final android.location.LocationListener mLocationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //called for when gps initially on and when gps location changes
            Log.d(TAG, "onLocationChanged: LAT: " + location.toString());
            //IDK? got weird error saying "location object is null"
            if(location != null){
                lat = location.getLatitude();
                lon = location.getLongitude();
                locationListener.locationUpdate(lat, lon);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {   }
        @Override
        public void onProviderEnabled(String provider) {   }
        @Override
        public void onProviderDisabled(String provider) {   }
    };

    public UserLocationListener(OnLocationListener locationListener, Context context){
        this.locationListener = locationListener;
        this.context = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    public void listen(){

        gpsEnabled();

        if(!gpsEnabled && !networkEnabled){
            //No GPS or Network
            Log.d(TAG, "listen: No GPS or Network");
        }else if(networkEnabled){
            //Network
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "getUserLocation: Getting Network Location");
                //Gets the last known Location to make waiting for the location faster
                String locationProvider = LocationManager.NETWORK_PROVIDER;
                Location location = mLocationManager.getLastKnownLocation(locationProvider);
                lat = location.getLatitude();
                lon = location.getLongitude();
                //updates the class that implements the interface on the last known location while waiting on the Network
                locationListener.locationUpdate(lat, lon);
                Log.d(TAG, "getUserLocation: Last Known Location LON: " + lat + "LAT: " + lon);

                //this is where we can update the camera

                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,200, mLocationListener);
            }else{
                Log.d(TAG, "getUserLocation: Network Error");
            }
        }else{//GPS
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "getUserLocation: Getting GPS Location");
                //Gets the last known Location to make waiting for the location faster
                String locationProvider = LocationManager.GPS_PROVIDER;
                Location location = mLocationManager.getLastKnownLocation(locationProvider);
                lat = location.getLatitude();
                lon = location.getLongitude();
                //updates the class that implements the interface on the last known location while waiting on the GPS
                locationListener.locationUpdate(lat, lon);
                Log.d(TAG, "getUserLocation: Last Known Location LON: " + lat + "LAT: " + lon);

                //this is where we can update the camera

                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, mLocationListener);
            }else{
                Log.d(TAG, "getUserLocation: GPS Error");
            }
        }
    }


    private void gpsEnabled(){
        gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!gpsEnabled){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Location")
                    .setMessage("Turn On Your Location")
                    .setCancelable(false)
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //direct the user to phones location settings
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public double getLat() {  return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon;  }
    public void setLon(double lon) { this.lon = lon; }

    public void setGpsEnabled(boolean gpsEnabled) { this.gpsEnabled = gpsEnabled; }
    public void setNetworkEnabled(boolean networkEnabled) {this.networkEnabled = networkEnabled; }
}
