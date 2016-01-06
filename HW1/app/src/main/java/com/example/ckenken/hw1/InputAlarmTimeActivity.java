package com.example.ckenken.hw1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class InputAlarmTimeActivity extends AppCompatActivity {

    public EditText edHour;
    public EditText edMin;

    public RadioButton am;
    public RadioButton pm;

    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_alarm_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setTitle("Set Alarm Time");

        edHour = (EditText)findViewById(R.id.editText);
        edMin = (EditText)findViewById(R.id.editText2);

        am = (RadioButton)findViewById(R.id.radioButton);
        pm = (RadioButton)findViewById(R.id.radioButton2);

        Bundle b = getIntent().getExtras();
        id = b.getInt("check_id");

//        String timeString = b.getString("check_time");
//
//        String [] SP = timeString.split(":");
//
//        int hour = Integer.parseInt(SP[0]);
//        int min = Integer.parseInt(SP[1]);
//
//        Log.d("id:", Integer.toString(id));
//        Log.d("time:", timeString);
//
//        edHour.setText(SP[0]);
//        edMin.setText(SP[1]);

        edHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edHour.getText().toString().length() > 0 && Integer.parseInt(edHour.getText().toString()) <= 12) {
                    MainActivity.hour[id] = Integer.parseInt(edHour.getText().toString());
                }
            }
        });
        edMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edMin.getText().toString().length() > 0 && Integer.parseInt(edMin.getText().toString()) <= 59) {
                    MainActivity.min[id] = Integer.parseInt(edMin.getText().toString());
                }
            }
        });

        am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ampm[id] = MainActivity.AM;
                pm.setChecked(false);
            }
        });

        pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ampm[id] = MainActivity.PM;
                am.setChecked(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        edHour.setText(String.format("%02d", MainActivity.hour[id]));
        edMin.setText(String.format("%02d", MainActivity.min[id]));

        if (MainActivity.ampm[id] == MainActivity.AM) {
            am.setChecked(true);
            pm.setChecked(false);
        }
        else {
            am.setChecked(false);
            pm.setChecked(true);
        }
    }

}
