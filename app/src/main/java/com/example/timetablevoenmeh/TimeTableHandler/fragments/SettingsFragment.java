package com.example.timetablevoenmeh.TimeTableHandler.fragments;



import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.formatters.DateFormatter;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Parsing.TimeTableHandler;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.formatters.FileWorker;

import java.io.IOException;

public class SettingsFragment extends Fragment {


    private TimeTableHandler timeTableHandler;
    private DateFormatter dateFormatter;
    private EditText groupNameTextView;
    private String TAG = "SETTINGSFRAGMENT";
    private TextView ErrorChangeGroupTextView;

    private Button groupNameAcceptButton;
    private Switch isAlternativeSwitch;
    private FileWorker settingsFileWorker;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupNameAcceptButton = getView().findViewById(R.id.groupNameInputButton);
        groupNameTextView = (EditText) getView().findViewById(R.id.groupNameInput);
        ErrorChangeGroupTextView = getView().findViewById(R.id.ErrorChangeGroupTextView);
        isAlternativeSwitch=getView().findViewById(R.id.isAlternativeSwitch);
        settingsFileWorker = new FileWorker("settings.txt");
        if(settingsFileWorker.isExists()&&settingsFileWorker.openText()!=null) {
            String[] params = settingsFileWorker.openText().split("\n");
            if(params[0]!=null&&params[0].contains("MODE:TRUE"))
            {
                isAlternativeSwitch.setChecked(true);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        isAlternativeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsFileWorker.saveText("MODE:"+isChecked+"\n");
            }
        });
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
            String textToNotification=null;
            if (timeTableHandler.setGroupName(groupNameTextView.getText().toString().replaceAll("\n", ""))) {
                textToNotification = "Группа изменена на " + timeTableHandler.getGroupName();
                ErrorChangeGroupTextView.setTextColor(Color.GREEN);
            } else {
                textToNotification = "Проверьте правильность ввода имени группы или соедниение с интернетом";
                ErrorChangeGroupTextView.setTextColor(Color.RED);
            }
            String finalTextToNotification = textToNotification;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ErrorChangeGroupTextView.setText(finalTextToNotification);
                    ErrorChangeGroupTextView.setVisibility(View.VISIBLE);
                    ErrorChangeGroupTextView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ErrorChangeGroupTextView.setVisibility(View.GONE);
                            groupNameTextView.setText("");
                        }
                    }, 3000);
                }
            });
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