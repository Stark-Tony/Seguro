package com.starklabs.seguro;


import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class AutoCompleteFragment extends Fragment {

    //AutoCompleteTextView source;
    //AutoCompleteTextView destination;
    Location currentLocation;
    LatLng sourceLatLang, destLatLang;
    AutocompleteSupportFragment autocompleteSource, autocompleteDestination;
    LinearLayout myLocation;
    Button doneButton;
    private static final String [] Addr=new String[]{"11", "12", "13"};
    ArrayAdapter<String> mArrayAdapter;
    PlacesClient mPlacesClient;
    public AutoCompleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DrawerLocker drawerLocker= (DrawerLocker) getActivity();
        drawerLocker.setDrawerEnabled(false);
        return inflater.inflate(R.layout.fragment_auto_complete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        autocompleteSource = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_source);
        autocompleteDestination = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_destination);
        doneButton = getActivity().findViewById(R.id.done_auto_complete);
        myLocation = getActivity().findViewById(R.id.mylocation_layout);

        autocompleteSource.a.setTextSize(12);
        autocompleteDestination.a.setTextSize(12);
        autocompleteSource.a.setHint("Source");
        autocompleteDestination.a.setHint("Destination");
        autocompleteSource.a.setTextColor(Color.WHITE);
        autocompleteDestination.a.setTextColor(Color.WHITE);

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                locationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location==null)
                        {
                            Snackbar.make(getView(),"Location unavailable\nEnter location manually",Snackbar.LENGTH_LONG).show();
                        }
                        else
                        {
                            Geocoder geocoder = new Geocoder (getContext());
                            List<Address> address;
                            try {
                                address=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                if(address==null || address.size()==0)
                                {
                                    Toast.makeText(getActivity(), "Unable to fetch location", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    autocompleteSource.a.setText(address.get(0).getAddressLine(0));
                                    currentLocation = location;
                                    LatLng tempLatLang = new LatLng(location.getLatitude(), location.getLongitude());
                                    sourceLatLang = tempLatLang;
                                }
                            } catch (IOException e) {
                                Toast.makeText(getActivity(), "Unable to fetch location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        //Sending data to Google Maps to de drawn on map
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.sourceLL=sourceLatLang;
                MainActivity.destLL=destLatLang;
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        if(!Places.isInitialized())
        {
            Places.initialize(getContext(),"AIzaSyAwdxRlmAXwjm_mcFQnM-f-vguZr6JkxV8");
        }
        mPlacesClient = Places.createClient(getContext());

        autocompleteSource.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG,Place.Field.ID,Place.Field.NAME));
        autocompleteSource.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.d("places","Places:"+place.getLatLng()+":"+place.getAddress());
                autocompleteSource.a.setText(place.getAddress());
                sourceLatLang = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getContext(),"Error in fetching the results",Toast.LENGTH_SHORT).show();
            }
        });

        autocompleteDestination.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG,Place.Field.ID,Place.Field.NAME));
        autocompleteDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.d("places","Places:"+place.getLatLng()+":"+place.getAddress());
                autocompleteDestination.a.setText(place.getAddress());
                destLatLang = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getContext(),"Error in fetching the results",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        DrawerLocker drawerLocker= (DrawerLocker) getActivity();
        drawerLocker.setDrawerEnabled(true);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
