package com.example.timetablevoenmeh.TimeTableHandler;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.DateFormatter;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Lesson;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.TimeTableHandler;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView toolBarTextView,groupNameTextView;
    Button groupNameAcceptButton,buttonPrev,buttonNext;
    TimeTableHandler timeTableHandler;
    ArrayList<String> lessonsList;
    DateFormatter dateFormatter;
    ListView timeTableList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dateFormatter = new DateFormatter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// используем адаптер данных
        if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }
        new Thread(new Runnable() {
            public void run() {
                dateFormatter.update();
                buttonNext=findViewById(R.id.buttonNext);
                buttonPrev=findViewById(R.id.buttonPrev);
                timeTableHandler = new TimeTableHandler("О719Б");
                timeTableList = findViewById(R.id.ListTimeTable);
                toolBarTextView = findViewById(R.id.toolBarTextView);
                loadTable();
                setAdapter(timeTableList,adapter);
            }
        }).start();

    }
    private void setAdapter(final ListView list,final ArrayAdapter<String> adapter){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.setAdapter(adapter);
            }
        });
    }
    private void loadTable()
    {
        lessonsList = timeTableHandler.getTimeTable(
                dateFormatter.getDayOfWeek(),
                dateFormatter.isThisWeekEven());
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lessonsList);
        toolBarTextView.setText(timeTableHandler.getGroupName());
        Log.d("TAG", "run: END");


    }

    @Override
    protected void onStart() {
        super.onStart();
        groupNameAcceptButton=findViewById(R.id.groupNameInputButton);
        groupNameTextView=findViewById(R.id.groupNameInput);
        groupNameAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        if(timeTableHandler.setGroupName(groupNameTextView.getText().toString())) {
                            groupNameTextView.setText("");
                            loadTable();
                            setAdapter(timeTableList, adapter);
                        }
                    }
                }).start();

            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        dateFormatter.addDays(1);
                            loadTable();
                            setAdapter(timeTableList, adapter);
                    }
                }).start();

            }
        });
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        dateFormatter.addDays(-1);
                            loadTable();
                            setAdapter(timeTableList, adapter);
                    }
                }).start();

            }
        });
    }
}