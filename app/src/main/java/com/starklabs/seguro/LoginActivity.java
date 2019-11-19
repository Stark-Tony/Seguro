package com.starklabs.seguro;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.audiofx.Equalizer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Space;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

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
                login_warning.setText("");
                final ProgressDialog localProgress=new ProgressDialog(LoginActivity.this);
                if (login_email.getText().toString().trim().equals("") || login_password.getText().toString().trim().equals("")) {
                    login_warning.setText("None of the feilds can be empty");
                }
                else
                {
                    localProgress.setCancelable(false);
                    localProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    localProgress.setMessage("Logging In...");
                    localProgress.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    final String user_email,user_pass;
                    user_email = login_email.getText().toString().trim();
                    user_pass = login_password.getText().toString().trim();
                    String url = "http://172.19.14.190:8081/api/login/email/" + user_email + "/" + user_pass;
                    StringRequest stringrequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response",response);
                            if (response.contains("invalid")) {
                                localProgress.cancel();
                                login_warning.setText("Invalid Username/Password");
                            } else if(response.equals("valid")){
                                Toast.makeText(LoginActivity.this,"Logged in",Toast.LENGTH_SHORT).show();
                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("LogInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isLoggedIn",true);
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                localProgress.cancel();
                                SplashActivity.staticProgress = new ProgressDialog(LoginActivity.this);
                                SplashActivity.staticProgress.setMessage("Loading Maps...");
                                SplashActivity.staticProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                SplashActivity.staticProgress.show();
                                finish();
                            }
                            else
                            {
                                localProgress.cancel();
                                Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            localProgress.cancel();
                            login_warning.setText("Error Connecting Server");
                        }
                    });
                    requestQueue.add(stringrequest);
                }
                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //startActivity(intent);
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
                        new HideStatus().hideStatus(getWindow());
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

            if (grantResults.length >= 2) {
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
    }

    public static void startAppSettings(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    public static void checkPerms(Activity context) {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1011);
        }
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        login_warning.setText("");
        //Shift Internet check to splash screen OnCreate so that app quits if no internet connection is found
        if (!checkInternet()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Check internet connection and restart");
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    dialog.dismiss();
                }
            }).setCancelable(false).show();
        }
    }
}
