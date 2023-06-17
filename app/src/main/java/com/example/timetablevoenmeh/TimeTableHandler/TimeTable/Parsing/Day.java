package com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Parsing;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Day implements Serializable {
    private String name;
    private List<Lesson> lessons;
    public Day(String day){
        FileOutputStream fos = null;
        day.replaceAll("\\t","\\n");
        day=day.replaceAll("\"[^>]*\">", "");
        day=day.replaceAll("(?m)^[ \t]*\r?\n","");
        String text="";
        for (String str:
             List.of(day.split("\n"))) {
            text+=str.trim()+"\n";
        }
        lessons=new ArrayList<>();
        List<String> lines = List.of(text.split("\n"));
        this.name=lines.get(0);
        String lesson="";
        for (int i = 0;i<lines.size();i++) {
            if(lines.get(i).contains(this.name)) {
                Lesson parsedLesson;
                try {
                    parsedLesson = Lesson.getLesson(lesson);
                }catch (RuntimeException e)
                {
                    parsedLesson=null;
                }
                if(parsedLesson!=null)
                    lessons.add(parsedLesson);
                lesson="";
            }else
            {
                lesson+=lines.get(i)+"\n";
            }
        }
        Lesson parsedLesson = Lesson.getLesson(lesson);
        if(parsedLesson!=null)
            lessons.add(parsedLesson);
    }

    public List<Lesson> getLessons(boolean isEven)
    {
        List<Lesson> result=new ArrayList<>();
        for (Lesson lesson:
                lessons) {
            if(lesson.isEven()==isEven) {
                result.add(lesson);
            }
        }
        return result;
    }

    public String getName()
    {
        return name;
    }
}
