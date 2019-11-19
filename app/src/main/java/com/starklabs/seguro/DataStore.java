package com.starklabs.seguro;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DataStore {
    private ArrayList<Integer> crimeData;
    private ArrayList<LatLng> dataPoints;
    private ArrayList<Integer> crimecounts;

    public ArrayList<Integer> getCrimecounts() {
        return crimecounts;
    }

    public void setCrimecounts(ArrayList<Integer> crimecounts) {
        this.crimecounts = crimecounts;
    }

    public ArrayList<Integer> getCrimeData() {
        return crimeData;
    }

    public ArrayList<LatLng> getDataPoints() {
        return dataPoints;
    }

    public void setCrimeData(ArrayList<Integer> crimeData) {
        this.crimeData = crimeData;
    }

    public void setDataPoints(ArrayList<LatLng> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
