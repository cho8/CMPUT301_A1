package com.example.habittracker;

import android.util.Log;
import android.util.SparseBooleanArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by cho8 on 2016-09-19.
 */
public class Habit {
    private String content;
    private Date date;
    private Integer completes;
    private SparseBooleanArray days;
    private ArrayList<Date> pastCompletes;

    public Habit(String content) {
        this.content = content;
        this.date = new Date();
        this.completes = 0;
        this.days = new SparseBooleanArray();
        this.pastCompletes = new ArrayList<Date>();

    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCompletes(Integer completes) { this.completes = completes; }

    public void setDays(SparseBooleanArray days) {
        this.days = days;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public Integer getCompletes() {
        return completes;
    }

    public SparseBooleanArray getDays() {
        return days;
    }

    public ArrayList<Date> getPastCompletes() {
        return pastCompletes;
    }

    public ArrayList<String> getPastCompletesStrings() {
        ArrayList<String> strings = new ArrayList<String>();
        for (Date d : pastCompletes) {
            strings.add(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(d));
        }
        return strings;
    }

    public String completesString() {
        return Integer.toString(completes);
    }

    public String dateString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public void addCompletes() {

        this.completes += 1;
        pastCompletes.add(new Date());
    }

    public void subCompletes() {
        if (pastCompletes.size()>0) {
            this.completes -= 1;
            pastCompletes.remove(pastCompletes.size() - 1);
        }
    }

    @Override
    public String toString() {
        return content;
    }
}

