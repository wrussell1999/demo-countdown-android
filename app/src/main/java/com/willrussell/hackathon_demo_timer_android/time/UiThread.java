package com.willrussell.hackathon_demo_timer_android.time_threads;

import android.widget.TextView;

import com.willrussell.hackathon_demo_timer_android.TimeActivity;

class UiThread extends TimeActivity implements Runnable {
    private String output;

    public UiThread(String output) {
        this.output = output;
    }

    @Override
    public void run() {
        timeView.setText(output);
    }
}