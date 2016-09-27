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

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListHabitActivity extends Activity implements AdapterView.OnItemClickListener{

    private ListView habitListView;
    private ArrayAdapter<Habit> arrayAdapter;
    private ArrayList<Habit> habitList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_habit);

        habitListView =  (ListView) findViewById(R.id.habitlistView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Gson gson = new Gson();
        if (extras != null) {
            String serialList = extras.getString("habitList");
            Type listType = new TypeToken<ArrayList<Habit>>(){}.getType();

            habitList = gson.fromJson(serialList,listType);

        }

        habitListView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> habitView, View v, int position, long id) {

        Gson gson = new Gson();
        Habit habit = habitList.get(position);
        Intent intent = new Intent();
        intent.setClass(this, DetailActivity.class);

        intent.putExtra("position", position);
        intent.putExtra("habit", gson.toJson(habit));
        intent.putExtra("viewOnly", Boolean.TRUE);

        startActivityForResult(intent,MainActivity.DetailActivityCode);

    }

    protected void onStart() {
        super.onStart();
        arrayAdapter = new ArrayAdapter<Habit>(this,
                R.layout.list_item, habitList);
        habitListView.setAdapter(arrayAdapter);

    }
}
