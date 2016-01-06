package com.example.ckenken.hw1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SetAlarmActivity extends AppCompatActivity {

    public CheckBox cb1;
    public CheckBox cb2;
    public CheckBox cb3;

    public TextView tv1;
    public TextView tv2;
    public TextView tv3;

//    public Button refreshButton;

    AlarmBroasdCastReceiver updateReceiver;

    class AlarmBroasdCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            JSONArray ja = null;
            try {
                ja = new JSONArray(b.getString("alarms"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i = 0; i<ja.length(); i++) {
                JSONObject j = null;
                try {
                    j = ja.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int id = -1;
                int hour = -1;
                int min = -1;
                int ampm = -1;
                boolean a_on = false;

                try {
                    id = j.getInt("alarm_id");
                    hour = j.getInt("hour");
                    min = j.getInt("min");
                    ampm = j.getInt("ampm");
                    a_on = j.getBoolean("a_on");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                switch (i) {
                    case 0:
                        tv1.setText(String.format("%02d", hour) + ":" + String.format("%02d", min) + (ampm==MainActivity.AM?"AM":"PM"));
                        cb1.setChecked(a_on);
                        break;
                    case 1:
                        tv2.setText(String.format("%02d", hour) + ":" + String.format("%02d", min) + (ampm == MainActivity.AM ? "AM" : "PM"));
                        cb2.setChecked(a_on);
                        break;
                    case 2:
                        tv3.setText(String.format("%02d", hour) + ":" + String.format("%02d", min) + (ampm == MainActivity.AM ? "AM" : "PM"));
                        cb3.setChecked(a_on);
                        break;
                    default:
                        break;

                }


            }
        }
    }

    class TextOnclickListener implements View.OnClickListener {

        private int id;

        public TextOnclickListener(int inputId)
        {
            id = inputId;
        }

        @Override
        public void onClick(View v) {
            TextView tv = (TextView) v;
//            if (cb.isChecked()) {
//                cb.setChecked(true);
//            }
//            else {
//                cb.setChecked(false);
//            }

            Bundle bundle = new Bundle();

            bundle.putInt("check_id", this.id);
            bundle.putString("check_time", tv.getText().toString());

            Intent intent = new Intent();
            intent.setClass(SetAlarmActivity.this, InputAlarmTimeActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    class CheckOnChangeListener implements CompoundButton.OnCheckedChangeListener {

        private int id;

        public CheckOnChangeListener(int inputId) {
            this.id = inputId;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MainActivity.a_on[this.id] = isChecked;

            if (cb1.isChecked() || cb2.isChecked() || cb3.isChecked()) {
                Intent startIntent = new Intent(SetAlarmActivity.this, TimeService.class);
                startService(startIntent);
            }
            else if (!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked()) {
                Intent startIntent = new Intent(SetAlarmActivity.this, TimeService.class);
                stopService(startIntent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
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

        setTitle("Set Alarm On/Off");

        cb1 = (CheckBox)findViewById(R.id.checkBox);
        cb2 = (CheckBox)findViewById(R.id.checkBox2);
        cb3 = (CheckBox)findViewById(R.id.checkBox3);

        tv1 = (TextView)findViewById(R.id.textView4);
        tv2 = (TextView)findViewById(R.id.textView5);
        tv3 = (TextView)findViewById(R.id.textView6);

//        refreshButton = (Button)findViewById(R.id.button6);

        tv1.setOnClickListener(new TextOnclickListener(0));
        tv2.setOnClickListener(new TextOnclickListener(1));
        tv3.setOnClickListener(new TextOnclickListener(2));

        cb1.setText("");
        cb2.setText("");
        cb3.setText("");

        cb1.setOnCheckedChangeListener(new CheckOnChangeListener(0));
        cb2.setOnCheckedChangeListener(new CheckOnChangeListener(1));
        cb3.setOnCheckedChangeListener(new CheckOnChangeListener(2));

//        refreshButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SetAlarmActivity.this, TimeService.class);
//                SetAlarmActivity.this.startService(intent);
//            }
//        });

        updateReceiver = new AlarmBroasdCastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(SetAlarmActivity.this, TimeService.class);
        SetAlarmActivity.this.startService(intent);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TimeService.UPDATE_ACTION);
        registerReceiver(updateReceiver, intentFilter);

//        tv1.setText(String.format("%02d", MainActivity.hour[0]) + ":" + String.format("%02d", MainActivity.min[0]) + (MainActivity.ampm[0]==MainActivity.AM?"AM":"PM"));
//        tv2.setText(String.format("%02d", MainActivity.hour[1]) + ":" + String.format("%02d", MainActivity.min[1]) + (MainActivity.ampm[1]==MainActivity.AM?"AM":"PM"));
//        tv3.setText(String.format("%02d", MainActivity.hour[2]) + ":" + String.format("%02d", MainActivity.min[2]) + (MainActivity.ampm[2]==MainActivity.AM?"AM":"PM"));
//
//        cb1.setChecked(MainActivity.a_on[0]);
//        cb2.setChecked(MainActivity.a_on[1]);
//        cb3.setChecked(MainActivity.a_on[2]);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(updateReceiver);

    }
}
