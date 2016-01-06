package com.example.ckenken.hw1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int ALARM_NUM = 3;

    public static final int REFRESH = 1;
    public static final int AM = 0;
    public static final int PM = 1;

    public Button b1;
    public Button b2;
    public Button setAlarmButton;

    private static TextView label;
    public static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aaa");

    public static int [] hour = new int[ALARM_NUM];

    public static int [] min = new int[ALARM_NUM];

    public static int [] ampm = new int[ALARM_NUM];

    public static boolean [] a_on = new boolean[ALARM_NUM];

    public Thread t1;

    public TimeThread run1 = new TimeThread();

    public static Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:

                    Calendar c = Calendar.getInstance();
                    Date d = c.getTime();

                    label.setText(sdf.format(d));

                    break;
                case 2:
                    label.setText("56789");
                    break;
                default:
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        b1 = (Button)findViewById(R.id.button);
        b2 = (Button)findViewById(R.id.button2);
        b1.setText("Start Time");
        b2.setText("Stop Time");

        setAlarmButton = (Button)findViewById(R.id.button3);

        label = (TextView)findViewById(R.id.textView);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, TimeService.class);
                startService(startIntent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this, TimeService.class);
                stopService(stopIntent);
            }
        });

        setAlarmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetAlarmActivity.class);

            //    intent.putExtras(bundle);

                startActivity(intent);
             //   MainActivity.this.finish();
            }
        });

        for(int i = 0; i<hour.length; i++) {
            hour[i] = 0;
        }

        for(int i = 0; i<min.length; i++) {
            min[i] = 0;
        }

        for(int i = 0; i< ampm.length; i++) {
            ampm[i] = AM;
        }

        for(int i = 0; i<a_on.length; i++) {
            a_on[i] = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        run1.stopThread();
        t1.interrupt();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        Intent startIntent = new Intent(MainActivity.this, TimeService.class);
        startService(startIntent);

    }
    @Override
    protected void onResume() {
        super.onResume();

//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);	//mainｬOclass name
//        builder.setTitle("Oops!");
//        builder.setMessage("GG!!!");
//
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//
//        builder.show();

        if (t1 == null) {
            t1 = new Thread(run1);
            t1.start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
