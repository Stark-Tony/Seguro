package com.starklabs.seguro;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PointParser
{
    DataStore mDataStore;
    public DataStore pointParser(String response)
    {
        mDataStore = new DataStore();
        ArrayList<LatLng> points = new ArrayList<>();
        ArrayList<Integer> crimeonpoints=new ArrayList<>();
        ArrayList<Integer> crimesCounts = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray pointsArray = jsonObject.getJSONArray("points");
            JSONArray crimeCountArray = jsonObject.getJSONArray("numofcrimes");
            String crimeString=crimeCountArray.toString();
            for(int i=0;i<crimeCountArray.length();i++)
            {
                String tempObj =crimeCountArray.get(i).toString();
                crimesCounts.add(Integer.parseInt(tempObj));
            }
            Log.d("datapointstoint",crimesCounts+"");
            Log.d("dataPoints",crimeString);
            Gson converter = new Gson();
            Type type = new TypeToken<List<String>>(){}.getType();
            for(int i =0;i<pointsArray.length();i++)
            {
                JSONObject jsonObject1 = pointsArray.getJSONObject(i);
                double lat,lon;
                String ll,ln,cc;
                int crimecount;
                ln = jsonObject1.getString("longitude");
                ll = jsonObject1.getString("latitude");
                cc=jsonObject1.getString("numberOfCrimes");
                crimecount=Integer.parseInt(cc);
                lat = Double.parseDouble(ll);
                lon = Double.parseDouble(ln);
                crimeonpoints.add(crimecount);
                points.add(new LatLng(lat,lon));
            }
            Log.d("latlan",points.toString());
            mDataStore.setCrimeData(crimesCounts);
            mDataStore.setDataPoints(points);
            mDataStore.setCrimecounts(crimeonpoints);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mDataStore;
    }
}
