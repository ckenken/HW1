package com.example.ckenken.hw1;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * Created by ckenken on 2016/1/4.
 */
public class TimeService extends Service {

    public final static String UPDATE_ACTION = "TimeService.UPDATE";


    public Thread tAlarm;

    public static Date alm1;
    public static Date alm2;
    public static Date alm3;

    public static boolean alm1_power;
    public static boolean alm2_power;
    public static boolean alm3_power;

    public static SimpleDateFormat sdf2 = new SimpleDateFormat("");

    AlarmThread runAlarm = new AlarmThread();

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            Intent intent = new Intent(TimeService.this, NotificationActivity.class);
            Bundle b = new Bundle();

            b.putInt("alarm_id", msg.what);
            b.putInt("hour", MainActivity.hour[msg.what]);
            b.putInt("min", MainActivity.min[msg.what]);
            b.putInt("ampm", MainActivity.ampm[msg.what]);

            intent.putExtras(b);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
    };

    public int checkAMPM(Date d) {
        if (d.getHours() >= 12) {
            return MainActivity.PM;
        }
        else {
            return MainActivity.AM;
        }
    }

    class AlarmThread implements Runnable {

        public boolean isRunning = true;

        @Override
        public void run() {
            Looper.prepare();
            while(isRunning) {
                MainActivity.handler.obtainMessage(MainActivity.REFRESH).sendToTarget();

                Calendar c = Calendar.getInstance();
                Date d = c.getTime();

                for(int i = 0; i<MainActivity.ALARM_NUM; i++) {
                    if (!NotificationActivity.isRinging && MainActivity.a_on[i] && MainActivity.hour[i] == (d.getHours()%12) && MainActivity.min[i] == d.getMinutes() && checkAMPM(d) == MainActivity.ampm[i]) {
                        handler.obtainMessage(i).sendToTarget();
                    }
                }
                Log.d("hour:min:", Integer.toString(d.getHours()) + ":" + Integer.toString(d.getMinutes()));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        public void stopThread() {
            this.isRunning = false;
        }

    }


    @Override
    public void onCreate() {

     //   MainActivity.sdf.parse("")


        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("OnStartCommand", "11111");

        if (tAlarm == null) {
            tAlarm = new Thread(runAlarm);
            tAlarm.start();
        }

        JSONArray ja = new JSONArray();
        for(int i = 0; i<MainActivity.ALARM_NUM; i++) {
            JSONObject j = new JSONObject();

            try {
                j.put("alarm_id", i);
                j.put("hour", MainActivity.hour[i]);
                j.put("min", MainActivity.min[i]);
                j.put("ampm", MainActivity.ampm[i]);
                j.put("a_on",MainActivity.a_on[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ja.put(j);

        }

        Bundle b = new Bundle();
        Log.d("alarms:", ja.toString());
        b.putString("alarms", ja.toString());

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(UPDATE_ACTION);
        broadcastIntent.putExtras(b);
        sendBroadcast(broadcastIntent);

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.d("OnStopCommand", "22222");
        //MainActivity.handler.obtainMessage(2).sendToTarget();
        runAlarm.stopThread();
        tAlarm.interrupt();

        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
