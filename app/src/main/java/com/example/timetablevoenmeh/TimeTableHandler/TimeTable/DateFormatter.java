package com.example.timetablevoenmeh.TimeTableHandler.TimeTable;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    private Calendar date;
    public boolean isThisWeekEven() {
        Log.d("DATEFORMATTER", "isThisWeekEven: "+date.get(Calendar.WEEK_OF_MONTH)%2);
        if (date.get(Calendar.WEEK_OF_YEAR)%2 == 1)
            return true;
        else
            return false;
    }

    public void update() {
        date=Calendar.getInstance();

    }
    public Calendar getCurrentDate()
    {
        Calendar currentDate = date;
        date.setFirstDayOfWeek(Calendar.MONDAY);
        return currentDate;
    }

    public String getDayOfWeek() {
        String result="";
        switch (date.get(Calendar.DAY_OF_WEEK)) {
            case 2:
                result = "Понедельник";
                break;
            case 3:
                result = "Вторник";
                break;
            case 4:
                result = "Среда";
                break;
            case 5:
                result = "Четверг";
                break;
            case 6:
                result = "Пятница";
                break;
            case 7:
                result = "Суббота";
                break;
            case 1:
                result = "Воскресенье";
                break;
        }
        return result;
    }

    public void addDays(int numOfDays) {
        date.add(Calendar.DATE,numOfDays);
    }
}
