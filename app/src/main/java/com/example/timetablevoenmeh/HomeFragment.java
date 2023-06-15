package com.example.timetablevoenmeh;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.DateFormatter;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Lesson;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.TimeTableHandler;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.customListVIew.CustomAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

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
    private ArrayList<FragmentContainerView> dateFragmentsContainers;
    private ArrayList<Fragment> dates;
    private ArrayList<Bundle> bundles;
    private int numOfDays=7;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        dates=new ArrayList<>();
        bundles=new ArrayList<>();
        if (bundle != null) {
                timeTableHandler = (TimeTableHandler) bundle.getSerializable("TIMETABLEHANDLER");
                dateFormatter = (DateFormatter) bundle.getSerializable("DATAFORMATTER");
            Log.i(TAG, "onCreate: DATAFORMATTER "+dateFormatter.getCurrentDate());
            DateFormatter tmpDateFormatter=(DateFormatter) dateFormatter.clone();
            String currentDay= tmpDateFormatter.getDayOfWeek();
            switch (currentDay) {
                case "Понедельник":
                    tmpDateFormatter.addDays(0);
                    break;
                case "Вторник":
                    tmpDateFormatter.addDays(-1);
                    break;
                case "Среда":
                    tmpDateFormatter.addDays(-2);
                    break;
                case "Четверг":
                    tmpDateFormatter.addDays(-3);
                    break;
                case "Пятница":
                    tmpDateFormatter.addDays(-4);
                    break;
                case "Суббота":
                    tmpDateFormatter.addDays(-5);
                    break;
                case "Воскресенье":
                    tmpDateFormatter.addDays(-6);
                    break;
            }
            for(int i = 0;i<numOfDays;i++)
            {
                boolean isActive = tmpDateFormatter.getNumOfDay()==dateFormatter.getNumOfDay();
                Bundle bundle1=new Bundle();
                DateFormatter myDataFormatter=tmpDateFormatter;
                bundle1.putSerializable("myDataFormatter",myDataFormatter);
                bundle1.putString("dayTextView",tmpDateFormatter.getShortDayOfWeek());
                bundle1.putString("numDayTextView", String.valueOf(tmpDateFormatter.getNumOfDay()));
                bundle1.putBoolean("isActive",isActive);
                bundles.add(bundle1);
                dates.add(new DateContainerFragment());
                tmpDateFormatter.addDays(1);
            }
            replaceDateFragments(dates,bundles);
            Log.i(TAG, "onCreate: " + dateFormatter.getCurrentDate());
        } else {
            Log.i(TAG, "onCreate: bundle NULL");
        }
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

    private void changeDay(int numOfDays) {
        dateFormatter.addDays(numOfDays);
        do {
        } while (timeTableHandler == null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("TIMETABLEHANDLER", timeTableHandler);
        bundle.putSerializable("DATAFORMATTER", dateFormatter);
        Log.i(TAG, "changeDay: I SAVE DATAFORMATTER"+dateFormatter.getCurrentDate());
        HomeFragment nextFragment = new HomeFragment();
        nextFragment.setArguments(bundle);
        if (numOfDays > 0)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.home_fragment_right_to_left,
                            R.anim.home_fragment_exit_right_to_left,
                            R.anim.home_fragment_left_to_right,
                            R.anim.home_fragment_exit_left_to_right
                    )
                    .replace(R.id.fragmentContainerView, nextFragment).commit();
        if(numOfDays<0)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.home_fragment_left_to_right,
                            R.anim.home_fragment_exit_left_to_right,
                            R.anim.home_fragment_right_to_left,
                            R.anim.home_fragment_exit_right_to_left
                    )
                    .replace(R.id.fragmentContainerView, nextFragment).commit();
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

    private void replaceDateFragments(ArrayList<Fragment> fragments, ArrayList<Bundle> bundle)
    {
        for(int i = 0;i<numOfDays;i++)
        {
            fragments.get(i).setArguments(bundles.get(i));
        }
        getActivity().getSupportFragmentManager()
                .beginTransaction()
        .replace(R.id.fragmentContainerView2, fragments.get(0))
        .commit();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView3, fragments.get(1))
                .commit();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView4, fragments.get(2))
                .commit();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView5, fragments.get(3))
                .commit();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView6, fragments.get(4))
                .commit();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView7, fragments.get(5))
                .commit();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView8, fragments.get(6))
                .commit();
    }

    public TimeTableHandler getTimeTableHandler() {
        return timeTableHandler;
    }

    public DateFormatter getDateFormatter() {
        return dateFormatter;
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
                                        if (xDiff > 0) {
                                            //right swipe
                                            changeDay(-1);
                                        } else {
                                            //left swipe
                                            changeDay(1);

                                        }
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