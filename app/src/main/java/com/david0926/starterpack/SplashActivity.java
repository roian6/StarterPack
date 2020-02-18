package com.david0926.starterpack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //fullscreen

        new Handler().postDelayed(() -> {

            boolean isLandingShown = getSharedPreferences(this).getBoolean("landing_shown", false);
            //isLandingShown = false; //remove this line, to show landing page only once

            if (isLandingShown)
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            else {
                getSharedPreferences(this)
                        .edit()
                        .putBoolean("landing_shown", true)
                        .apply();
                startActivity(new Intent(SplashActivity.this, LandingActivity.class));
            }

            finish();
        }, 2000); //wait 2 sec
    }
}
