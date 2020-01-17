package com.willrussell.hackathon_demo_timer_android.countdown_threads;

import android.os.Handler;
import android.widget.TextView;

import com.willrussell.hackathon_demo_timer_android.CountdownFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountdownThread extends CountdownFragment implements Runnable {
    private String endTime;
    private String countdown;
    private boolean flash;
    private boolean finish;
    private TextView countdownView;
    private Handler handler;

    public CountdownThread(String endTime, TextView view) {
        this.endTime = endTime;
        this.countdown = "";
        this.flash = false;
        this.finish = false;
        this.countdownView = view;
        handler = new Handler();
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                countdown = getTime();
                handler.post(new UIThread(this.countdown, this.flash, this.countdownView));
                if (finish) {
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTime() throws ParseException {
        Date dateNow = new Date();
        SimpleDateFormat formatterEnd = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
        Date dateEnd = formatterEnd.parse(endTime);
        long timeLeft = (dateEnd.getTime() - dateNow.getTime()) / 1000;
        String timeFormatted;
        long minutes = timeLeft / 60;
        long seconds = timeLeft % 60;

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
           timeFormatted = String.format("%d:%02d", (int) minutes, (int) seconds);
        }
        return timeFormatted;
    }
}
