package com.example.timetablevoenmeh.TimeTableHandler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.timetablevoenmeh.HomeFragment;
import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.SettingsFragment;
import com.example.timetablevoenmeh.databinding.ActivityMainBinding;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout layout;
    ActivityMainBinding binding;

    private String groupName = "О719Б";
    private Calendar calendar;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String tmp = String.valueOf(calendar.get(Calendar.DATE));
        Bundle bundle = new Bundle();
        bundle.putString("groupName", groupName);
        bundle.putString("calendar", tmp);
        replaceFragment(new HomeFragment(), bundle);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment(), bundle);
            } else if (item.getItemId() == R.id.settings) {
                replaceFragment(new SettingsFragment(), bundle);
            }

            return true;
        });

// используем адаптер данных
        if (Build.VERSION.SDK_INT >= 30) {
            while (!Environment.isExternalStorageManager()) {
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }

    }


    private void replaceFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();

    }


}