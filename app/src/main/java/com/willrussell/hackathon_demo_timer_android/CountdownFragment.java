package com.willrussell.hackathon_demo_timer_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountdownFragment extends Fragment {

    public static CountdownFragment newInstance() {
        CountdownFragment fragment = new CountdownFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.countdown_fragment, container, false);
        return view;
    }

    public Date getCountDownTime(String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
            Date date = formatter.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
