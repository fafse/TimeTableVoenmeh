package com.example.timetablevoenmeh.TimeTableHandler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.timetablevoenmeh.HomeFragment;
import com.example.timetablevoenmeh.HomeWorkFragment;
import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.SettingsFragment;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.DateFormatter;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.TimeTableHandler;
import com.example.timetablevoenmeh.databinding.ActivityMainBinding;

import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout layout;
    ActivityMainBinding binding;



    private TimeTableHandler timeTableHandler;
    private DateFormatter dateFormatter;

    private HomeFragment homeFragment=new HomeFragment();
    private SettingsFragment settingsFragment =new SettingsFragment();
    private HomeWorkFragment homeWorkFragment=new HomeWorkFragment();
    private String currentFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null)
        {
            timeTableHandler=(TimeTableHandler) savedInstanceState.getSerializable("TIMETABLEHANDLER");
            dateFormatter=(DateFormatter) savedInstanceState.getSerializable("DATAFORMATTER");
            currentFragment=savedInstanceState.getString("FRAGMENT");
        }
        if (Build.VERSION.SDK_INT >= 30) {
            if(!Environment.isExternalStorageManager()) {
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        new Thread(new Runnable() {
            public void run() {
                if(timeTableHandler==null)
                    timeTableHandler= new TimeTableHandler("О719Б");

            }
        }).start();
        if(dateFormatter==null) {
            dateFormatter = new DateFormatter();
            dateFormatter.update();
        }
        setContentView(binding.getRoot());
        Bundle bundle = new Bundle();
        if(currentFragment==null||currentFragment.equals("HomeFragment"))
            replaceFragment(homeFragment, bundle);
        else if (currentFragment.equals("SettingsFragment")) {
            replaceFragment(settingsFragment,bundle);
        } else if (currentFragment.equals("HomeWorkFragment")) {
            replaceFragment(homeWorkFragment,bundle);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(homeFragment, bundle);
            } else if (item.getItemId() == R.id.settings) {
                replaceFragment(settingsFragment, bundle);
            }else if (item.getItemId() == R.id.homeWork) {
                replaceFragment(homeWorkFragment, bundle);
            }

            return true;
        });



// используем адаптер данных


    }


    private void replaceFragment(Fragment fragment, Bundle bundle) {
        do {
        }while(timeTableHandler==null);
        String currentFragmentTmp=currentFragment;
        boolean flag=true;
        DateFormatter tmpData = homeFragment.getDateFormatter();
        TimeTableHandler tmpTimeTable=null;
        if(fragment instanceof HomeFragment) {
            tmpTimeTable = homeFragment.getTimeTableHandler();
            currentFragment="HomeFragment";
            if(currentFragmentTmp!=null&& currentFragmentTmp.equals(currentFragment)) {
                dateFormatter.update();
                Log.i("MAINACTIVITY", "replaceFragment: change DATE");
                flag=false;
            }
        }
        else if(fragment instanceof SettingsFragment){
            tmpTimeTable = settingsFragment.getTimeTableHandler();
            currentFragment="SettingsFragment";
        }
        else if(fragment instanceof HomeWorkFragment){
            currentFragment="HomeWorkFragment";
        }
        if(tmpData!=null)
        {
            if (!tmpData.equals(dateFormatter)&&flag)
            {
                dateFormatter=tmpData;
            }
        }
        if(tmpTimeTable!=null)
        {
            if(!tmpTimeTable.equals(timeTableHandler))
            {
                timeTableHandler=tmpTimeTable;
            }
        }
        bundle.putSerializable("TIMETABLEHANDLER",timeTableHandler);
        bundle.putSerializable("DATAFORMATTER", dateFormatter);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("TIMETABLEHANDLER",timeTableHandler);
        outState.putSerializable("DATAFORMATTER",dateFormatter);
        if(Objects.equals(currentFragment, "HomeFragment"))
        {
            outState.putString("FRAGMENT","HomeFragment");
        } else if (Objects.equals(currentFragment, "SettingsFragment")) {
            outState.putString("FRAGMENT","SettingsFragment");
        } else if (Objects.equals(currentFragment,"HomeWorkFragment")) {
            outState.putString("FRAGMENT","HomeWorkFragment");
        }
        super.onSaveInstanceState(outState);
    }

}