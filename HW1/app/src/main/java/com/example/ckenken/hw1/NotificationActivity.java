package com.example.ckenken.hw1;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class NotificationActivity extends AppCompatActivity {

    public TextView tv;
    public Button close;
    public Button later;

    public int id;
    public int hour;
    public int min;
    public int ampm;

    public MediaPlayer mp;

    public static boolean isRinging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
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

        tv = (TextView)findViewById(R.id.textView7);
        close = (Button)findViewById(R.id.button4);
        later = (Button)findViewById(R.id.button5);

        Bundle b = getIntent().getExtras();
        id = b.getInt("alarm_id");
        hour = b.getInt("hour");
        min = b.getInt("min");
        ampm = b.getInt("ampm");

        tv.setText(String.format("%02d:%02d", hour, min) + " " + ((ampm==MainActivity.AM)?"AM":"PM"));
        close.setText("Close");
        later.setText("Later");

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRinging = false;
                Intent startIntent = new Intent(NotificationActivity.this, TenMinuteService.class);

                Bundle sendB = new Bundle();

                sendB.putInt("alarm_id", id);
                sendB.putInt("hour", (hour + ((int) (min / 60))) % 12);
                sendB.putInt("min", (min + 10) % 60);
                sendB.putInt("ampm", ampm);

                startIntent.putExtras(sendB);

                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                MainActivity.a_on[id] = false;

                startService(startIntent);
                NotificationActivity.this.finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.a_on[id] = false;
                isRinging = false;
                mp.stop();
                NotificationActivity.this.finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        isRinging = true;

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
       // r.play();
        mp = new MediaPlayer();

        try {
            mp.setDataSource(NotificationActivity.this, notification);
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
            mp.prepare();
            mp.setLooping(true);
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.a_on[id] = false;
        mp.release();
        isRinging = false;
    }
}
