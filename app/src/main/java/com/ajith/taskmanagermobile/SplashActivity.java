package com.ajith.taskmanagermobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Hide actionbar for the splash screen
        getSupportActionBar().hide();

        final Intent i = new Intent(SplashActivity.this, MainActivity.class);

        //Close the splash screen and open the main activity after 1 second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
                //Fade in fade out for smooth transition between the activities
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        },1000);
    }
}