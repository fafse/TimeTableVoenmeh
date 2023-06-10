package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group implements Serializable {
    private String name;
    private String id;
    private List<Day> days;

    public Group(String text)
    {
        if(text.length()<100)
        {
            return;
        }
        name=text.substring(0,text.indexOf("\n"));
        name=name.replaceAll("<[^>]*>", "");
        name=name.replaceAll("(?m)^[ \t]*\r?\n","");


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

    public ArrayList<String> getTimeTable(String day, Boolean isEven)
    {
        ArrayList<String> result= new ArrayList<>();
        for (int i = 0;i< days.size();i++)
        {
            if(days.get(i).getName().toLowerCase().equals(day.toLowerCase()))
            {
                for(Lesson lesson: days.get(i).getLessons(isEven))
                {
                    result.add(lesson.toString());
                }
                break;
            }
        }
        return result;
    }

    public String getName()
    {
        return name;
    }

}
