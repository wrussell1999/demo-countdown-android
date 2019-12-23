package com.willrussell.hackathon_timer_android_client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    private TextView time;
    private MaterialButton connectionButton;
    private Handler uiHandler;


    final int PORT = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = findViewById(R.id.time);
        connectionButton = findViewById(R.id.connection_button);
        socket = new Socket();

        uiHandler = new Handler();
    }


    public void onClick(View v) {
        final TextInputEditText ipText = (TextInputEditText) findViewById(R.id.ip);

        if (connectionButton.getText().toString().equals(getString(R.string.connect))) {
            connectionButton.setText(getString(R.string.disconnect));

            String ip = ipText.getText().toString().trim();
            try {
                connect(ip);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            connectionButton.setText(getString(R.string.connect));

            try {
                disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void connect(String ip) throws IOException {
        socket.connect(new InetSocketAddress(ip, PORT), 2000);
        socket.close();
    }

    private void disconnect() throws IOException {
        socket.close();
    }

    class NetworkThread implements Runnable {
        private Socket socket;

        public NetworkThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // Check for new data
            // Only allow for UIThread to be updated if time has finished
            uiHandler.post(new UIThread(1));
            // send data back that the timer has started and how long is left.
        }
    }

    class UIThread implements Runnable {
        private int minutes;
        private boolean timer;

        public UIThread(int minutes) {
            this.minutes = minutes;
            this.timer = false;
        }

        @Override
        public void run() {
            // Update time
            if (startTimer(minutes)) {
                this.timer = true;
            }

        }

        public boolean startTimer(int minutes) {
            this.timer = true;
            // do a timer here and prevent other timers being started
            return true;
        }

    }
}

