package com.willrussell.hackathon_demo_countdown_wearos;

import android.os.Build;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    private final String COUNTDOWN_TAG = "Countdown";

    private TextView countdownView;
    private TextView timeView;
    private TimeThread thread;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("countdown");
    private Thread countdownThread;
    private Time time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdownView = findViewById(R.id.time);
        timeView = findViewById(R.id.actual_time);

        thread = new TimeThread();
        new Thread(thread).start();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Time value = dataSnapshot.getValue(Time.class);
                Log.d(COUNTDOWN_TAG, "Value is: " + value);

                if (time == null || !time.equals(value)){
                    time = value;
                    CountdownThread countdown = new CountdownThread(time);

                    if (countdownThread == null){
                        Log.w(COUNTDOWN_TAG, "creating first countdown");
                        countdownThread = new Thread(countdown);
                        countdownThread.start();
                    } else {
                        Log.w(COUNTDOWN_TAG, "Interupting existing countdown");
                        countdownThread.interrupt();
                        try {
                            countdownThread.join();
                        } catch (InterruptedException ignored) {}
                        Log.w(COUNTDOWN_TAG, "Creating new countdown");
                        countdownThread = new Thread(countdown);
                        countdownThread.start();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        setAmbientEnabled();
    }

    class TimeThread implements Runnable {
        private String time;


        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                time = getTime();
                runOnUiThread(() -> {
                    timeView.setText(time);
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

    class CountdownThread implements Runnable {
        private Time time;
        private Date endTime;
        protected String countdown;
        private boolean finish;

        public CountdownThread(Time time) {
            this.time = time;
            this.countdown = "";
            this.finish = false;
            if (this.time.getStart()){
                Calendar date = Calendar.getInstance();
                date.add(Calendar.MINUTE, this.time.getTime());
                this.endTime = date.getTime();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                countdown = getTime(); // Must not be on UiThread
                runOnUiThread(() -> {
                    countdownView.setText(countdown);
                });

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
            if (this.time.getStart()) {

                long timeLeft = (this.endTime.getTime() - (new Date()).getTime()) / 1000;
                minutes = timeLeft / 60;
                seconds = timeLeft % 60;
            } else {
                minutes = this.time.getTime();
                seconds = 0;
            }

            if (minutes == 0 && seconds < 0 && seconds > -10) {
                timeFormatted = "0:00";
            } else if (minutes == 0 && seconds <= -10) {
                this.finish = true;
                timeFormatted = "0:00";
            } else {
                timeFormatted = String.format(Locale.ENGLISH,"%d:%02d", (int) minutes, (int) seconds);
            }

            return timeFormatted;
        }
    }
}
