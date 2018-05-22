package com.technology.team.rahmaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.technology.team.rahmaapp.activities.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    Runnable run;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        run = new Runnable() {
            @Override

            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
                finish();
            }
        };
        handler.postDelayed(run, 2000);
    }
}
