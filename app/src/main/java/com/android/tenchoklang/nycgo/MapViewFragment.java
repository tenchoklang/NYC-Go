package com.android.tenchoklang.nycgo;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
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

/**
 * Created by tench on 4/30/2018.
 */
public class MapViewFragment extends Fragment{

    MapView mMapView;
    private GoogleMap googleMap;
    private static final String TAG = "MapViewFragment";
    private static double lat = 0;
    private static double lon = 0;

    private LocationManager mLocationManager;


    private static final int REQUESTED_CODE_LOCATION = 1;

    private FloatingActionButton floatingActionButton;

    //listener for when users location changes
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //called for when gps initially on and when gps location changes
            Log.d(TAG, "onLocationChanged: LAT: " + location.toString());

            lat = location.getLatitude();
            lon = location.getLongitude();
            updateLocation();
//            Toast.makeText(getContext(), "LOCATION UPDATED",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {   }
        @Override
        public void onProviderEnabled(String provider) {   }
        @Override
        public void onProviderDisabled(String provider) {   }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mapview, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);


        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
//        getLocationOnClick();


        int hasLocationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if(hasLocationPermission == PackageManager.PERMISSION_GRANTED){
            //permission had been granted
            Log.d(TAG, "onClick: Location Permission Granted");
            enableMap();
            locationOnClick();
        }else{
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
        switch (requestCode){
            case REQUESTED_CODE_LOCATION:{
                //if the grantResults arr is not empty && grantResult == PackageManager.PERMISION_GRANTED
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission has been granted
                    //do the contacts related task you need to do
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    enableMap();
                    locationOnClick();//get location when button clicked
                }else{
                    //permission denied
                    //disable the functionality that depends on this permission
                    Log.d(TAG, "onRequestPermissionsResult: Permission Denied");
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Ends");
    }

    private void locationOnClick(){

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this would launch the NYC Open data API to get the landmarks around specified by the distance
                Toast.makeText(getContext(), "LAT: " + lat + " LON: "+ lon,Toast.LENGTH_SHORT).show();

                //this is where we make the call to the NYC Open Data to get the LandMark Information

            }
        });
    }


    private void updateLocation(){
        //update the location on maps after user gives permission for location
        Log.d(TAG, "updateLocation: Updating locaiton LAT: " + lat + " LON: " + lon);
        LatLng usersLocation = new LatLng(lat, lon);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(usersLocation).zoom(15).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void enableMap(){
        Log.d(TAG, "enableMap: Enabling ");
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                Toast.makeText(getContext(), "MAP READY", Toast.LENGTH_SHORT).show();
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,50, mLocationListener);
                    googleMap.setMyLocationEnabled(true);
                    //updateLocation();
                }

                // For dropping a marker at a point on the Map
//                LatLng usersLocation = new LatLng(-34, 151);
//                Log.d(TAG, "onMapReady: LAT: " + lat + " LON: " + lon);
//                googleMap.addMarker(new MarkerOptions().position(usersLocation).title("Marker Title").snippet("Marker Description"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(usersLocation).zoom(15).build();
//                //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });
    }


}
