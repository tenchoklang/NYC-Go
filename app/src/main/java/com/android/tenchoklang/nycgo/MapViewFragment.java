package com.android.tenchoklang.nycgo;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by tench on 4/30/2018.
 */
public class MapViewFragment extends Fragment implements UserLocationListener.OnLocationListener, GetJsonData.OnDataAvailable {

    private static final String TAG = "MapViewFragment";
    MapView mMapView;
    private GoogleMap googleMap;

    private boolean networkEnabled = false;
    private boolean gpsEnabled = false;

    private static double lat = 0;
    private static double lon = 0;

    private LocationManager mLocationManager;

    private UserLocationListener locationListener;

    private static final int REQUESTED_CODE_LOCATION = 1;

    private static final int DISTANCE_TO_UNLOCK = 50;

    private FloatingActionButton floatingActionButton;

    private HologramRecyclerViewAdapter recyclerViewAdapter;

    public final static String MY_GLOBAL_PREFS = "my_global_prefs";
    public final static String SAVED_HOLO_KEY = "saved_loc_key";

    private static final String API_KEY = "KEY_HERE";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mapview, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new UserLocationListener(this, getContext());

        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this would launch the NYC Open data API to get the Crime around specified by the distance
                Toast.makeText(getContext(), "LAT: " + lat + " LON: " + lon, Toast.LENGTH_SHORT).show();
                getData();
                //this is where we make the call to the NYC Open Data to get the Crime Information
            }
        });

        gpsEnabled();

        int hasLocationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasLocationPermission == PackageManager.PERMISSION_GRANTED) {
            //permission had been granted
            Log.d(TAG, "onClick: Location Permission Granted");
//            getUserLocation();
            locationListener.listen();
            enableMap();
        } else {
            //permission has not been granted
            Log.d(TAG, "onClick: Location Permission Denied");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUESTED_CODE_LOCATION);
        }

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Starts");
        switch (requestCode) {
            case REQUESTED_CODE_LOCATION: {
                //if the grantResults arr is not empty && grantResult == PackageManager.PERMISION_GRANTED
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission has been granted
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
//                    getUserLocation();
                    locationListener.listen();
                    enableMap();
                } else {
                    //permission denied
                    //disable the functionality that depends on this permission
                    Log.d(TAG, "onRequestPermissionsResult: Permission Denied");
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Ends");
    }

//    private void getUserLocation(){
//        if(!gpsEnabled && !networkEnabled){
//            //No GPS or Network
//        }else if(networkEnabled){
//            //Network
//            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                Log.d(TAG, "getUserLocation: Getting Network Location");
//                //Gets the last known Location to make waiting for the location faster
//                String locationProvider = LocationManager.NETWORK_PROVIDER;
//                Location location = mLocationManager.getLastKnownLocation(locationProvider);
//                lat = location.getLatitude();
//                lon = location.getLongitude();
//                Log.d(TAG, "getUserLocation: Last Known Location LON: " + lat + "LAT: " + lon);
//
//                //this is where we can update the camera
//
//                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,200, mLocationListener);
//            }else{
//                Log.d(TAG, "getUserLocation: Network Error");
//            }
//        }else{//GPS
//            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                Log.d(TAG, "getUserLocation: Getting GPS Location");
//                //Gets the last known Location to make waiting for the location faster
//                String locationProvider = LocationManager.GPS_PROVIDER;
//                Location location = mLocationManager.getLastKnownLocation(locationProvider);
//                lat = location.getLatitude();
//                lon = location.getLongitude();
//                Log.d(TAG, "getUserLocation: Last Known Location LON: " + lat + "LAT: " + lon);
//
//                //this is where we can update the camera
//
//                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, mLocationListener);
//            }else{
//                Log.d(TAG, "getUserLocation: GPS Error");
//            }
//        }
//    }

    //move the camera to the lat and lon
    private void updateCamera() {
        //doesnt run if onMapReady isnt called
        if (googleMap != null) {
            //update the location on maps after user gives permission for location
            Log.d(TAG, "updateCamera: Updating Camera at location  LAT: " + lat + " LON: " + lon);
            LatLng usersLocation = new LatLng(lat, lon);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(usersLocation).zoom(15).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    //get the GoogleMap when its ready
    private void enableMap() {
        Log.d(TAG, "enableMap: Enabling ");
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                updateCamera();
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }
            }
        });
    }

    /*
     *get boolean value of if GPS and Network is enabled
     * if not then alert the user to turn on GPS
     */
    private void gpsEnabled() {
        gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Location")
                    .setMessage("Turn On Your Location")
                    .setCancelable(false)
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //direct the user to phones location settings
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

    private void getData() {
        Log.d(TAG, "getData: Called");
        GetJsonData getJsonData = new GetJsonData("", Double.toString(lat), Double.toString(lon), MapViewFragment.this);
        getJsonData.execute();
    }

    //plots the crime data onto the google maps
    private void plotData(List<HistoricLocation> historicData) {
        if (googleMap != null) {
            googleMap.clear();//remove any other markers

            //loops though thte data and adds marker to each specified location
            for (HistoricLocation data : historicData) {
                LatLng usersLocation = new LatLng(data.getLat(), data.getLon());
                Log.d(TAG, "onMapReady: LAT: " + data.getLat() + " LON: " + data.getLon());
                googleMap.addMarker(new MarkerOptions().position(usersLocation).title("Marker Title").snippet("Marker Description"));
            }
        }
    }

    @Override
    public void locationUpdate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        getData();
        updateCamera();

    }

    @Override
    public void onDataAvailable(List<HistoricLocation> historicLocationsData) {
        //Toast.makeText(getContext(),"Data size = " + historicLocationsData.size(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDataAvailable: Data size " + historicLocationsData.size());
        checkDistance(historicLocationsData);
        plotData(historicLocationsData);
    }

    private void checkDistance(List<HistoricLocation> historicLocationsData) {
        Log.d(TAG, "checkDistance: Check Distance Starts");
        Location usersLoc = new Location("");
        usersLoc.setLatitude(lat);
        usersLoc.setLongitude(lon);

        int modelUnlockedCtr = 0;

        for (HistoricLocation location : historicLocationsData) {
            Location target = new Location("target");
            target.setLatitude(location.getLat());
            target.setLongitude(location.getLon());

            if (usersLoc.distanceTo(target) < DISTANCE_TO_UNLOCK) {
                Log.d(TAG, "checkDistance: ****UNLOCKED MODEL****");
                //this is where we will first check if the hologram is in the database already or not
                //and depending on that we can save it in the database
                String photoUrl = "https://maps.googleapis.com/maps/api/streetview?size=400x400&location="
                        +target.getLatitude()+","+target.getLongitude()
                        +"&size=20x20&key=" + API_KEY;
                Hologram newHologram = new Hologram(target.getLatitude(), target.getLongitude(), photoUrl);

                saveLocation(newHologram);
                modelUnlockedCtr++;

            } else {
                Log.d(TAG, "checkDistance: ****MODEL NOT UNLOCKED****");
            }
            Log.d(TAG, "checkDistance: Total Number of models Unlocked : " + modelUnlockedCtr);

        }
    }

    //this adds to the sharedprefs the users chosen spot to save location
    private void saveLocation(Hologram locationData) {

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE);
        String value = prefs.getString(SAVED_HOLO_KEY, "");

        SharedPreferences.Editor editor = getActivity()
                .getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE)
                .edit();
