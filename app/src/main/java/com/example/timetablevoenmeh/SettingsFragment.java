package com.example.timetablevoenmeh;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.DateFormatter;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.TimeTableHandler;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.PrintStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView lessonNotFoundTextView;
    private TimeTableHandler timeTableHandler;
    private DateFormatter dateFormatter;
    private TextView groupNameTextView;
    private String TAG = "SETTINGSFRAGMENT";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupNameAcceptButton = getView().findViewById(R.id.groupNameInputButton);
        groupNameTextView=getView().findViewById(R.id.groupNameInput);
    }

    private Button groupNameAcceptButton;
    public SettingsFragment() {
        // Required empty public constructor
    }

    public interface PassMeLinkObject{

    }

    @Override
    public void onStart() {
        super.onStart();
        groupNameAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Log.i(TAG, "run: "+timeTableHandler.getGroupName());
                            if (timeTableHandler.setGroupName(groupNameTextView.getText().toString())) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        groupNameTextView.setText("");
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
                }).start();


            }
        });
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public TimeTableHandler getTimeTableHandler() {
        return timeTableHandler;
    }

}