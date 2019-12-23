package com.willrussell.hackathon_timer_android_client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    final int PORT = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = findViewById(R.id.time);
        connectionButton = findViewById(R.id.connection_button);
        socket = new Socket();
    }


    public void onClick(View v) {
        final TextInputEditText ipText = (TextInputEditText) findViewById(R.id.ip);

    }

    private void connect() throws IOException {
        socket.connect(new InetSocketAddress("localhost", PORT), 2000);
        socket.close();
    }

    private void disconnect() throws IOException {

    }
}

