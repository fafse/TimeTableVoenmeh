package com.example.timetablevoenmeh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetablevoenmeh.TimeTableHandler.MainActivity;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.DateFormatter;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.TimeTableHandler;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private String TAG="HOMEFRAGMENTER";
    private TextView toolBarTextView, groupNameTextView, dataTextView,lessonNotFoundTextView;
    private Button buttonPrev, buttonNext;
    private TimeTableHandler timeTableHandler;
    private ArrayList<String> lessonsList;
    private DateFormatter dateFormatter;
    private ListView timeTableList;
    private ArrayAdapter<String> adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View view;
    private Context context;
    private Activity activity;
    private String groupName;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle= this.getArguments();
        if(bundle!=null)
        {
            timeTableHandler=(TimeTableHandler) bundle.getSerializable("TIMETABLEHANDLER");
            dateFormatter=(DateFormatter) bundle.getSerializable("DATAFORMATTER");
            Log.i(TAG, "onCreate: "+dateFormatter.getCurrentDate());
        }
        else
        {
            Log.i(TAG, "onCreate: bundle NULL");
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity=getActivity();
        if(view != null) {
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
            timeTableList = view.findViewById(R.id.ListTimeTable);
            toolBarTextView = view.findViewById(R.id.toolBarTextView);
            dataTextView = view.findViewById(R.id.dateTextView);
            lessonNotFoundTextView=view.findViewById(R.id.lessonNotFoundTextView);
            buttonNext = view.findViewById(R.id.buttonNext);
            buttonPrev = view.findViewById(R.id.buttonPrev);
            groupNameTextView = view.findViewById(R.id.groupNameInput);
            Log.i(TAG, "onActivityCreated: VIEW!=NULL");
            if(timeTableList==null)
                Log.i(TAG, "onActivityCreated: TIMETABLELIST=NULL");
        }else
        {
            Log.i(TAG, "onActivityCreated: VIEW=NULL");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_home,  container, false);
        this.context=getContext();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadTable();

        setAdapter(timeTableList, adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                timeTableHandler = new TimeTableHandler(timeTableHandler.getGroupName());
                loadTable();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        nextDay();
                    }
                }).start();

            }
        });
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        prevDay();
                    }
                }).start();

            }
    });
    }

    private void nextDay() {
        dateFormatter.addDays(1);
        loadTable();
        setAdapter(timeTableList, adapter);
    }

    private void prevDay() {
        dateFormatter.addDays(-1);
        loadTable();
        setAdapter(timeTableList, adapter);
    }


    private void loadTable() {
        Log.i(TAG, "loadTable: "+dateFormatter.getCurrentDate());
        lessonsList = timeTableHandler.getTimeTable(
                dateFormatter.getDayOfWeek(),
                dateFormatter.isThisWeekEven());
        if (lessonsList != null)
            if (lessonsList.size() == 0) {
                adapter = null;
                lessonNotFoundTextView.setText("На выбранную дату не найдено предметов");
            } else {
                if(context==null)
                {
                    Log.i(TAG, "loadTable: GETCONTEXT======NULL");
                }
                adapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1, lessonsList);
                lessonNotFoundTextView.setText("");
            }
        dataTextView.setText(dateFormatter.getCurrentDate());
        toolBarTextView.setText(timeTableHandler.getGroupName());
        Log.d("TAG", "run: END");


    }

    private void setAdapter(final ListView list, final ArrayAdapter<String> adapter) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.setAdapter(adapter);
            }
        });
    }

    public TimeTableHandler getTimeTableHandler() {
        return timeTableHandler;
    }

    public DateFormatter getDateFormatter() {
        return dateFormatter;
    }

    private class GestureHandler implements View.OnTouchListener {

        GestureDetector gestureDetector;

        GestureHandler(View view) {
            int threshold = 100;
            int velocity_threshold = 100;

            GestureDetector.SimpleOnGestureListener listener =
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDown(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            float xDiff = e2.getX() - e1.getX();
                            float yDiff = e2.getY() - e1.getY();
                            try {
                                if (Math.abs(xDiff) > Math.abs(yDiff)) {
                                    if (Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold) {
                                        if (xDiff > 0) {
                                            //right swipe
                                            nextDay();
                                        } else {
                                            //left swipe
                                            prevDay();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };
            gestureDetector=new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

    }
}