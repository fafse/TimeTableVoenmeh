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
    public String getCurrentDate()
    {
        String currentDate = "";
        String month="";
        switch (date.get(Calendar.MONTH))
        {
            case 0:
                month="Январь";
                break;
            case 1:
                month="Февраль";
                break;
            case 2:
                month="Март";
                break;
            case 3:
                month="Апрель";
                break;
            case 4:
                month="Май";
                break;
            case 5:
                month="Июнь";
                break;
            case 6:
                month="Июль";
                break;
            case 7:
                month="Август";
                break;
            case 8:
                month="Сентябрь";
                break;
            case 9:
                month="Октябрь";
                break;
            case 10:
                month="Ноябрь";
                break;
            case 11:
                month="Декабрь";
                break;
        }
        currentDate = getDayOfWeek()+", "+date.get(Calendar.DAY_OF_MONTH)+", "+month;
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
