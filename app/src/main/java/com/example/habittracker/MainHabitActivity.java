package com.example.habittracker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by cho8 on 2016-09-19.
 */

public class MainHabitActivity extends Activity implements AdapterView.OnItemClickListener{

    private static final String FILENAME = "file.sav";
    private EditText bodyText;
    private ListView oldHabitsList;

    private HabitList habitList;
    private ArrayList<Habit> todaysList;

    private ArrayAdapter<Habit> adapter;

    protected static final int NewHabitActivityCode = 1;
    protected static final int DetailActivityCode = 2;
    protected static final int ListActvityCode = 3;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oldHabitsList = (ListView) findViewById(R.id.oldHabitsList);

        Button newButton = (Button) findViewById(R.id.newHabit);
        Button listButton = (Button) findViewById(R.id.habitList);

        habitList = new HabitList();

        newButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Log.i("NewListView", "New habit");
                Intent intent = new Intent();

                intent.setClass(getApplicationContext(),NewHabitActivity.class);

                startActivityForResult(intent,NewHabitActivityCode);
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent();

                intent.setClass(getApplicationContext(),ListHabitActivity.class);

                startActivityForResult(intent,ListActvityCode);
            }
        });

        oldHabitsList.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();
        todaysList = habitList.updateTodaysList();

        adapter = new CustomItemAdapter(this,
                R.layout.list_item, todaysList);
        oldHabitsList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile();
        todaysList = habitList.updateTodaysList();
        adapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> habitView, View v, int position, long id) {

        Gson gson = new Gson();
        Habit habit = habitList.getHabit(position);
        Intent intent = new Intent();
        intent.setClass(this, DetailActivity.class);

        intent.putExtra("position", position);

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
                        showToast(R.string.habit_created);
                    }

                }

                break;
            }
            case (DetailActivityCode) : {
                if (resultCode == Activity.RESULT_OK) {

                    Integer position = data.getIntExtra("position", -1);
                    if (data.getBooleanExtra("delete", Boolean.FALSE) == Boolean.TRUE) {

                        showToast(R.string.habit_deleted);

                    } else if (data.getBooleanExtra("incr", Boolean.FALSE) == Boolean.TRUE) {

                        showToast(R.string.habit_complete);
                    } else if (data.getBooleanExtra("save", Boolean.FALSE) == Boolean.TRUE) {
                        showToast(R.string.habit_saved);
                    } else if (data.getBooleanExtra("decr", Boolean.FALSE) == Boolean.TRUE) {
                        showToast(R.string.habit_decr);
                    }

                }
                break;
            }
        }
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
            ArrayList<Habit> intermList = gson.fromJson(in,listType);
            habitList.setHabitList(intermList);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            habitList.setHabitList(new ArrayList<Habit>());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}