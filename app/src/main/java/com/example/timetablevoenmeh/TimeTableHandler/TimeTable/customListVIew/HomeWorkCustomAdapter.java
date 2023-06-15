package com.example.timetablevoenmeh.TimeTableHandler.TimeTable.customListVIew;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.timetablevoenmeh.HomeWorkFragment;
import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.HomeWork;

import java.util.ArrayList;

public class HomeWorkCustomAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private ArrayList<HomeWork> lessons;
    private String TAG="CUSTOMADAPTER";
    private int counterHomeWorks=1;


    public HomeWorkCustomAdapter(Context context, ArrayList<HomeWork> lessons) {
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
    public HomeWork getItem(int position) {
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
        HomeWork lesson= getItem(position);
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.home_work_row,null);
        }
        TextView lessonNumberTextView = convertView.findViewById(R.id.lessonNumberTextView);
        lessonNumberTextView.setText(String.valueOf(lessons.indexOf(lesson)+1));
        Log.i(TAG, "getView: "+lessons.indexOf(lesson));
        TextView timeLessonTextView = convertView.findViewById(R.id.timeLessonTextView);
        timeLessonTextView.setText(lesson.getDate());
        TextView lessonNameTextView = convertView.findViewById(R.id.lessonNameTextView);
        lessonNameTextView.setText(lesson.getLessonName());
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(lesson.getDescription());
        TextView typeTextView = convertView.findViewById(R.id.typeTextView);
        typeTextView.setText(lesson.getType());


        return convertView;
    }
}