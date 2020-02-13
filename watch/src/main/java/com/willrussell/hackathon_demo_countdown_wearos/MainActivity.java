package com.willrussell.hackathon_demo_countdown_wearos;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    private TextView countdownTimeView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("countdown");
    private Time time;
    private Date endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        countdownTimeView = findViewById(R.id.time);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Time value = dataSnapshot.getValue(Time.class);
                Log.d(COUNTDOWN_TAG, "Value is: " + value);

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
                                minutes = (millisUntilFinished / 1000) / 60;
                                seconds = (millisUntilFinished / 1000) % 60;
                            } else {
                                minutes = time.getTime();
                                seconds = 0;
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
