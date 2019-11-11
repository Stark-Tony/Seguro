package com.starklabs.seguro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;

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

        signup_button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signup_name.getText().toString().trim().equals(""))
                {
                    Toast.makeText(SignupActivity.this, "Please fill the name", Toast.LENGTH_LONG).show();
                }
                if (!rbMale.isChecked() && !rbFemale.isChecked() && !rbOthers.isChecked())
                {
                    Toast.makeText(SignupActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                }
                JSONObject signup_object = new JSONObject();
                String name = signup_name.getText().toString().trim();
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
                signup_date.setText(new StringBuilder().append(day_of_month).append("/").append(month_str).append("/").append(year));
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
        super.onResume();
    }

}
