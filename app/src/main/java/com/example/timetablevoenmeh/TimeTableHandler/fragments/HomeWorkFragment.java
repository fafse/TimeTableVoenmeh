package com.example.timetablevoenmeh.TimeTableHandler.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.HomeWork;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.customListVIew.HomeWorkCustomAdapter;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.customListVIew.HomeWorksHandler;

import java.util.ArrayList;

public class HomeWorkFragment extends Fragment {
    private HomeWorksHandler homeWorksHandler;
    private ArrayList<HomeWork> lessons;
    private View view;
    private ListView homeWorkList;
    private Button addHomework;
    private Context context;
    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String TAG = "HomeWorkFragment";

    public HomeWorkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lessons = new ArrayList<>();
        homeWorksHandler = new HomeWorksHandler(lessons);
        lessons = homeWorksHandler.getHomeWorks();
    }

    @Override
    public void onStart() {
        fillList();
        super.onStart();
    }

    private void fillList() {
        if (lessons != null && lessons.size() > 0) {
            HomeWorkCustomAdapter adapter = new HomeWorkCustomAdapter(context,homeWorksHandler);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    homeWorkList.setAdapter(adapter);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_work, container, false);
        context = getContext();
        activity = getActivity();
        homeWorkList = view.findViewById(R.id.homeWorkList);
        swipeRefreshLayout = view.findViewById(R.id.HomeWorkList);
        addHomework = view.findViewById(R.id.addHomework);
        addHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHomeworkFragment nextFragment = new AddHomeworkFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.home_fragment_right_to_left,
                                R.anim.home_fragment_exit_right_to_left,
                                R.anim.home_fragment_left_to_right,
                                R.anim.home_fragment_exit_left_to_right
                        )
                        .replace(R.id.fragmentContainerView, nextFragment).commit();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }
}