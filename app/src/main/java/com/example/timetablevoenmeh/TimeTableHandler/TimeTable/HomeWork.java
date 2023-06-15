package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;

import java.io.Serializable;

public class HomeWork implements Serializable {
    private String type;
    private String lessonName;
    private String date;
    private String description;
    private String task;

    public HomeWork(String type, String lessonName, String date, String description,String task) {
        this.type = type;
        this.lessonName = lessonName;
        this.date = date;
        this.description = description;
        this.task= task;
    }

    public String getType() {
        return type;
    }

    public String getLessonName() {
        return lessonName;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
