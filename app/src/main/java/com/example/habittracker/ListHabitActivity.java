package com.example.habittracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class ListHabitActivity extends Activity implements AdapterView.OnItemClickListener{

    private static final String FILENAME = "file.sav";

    private ListView habitListView;
    private ArrayAdapter<Habit> arrayAdapter;
    private ArrayList<Habit> habitList;

    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_habit);

        habitListView =  (ListView) findViewById(R.id.habitlistView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            position = extras.getInt("postion");
        }

        habitListView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> habitView, View v, int position, long id) {

        Gson gson = new Gson();
        Habit habit = habitList.get(position);
        Intent intent = new Intent();
        intent.setClass(this, DetailActivity.class);

        intent.putExtra("position", position);

        startActivityForResult(intent, MainHabitActivity.DetailActivityCode);

    }
    protected void onStart() {
        super.onStart();
        loadFromFile();
        arrayAdapter = new ArrayAdapter<Habit>(this,
                R.layout.list_item, habitList);
        habitListView.setAdapter(arrayAdapter);

    }
    @Override
    protected void onPause() {
        super.onPause();
        saveInFile();
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
}
