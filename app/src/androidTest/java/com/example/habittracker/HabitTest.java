package com.example.habittracker;

import android.test.ActivityInstrumentationTestCase2;
import java.util.Date;

/**
 * Created by cho8 on 2016-09-27.
 */
public class HabitTest extends ActivityInstrumentationTestCase2<MainHabitActivity> {
    public HabitTest() {
        super(MainHabitActivity.class);
    }

    public void testNewHabit() {
        String content = "This is a test!";
        Habit habit = new Habit(content);
        Date date = new Date();
        habit.setDate(date);

        assertTrue(habit.getContent().equals("This is a test!"));
        assertTrue(habit.getDate().equals(date));

        assertFalse(habit.getCompletes()>0);
    }

    public void testAddCompletes() {
        String content = "This is a test!";
        Habit habit = new Habit(content);

        habit.addCompletes();
        assertTrue(habit.getCompletes()>0);
        assertTrue(habit.getPastCompletes().size()>0);
    }

    public void testSubCompletes() {
        String content = "This is a test!";
        Habit habit = new Habit(content);

        habit.addCompletes();
        assertTrue(habit.getCompletes()>0);
        habit.subCompletes();
        assertFalse(habit.getPastCompletes().size()>0);
    }

}
