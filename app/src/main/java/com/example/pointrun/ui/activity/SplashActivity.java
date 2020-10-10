package com.example.pointrun.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.pointrun.R;
import com.example.pointrun.databinding.ActivitySpalshBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySpalshBinding binding;
    Animation topAnim,bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_spalsh);

        //set animation
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        topAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        binding.logoImageView.setAnimation(topAnim);
        binding.nameTextView.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, ViewPagerActivity.class));
                finish();
            }
        },3500);
    }

    @Override
    public void onBackPressed() {
    }
}