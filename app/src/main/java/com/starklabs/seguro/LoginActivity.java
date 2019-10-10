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
import android.util.Log;
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
    private DialogInterface.OnClickListener dialogClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginActivity.checkPerms(this);

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
        if (requestCode == 1011) {


            if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                rationalBuilder = new AlertDialog.Builder(this);
                rationalBuilder.setMessage("Location permission is required\nGrant permission?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.startAppSettings(LoginActivity.this);

                        dialog.dismiss();
                    }
                }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
            }
        }

    }

    public static void startAppSettings(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    public static void checkPerms(Activity context)
    {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1011);
        }
    }
}
