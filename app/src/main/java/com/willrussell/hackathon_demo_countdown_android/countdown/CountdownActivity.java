package com.willrussell.hackathon_demo_countdown_android.countdown;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.willrussell.hackathon_demo_countdown_android.R;

import java.time.Instant;
import java.util.Map;

public class CountdownActivity extends AppCompatActivity {

    private final String TAG = "CountdownActivity";
    protected TextView countdownTimeView;
    protected View decorView;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference myRef = database.collection("countdown");
    private DocumentReference docRef = myRef.document("1");
    private Time time;
    private int totalSeconds;
    private CountDownTimer countdown;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        countdownTimeView = findViewById(R.id.time);
        decorView = getWindow().getDecorView();
        countdown = null;

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
                        countdownTimeView.setTextColor(getResources().getColor(android.R.color.black));
                        decorView.setBackgroundColor(getResources().getColor(android.R.color.background_light));
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
                // Flash in last 10 seconds
                if (minutes == 0 && seconds <= 10 && seconds % 2 == 0) {
                    countdownTimeView.setTextColor(getResources().getColor(android.R.color.white));
                    decorView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    countdownTimeView.setTextColor(getResources().getColor(android.R.color.black));
                    decorView.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                }

                timeFormatted = String.format("%d:%02d", (int) minutes, (int) seconds);

                Log.d(TAG, "Formatted: " + timeFormatted);
                countdownTimeView.setText(timeFormatted);
            }

            public void onFinish() {
                Log.d(TAG, "Countdown finished");
                countdownTimeView.setText("0:00");
                countdownTimeView.setTextColor(getResources().getColor(android.R.color.black));
                decorView.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }
        };
    }
}
