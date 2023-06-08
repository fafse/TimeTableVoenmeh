package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;


import java.io.IOException;
import java.util.List;

public class TimeTableHandler {
    private Group myGroup = null;
    private String groupName;

    public TimeTableHandler(String groupName) {
        this.groupName = groupName;
    }

    public void Update() throws IOException {
        HTTPDownloader downloader = new HTTPDownloader();
        downloader.Download();
        myGroup = downloader.getCurrentGroup(groupName);
    }


    public List<Lesson> getTimeTable(String name, Boolean isEven) {
        return myGroup.getTimeTable(name, isEven);
    }
}
