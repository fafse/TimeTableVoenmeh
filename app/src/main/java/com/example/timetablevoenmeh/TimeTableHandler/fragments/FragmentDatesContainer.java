package com.example.timetablevoenmeh.TimeTableHandler.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timetablevoenmeh.R;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Parsing.TimeTableHandler;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.formatters.DateFormatter;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentDatesContainer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String TAG="DATESCONTAINER";
    private ArrayList<Fragment> dates;
    private ArrayList<Bundle> bundles;
    private DateFormatter myDateFormatter;
    private int numOfDays;

    public FragmentDatesContainer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment fragment_dates_container_home.
     */
    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            myDateFormatter = (DateFormatter) bundle.getSerializable("DATAFORMATTER");
            numOfDays= (int) bundle.getInt("NUMOFDAYS", numOfDays);
            DateFormatter tmpDateFormatter=(DateFormatter) myDateFormatter.clone();
            tmpDateFormatter.addDays(-3);
            bundles= new ArrayList<>();
            dates= new ArrayList<>();
            for(int i = 0;i<numOfDays;i++)
            {
                boolean isActive = tmpDateFormatter.compareTo(myDateFormatter)==-1;
                Bundle bundle1=new Bundle();
                DateFormatter myDataFormatter= (DateFormatter) tmpDateFormatter.clone();
                bundle1.putSerializable("myDataFormatter",myDataFormatter);
                bundle1.putString("dayTextView",tmpDateFormatter.getShortDayOfWeek());
                bundle1.putString("numDayTextView", String.valueOf(tmpDateFormatter.getNumOfDay()));
                bundle1.putBoolean("isActive",isActive);
                bundles.add(bundle1);
                DateContainerFragment tmp = new DateContainerFragment();

                dates.add(tmp);
                tmpDateFormatter.addDays(1);
            }
            replaceDateFragments(dates,bundles);
        } else {
            Log.i(TAG, "onCreate: bundle NULL");
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dates_container_home, container, false);
    }
}