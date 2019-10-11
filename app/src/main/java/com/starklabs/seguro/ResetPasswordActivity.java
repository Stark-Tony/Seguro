package com.starklabs.seguro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }
    @Override
    protected void onResume() {
        super.onResume();
        new HideStatus().hideStatus(getWindow());
    }
}
