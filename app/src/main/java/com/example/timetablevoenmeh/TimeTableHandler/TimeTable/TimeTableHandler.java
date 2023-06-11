package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;


import android.icu.util.LocaleData;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeTableHandler implements Serializable {
    private Group myGroup = null;
    private String groupName;
    private HTTPDownloader downloader;
    private FileWorker groupSaver;
    private FileWorker myGroupSaver;
    private String groupListNames;

    public TimeTableHandler(String groupName) {
        groupSaver = new FileWorker("Groups.txt");
        myGroupSaver = new FileWorker("CurrentGroup.ser");
        myGroup=null;
        groupListNames=null;
        if (myGroupSaver.isExists()) {
            myGroup = myGroupSaver.readGroup();
            Log.i("TIMETABLEHANDLER","READING MYGROUP...");
        }
        if (groupSaver.isExists())
            groupListNames = groupSaver.openText();
        if (myGroup == null) {
            this.groupName = "О719Б";
            try {
                update();
            } catch (IOException e) {
                Log.d("TIMETABLEHANDLER", "Update: " + new RuntimeException(e));
            }
        } else {
            groupName = myGroup.getName();
            Log.d(groupName, "savedGroup: " + myGroup.getTimeTable("Понедельник", true));
        }
        this.groupName = groupName;
    }

    private void update() throws IOException {
        Log.d("TimeTableHandler", "Update: " + groupName);
        downloader = new HTTPDownloader();
        downloader.setGroupName(groupName);
        downloader.Download();
        groupSaver.saveText(downloader.getGroups());
        if(groupListNames==null)
        {
            groupListNames=groupSaver.openText();
        }
        //Log.d("DOWNLOADER", "groups: "+groupSaver.openText());
        myGroup = downloader.getCurrentGroup();
        groupName = myGroup.getName();
        myGroupSaver.saveGroup(myGroup);
    }


    public ArrayList<String> getTimeTable(String dayOfWeek, boolean isEven) {
        Log.d("TIMETABLEHANDLER", "getTimeTable: " + dayOfWeek + " " + isEven);
        return myGroup.getTimeTable(dayOfWeek, isEven);
    }


    public boolean setGroupName(String newGroupName) {
        if (newGroupName.length() < 4) return false;
        newGroupName = newGroupName.toUpperCase();
        Log.d("TIMETABLEHANDLER", "setGroupName: " + newGroupName);
        if(groupListNames==null) {
            try {
                update();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (groupListNames.contains(newGroupName) && newGroupName != "") {
            groupName = newGroupName;
            try {
                update();
                return true;
            } catch (IOException e) {
                Log.d("TIMETABLEHANDLER", "Update: " + new RuntimeException(e));
            }
        }
        return false;
    }

    public String getGroupName() {
        return groupName;
    }

}
