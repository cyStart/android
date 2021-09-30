package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    //Variables
    ConstraintLayout layout;
    Animation topAnim, bottomAnim, layoutAnim;
    ImageView image;
    TextView slogan, url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        layoutAnim = AnimationUtils.loadAnimation(this, R.anim.layout_animation);

        //initializing variables from the design
        layout = findViewById(R.id.layout);
        image = findViewById(R.id.imageView);
        slogan = findViewById(R.id.textView);
        url = findViewById(R.id.textView2);

        //set variable to its animation

        layout.setAnimation(layoutAnim);
        image.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);
        url.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { 

                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 4000);


    }
}