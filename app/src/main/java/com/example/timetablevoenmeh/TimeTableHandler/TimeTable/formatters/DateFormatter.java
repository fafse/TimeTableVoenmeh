package com.example.timetablevoenmeh.TimeTableHandler.TimeTable.formatters;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatter implements Serializable, Comparable<DateFormatter> {
    private Calendar date;
    public boolean isThisWeekEven() {
        int month = date.get(Calendar.MONTH) + 1;
        int year = month > 0 && month < 9 ? date.get(Calendar.YEAR) - 1 : date.get(Calendar.YEAR);
        Date startDate = null;
        try {
            startDate = new SimpleDateFormat("dd.MM.yyyy").parse("01.09." + year);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        int numberStartWeek = Integer.parseInt(new SimpleDateFormat("w").format(startDate));
        int numberNowWeek = date.get(Calendar.WEEK_OF_YEAR);

        int countWeeks = 0;
        if (month > 0 && month < 9) {
            Date lastDateYear = null;
            try {
                lastDateYear = new SimpleDateFormat("dd.MM.yyyy").parse("31.12." + year);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (!(new SimpleDateFormat("E").format(lastDateYear).equals("Пн"))) {
                numberNowWeek--;
            }
            countWeeks = Integer.parseInt(new SimpleDateFormat("w").format(lastDateYear)) -
                    numberStartWeek + numberNowWeek;
        } else {
            countWeeks = numberNowWeek - numberStartWeek;
        }
        return countWeeks % 2 != 0;
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
    public String getShortDayOfWeek() {
        String result="";
        switch (date.get(Calendar.DAY_OF_WEEK)) {
            case 2:
                result = "Пн";
                break;
            case 3:
                result = "Вт";
                break;
            case 4:
                result = "Ср";
                break;
            case 5:
                result = "Чт";
                break;
            case 6:
                result = "Пт";
                break;
            case 7:
                result = "Сб";
                break;
            case 1:
                result = "Вс";
                break;
        }
        return result;
    }

    public void addDays(int num)
    {
        date.add(Calendar.DAY_OF_MONTH,num);
    }
    public Object clone()
    {
        Calendar newCalendar= (Calendar) date.clone();
        DateFormatter dateFormatter= new DateFormatter();
        dateFormatter.setCalendar(newCalendar);
        return dateFormatter;
    }
    public int getNumOfDay()
    {
        return date.get(Calendar.DAY_OF_MONTH);
    }
    private void setCalendar(Calendar calendar)
    {
        date=calendar;
    }

    @Override
    public int compareTo(DateFormatter o) {
        if (date.after(o.date))
        {
            return 1;
        }
        else if(date.before(o.date))
        {
            return 0;
        }
        return -1;
    }

}
