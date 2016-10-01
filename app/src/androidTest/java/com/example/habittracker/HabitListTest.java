package com.example.habittracker;

import android.test.ActivityInstrumentationTestCase2;
import android.util.SparseBooleanArray;

import java.util.Calendar;
import java.util.ArrayList;

/**
 * Created by cho on 2016-10-01.
 */
public class HabitListTest extends ActivityInstrumentationTestCase2<MainHabitActivity> {
    public HabitListTest() {
        super(MainHabitActivity.class);
    }

    public void testAddNewHabit() {
        HabitList habitList = new HabitList();
        Habit a = new Habit("Hello!");
        Habit b = new Habit("Hi?");

        habitList.addNewHabit(a);

        assertEquals(habitList.getHabit(0),a);

        habitList.addNewHabit(b);

        assertEquals(habitList.getHabit(1),b);

    }

    public void testUpdateTodaysList() {
        HabitList habitList = new HabitList();

        Habit odd = new Habit("This is a test!");
        SparseBooleanArray daysChecked = new SparseBooleanArray();
        daysChecked.append(0, Boolean.TRUE); // S

        daysChecked.append(2, Boolean.TRUE); // T

        daysChecked.append(4, Boolean.TRUE); // R

        daysChecked.append(6, Boolean.TRUE); // S
        odd.setDays(daysChecked);

        Habit even = new Habit("This is a quiz?");
        SparseBooleanArray daysChecked2 = new SparseBooleanArray();
        daysChecked2.append(1, Boolean.TRUE); // M
        daysChecked2.append(3, Boolean.TRUE); // W
        daysChecked2.append(5, Boolean.TRUE); // F

        even.setDays(daysChecked2);

        habitList.addNewHabit(odd);
        habitList.addNewHabit(even);

        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        ArrayList<Habit> todaysList = habitList.updateTodaysList();
        for (Habit h : todaysList) {
            SparseBooleanArray days = h.getDays();
            assertTrue(days.get(today-1));
        }
    }

    public void testRemoveHabit() {
        HabitList habitList = new HabitList();
        Habit a = new Habit("Hello!");
        Habit b = new Habit("Hi?");

        habitList.addNewHabit(a);
        habitList.addNewHabit(b);

        assertEquals(habitList.getHabit(1),b);

        habitList.removeHabit(0);
        assertEquals(habitList.getHabit(0), b);
    }

    public void testUpdateHabit() {
        HabitList habitList = new HabitList();
        Habit a = new Habit("Hello!");
        Habit b = new Habit("Hi?");

        habitList.addNewHabit(a);
        habitList.addNewHabit(b);

        assertEquals(habitList.getHabit(0),a);
        Habit c = new Habit("Yo!!");

        habitList.updateHabit(1,c);

        assertEquals(habitList.getHabit(1),c);
    }


}
