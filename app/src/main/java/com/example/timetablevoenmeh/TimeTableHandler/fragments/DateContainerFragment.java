package com.example.timetablevoenmeh.TimeTableHandler.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.TimeTableHandler.DayDataListener;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.formatters.DateFormatter;


public class DateContainerFragment extends Fragment{
    private TextView dayTextView, numDayTextView;
    private ImageView imageViewCircle;
    private Button getThisDayButton;
    private boolean isActive;
    private DateFormatter myDateFormatter;
    private String TAG="DateContainerFragment";
    private String dayText,numDay;
    private View view;
    private DayDataListener mListener;

    private int getNumDay() {
        return Integer.parseInt(numDay);
    }

    public DateContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dayText = bundle.getString("dayTextView");
            numDay= bundle.getString("numDayTextView");
            this.isActive = bundle.getBoolean("isActive");
            this.myDateFormatter = (DateFormatter) bundle.getSerializable("myDataFormatter");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isActive) {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run() {
                    imageViewCircle.setImageDrawable(getResources().getDrawable(R.drawable.button__1_));

                }
            });
        }
        getThisDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: i am day number "+ getNumDay());
                mListener.onToggleDay(myDateFormatter);
            }
        });
        dayTextView.setText(dayText);
        numDayTextView.setText(numDay);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_container, container, false);
        if (view != null) {
            dayTextView = view.findViewById(R.id.dayTextView);
            numDayTextView = view.findViewById(R.id.numDayTextView);
            imageViewCircle = view.findViewById(R.id.imageViewCircle);
            getThisDayButton = view.findViewById(R.id.getThisDayButton);
        }
        this.view=view;
        return view;
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
}