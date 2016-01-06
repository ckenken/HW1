package com.example.ckenken.hw1;

import android.os.Looper;

/**
 * Created by ckenken on 2016/1/5.
 */
public class TimeThread implements Runnable {

    private boolean isRunning = true;

    @Override
    public void run() {
        Looper.prepare();
        while(isRunning) {
            MainActivity.handler.obtainMessage(MainActivity.REFRESH).sendToTarget();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        this.isRunning = false;
    }
}
