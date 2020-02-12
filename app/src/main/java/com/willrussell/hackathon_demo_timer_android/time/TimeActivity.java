package com.willrussell.hackathon_demo_timer_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willrussell.hackathon_demo_timer_android.time_threads.TimeThread;

public class TimeActivity extends AppCompatActivity {

    protected TextView timeView;
    protected Handler handler;
    private TimeThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        handler = new Handler(Looper.getMainLooper());
        timeView = findViewById(R.id.time_remaining);

        thread = new TimeThread();
        new Thread(thread).start();
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
