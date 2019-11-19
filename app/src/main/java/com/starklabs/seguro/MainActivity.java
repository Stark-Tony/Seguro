package com.starklabs.seguro;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements com.starklabs.seguro.DrawerLocker {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    static View rootLayout;
    static LatLng sourceLL=null;
    static LatLng destLL=null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        rootLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.main_navigation_view);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_about:
                        Intent intent_about = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent_about);
                        break;
                    case R.id.menu_contact:
                        Intent intent_contact = new Intent(MainActivity.this, ContactActivity.class);
                        startActivity(intent_contact);
                        break;
                    case R.id.menu_item_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new SettingsFragment()).addToBackStack("SettingsFragment").commit();

                        break;
                    case R.id.menu_item_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new AccountFragment()).addToBackStack("AccountFragment").commit();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Implementaion in progress", Toast.LENGTH_SHORT).show();

                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if (savedInstanceState == null) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new GMapFragment()).commit();
        }

    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            MenuItem item = mNavigationView.getCheckedItem();
            if (item != null)
                item.setChecked(false);
            super.onBackPressed();
        }

    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        mDrawerLayout.setDrawerLockMode(lockMode);
    }
    @Override
    protected void onResume() {
        super.onResume();
        new HideStatus().hideStatus(getWindow());
    }
}
