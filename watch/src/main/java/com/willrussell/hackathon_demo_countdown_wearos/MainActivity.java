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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends WearableActivity {

    private final String TAG = "Countdown";

    private TextView countdownTimeView;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference myRef = database.collection("countdown");
    private DocumentReference docRef = myRef.document("1");
    private Time time;
    private int totalSeconds;
    private CountDownTimer countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();
        countdown = null;

        countdownTimeView = findViewById(R.id.time);
        docRef.addSnapshotListener((documentSnapshot, e) -> {

            System.out.println(documentSnapshot.getData());
            Map<String, Object> value = documentSnapshot.getData();
            Time values = new Time((Long) value.get("time"), (Boolean) value.get("start"), (Long) value.get("epoch"));

            Log.d(TAG, "Value is: " + values);
            if (time == null || !time.equals(values)){
                time = values;
                if (time.getStart()){
                    long input = (long) time.getEpoch();
                    long now = Instant.now().toEpochMilli();
                    totalSeconds = (int) (Math.abs(input - now));

                    if (countdown == null) {
                        Log.d(TAG, "Start first countdown");
                        initCountdown();
                        countdown.start();
                    } else {
                        Log.d(TAG, "Resetting");
                        countdown.cancel();
                        initCountdown();
                        countdown.start();
                    }
                } else {
                    countdownTimeView.setText(time.getTime() + ":00");
                    if (countdown != null) {
                        countdown.cancel();
                    }
                }
            }
        });
    }

    public void initCountdown() {

        Log.d(TAG, totalSeconds + "");
        countdown = new CountDownTimer(totalSeconds, 1000) {

            public void onTick(long millisUntilFinished) {
                String timeFormatted;
                long minutes;
                long seconds;

                // Start countdown
                if (time.getStart()) {
                    minutes = (((millisUntilFinished + 500) / 1000 * 1000)  / 1000) / 60;
                    seconds = (((millisUntilFinished + 500) / 1000 * 1000) / 1000) % 60;
                } else {
                    minutes = time.getTime();
                    seconds = 0;
                }

                timeFormatted = String.format("%d:%02d", (int) minutes, (int) seconds);

                Log.d(TAG, "Formatted: " + timeFormatted);
                countdownTimeView.setText(timeFormatted);
            }

            public void onFinish() {
                Log.d(TAG, "Countdown finished");
                countdownTimeView.setText("0:00");
            }
        };
    }
}
