package com.willrussell.hackathon_demo_timer_android;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.willrussell.hackathon_demo_timer_android.countdown_threads.CountdownThread;

import org.json.JSONException;
import org.json.JSONObject;

public class CountdownFragment extends Fragment {

    private final String SERVER_URL = "https://4b2f272e.ngrok.io/get_countdown"; // Must be https
    private TextView countdownView;
    private View background;

    public static CountdownFragment newInstance() {
        CountdownFragment fragment = new CountdownFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.countdown_fragment, container, false);
        countdownView = view.findViewById(R.id.time);
        background = view;

        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        CountdownThread countdownThread = new CountdownThread(obj.getString("time"), this.countdownView);
                        new Thread(countdownThread).start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
        });
        queue.add(stringRequest);

        return view;
    }

}
