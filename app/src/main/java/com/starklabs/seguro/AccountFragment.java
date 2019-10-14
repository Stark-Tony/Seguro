package com.starklabs.seguro;


import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;


public class AccountFragment extends Fragment {


    MaterialTextView edit_acount_name,edit_acount_email,edit_acount_dob,edit_acount_phone_no,acount_change_pass;
    TextInputEditText acount_name,acount_email,acount_dob,acount_phone_no;

    public AccountFragment() {
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

        View acount_view=inflater.inflate(R.layout.fragment_account, container, false);
        edit_acount_name = acount_view.findViewById(R.id.edit_acount_name);
        edit_acount_email =  acount_view.findViewById(R.id.edit_acount_email);
        edit_acount_dob =  acount_view.findViewById(R.id.edit_acount_dob);
        edit_acount_phone_no = acount_view.findViewById(R.id.edit_acount_phone_no);
        acount_name= acount_view.findViewById(R.id.acount_name);
        acount_email= acount_view.findViewById(R.id.acount_email);
        acount_dob= acount_view.findViewById(R.id.acount_dob);
        acount_phone_no= acount_view.findViewById(R.id.acount_phone_no);
        acount_change_pass= acount_view.findViewById(R.id.acount_change_pass);

        edit_acount_name.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(acount_name.isEnabled())
                 {
                     //savehere
                     acount_name.setEnabled(false);
                 }
                 else
                 {
                     acount_name.setEnabled(true);
                 }
             }
         });

        edit_acount_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(acount_email.isEnabled())
                {
                    //save here
                    acount_email.setEnabled(false);
                }
                else
                {
                    acount_email.setEnabled(true);
                }
            }
        });

        edit_acount_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(acount_dob.isEnabled())
                {
                    //save here
                    acount_dob.setEnabled(false);
                }
                else
                {
                    acount_dob.setEnabled(true);
                }
            }
        });

        edit_acount_phone_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(acount_phone_no.isEnabled())
                {
                    acount_phone_no.setEnabled(false);
                }
                else
                {
                    acount_phone_no.setEnabled(true);
                }
            }
        });


        acount_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        return acount_view;
    }

    @Override
    public void onDestroy() {
        DrawerLocker drawerLocker= (DrawerLocker) getActivity();
        drawerLocker.setDrawerEnabled(true);
        super.onDestroy();
    }
}
