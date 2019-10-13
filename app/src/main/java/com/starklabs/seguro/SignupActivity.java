package com.starklabs.seguro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
    @Override
    protected void onResume() {
        super.onResume();
        new HideStatus().hideStatus(getWindow());
    }
}
