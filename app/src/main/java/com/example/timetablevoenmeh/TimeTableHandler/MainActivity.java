package com.example.timetablevoenmeh.TimeTableHandler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.DateFormatter;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Lesson;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.TimeTableHandler;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView timetable,groupNameTextView;
    Button groupNameAcceptButton;
    TimeTableHandler timeTableHandler;
    List<Lesson> lessonsList;
    DateFormatter dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dateFormatter = new DateFormatter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            public void run() {
                timetable = findViewById(R.id.timetable);
                timeTableHandler = new TimeTableHandler("О718Б");
                try {
                    timeTableHandler.Update();
                    lessonsList = timeTableHandler.getTimeTable(
                            dateFormatter.getDayOfWeek(dateFormatter.getCurrentDate()),
                            dateFormatter.isThisWeekEven(dateFormatter.getCurrentDate()));
                    timetable.setText(lessonsList.toString());
                    Log.d("TAG", "run: END");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        groupNameAcceptButton=findViewById(R.id.groupNameInputButton);
        groupNameTextView=findViewById(R.id.groupNameInput);
        groupNameAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeTableHandler.setGroupName(groupNameTextView.getText().toString());
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            timeTableHandler.Update();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        lessonsList = timeTableHandler.getTimeTable(
                                dateFormatter.getDayOfWeek(dateFormatter.getCurrentDate()),
                                dateFormatter.isThisWeekEven(dateFormatter.getCurrentDate()));
                        Log.d("MAINACTIVITY", "onClick: "+lessonsList.toString());
                        timetable.setText("");
                        timetable.setText(lessonsList.toString());
                    }
                }).start();

            }
        });
    }
}