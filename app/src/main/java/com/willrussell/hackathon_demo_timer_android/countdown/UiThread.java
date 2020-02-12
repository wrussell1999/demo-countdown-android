package com.willrussell.hackathon_demo_timer_android.countdown_threads;

import android.widget.TextView;

import com.willrussell.hackathon_demo_timer_android.CountdownFragment;

public class UiThread extends CountdownFragment implements Runnable {
    private String output;
    private boolean flash;
    private TextView countdownView;

    public UiThread(String output, Boolean flash, TextView view) {
        this.output = output;
        this.flash = flash;
        this.countdownView = view;
    }

    @Override
    public void run() {
        this.countdownView.setText(output);
        if (flash) {
            //background.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            //background.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        }
    }
}
