package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;

import java.util.List;

public class Lesson {
    private String time;
    private boolean isEven;
    private String lessonName;
    private String teacherName;
    private int teacherId;
    private String office;

    private Lesson(String text)
    {
        List<String> lines= List.of(text.split("\n"));
        if(lines.get(0).contains("1")) isEven=false;
        else isEven=true;
        time=List.of(lines.get(1).split(" ")).get(0);
        lessonName=lines.get(2);
        if (!text.contains("пр ЭК ПО ФК И СПОРТУ")) {
            if (lines.size() > 3) {
                teacherId = Integer.parseInt(lines.get(3));
                if (lines.size() > 3) {
                    teacherName = lines.get(4);
                    if (lines.size() > 5) {
                        office = lines.get(5);
                        office = office.substring(0, office.length() - 1);
                    } else {
                        office = " ";
                    }
                }else
                {
                    teacherName = "-";
                }
            }
            else
            {
                teacherId = -1;
            }


        }
        else
        {
            teacherId = 0;
            teacherName = "-";
            office = "В зависимости от направления";
        }
    }

    public static Lesson getLesson(String text)
    {
        if(text.equals(""))
            return null;
        else
            return new Lesson(text);
    }

    @Override
    public String toString()
    {
        String result="";
        result+=lessonName+"\n";
        result+= time+"\n";
        result+= teacherName+"\n";
        result+=office+"\n";
        return result;
    }

    public boolean isEven()
    {
        return isEven;
    }
}
