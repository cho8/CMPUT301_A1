package com.example.habittracker;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

/**
 * Created by cho8 on 2016-09-19.
 */

public class DetailActivity extends Activity {

    private static final String FILENAME = "file.sav";

    private List<String> daysArray;
    private ListView setDaysView;
    private TextView habitContentView;
    private TextView habitDateView;
    private TextView habitCompletesView;

    private SparseBooleanArray daysChecked;
    private ArrayAdapter<String> adapter;

    private Habit habit;
    private ArrayList<Habit> habitList;
    private Integer position;

    private int dayOfWeek;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_habit);

        setDaysView = (ListView) findViewById(R.id.setDaysView);
        habitContentView = (TextView) findViewById(R.id.ContentView);
        habitDateView = (TextView) findViewById(R.id.DateView);
        habitCompletesView = (TextView) findViewById(R.id.nCompletes);

        dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            position = extras.getInt("position");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveInFile();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadFromFile();
        habit = habitList.get(position);
        daysChecked = habit.getDays();


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

        Button completeButton = (Button) findViewById(R.id.completeHabit);
        Button deleteButton = (Button) findViewById(R.id.deleteHabit);

        // Make completeButton invisible for non habit days
        if (!habit.getDays().get(dayOfWeek-1, Boolean.FALSE)) {
            completeButton.setVisibility(View.GONE);
        }
        completeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent returnIntent = new Intent();

                incrementCompletes(position);
                returnIntent.putExtra("incr", Boolean.TRUE);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                deleteHabit(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("delete", Boolean.TRUE);

                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }
        });

    }

    private void deleteHabit(int position) {
        habitList.remove(position);
        adapter.notifyDataSetChanged();

    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type listType = new TypeToken<ArrayList<Habit>>(){}.getType();

            habitList = gson.fromJson(in,listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            habitList = new ArrayList<Habit>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(habitList, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    private void incrementCompletes(int position) {
        Habit habit = habitList.get(position);
        habit.addCompletes();

        habitList.set(position, habit);

        adapter.notifyDataSetChanged();

        saveInFile();
    }

}

