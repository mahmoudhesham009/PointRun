package com.example.pointrun.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.pointrun.Helper;
import com.example.pointrun.R;
import com.example.pointrun.databinding.ActivityHomeBinding;
import com.example.pointrun.ui.fragment.AccountFragment;
import com.example.pointrun.ui.fragment.HistoryFragment;
import com.example.pointrun.ui.fragment.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_home);
        Helper.replaceFragmentS(getSupportFragmentManager(),binding.frame.getId(), new MapFragment());
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.missions:
                        Helper.replaceFragmentS(getSupportFragmentManager(),binding.frame.getId(), new MapFragment());
                        return true;


                    case R.id.Account:
                        Helper.replaceFragmentS(getSupportFragmentManager(),binding.frame.getId(), new AccountFragment());
                        return true;


                    case R.id.history:
                        Helper.replaceFragmentS(getSupportFragmentManager(),binding.frame.getId(), new HistoryFragment());
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}