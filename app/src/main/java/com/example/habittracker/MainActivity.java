package com.example.habittracker;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener{

    private static final String FILENAME = "file.sav";
    private EditText bodyText;
    private ListView oldHabitsList;

    private ArrayList<Habit> habitList = new ArrayList<Habit>();

    private ArrayAdapter<Habit> adapter;

    private static final int NewHabitActivityCode = 1;
    private static final int DetailActivityCode = 2;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oldHabitsList = (ListView) findViewById(R.id.oldHabitsList);

        Button newButton = (Button) findViewById(R.id.newHabit);

        newButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Log.i("NewListView", "New habit");
                Intent intent = new Intent();

                intent.setClass(getApplicationContext(),NewHabitActivity.class);

                startActivityForResult(intent,1);
            }
        });

        oldHabitsList.setOnItemClickListener(this);


    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();
        adapter = new CustomItemAdapter(this,
                R.layout.list_item, habitList);
        oldHabitsList.setAdapter(adapter);

    }

    public void onItemClick(AdapterView<?> habitView, View v, int position, long id) {

        Gson gson = new Gson();
        Habit habit = habitList.get(position);
        Intent intent = new Intent();
        intent.setClass(this, DetailActivity.class);

        intent.putExtra("position", position);
        intent.putExtra("habit", gson.toJson(habit));

        startActivityForResult(intent,DetailActivityCode);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (NewHabitActivityCode) : {

                if (resultCode == Activity.RESULT_OK) {

                    if (data.getBooleanExtra("requireContent",Boolean.FALSE) == Boolean.TRUE) {

                        showToast(R.string.no_habit);
                    } else if (data.getBooleanExtra("requireDays",Boolean.FALSE) == Boolean.TRUE) {
                        showToast(R.string.no_days);
                    } else {

                        String content = data.getStringExtra("content");
                        Gson gson = new Gson();
                        String daysString = data.getStringExtra("days");
                        SparseBooleanArray daysBoolean = gson.fromJson(daysString, SparseBooleanArray.class);


                        addNewHabit(content, daysBoolean);
                        showToast(R.string.habit_created);
                    }

                }

                break;
            }
            case (DetailActivityCode) : {
                if (resultCode == Activity.RESULT_OK) {

                    Integer position = data.getIntExtra("position", -1);
                    if (data.getBooleanExtra("shouldDelete", Boolean.FALSE) == Boolean.TRUE) {

                        deleteHabit(position);
                        showToast(R.string.habit_deleted);

                    } else if (data.getBooleanExtra("shouldIncr", Boolean.FALSE) == Boolean.TRUE) {
                        incrementCompletes(position);

                        showToast(R.string.habit_complete);
                    }

                }
                break;
            }
        }
    }

    private void addNewHabit(String content, SparseBooleanArray daysBoolean) {
        Habit newHabit = new Habit(content);
        newHabit.setDays(daysBoolean);

        habitList.add(newHabit);
        adapter.notifyDataSetChanged();

        saveInFile();
    }

    private void deleteHabit(int position) {
        habitList.remove(position);
        adapter.notifyDataSetChanged();

        saveInFile();
    }

    private void incrementCompletes(int position) {
        Habit habit = habitList.get(position);
        habit.addCompleted();

        habitList.set(position, habit);

        adapter.notifyDataSetChanged();

        saveInFile();
    }

    private void showToast(int resourceStringID) {
        Context context = getApplicationContext();
        CharSequence text = context.getResources().getString(resourceStringID);

        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
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