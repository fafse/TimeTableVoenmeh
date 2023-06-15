package com.example.timetablevoenmeh;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.DateFormatter;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.TimeTableHandler;

import java.io.IOException;

public class SettingsFragment extends Fragment {


    private TimeTableHandler timeTableHandler;
    private DateFormatter dateFormatter;
    private EditText groupNameTextView;
    private String TAG = "SETTINGSFRAGMENT";
    private TextView ErrorChangeGroupTextView;

    private Button groupNameAcceptButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupNameAcceptButton = getView().findViewById(R.id.groupNameInputButton);
        groupNameTextView = (EditText) getView().findViewById(R.id.groupNameInput);
        ErrorChangeGroupTextView = getView().findViewById(R.id.ErrorChangeGroupTextView);
    }

    @Override
    public void onStart() {
        super.onStart();
        groupNameTextView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    new Thread(new Runnable() {
                        public void run() {
                            handleGroupChange();
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        groupNameAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        handleGroupChange();
                    }
                }).start();


            }
        });
    }

    private void handleGroupChange() {
        try {
            Log.i(TAG, "run: " + timeTableHandler.getGroupName());
            if (timeTableHandler.setGroupName(groupNameTextView.getText().toString().replaceAll("\n", ""))) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Группа изменена на " + timeTableHandler.getGroupName(), Toast.LENGTH_SHORT).show();
                        groupNameTextView.setText("");
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ErrorChangeGroupTextView.setVisibility(View.VISIBLE);
                        ErrorChangeGroupTextView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ErrorChangeGroupTextView.setVisibility(View.GONE);

                            }
                        }, 3000);
                    }
                });

            }
        } catch (IOException e) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getActivity(),
                            "Ресурс недоступен. Проверьте соединение с интернетом", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            timeTableHandler = (TimeTableHandler) bundle.getSerializable("TIMETABLEHANDLER");
            dateFormatter = (DateFormatter) bundle.getSerializable("DATAFORMATTER");
            Log.i(TAG, "onCreate: " + dateFormatter.getCurrentDate());
        } else {
            Log.i(TAG, "onCreate: bundle NULL");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public TimeTableHandler getTimeTableHandler() {
        return timeTableHandler;
    }

}