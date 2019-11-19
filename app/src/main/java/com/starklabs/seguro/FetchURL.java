package com.starklabs.seguro;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.JointType;
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
    static ArrayList<ArrayList<String>> routesList;
    GoogleMap mMap;
    LatLng source, destination;
    ProgressDialog mProgressDialog;
    Window mWindow;
    public FetchURL(Window window, Context context, GoogleMap mMap, LatLng source, LatLng destination)
    {
        mWindow=window;
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
        routesList= new ArrayList<>();
        DataParser dataParser = new DataParser();
        routesList=dataParser.getPolyline(object);
        Log.d("size",routesList.size()+"");
        int i=1;
        int r=40,g=40,b=40;
        mMap.clear();
        /*for(ArrayList<String> route:routesList)
        {
            polyLines = route;
            for(String poly:polyLines)
            {
                PolylineOptions options = new PolylineOptions();
                options.color(Color.rgb(r,g,b));
                options.width(12);
                options.jointType(JointType.ROUND);
                options.addAll(PolyUtil.decode(poly));
                mMap.addPolyline(options);
            }
            if(i%3==1)
            {
                g+=100;
            }
            else if (i%3==2)
            {
                b+=100;
            }
            else if(i%3==0)
            {
                r+=100;
            }
            i++;
            if(r>255)
                r=40;
            if(g>255)
                g=40;
            if(b>255)
                b=40;
        }*/
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,15));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(source.latitude,source.longitude)).title("Source"));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(destination.latitude,destination.longitude)).title("Destination"));
        mProgressDialog.cancel();
        new HideStatus().hideStatus(mWindow);
    }

    private String fetchData (String url) throws IOException {
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Loading your routes...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }
}
