package com.example.timetablevoenmeh;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.FileWorker;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.HomeWork;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.customListVIew.HomeWorksHandler;

import java.util.ArrayList;

public class AddHomeworkFragment extends Fragment {

    private Context context;
    private Activity activity;
    private View view;
    private HomeWorksHandler homeWorksHandler;
    private Button resetButton, submitButton;
    private FileWorker fileWorker;
    private EditText homeWorkInput,lessonNameInput,typeNameInput,descriptionInput,dateInput;
    public AddHomeworkFragment() {
        // Required empty public constructor
    }
    public static AddHomeworkFragment newInstance(String param1, String param2) {
        AddHomeworkFragment fragment = new AddHomeworkFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<HomeWork> tmp=fileWorker.readHomeWorks();
                if(tmp==null)
                tmp=new ArrayList<>();
                String type = typeNameInput.getText().toString();
                String lessonName = lessonNameInput.getText().toString();
                String date = dateInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String homeWork = homeWorkInput.getText().toString();

                if(type!=null&&
                        lessonName!=null&&
                        date!=null&&
                        description!=null&&
                        homeWork!=null&&
                        !type.equals("") &&
                        !lessonName.equals("") &&
                        !date.equals("") &&
                        !description.equals("") &&
                        !homeWork.equals("")
                ) {
                    Log.i("ADDHOMEWORKFRAGMENTS", "onClick: "+tmp.size());
                    tmp.add(new HomeWork(
                            type,
                            lessonName,
                            date,
                            description,
                            homeWork
                    ));
                    fileWorker.saveHomeWorks(tmp);
                    Toast.makeText(context,"Домашнее задание добавлено",Toast.LENGTH_SHORT).show();
                    HomeWorkFragment nextFragment = new HomeWorkFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.home_fragment_right_to_left,
                                    R.anim.home_fragment_exit_right_to_left,
                                    R.anim.home_fragment_left_to_right,
                                    R.anim.home_fragment_exit_left_to_right
                            )
                            .replace(R.id.fragmentContainerView, nextFragment).commit();
                    homeWorkInput.setText("");
                    lessonNameInput.setText("");
                    typeNameInput.setText("");
                    descriptionInput.setText("");
                    dateInput.setText("");
                }else
                {
                    Toast.makeText(context,"Все поля должны быть заполнены",Toast.LENGTH_SHORT).show();
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeWorkInput.setText("");
                lessonNameInput.setText("");
                typeNameInput.setText("");
                descriptionInput.setText("");
                dateInput.setText("");
            }
        });
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=getContext();
        view=inflater.inflate(R.layout.fragment_add_homework, container, false);
        homeWorkInput=view.findViewById(R.id.homeWorkInput);
        lessonNameInput=view.findViewById(R.id.lessonNameInput);
        typeNameInput=view.findViewById(R.id.typeNameInput);
        descriptionInput=view.findViewById(R.id.descriptionInput);
        resetButton=view.findViewById(R.id.resetButton);
        dateInput=view.findViewById(R.id.dateInput);
        fileWorker= new FileWorker("Homework.txt");

        submitButton=view.findViewById(R.id.submitButton);

        return view;
    }
}