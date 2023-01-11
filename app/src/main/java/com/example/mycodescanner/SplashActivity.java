package com.example.mycodescanner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.window.SplashScreen;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT=2500;
    ImageView splashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        splashImage = findViewById(R.id.spashImageView);

//        // Animation for ImageView in splash Screen
//        RotateAnimation anim = new RotateAnimation(10f, 0f, 0f, 0f);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setRepeatCount(Animation.INFINITE);
//        anim.setDuration(700);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_animation);
        splashImage.startAnimation(animation);

        splashImage.startAnimation(animation); // start animation

//        splashImage.setAnimation(null);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,
                        MainActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}