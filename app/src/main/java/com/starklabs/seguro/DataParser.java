package com.starklabs.seguro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataParser {
    public ArrayList<ArrayList<String>> getPolyline(String responseBody)
    {
        ArrayList<ArrayList<String>>  routesList = new ArrayList<>();
        ArrayList<String> polyLinesList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray routeJsonArray = jsonObject.getJSONArray("routes");
            for (int i = 0; i < routeJsonArray.length(); i++)
            {
                ArrayList<String> routelist=new ArrayList<>();
                JSONObject route = routeJsonArray.getJSONObject(i);
                JSONArray legJsonArray = route.getJSONArray("legs");
                for(int j=0;j<legJsonArray.length();j++)
                {
                    JSONObject leg = legJsonArray.getJSONObject(j);
                    JSONArray stepJsonArray = leg.getJSONArray("steps");
                    for(int k =0;k<stepJsonArray.length();k++)
                    {
                        JSONObject step = stepJsonArray.getJSONObject(k);
                        String polyLine =step.getJSONObject("polyline").getString("points");
                        routelist.add(polyLine);
                    }
                }
                routesList.add(routelist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routesList;
    }
}
