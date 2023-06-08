package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private String id;
    private List<Day> days;

    public Group(String text)
    {
        Log.d("group", text);
        name=text.substring(0,text.indexOf("\n"));
        name=name.replaceAll("<[^>]*>", "");
        name=name.replaceAll("(?m)^[ \t]*\r?\n","");

        Log.d(name, text);

        id = name.substring(name.indexOf("=\"")+2,name.indexOf("\">"));
        name=name.substring(0,name.indexOf("\""));
        days=new ArrayList<>();

        for (String day:
             List.of(text.split("<Day Title="))) {
            day=day.replaceAll("<[^>]*>", "");
            day=day.replaceAll("(?m)^[ \t]*\r?\n","");
            days.add(new Day(day));
        }
        days.remove(0);
    }

    public List<Lesson> getTimeTable(String day, Boolean isEven)
    {
        List<Lesson> result= new ArrayList<>();
        for (int i = 0;i< days.size();i++)
        {
            if(days.get(i).getName().equals(day))
            {
                result.addAll(days.get(i).getLessons(isEven));
            }
        }
        return result;
    }

    public String getName()
    {
        return name;
    }

}
