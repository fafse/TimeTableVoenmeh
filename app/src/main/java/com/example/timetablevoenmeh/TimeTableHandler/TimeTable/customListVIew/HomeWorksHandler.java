package com.example.timetablevoenmeh.TimeTableHandler.TimeTable.customListVIew;

import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.FileWorker;
import com.example.timetablevoenmeh.TimeTableHandler.TimeTable.HomeWork;

import java.util.ArrayList;

public class HomeWorksHandler {
    FileWorker fileWorker;
    ArrayList<HomeWork> homeWorks;

    public HomeWorksHandler(ArrayList<HomeWork> homeWorks) {
        fileWorker = new FileWorker("Homework.txt");
        this.homeWorks = fileWorker.readHomeWorks();
        if (this.homeWorks == null) {
            this.homeWorks = homeWorks;
            fileWorker.saveHomeWorks(this.homeWorks);
        }
    }

    public ArrayList<HomeWork> getHomeWorks() {
        return homeWorks;
    }

    public void addHomeWork(HomeWork homeWork) {
        if (homeWork != null) {
            homeWorks.add(homeWork);
        }
    }
}
