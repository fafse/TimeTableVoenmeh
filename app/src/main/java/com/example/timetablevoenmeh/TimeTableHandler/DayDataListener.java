package com.example.timetablevoenmeh.TimeTableHandler;

import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.formatters.DateFormatter;

public interface DayDataListener {

    void onToggleDay(DateFormatter newDateFormatter);
}
