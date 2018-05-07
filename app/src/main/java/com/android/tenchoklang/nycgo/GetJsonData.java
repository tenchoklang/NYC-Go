package com.android.tenchoklang.nycgo;

/**
 * Created by tench on 5/7/2018.
 */


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetJsonData extends AsyncTask<String, Void ,String> implements GetRawData.OnDownloadComplete{

    private static final String TAG = "GetJsonData";

    private String mBaseUrl;
    private String latitude;
    private String longitude;

    private final OnDataAvailable mCallBack;

    private List<HistoricLocation> historicLocationData = null;

    interface OnDataAvailable{
        void onDataAvailable(List<HistoricLocation> crimeData);
    }

    public GetJsonData(String baseUrl, String latitude, String longitude, OnDataAvailable callBack){
        this.mBaseUrl = baseUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        mCallBack = callBack;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mCallBack.onDataAvailable(historicLocationData);
    }

    @Override
    protected String doInBackground(String... strings) {
        GetRawData getRawData = new GetRawData(this);
        Log.d(TAG, "doInBackground: starts");
        Log.d(TAG, "doInBackground: createUrl " + createUrl().replace(" ", "%20"));
        getRawData.runInSameThread(createUrl().replace(" ", "%20"));//replace the spaces
        return null;
    }

    private String createUrl(){
        //return "https://data.cityofnewyork.us/resource/7x9x-zpz6.json?$where=within_circle(lat_lon, 40.761185, -73.905086, 500)&ofns_desc='ROBBERY'";
        return "https://data.cityofnewyork.us/resource/rb9s-d3m8.json?$where=within_circle(the_geom, "
                + latitude
                +", "
                + longitude + ", 100)";
    }

    /**
     *
     * @param data: data is the rawJson data returned as a string
     */
    @Override
    public void onDownloadComplete(String data) {
        try{

            historicLocationData = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(data);
            Log.d(TAG, "onDownloadComplete: Json array Size " + jsonArray.length());
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonData = jsonArray.getJSONObject(i);


                JSONObject jsonLat_Lon = jsonData.getJSONObject("the_geom");
                JSONArray coordinates = jsonLat_Lon.getJSONArray("coordinates");

                double lat = Double.parseDouble(coordinates.get(1).toString());//latitude
                double lon = Double.parseDouble(coordinates.get(0).toString());//longitude
                HistoricLocation historicLocation = new HistoricLocation(lat, lon, null);
                Log.d(TAG, "onDownloadComplete: Data: " + historicLocation.toString());
                historicLocationData.add(historicLocation);

            }
            Log.d(TAG, "onDownloadComplete: CrimeData Size " + historicLocationData.size());
        }catch(org.json.JSONException e){

        }

    }
}