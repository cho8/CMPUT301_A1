package com.example.habittracker;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends Activity {

    private List<String> daysArray;
    private ListView setDaysView;
    private SparseBooleanArray daysChecked;
    private ArrayAdapter<String> adapter;

    private Habit habit;
    private Integer position;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_habit);

        Button completeButton = (Button) findViewById(R.id.completeHabit);
        Button deleteButton = (Button) findViewById(R.id.deleteHabit);

        setDaysView = (ListView) findViewById(R.id.setDaysView);
        Gson gson = new Gson();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String serialHabit = extras.getString("habit");
            habit = gson.fromJson(serialHabit, Habit.class);
            position = extras.getInt("position");
            daysChecked = habit.getDays();

            TextView habitContentView = (TextView) findViewById(R.id.ContentView);
            TextView habitDateView = (TextView) findViewById(R.id.DateView);
            TextView habitCompletesView = (TextView) findViewById(R.id.nCompletes);

            habitDateView.setText(habit.dateString());
            habitContentView.setText(habit.getContent());
            habitCompletesView.setText(habit.completesString());

            String[] days = getResources().getStringArray(R.array.dayofweek);
            daysArray = new ArrayList<String>();
            for (int i = 0; i < 7; i++) {
                if (daysChecked.get(i, Boolean.FALSE) == Boolean.TRUE) {
                    daysArray.add(days[i]);
                }
            }

            adapter = new ArrayAdapter<>(
                    this,
                    R.layout.list_item,
                    daysArray);
            setDaysView.setAdapter(adapter);
        }

        completeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent returnIntent = new Intent();


                returnIntent.putExtra("shouldIncr", Boolean.TRUE);
                returnIntent.putExtra("position", position);

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("position", position);
                returnIntent.putExtra("shouldDelete", Boolean.TRUE);

                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }
        });



    }
}

