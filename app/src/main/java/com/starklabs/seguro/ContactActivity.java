package com.starklabs.seguro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class ContactActivity extends AppCompatActivity {

    private MaterialTextView contact_email_1, contact_email_2, contact_phone_1, contact_phone_2;
    private ImageView contact_social_1, contact_social_2, contact_social_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contact_email_1 = findViewById(R.id.contact_email_1);
        contact_email_2 = findViewById(R.id.contact_email_2);
        contact_phone_1 = findViewById(R.id.contact_phone_1);
        contact_phone_2 = findViewById(R.id.contact_phone_2);
        contact_social_1 = findViewById(R.id.contact_social_1);
        contact_social_2 = findViewById(R.id.contact_social_2);
        contact_social_3 = findViewById(R.id.contact_social_3);


        contact_email_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", contact_email_1.getText().toString(), null));
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        contact_email_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", contact_email_2.getText().toString(), null));
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        contact_phone_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact_phone_1.getText().toString()));
                startActivity(intent);
            }
        });

        contact_phone_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact_phone_2.getText().toString()));
                startActivity(intent);
            }
        });

        contact_social_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.linkedin.com/in/mohd-mohtashim-nawaz-423812148"));
                startActivity(intent);
            }
        });

        contact_social_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://m.facebook.com/in/nawaz.mohtashim"));
                startActivity(intent);
            }
        });

        contact_social_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://mobile.twitter.com/iMMNawaz"));
                startActivity(intent);
            }
        });
    }
}
