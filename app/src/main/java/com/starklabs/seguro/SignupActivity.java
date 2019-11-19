package com.starklabs.seguro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    TextView signup_date;
    ImageView signup_calender;
    MaterialTextView signup_warning;
    TextInputEditText signup_name;
    TextInputEditText signup_email;
    TextInputEditText signup_phone;
    TextInputEditText signup_password;
    TextInputEditText signup_confirm_password;
    MaterialButton signup_button_confirm;
    RadioGroup radioGroup;
    RadioButton rbMale, rbFemale, rbOthers;
    Window window;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        window = getWindow();
        signup_date = findViewById(R.id.signup_date);
        signup_calender = findViewById(R.id.signup_calender);
        signup_warning = findViewById(R.id.signup_warning);
        signup_name = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_phone = findViewById(R.id.signup_phone);
        signup_password = findViewById(R.id.signup_password);
        signup_confirm_password = findViewById(R.id.signup_confirm_password);
        signup_button_confirm = findViewById(R.id.signup_button_confirm);
        radioGroup = findViewById(R.id.signup_gender);
        rbMale = findViewById(R.id.signup_radio_male);
        rbFemale = findViewById(R.id.signup_radio_female);
        rbOthers = findViewById(R.id.signup_radio_others);

        mProgressDialog = new ProgressDialog(this);
        signup_button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check_signup = signup_validation();
                if (check_signup == 1) {
                    mProgressDialog.setMessage("Signing up...");
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
                    String fullname, email, phonenumber, password, confirm_password;
                    fullname = signup_name.getText().toString().trim();
                    email = signup_email.getText().toString().trim();
                    phonenumber = signup_phone.getText().toString().trim();
                    password = signup_password.getText().toString().trim();
                    confirm_password = signup_confirm_password.getText().toString().trim();

                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    String sDate1 = signup_date.getText().toString().trim();
                    Date date1 = null;
                    try {
                        date1 = new SimpleDateFormat("dd-MM-yyyy").parse(sDate1);
                    } catch (ParseException e) {
                        Toast.makeText(SignupActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                    RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                    String gender = (String) radioButton.getText();

                    String url = "http://192.168.43.146:8081/api/SignUp/AddUser/"+fullname+"/"+email+"/"+phonenumber+"/"+password+"/"+gender+"/"+sDate1;
                    Log.d("url",url);
                    StringRequest stringrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("emailerror")) {
                                mProgressDialog.cancel();
                                signup_warning.setText("Email already exists");
                            } else if(response.equals("success")){
                                mProgressDialog.cancel();
                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("LogInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isLoggedIn",true);
                                editor.commit();
                                Toast.makeText(SignupActivity.this,"Successfully Signed Up",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                SplashActivity.staticProgress = new ProgressDialog(SignupActivity.this);
                                SplashActivity.staticProgress.setMessage("Loading Maps...");
                                SplashActivity.staticProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                SplashActivity.staticProgress.show();
                                finish();
                            }
                            else if(response.equals("phoneerror"))
                            {
                                mProgressDialog.cancel();
                                signup_warning.setText("Phone number already exists");
                            }
                            else
                            {
                                mProgressDialog.cancel();
                                signup_warning.setText("Something went wrong:"+response);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mProgressDialog.cancel();
                            Log.d("error",error.toString());
                            signup_warning.setText("Error Connecting Server:"+error.toString());
                        }
                    });
                    requestQueue.add(stringrequest);
                }
                else {
                    //signup_warning.setText("");
                }
            }
        });

        signup_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });


    }

    private void pickDate() {
        Calendar calender = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String day_of_month, month_str;
                if (dayOfMonth < 10) {
                    day_of_month = "0" + dayOfMonth;
                } else {
                    day_of_month = dayOfMonth + "";
                }
                month = month + 1;
                if (month < 10) {
                    month_str = "0" + month;
                } else {
                    month_str = "" + month;
                }
                signup_date.setText(new StringBuilder().append(day_of_month).append("-").append(month_str).append("-").append(year));
            }
        }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        datePickerDialog.setCanceledOnTouchOutside(false);
        datePickerDialog.show();

    }

    @Override
    protected void onResume() {
        signup_warning.setText("here in on resume");
        super.onResume();
    }

    public int signup_validation() {
        String fullname, email, phonenumber, password, confirm_password;

        fullname = signup_name.getText().toString();
        email = signup_email.getText().toString();
        phonenumber = signup_phone.getText().toString();
        password = signup_password.getText().toString();
        confirm_password = signup_confirm_password.getText().toString();

        int check_password = 0, check_email = 0, check_phonenumber = 0, check_gender = 0, check_name = 0;

        check_password = validate_password(password, confirm_password);
        check_phonenumber = validate_phone(phonenumber);
        check_email = validate_email(email);
        check_name = validate_name(fullname);
        int id = radioGroup.getCheckedRadioButtonId();
        if (id == -1) {
            // no radio buttons are checked
            check_gender = 0;
        } else {
            // one of the radio buttons is checked
            check_gender = 1;
        }
        if (check_email == 1 && check_password == 1 && check_phonenumber == 1 && check_name == 1 && check_gender == 1) {
            return 1;
        } else {
            signup_warning.setText("invalid details");
            return 0;
        }
    }

    public int validate_password(String password, String confirm_password) {
        password.trim();
        confirm_password.trim();
        if (password.contentEquals(confirm_password)) {
            return 1;
        }
        return 0;
    }

    public int validate_phone(String phonenumber) {
        phonenumber.trim();
        Pattern phone_pattern = Pattern.compile("(0|91)?[7-9][0-9]{9}");
        Matcher phone_matcher = phone_pattern.matcher(phonenumber);
        if (phone_matcher.find() && (phone_matcher.group().equals(phonenumber))) {
            return 1;
        }
        return 0;
    }

    public int validate_email(String email) {
        email.trim();
        Pattern phone_pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher phone_matcher = phone_pattern.matcher(email);
        if (phone_matcher.find() && (phone_matcher.group().equals(email))) {
            return 1;
        }
        return 0;
    }

    public int validate_name(String name) {
        name.trim();
        if (name.equals("")) {
            return 0;
        }
        return 1;
    }
}
