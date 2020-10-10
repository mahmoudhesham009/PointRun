package com.example.pointrun.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pointrun.R;
import com.example.pointrun.adapter.ViewPagerAdapter;
import com.example.pointrun.databinding.ActivityViewPagerBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ViewPagerActivity extends AppCompatActivity {
    ActivityViewPagerBinding binding;
    ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
    ImageView dots[];

    int oldPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_view_pager);
        binding.veiwPager.setAdapter(viewPagerAdapter);

        final ImageView dots[]=new ImageView[]{binding.dot1,binding.dot2,binding.dot3};

        binding.veiwPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<dots.length;i++){
                    dots[i].setImageResource(R.drawable.passive_point);
                }
                dots[position].setImageResource(R.drawable.active_point);

                if (position>oldPosition){
                    binding.imageView5.animate().translationX(binding.imageView5.getTranslationX()+20).setDuration(600);
                    binding.imageView6.animate().translationX(binding.imageView6.getTranslationX()+40).setDuration(600);
                    binding.imageView7.animate().translationX(binding.imageView7.getTranslationX()+80).setDuration(600);
                    binding.imageView8.animate().translationX(binding.imageView8.getTranslationX()+70).setDuration(600);
                    oldPosition=position;
                }else {
                    binding.imageView5.animate().translationX(binding.imageView5.getTranslationX()-20).setDuration(600);
                    binding.imageView6.animate().translationX(binding.imageView6.getTranslationX()-40).setDuration(600);
                    binding.imageView7.animate().translationX(binding.imageView7.getTranslationX()-80).setDuration(600);
                    binding.imageView8.animate().translationX(binding.imageView8.getTranslationX()-70).setDuration(600);
                    oldPosition=position;
                }


                if (position==2){
                    binding.button.setVisibility(View.VISIBLE);
                    binding.button.setClickable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplication())
                        .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Dexter.withContext(getApplication())
                                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Toast.makeText(ViewPagerActivity.this,"granted",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ViewPagerActivity.this,HomeActivity.class));
                                finish();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Toast.makeText(ViewPagerActivity.this,"location permission is not granted you can fix it from app sittings",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(ViewPagerActivity.this,"location permission is not granted you can fix it from app sittings",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }
        });
    }
}