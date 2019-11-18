package com.starklabs.seguro;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FetchURL extends AsyncTask<String, Void,String> {
    Context mContext;
    String object="";
    ArrayList<String> polyLines;
    GoogleMap mMap;
    LatLng source, destination;
    public FetchURL(Context context, GoogleMap mMap, LatLng source, LatLng destination)
    {
        mContext = context;
        this.mMap = mMap;
        this.source=source;
        this.destination=destination;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            object=fetchData("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        polyLines = new ArrayList<>();
        DataParser dataParser = new DataParser();
        polyLines=dataParser.getPolyline(object);
        for(int i=0;i<polyLines.size();i++)
        {
            Log.d("mylog",polyLines.get(i)+"\n");
        }
        for(String poly:polyLines)
        {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.rgb(40,40,40));

            options.width(10);

            options.addAll(PolyUtil.decode(poly));
            mMap.addPolyline(options);
            mMap.addMarker(new MarkerOptions().position(new LatLng(source.latitude,source.longitude)).title("Source"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(destination.latitude,destination.longitude)).title("Destination"));
        }
    }

    public String fetchData (String url) throws IOException {
        HttpURLConnection connection=null;
        InputStream inputStreamReader=null;
        String line="";
        StringBuffer responseContent = null;
        try {

            URL url1 = new URL("https://maps.googleapis.com/maps/api/directions/json?origin="+source.latitude+","+source.longitude+"&destination="+destination.latitude+","+destination.longitude+"&alternatives=true&key=AIzaSyAwdxRlmAXwjm_mcFQnM-f-vguZr6JkxV8");
            connection = (HttpURLConnection) url1.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();
            inputStreamReader= connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamReader));
            responseContent = new StringBuffer();
            while((line=reader.readLine())!=null)
            {
                responseContent.append(line);
            }
            Log.d("mylog", "Downloaded URL: " + responseContent.toString());
            reader.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
        finally {
            inputStreamReader.close();
            connection.disconnect();
        }
        return responseContent.toString();
    }

}
