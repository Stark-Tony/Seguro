package com.starklabs.seguro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Calendar;
import java.util.TimeZone;

public class SignupActivity extends AppCompatActivity {

    HideStatus hideStatusBar;
    TextInputEditText signup_date;
    MaterialTextView signup_warning;
    TextInputEditText signup_name;
    TextInputEditText signup_email;
    TextInputEditText signup_phone;
    TextInputEditText signup_password;
    TextInputEditText signup_confirm_password;
    MaterialButton signup_button_confirm;
    RadioGroup radioGroup;
    RadioButton rbMale, rbFemale, rbOthers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar = new HideStatus();
        setContentView(R.layout.activity_signup);

        signup_date = findViewById(R.id.signup_date);
        signup_warning = findViewById(R.id.signup_warning);
        signup_name=findViewById(R.id.signup_name);
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

            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (!isOpen) {
                    hideStatusBar.hideStatus(getWindow());
                }
            }
        });

        signup_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (signup_date.getRight() - signup_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        pickDate();
                        return true;
                    }
                }

                return false;
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
                    day_of_month=dayOfMonth+"";
                }
                month=month+1;
                if(month<10)
                {
                    month_str="0"+month;
                }
                else
                {
                    month_str=""+month;
                }
                signup_date.setText(new StringBuilder().append(day_of_month).append("/").append(month_str).append("/").append(year));
            }
        }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        hideStatusBar.hideStatus(getWindow());
    }

    @Override
    protected void onResume() {
        hideStatusBar.hideStatus(getWindow());
        super.onResume();
    }

}
