package com.example.habittracker;

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
import android.app.Activity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NewHabitActivity extends Activity {

    private EditText habitTextView;
    private ListView daysListView;
    ArrayAdapter<String> arrayAdapter;
    private int ActivityCode = 1;

    private List<String> daysArray;

    private SparseBooleanArray daysChecked;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_habit);

        habitTextView = (EditText) findViewById(R.id.habitTextView);
        daysListView = (ListView) findViewById(R.id.daysListView);

        Button saveButton = (Button) findViewById(R.id.saveHabit);


        String[] days = getResources().getStringArray(R.array.dayofweek);
        daysArray= new ArrayList<String>();
        for (String day : days) {
            daysArray.add(day);
        }

        arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.checked_list_item,
                daysArray );

        daysListView.setAdapter(arrayAdapter);
        daysListView.setItemsCanFocus(false);
        // we want multiple clicks
        daysListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String content = habitTextView.getText().toString();
                daysChecked = daysListView.getCheckedItemPositions();

                Intent resultIntent = new Intent();
                // Check if content is entered
                if (content.isEmpty()) {
                    Log.i("NewHabit!", "NO STRING!!");
                    resultIntent.putExtra("requireContent", Boolean.TRUE);

                } else if (daysChecked.size()==0) {
                    Log.i("NewHabit!", "NO date!");
                    resultIntent.putExtra("requireDays", Boolean.TRUE);

                } else {

                    Gson gson = new Gson();

                    resultIntent.putExtra("content", content);

                    resultIntent.putExtra("days", gson.toJson(daysChecked));
                }

                setResult(Activity.RESULT_OK, resultIntent);
                finish();



                // Returning to MainActivity referenced from here
                // http://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android

            }
        });


    }

}


