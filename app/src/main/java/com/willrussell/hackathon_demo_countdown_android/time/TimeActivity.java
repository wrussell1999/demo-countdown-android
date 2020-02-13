package com.willrussell.hackathon_demo_countdown_android.time;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willrussell.hackathon_demo_countdown_android.R;
import com.willrussell.hackathon_demo_countdown_android.AboutActivity;
import com.willrussell.hackathon_demo_countdown_android.countdown.CountdownActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeActivity extends AppCompatActivity {

    protected TextView timeView;
    protected Handler handler;
    private TimeThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        handler = new Handler();
        timeView = findViewById(R.id.time);

        thread = new TimeThread();
        new Thread(thread).start();
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
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String time = formatter.format(date);
            return time;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_countdown) {
            Intent intent = new Intent(this, CountdownActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
