package com.example.timetablevoenmeh.TimeTableHandler.TimeTable.customListVIew;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Lesson;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private ArrayList<Lesson> lessons;
    private String TAG="CUSTOMADAPTER";


    public CustomAdapter(Context context, ArrayList<Lesson> lessons) {
        this.lessons=lessons;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lessons.size();
    }

    @Override
    public Lesson getItem(int position) {
        // TODO Auto-generated method stub
        return lessons.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "CustomAdapter: HERE");
        Lesson lesson= getItem(position);
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row,null);
        }
        TextView lessonNumberTextView = convertView.findViewById(R.id.lessonNumberTextView);
        lessonNumberTextView.setText(lesson.getLessonNum());
        Log.i(TAG, "lessonNumberTextView: "+lessonNumberTextView.getText());
        TextView timeLessonTextView = convertView.findViewById(R.id.timeLessonTextView);
        timeLessonTextView.setText(lesson.getCurrentTime()+"-"+lesson.getTime(1,30));
        Log.i(TAG, "timeLessonTextView: "+timeLessonTextView.getText());
        TextView lessonNameTextView = convertView.findViewById(R.id.lessonNameTextView);
        lessonNameTextView.setText(lesson.getLessonName());
        Log.i(TAG, "lessonNameTextView: "+lessonNameTextView.getText());
        TextView typeLessonTextView = convertView.findViewById(R.id.typeLessonTextView);
        typeLessonTextView.setText(lesson.getlessonType());
        Log.i(TAG, "typeLessonTextView: "+typeLessonTextView.getText());
        TextView teacherNameTextView = convertView.findViewById(R.id.teacherNameTextView);
        teacherNameTextView.setText(lesson.getTeacherName());
        TextView cabinetNameTextView = convertView.findViewById(R.id.cabinetNameTextView);
        cabinetNameTextView.setText(lesson.getOffice());
        return convertView;
    }
}