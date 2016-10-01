package com.example.habittracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.app.Activity;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by cho8 on 2016-09-19.
 */
public class NewHabitActivity extends Activity {

    private static final String FILENAME = "file.sav";
    Calendar myCalendar;

    private EditText habitTextView;
    private ListView dayListView;
    private EditText dateTextView;

    private Button saveButton;

    private FragmentManager fragManage = getFragmentManager();
    private ArrayAdapter<String> adapter;

    private List<String> daysArray;
    private HabitList habitList;

    private String content;
    private String dateSet;
    private SparseBooleanArray daysChecked;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_habit);

        habitTextView = (EditText) findViewById(R.id.habitTextView);
        dayListView = (ListView) findViewById(R.id.daysListView);
        dateTextView = (EditText) findViewById(R.id.dateText);
        dateTextView.setFocusable(Boolean.FALSE);

        saveButton = (Button) findViewById(R.id.saveHabit);

        habitList = new HabitList();

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                content = habitTextView.getText().toString();
                daysChecked = dayListView.getCheckedItemPositions();

                Intent resultIntent = new Intent();
                // Check if content is entered
                if (content.isEmpty()) {
                    resultIntent.putExtra("requireContent", Boolean.TRUE);

                } else if (daysChecked.size()==0) {
                    resultIntent.putExtra("requireDays", Boolean.TRUE);

                } else {

                    addNewHabit();
                }

                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Dialog dialog = new DatePickerDialog(NewHabitActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

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

        myCalendar = Calendar.getInstance();

        String[] days = getResources().getStringArray(R.array.dayofweek);
        daysArray = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
                daysArray.add(days[i]);
        }
        adapter = new ArrayAdapter<String>(
                this,
                R.layout.checked_list_item,
                daysArray);
        dayListView.setAdapter(adapter);

        dayListView.setAdapter(adapter);
        dayListView.setItemsCanFocus(false);
        // we want multiple clicks
        dayListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


    }

    private void addNewHabit() {
        Habit newHabit = new Habit(content);
        newHabit.setDays(daysChecked);

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        try {
            Date userDate = sdf.parse(dateSet);
            newHabit.setDate(userDate);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        habitList.addNewHabit(newHabit);
        saveInFile();
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

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(habitList.getHabitList(), out);
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

    // date picker things
    // from http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateSet = sdf.format(myCalendar.getTime());
        dateTextView.setText(dateSet);
    }
}



