package com.starklabs.seguro;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;


public class AutoCompleteFragment extends Fragment {

    TextInputEditText source;
    TextInputEditText destination;
    Location currentLocation;
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

        source = view.findViewById(R.id.source_place);
        destination = view.findViewById(R.id.destination_place);

        source.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_RIGHT=2;
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    if(event.getRawX()>=(source.getRight()-source.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        source.setShowSoftInputOnFocus(false);
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
                                            source.setText(address.get(0).getAddressLine(0));
                                            currentLocation = location;
                                        }
                                    } catch (IOException e) {
                                        Toast.makeText(getActivity(), "Unable to fetch location", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        return true;
                    }
                    else
                    {
                        source.setShowSoftInputOnFocus(true);
                    }
                }
                return false;
            }
        });

        source.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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
