package com.willrussell.hackathon_demo_countdown_android.time;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willrussell.hackathon_demo_countdown_android.R;
import com.willrussell.hackathon_demo_countdown_android.AboutActivity;
import com.willrussell.hackathon_demo_countdown_android.countdown.CountdownActivity;
import com.willrussell.hackathon_demo_countdown_android.countdown.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeActivity extends AppCompatActivity {

    private final String TAG = "TimeActivity";

    protected TextView timeView;
    TimeTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        timeView = findViewById(R.id.time);


        task = new TimeTask();
        task.execute();
        Log.d(TAG, "Finished");
    }

    private class TimeTask extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG, "Background task...");
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String time = formatter.format(date);
            return time;
        }

        @Override
        protected void onProgressUpdate(String... time) {
            Log.d(TAG, "Progress Update: " + time[0]);
            timeView.setText(time[0]);
        }

        @Override
        protected void onPostExecute(String time) {
            Log.d(TAG, "Post Execute: " + time);
            timeView.setText(time);
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
