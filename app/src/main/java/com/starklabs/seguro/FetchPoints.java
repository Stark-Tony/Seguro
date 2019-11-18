package com.starklabs.seguro;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchPoints extends AsyncTask {
    Context mContext;
    GoogleMap mMap;
    LatLng source, dest;
    String retString="";
    public FetchPoints(Context mContext,GoogleMap mMap, LatLng source, LatLng dest) {
        this.mMap=mMap;
        this.source=source;
        this.dest=dest;
        this.mContext=mContext;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try{
            retString = fetchPoints();
        }
        catch (Exception e)
        {
            Toast.makeText(mContext,"Couldn't read the points",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }

    private String fetchPoints() throws Exception
    {
        HttpURLConnection connection=null;
        InputStream inputStreamReader=null;
        String line="";
        StringBuffer responseContent = null;
        try{
            URL url= new URL("http://172.19.14.190/api/Getpath/"+source.latitude+"/"+source.longitude+"/"+dest.latitude+"/"+dest.longitude);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
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
            inputStreamReader.close();
            connection.disconnect();
        }
        return responseContent.toString();
    }
}
