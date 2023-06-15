package com.example.timetablevoenmeh.TimeTableHandler.TimeTable.Parsing;

import java.io.Serializable;
import java.util.List;

public class Lesson  implements Serializable {
    private String hour,minute;
    private boolean isEven;
    private String lessonName;
    private String teacherName;

    private String office;
    private String lessonType;
    private Lesson(String text)
    {
        List<String> lines= List.of(text.split("\n"));
        if(lines.get(0).contains("1")) isEven=false;
        else isEven=true;
        String tmptime=List.of(lines.get(1).split(" ")).get(0);
        hour=tmptime.substring(0,tmptime.indexOf(":"));
        minute=tmptime.substring(tmptime.indexOf(":")+1,tmptime.length());
        lessonName=lines.get(2);
        lessonType=lessonName.substring(0,lessonName.indexOf(" "));
        lessonName=lessonName.substring(lessonType.length()+1,lessonName.length());
        if (!text.contains("пр ЭК ПО ФК И СПОРТУ")) {

                if (lines.size() > 4) {
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


    public String getTime(int h,int minutes) {
        int tmpHour = Integer.parseInt(hour);
        int tmpMinute = Integer.parseInt(minute);

        tmpHour+=h;
        tmpMinute+=minutes;
        if(tmpMinute>=60)
        {
            tmpHour+=1;
            tmpMinute-=60;
        }
        return tmpHour+":"+tmpMinute;
    }
    public String getCurrentTime() {
        return hour+":"+minute;

    }

    public String getLessonName() {
        return lessonName;
    }

    public String getTeacherName() {
        return teacherName;
    }
    public String getLessonNum()
    {
        String num="";
        switch (hour)
        {
            case "9":
                num="1";
                break;
            case "10":
                num="2";
                break;
            case "12":
                num="3";
                break;
            case "14":
                num="4";
                break;
            case "16":
                num="5";
                break;
            case "18":
                num="6";
                break;
        }
        return num;
    }

    public String getOffice() {
        return office;
    }

    public String getlessonType() {
        return lessonType;
    }

    public boolean isEven()
    {
        return isEven;
    }
}
