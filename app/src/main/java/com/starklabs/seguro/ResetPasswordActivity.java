package com.starklabs.seguro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class ResetPasswordActivity extends AppCompatActivity {

    HideStatus hideStatusBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        hideStatusBar = new HideStatus();
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (!isOpen) {
                    hideStatusBar.hideStatus(getWindow());
                }
            }
        });
    }
    @Override
    protected void onResume() {
        hideStatusBar.hideStatus(getWindow());
        super.onResume();
    }
}
