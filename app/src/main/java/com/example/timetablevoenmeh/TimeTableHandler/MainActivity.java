package com.example.timetablevoenmeh.TimeTableHandler;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.TimeTableHandler;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            public void run() {
                TimeTableHandler timeTableHandler = new TimeTableHandler("О719Б");
                try {
                    timeTableHandler.Update();
                    TextView timetable = findViewById(R.id.TimeTable);
                    timetable.setText(timeTableHandler.getTimeTable("Вторник",true).toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}