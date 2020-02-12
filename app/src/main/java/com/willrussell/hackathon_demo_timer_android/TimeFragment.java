package com.willrussell.hackathon_demo_timer_android;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TimeFragment extends Fragment {

    private TextView timeView;
    private Handler handler;

    public static TimeFragment newInstance() {
        TimeFragment fragment = new TimeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_time, container, false);
        timeView = view.findViewById(R.id.time);
        handler = new Handler();
        TimeThread timeThread = new TimeThread();
        new Thread(timeThread).start();
        return view;
    }


    class TimeThread implements Runnable {
        private String time;

        public TimeThread() {

        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                time = getTime();
                handler.post(new UIThread(time));
            }
        }

        public String getTime() {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String time = formatter.format(date);
            return time;
        }
    }

    class UIThread implements Runnable {
        private String output;

        public UIThread(String output) {
            this.output = output;
        }

        @Override
        public void run() {
            timeView.setText(output);
        }
    }


}
