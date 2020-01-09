package com.willrussell.hackathon_timer_android_client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
        InetAddress serverAddr = InetAddress.getByName("Wills-MacBook-Pro.local");
        socket.connect(new InetSocketAddress(serverAddr, PORT));
        NetworkThread networkThread = new NetworkThread(socket);
        new Thread(networkThread).start();
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
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    InputStream in = socket.getInputStream();
                    DataInputStream dataIn = new DataInputStream(in);
                    String output = dataIn.readUTF();
                    System.out.println("OUTPUT: " + output);
                    uiHandler.post(new UIThread(1));
                } catch (IOException e) {
                    break;
                }
            }
            // Check for new data
            // Only allow for UIThread to be updated if time has finished
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
            System.out.println("TIME: " + minutes);
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

