package com.starklabs.seguro;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsFragment extends Fragment {

    Switch mSwitch, sSwitch;
    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DrawerLocker drawerLocker= (DrawerLocker) getActivity();
        drawerLocker.setDrawerEnabled(false);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        return inflater.inflate(R.layout.fragment_settings, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwitch = getActivity().findViewById(R.id.switch_settings);
        sSwitch = getActivity().findViewById(R.id.switch_satellite);
        final SharedPreferences sharedPreferences= getActivity().getSharedPreferences("SettingsPref", Context.MODE_PRIVATE);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(mSwitch.isChecked())
                {
                    editor.putBoolean("traffic",true);
                }
                else
                {
                    editor.putBoolean("traffic",false);
                }
                editor.commit();
            }
        });
        sSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(sSwitch.isChecked())
                {
                    editor.putBoolean("satellite",true);
                }
                else
                {
                    editor.putBoolean("satellite",false);
                }
                editor.commit();
            }
        });
        boolean traffic= sharedPreferences.getBoolean("traffic",false);
        if(traffic)
        {
            mSwitch.setChecked(true);
        }
        else
        {
            mSwitch.setChecked(false);
        }
        boolean satellite= sharedPreferences.getBoolean("satellite",false);
        if(satellite)
        {
            sSwitch.setChecked(true);
        }
        else
        {
            sSwitch.setChecked(false);
        }
    }

    @Override
    public void onDestroy() {
        DrawerLocker drawerLocker= (DrawerLocker) getActivity();
        drawerLocker.setDrawerEnabled(true);
        super.onDestroy();
    }


}
