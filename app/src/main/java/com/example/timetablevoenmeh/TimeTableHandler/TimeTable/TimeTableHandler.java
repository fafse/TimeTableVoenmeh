package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;


import android.icu.util.LocaleData;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeTableHandler {
    private Group myGroup = null;
    private String groupName;
    private HTTPDownloader downloader;

    public TimeTableHandler(String groupName) {
        this.groupName = groupName;
    }

    public void Update() throws IOException {
        Log.d("TimeTableHandler", "Update: "+groupName);
        downloader = new HTTPDownloader();
        downloader.setGroupName(groupName);
        downloader.Download();
        myGroup = downloader.getCurrentGroup(groupName);
    }


    public List<Lesson> getTimeTable(String dayOfWeek,boolean isEven) {
        Log.d("TIMETABLEHANDLER", "getTimeTable: "+dayOfWeek+" "+isEven);
        return myGroup.getTimeTable(dayOfWeek,isEven);
    }

    public void setGroupName(String groupName)
    {
        Log.d("TIMETABLEHANDLER", "setGroupName: "+groupName);
        if(groupName.equals("")||groupName==null)
            return;
        else
            this.groupName=groupName;
    }



}
