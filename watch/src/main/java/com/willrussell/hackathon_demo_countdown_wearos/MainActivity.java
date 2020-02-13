package com.willrussell.hackathon_demo_countdown_wearos;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends WearableActivity {

    private final String COUNTDOWN_TAG = "Countdown";

    private TextView countdown;
    private TextView realTime;
    private TimeThread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdown = findViewById(R.id.time);
        realTime = findViewById(R.id.actual_time);

        thread = new TimeThread();
        new Thread(thread).start();

        setAmbientEnabled();
    }

    class TimeThread implements Runnable {
        private String time;


        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                time = getTime();
                runOnUiThread(() -> {
                    realTime.setText(time);
                });
            }
        }

        public String getTime() {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            String time = formatter.format(date);
            return time;
        }
    }
}
