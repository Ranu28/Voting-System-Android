package com.example.votingsign.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.votingsign.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMER=4000;

    //variables
    ImageView backgroundImage;
    TextView splashMotto;
    LottieAnimationView lottieAnimationView;

    //Animations
    Animation sideAnim,bottomAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //hooks
        backgroundImage=findViewById(R.id.background_image);
        splashMotto=findViewById(R.id.motto);
        lottieAnimationView=findViewById(R.id.lottie);



        //Animations
        sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(4000);

        //set animations on elements
        backgroundImage.setAnimation(sideAnim);
        splashMotto.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(),Onboarding.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_TIMER);
    }
}