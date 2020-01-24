package com.willrussell.hackathon_demo_timer_android;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.willrussell.hackathon_demo_timer_android.countdown_threads.CountdownThread;

import org.jetbrains.annotations.NotNull;

public class CountdownFragment extends Fragment {
    private final String TAG = "CountdownFragment";
    private final String SERVER_URL = "https://4b2f272e.ngrok.io/get_countdown"; // Must be https
    private TextView countdownView;
    private View background;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("countdown");
    private Thread countdownThread;
    private Time time;

    public static CountdownFragment newInstance() {
        CountdownFragment fragment = new CountdownFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.countdown_fragment, container, false);
        countdownView = view.findViewById(R.id.time);
        background = view;

        Log.w(TAG,"Creating view!...");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Time value = dataSnapshot.getValue(Time.class);
                Log.d(TAG, "Value is: " + value);
                if(time == null || !time.equals(value)){
                    time = value;
                    CountdownThread countdown = new CountdownThread(time, countdownView);
                    if(countdownThread == null){
                        Log.w(TAG, "creating first countdown");
                        countdownThread = new Thread(countdown);
                        countdownThread.start();
                    }else {
                        Log.w(TAG, "Interupting existing countdown");
                        countdownThread.interrupt();
                        try {
                            countdownThread.join();
                        } catch (InterruptedException ignored) {
                        }
                        Log.w(TAG, "Creating new countdown");
                        countdownThread = new Thread(countdown);
                        countdownThread.start();
                    }
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return view;
    }

}
