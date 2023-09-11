package com.example.timetablevoenmeh.TimeTableHandler.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.TimeTableHandler.DayDataListener;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Parsing.Lesson;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Parsing.TimeTableHandler;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.customListVIew.CustomAdapter;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.formatters.DateFormatter;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment{

    // TODO: Rename and change types of parameters
    private String TAG = "HOMEFRAGMENTER";
    private TextView lessonNotFoundTextView;
    private TimeTableHandler timeTableHandler;
    private ArrayList<Lesson> lessonsList;
    private DateFormatter dateFormatter;
    private ListView timeTableList;
    private CustomAdapter adapter;
    private GestureHandler gestureHandler;

    private View view;
    private Context context;
    private Activity activity;
    private final int numOfDays=7;
    private DayDataListener mListener;
    private Fragment datesContainer;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
                timeTableHandler = (TimeTableHandler) bundle.getSerializable("TIMETABLEHANDLER");
                dateFormatter = (DateFormatter) bundle.getSerializable("DATAFORMATTER");
            fillDatesContainer();
            Log.i(TAG, "onCreate: DATAFORMATTER "+dateFormatter.getCurrentDate());
            Log.i(TAG, "onCreate: " + dateFormatter.getCurrentDate());
        } else {
            Log.i(TAG, "onCreate: bundle NULL");
        }
    }

    private void fillDatesContainer()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATAFORMATTER", dateFormatter);
        bundle.putInt("NUMOFDAYS", numOfDays);
        datesContainer = new FragmentDatesContainer();
        datesContainer.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.DateContainer, datesContainer);
        fragmentTransaction.commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();
        if (view != null) {
            timeTableList = view.findViewById(R.id.ListTimeTable);
            lessonNotFoundTextView = view.findViewById(R.id.lessonNotFoundTextView);
            gestureHandler = new GestureHandler();
            view.setOnTouchListener(gestureHandler);
            timeTableList.setOnTouchListener(gestureHandler);

            Log.i(TAG, "onActivityCreated: VIEW!=NULL");
            if (timeTableList == null)
                Log.i(TAG, "onActivityCreated: TIMETABLELIST=NULL");
        } else {
            Log.i(TAG, "onActivityCreated: VIEW=NULL");

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_home, container, false);
        this.context = getContext();
        this.activity=getActivity();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadTable();
        setAdapter(timeTableList, adapter);
    }


    private void loadTable() {
        lessonsList = timeTableHandler.getTimeTable(
                dateFormatter.getDayOfWeek(),
                dateFormatter.isThisWeekEven());
        if (lessonsList != null)
            if (lessonsList.size() == 0) {
                adapter = null;
                lessonNotFoundTextView.setText("На выбранную дату не найдено предметов");
            } else {
                if (context == null) {
                }
                adapter = new CustomAdapter(context,
                        lessonsList);
                lessonNotFoundTextView.setText("");
            }

    }

    private void setAdapter(final ListView list, final CustomAdapter adapter) {
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
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof DayDataListener)
        {
            mListener = (DayDataListener) context;
        }else
        {
            throw new RuntimeException(context.toString()+ " must implement DayDataListener");
        }
    }

    private class GestureHandler implements View.OnTouchListener {

        GestureDetector gestureDetector;

        GestureHandler() {
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
                                        DateFormatter tmpDateFormatter = (DateFormatter) dateFormatter.clone();
                                        if (xDiff > 0) {
                                            tmpDateFormatter.addDays(-1);
                                            //right swipe
                                        } else {
                                            //left swipe
                                            tmpDateFormatter.addDays(1);
                                        }
                                        mListener.onToggleDay(tmpDateFormatter);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };
            gestureDetector = new GestureDetector(listener);
        }


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

    }
}