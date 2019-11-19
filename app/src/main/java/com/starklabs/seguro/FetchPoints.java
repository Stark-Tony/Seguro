package com.starklabs.seguro;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import java.net.URL;
import java.util.ArrayList;

public class FetchPoints extends AsyncTask <String,Void,String>{
    Context mContext;
    GoogleMap mMap;
    LatLng source, dest;
    String retString="";
    ProgressDialog mProgressDialog;
    ArrayList<String> polyLines;
    public FetchPoints(Context mContext,GoogleMap mMap, LatLng source, LatLng dest) {
        this.mMap=mMap;
        this.source=source;
        this.dest=dest;
        this.mContext=mContext;
    }

    @Override
    protected String doInBackground(String... strings) {
        try{
            retString = fetchPoints();
        }
        catch (Exception e)
        {
            //Toast.makeText(mContext,"Couldn't read the points",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try
        {
            DataStore tempData = new DataStore();
            tempData = new PointParser().pointParser(retString);
            ArrayList<LatLng> crimePoints = tempData.getDataPoints();
            ArrayList<Integer> crimeCount = tempData.getCrimeData();
            ArrayList<Integer> crimeonpoints=tempData.getCrimecounts();
            mMap.clear();
            for(int i=0;i<crimePoints.size();i++)
            {
                CircleOptions circleOptions = new CircleOptions();
                LatLng tempLatLan = crimePoints.get(i);
                circleOptions.center(tempLatLan).radius(5).visible(true);
                if(crimeonpoints.get(i)<=30)
                {
                    circleOptions.fillColor(Color.rgb(10,82,2)).strokeWidth(2).strokeColor(Color.rgb(10,82,2));
                }
                else if(crimeonpoints.get(i)<=70)
                {
                    circleOptions.fillColor(Color.rgb(93,91,11)).strokeWidth(2).strokeColor(Color.rgb(93,91,11));
                }
                else
                {
                    circleOptions.fillColor(Color.rgb(255,0,0)).strokeWidth(2).strokeColor(Color.rgb(255,0,0));
                }
                mMap.addCircle(circleOptions);
            }
            int safe=0;
            int min = crimeCount.get(0);
            for(int i=1;i<crimeCount.size();i++)
            {
                if(crimeCount.get(i)<min)
                {
                    min=crimeCount.get(i);
                    safe=i;
                }
            }
            if(FetchURL.routesList!=null)
            {
                ArrayList<String>route= new ArrayList<>();
                for(int i=0;i<FetchURL.routesList.size();i++)
                {
                    if(i!=safe)
                    {
                        route=FetchURL.routesList.get(i);
                        for(String poly:route)
                        {
                            PolylineOptions options = new PolylineOptions();
                            options.color(Color.rgb(180,180,180));
                            options.width(12);
                            options.jointType(JointType.ROUND);
                            options.addAll(PolyUtil.decode(poly));
                            mMap.addPolyline(options);
                        }
                    }

                }
                route=FetchURL.routesList.get(safe);
                for(String poly:route)
                {
                    PolylineOptions options = new PolylineOptions();
                    options.color(Color.rgb(0,0,50));
                    options.width(12);
                    options.jointType(JointType.ROUND);
                    options.addAll(PolyUtil.decode(poly));
                    mMap.addPolyline(options);
                }
                String lastPoly = route.get(route.size()-1);
                String firstPoly = route.get(0);
                ArrayList<LatLng> startRoad = decodePoly(firstPoly);
                ArrayList<LatLng> endRoad = decodePoly(lastPoly);
                Bitmap start;
                start = bitmapConverter.getBitmap(mContext,R.drawable.start_point);
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(start))
                        .position(startRoad.get(0)));
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmapConverter.getBitmap(mContext,R.drawable.road_end)))
                        .position(endRoad.get(endRoad.size()-1)));
                mMap.addMarker(new MarkerOptions().position(endRoad.get(endRoad.size()-1)));
                mMap.addMarker(new MarkerOptions().position(startRoad.get(0)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startRoad.get(0),15));
            }

        }
        catch (Exception e)
        {
            Log.d("error",e.getMessage());
            Toast.makeText(mContext,"Error fetching data"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally {
            FetchURL.routesList.clear();
            mProgressDialog.cancel();
        }
    }

    private String fetchPoints()
    {
        HttpURLConnection connection=null;
        InputStream inputStreamReader=null;
        String line="";
        StringBuffer responseContent = null;
        try{
            URL url= new URL("http://172.19.14.190:8081/api/Getpath/"+source.latitude+"/"+source.longitude+"/"+dest.latitude+"/"+dest.longitude);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(60*1000);
            connection.setReadTimeout(60*1000);
            connection.connect();
            inputStreamReader =connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamReader));
            responseContent = new StringBuffer();
            while((line=reader.readLine())!=null)
            {
                responseContent.append(line);
            }
            Log.d("fetched", "Downloaded URL: " + responseContent.toString());
            reader.close();
        }
        catch (Exception e)
        {
            Toast.makeText(mContext,"Error connecting to server",Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                Toast.makeText(mContext,"Error loading the data",Toast.LENGTH_SHORT).show();
            }
            connection.disconnect();
        }
        return responseContent.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Getting Data...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        System.out.println("i am returning");
        return poly;
    }

}
