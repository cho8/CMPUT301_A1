package com.example.habittracker;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by cho on 2016-10-01.
 */
public class HabitList {
    private ArrayList<Habit> habitList;
    private ArrayList<Habit> todaysList;



    public HabitList() {
        habitList = new ArrayList<Habit>();
        todaysList = new ArrayList<Habit>();
    }

    public ArrayList<Habit> getHabitList() {
        return habitList;
    }

    public Habit getHabit(int position) {
        return habitList.get(position);
    }

    public void setHabitList(ArrayList<Habit> habitList) {
        this.habitList = habitList;
        updateTodaysList();
    }

    protected ArrayList<Habit> updateTodaysList() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        todaysList.clear();
        for (Habit h : habitList) {
            if (h.getDays().get(day-1, Boolean.FALSE)==Boolean.TRUE) {
                todaysList.add(h);
            }
        }
        return this.todaysList;
    }

    public void addNewHabit(Habit habit) {
        habitList.add(habit);
    }

    public void removeHabit(int position) { habitList.remove(position); }

    public void updateHabit(int position, Habit habit) {
        habitList.set(position, habit);
    }
}
