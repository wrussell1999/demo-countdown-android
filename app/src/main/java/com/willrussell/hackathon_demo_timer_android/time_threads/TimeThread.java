package com.willrussell.hackathon_demo_timer_android.time_threads;

import android.os.Handler;
import android.widget.TextView;

import com.willrussell.hackathon_demo_timer_android.time_threads.UiThread;

import com.willrussell.hackathon_demo_timer_android.TimeActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeThread extends TimeActivity implements Runnable {

    private String time;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            time = getTime();
            handler.post(new UiThread(time));
        }
    }

    public String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String time = formatter.format(date);
        return time;
    }
}
