package com.willrussell.hackathon_demo_timer_android.countdown_threads;

import android.os.Build;
import android.os.Handler;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

import com.willrussell.hackathon_demo_timer_android.CountdownFragment;
import com.willrussell.hackathon_demo_timer_android.Time;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CountdownThread extends CountdownFragment implements Runnable {
    private Time time;
    private Date endTime;
    private String countdown;
    private boolean flash;
    private boolean finish;
    private TextView countdownView;
    private Handler handler;

    public CountdownThread(Time time, TextView view) {
        this.time = time;
        this.countdown = "";
        this.flash = false;
        this.finish = false;
        this.countdownView = view;
        handler = new Handler();
        if(this.time.getStart()){
            Calendar date = Calendar.getInstance();
            date.add(Calendar.MINUTE, this.time.getTime());
            this.endTime = date.getTime();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            countdown = getTime();
            handler.post(new UIThread(this.countdown, this.flash, this.countdownView));
            if (finish) {
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getTime() {
        String timeFormatted;
        long minutes;
        long seconds;
        if(this.time.getStart()) {

            long timeLeft = (this.endTime.getTime() - (new Date()).getTime()) / 1000;
            minutes = timeLeft / 60;
            seconds = timeLeft % 60;
        }else {
            minutes = this.time.getTime();
            seconds = 0;
        }
        if (minutes == 0 && seconds <= 10 && seconds % 2 == 0) {
            this.flash = true;
        } else {
            this.flash = false;
        }

        if (minutes == 0 && seconds < 0 && seconds > -10) {
            timeFormatted = "0:00";
        } else if (minutes == 0 && seconds <= -10) {
            this.finish = true;
            this.flash = false;
            timeFormatted = "0:00";
        } else {
           timeFormatted = String.format(Locale.ENGLISH,"%d:%02d", (int) minutes, (int) seconds);
        }
        return timeFormatted;
    }
}