//        editor.clear().apply();
        Gson gson = new Gson();

        Log.d(TAG, "saveLocation: " + locationData.getPhotoUrl());

        if (value.equals("")) {

            //shared pref is empty, then create a new one
            Log.d(TAG, "saveLocation: Shared prefs is empty");

            //Create a new List to add to the shared prefs
            List<Hologram> savedLoc = new ArrayList<>();
            savedLoc.add(locationData);
            //recyclerViewAdapter.loadNewData(savedLoc);
            String json = gson.toJson(savedLoc);
            Log.d(TAG, "saveLocation: data is " + json);
            editor.putString(SAVED_HOLO_KEY, json);
            editor.apply();
        } else {
            //get the key val pair from shared pref
            Log.d(TAG, "saveLocation: Shared prefs data " + value);
            //get the list from shared prefs
            String json = prefs.getString(SAVED_HOLO_KEY, "");
            Type type = new TypeToken<List<Hologram>>() {
            }.getType();

            //retrieve the arraylist from JSON given the type
            List<Hologram> savedLoc = gson.fromJson(json, type);
            savedLoc.add(locationData);

            //recyclerViewAdapter.loadNewData(savedLoc);

            Log.d(TAG, "saveLocation: Data Size is " + savedLoc.size());

            //convert the savedLoc list to JSON to store it as a string
            String newJson = gson.toJson(savedLoc);
            editor.putString(SAVED_HOLO_KEY, newJson);
            editor.apply();
        }
    }

}