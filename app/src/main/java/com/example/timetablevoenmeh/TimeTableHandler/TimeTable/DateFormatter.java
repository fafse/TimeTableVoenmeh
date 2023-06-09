package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public boolean isThisWeekEven(Date day)
    {

        long delay = System.currentTimeMillis() - day.getTime(); //получим разницу (в мс) между сегодня и 1-ым сентября
        long week = 1000 * 60 * 60 * 24 * 7; //кол-во миллисекунд в одной неделе
        delay %= week * 2; //найдем остаток от деления разницы на две недели

        if(delay<= week)
        {
            return true;
        }else
        {
            return false;
        }
    }

    public Date getCurrentDate()
    {
        Date tmp = new Date();
        Log.d("DATETMP", "getCurrentDate: "+tmp);
        return tmp;
    }
    public String getDayOfWeek(Date dateFormat)
    {
        Log.d("dateFormat", "getDayOfWeek: "+dateFormat);
        String newFormat = new SimpleDateFormat("EEEE",new Locale("ru")).format(dateFormat);
        Log.d("RESULT", "getDayOfWeek: "+newFormat);
        return newFormat;
    }

}
