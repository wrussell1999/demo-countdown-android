package com.willrussell.hackathon_demo_timer_android.time;

import android.widget.TextView;

class UiThread implements Runnable {
    private String output;
    private TextView timeView;

    public UiThread(String output, TextView timeView) {
        this.output = output;
        this.timeView = timeView;
    }

    @Override
    public void run() {
        timeView.setText(output);
    }
}