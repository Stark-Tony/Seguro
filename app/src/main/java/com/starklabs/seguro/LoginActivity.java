package com.starklabs.seguro;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity {

    private MaterialTextView login_warning, login_forgot_password, login_signup_link, login_about_link, login_contact_link;
    private TextInputEditText login_email, login_password;
    private MaterialButton login_button_login;
    private AlertDialog.Builder dialogBuilder, rationalBuilder;
    private DialogInterface.OnClickListener dialogClickListener, rationalClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1011);
        }

        login_warning = findViewById(R.id.login_warning);
        login_forgot_password = findViewById(R.id.login_forgot_password);
        login_signup_link = findViewById(R.id.login_signup_link);
        login_about_link = findViewById(R.id.login_about_link);
        login_contact_link = findViewById(R.id.login_contact_link);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_button_login = findViewById(R.id.login_button_login);

        if (savedInstanceState != null) {
            login_email.setText(savedInstanceState.getString("string_email"));
            login_password.setText(savedInstanceState.getString("string_password"));
        }

        login_button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        login_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        login_signup_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login_about_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        login_contact_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {

        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Do you want to exit?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        //super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("string_email", login_email.getText().toString());
        outState.putString("string_password", login_password.getText().toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1011)
        {
            for(int i=0;i<permissions.length;i++)
            {
                String perm=permissions[i];
                if(grantResults[i]==PackageManager.PERMISSION_DENIED)
                {
                    rationalBuilder = new AlertDialog.Builder(this);
                    boolean showRationle = ActivityCompat.shouldShowRequestPermissionRationale(this,perm);
                    if(!showRationle)
                    {
                        rationalBuilder.setMessage("Location permission is required\nGrant permission?").setPositiveButton("Yes", rationalClickListener).setNegativeButton("Exit", rationalClickListener).show();
                    }
                    else
                    {
                        rationalBuilder.setMessage("Location permission is required\nGrant permission?").setPositiveButton("Yes", rationalClickListener).setNegativeButton("Exit", rationalClickListener).show();
                    }
                }
            }
        }

        rationalClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",getPackageName(),null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        finish();
                        break;
                }
            }
        };

    }
}
