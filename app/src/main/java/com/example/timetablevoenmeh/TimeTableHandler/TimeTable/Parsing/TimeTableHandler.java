package com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Parsing;


import android.util.Log;

import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.formatters.FileWorker;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class TimeTableHandler implements Serializable {
    private Group myGroup = null;
    private String groupName;
    private HTTPDownloader downloader;
    private FileWorker groupSaver;
    private FileWorker myGroupSaver;
    private FileWorker settingsFileWorker;
    private boolean isAlternative;
    private String groupListNames;
    private String TAG = "TimeTableHandler";

    public TimeTableHandler(String groupName) {
        groupSaver = new FileWorker("Groups.txt");
        myGroupSaver = new FileWorker("CurrentGroup.ser");
        settingsFileWorker = new FileWorker("settings.txt");
        myGroup = null;
        groupListNames = null;
        if (myGroupSaver.isExists()) {
            myGroup = myGroupSaver.readGroup();
            Log.i("TIMETABLEHANDLER", "READING MYGROUP...");
        }
        if (groupSaver.isExists()) {
            groupListNames = groupSaver.openText();
        }
        if (myGroup == null) {
            Log.i(TAG, "TimeTableHandler: mygroup=null");
            this.groupName = groupName;
            try {
                Log.i(TAG, "TimeTableHandler: I UPDATE");
                update();
                Log.i(TAG, "TimeTableHandler: I UPDATED=========================");
            } catch (IOException e) {
                Log.d("TIMETABLEHANDLER", "Update: " + new RuntimeException(e));
            }
        } else {
            groupName = myGroup.getName();
        }
        this.groupName = groupName;
    }

    private void update() throws IOException {
        Log.d("TimeTableHandler", "Update: " + groupName);
        downloader = new HTTPDownloader();
        Log.i(TAG, "update:I START DOWNLOAD");
        downloader.Download(groupName);
        Log.i(TAG, "update: DOWNLOADED");
        if (downloader.getGroups() != null)
            groupSaver.saveText(downloader.getGroups());
        groupListNames = groupSaver.openText();
        if (downloader.getCurrentGroup() != null) {
            myGroup = downloader.getCurrentGroup();
            groupName = myGroup.getName();
        }
        Log.i("TIMETABLEHANDLER", "update: " + groupName);
        if (myGroup != null)
            myGroupSaver.saveGroup(myGroup);

    }


    public ArrayList<Lesson> getTimeTable(String dayOfWeek, boolean isEven) {
        if (myGroup != null) {
            if (settingsFileWorker.isExists() && settingsFileWorker.openText() != null) {
                String[] params = settingsFileWorker.openText().split("\n");
                Log.i(TAG, "TimeTableHandler: getTimeTable() getting mode");
                if (params[0] != null && params[0].contains("MODE:TRUE")) {
                    isAlternative = true;
                }else
                {
                    isAlternative=false;
                }
            }
            Log.i(TAG, "getTimeTable: isAlternative? "+isAlternative);
            if (isAlternative) {
                return myGroup.getTimeTable(dayOfWeek, !isEven);
            } else {
                return myGroup.getTimeTable(dayOfWeek, isEven);
            }
        }
        return null;
    }


    public boolean setGroupName(String newGroupName) throws IOException {
        if (newGroupName.length() < 4) return false;
        newGroupName = newGroupName.toUpperCase();
        if (groupListNames == null) {
            update();
            Log.i(TAG, "setGroupName: updatin group list");
        }
        Log.i(TAG, String.valueOf("setGroupName: check Group List:" + groupListNames != null));
        Log.i(TAG, "setGroupName: " + groupListNames);
        Log.i(TAG, String.valueOf("setGroupName: " + groupListNames.contains(newGroupName)));
        Log.i(TAG, String.valueOf("setGroupName: " + newGroupName != ""));
        if (groupListNames != null && groupListNames.contains(newGroupName) && newGroupName != "") {
            String oldGroupName = groupName;
            groupName = newGroupName;
            try {
                update();
                groupName = downloader.getCurrentGroup().getName();
                return true;
            } catch (IOException e) {
                groupName = oldGroupName;
            }
        }
        return false;
    }

    public String getGroupName() {
        return groupName;
    }

}
