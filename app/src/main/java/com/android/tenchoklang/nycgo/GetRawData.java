package com.android.tenchoklang.nycgo;

/**
 * Created by tench on 5/7/2018.
 */


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tench on 1/25/2018.
 */


class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";
    private final OnDownloadComplete mCallBack;

    interface OnDownloadComplete{//used for callback in GetNewsJsonData
        void onDownloadComplete(String data);
    }

    public GetRawData(OnDownloadComplete callBack) {
        this.mCallBack = callBack;
    }

    //runs together on the GetNewsJsonData thread
    void runInSameThread(String newsApiUrl){
        Log.d(TAG, "runInSameThread: Starts");
        if(mCallBack != null){
            mCallBack.onDownloadComplete(doInBackground(newsApiUrl));
        }
        Log.d(TAG, "runInSameThread: Ends");
    }

    //This is not used when this class is running on the same thread as another class
    //used only when this is running on its own thread
    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: " + s);
    }


    //ApiUrl: is the final build up URL received from the GetNewsJsonData class
    @Override
    protected String doInBackground(String... apiUrl) {


        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;

        if(apiUrl == null){//
            Log.d(TAG, "doInBackground: ApiUrl is null " + apiUrl);
            return null;
        }

        try{
            URL url = new URL(apiUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "doInBackground: Response code = " + responseCode);
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder result = new StringBuilder();

            for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()){
                result.append(line).append("\n");
            }
            return result.toString();//return the raw json data as a string

        }catch(MalformedURLException e){
            Log.d(TAG, "doInBackground: MalformedURL exception " + e.getMessage());
        }catch(IOException e){
            Log.d(TAG, "doInBackground: IOException " + e.getMessage());
        }catch(SecurityException e){
            Log.d(TAG, "doInBackground: Security Exception " + e.getMessage());
        }finally {//this is aways executed, called before the return
            if(connection != null){
                connection.disconnect();
            }
            if(bufferedReader != null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream" + e.getMessage());
                }
            }
        }

        return null;
    }
}
