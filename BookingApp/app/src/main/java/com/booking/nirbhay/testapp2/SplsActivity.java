package com.booking.nirbhay.testapp2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplsActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 3000;
    Handler myHandler;
    Runnable myRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spl);
        myHandler = new Handler();
        myRunnable = new Runnable() {
            public void run() {
                //Some interesting task

                Intent mainIntent = new Intent(SplsActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();


            }
        };
        myHandler.postDelayed(myRunnable, SPLASH_DISPLAY_LENGHT);
    }
}
