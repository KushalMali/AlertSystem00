package com.alertsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.alertsystem.R;
import com.alertsystem.utils.Constants;
import com.alertsystem.utils.SharedPreferences;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences.getInstance().initializeSharePreferences(SplashActivity.this);

        new Handler().postDelayed(this::checkLoginStatus, 3*1000);
    }

    private void checkLoginStatus() {

        boolean status = SharedPreferences.getInstance().fetchSharedPreferenesBoolean(Constants.PREF_USER_LOGGED_IN,false);
        if (status) {
            Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
            finish();
        } else {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
            finish();
        }
    }
}