package com.willrussell.hackathon_demo_countdown_android.countdown;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.willrussell.hackathon_demo_countdown_android.R;

import java.util.Calendar;
import java.util.Date;

public class CountdownActivity extends AppCompatActivity {

    private final String TAG = "CountdownActivity";
    protected TextView countdownTimeView;
    protected View decorView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("countdown");

    private Time time;
    private boolean flash;
    private Date endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        countdownTimeView = findViewById(R.id.time);
        decorView = getWindow().getDecorView();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Time value = dataSnapshot.getValue(Time.class);
                Log.d(TAG, "Value is: " + value);

                if (time == null || !time.equals(value)){
                    time = value;

                    int limit = (time.getTime() * 60) * 1000 ;

                    if (time.getStart()){
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.MINUTE, time.getTime());
                        endTime = date.getTime();
                    }

                    new CountDownTimer(limit, 1000) {

                        public void onTick(long millisUntilFinished) {

                            String timeFormatted;
                            long minutes;
                            long seconds;

                            // Start countdown
                            if (time.getStart()) {
                                long timeLeft = (endTime.getTime() - (new Date()).getTime()) / 1000;
                                minutes = timeLeft / 60;
                                seconds = timeLeft % 60;
                            } else {
                                minutes = time.getTime();
                                seconds = 0;
                            }

                            // Flash in last 10 seconds
                            if (minutes == 0 && seconds <= 10 && seconds % 2 == 0) {
                                countdownTimeView.setTextColor(getResources().getColor(android.R.color.white));
                                decorView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                            } else {
                                countdownTimeView.setTextColor(getResources().getColor(android.R.color.black));
                                decorView.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                            }

                            timeFormatted = String.format("%d:%02d", (int) minutes, (int) seconds);

                            countdownTimeView.setText(timeFormatted);
                        }

                        public void onFinish() {
                            countdownTimeView.setText("0:00");
                        }
                    }.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
