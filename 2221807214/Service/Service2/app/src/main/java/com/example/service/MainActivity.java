package com.example.service;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = findViewById(R.id.btnStartService);
        Button btnStop = findViewById(R.id.btnStopService);

        btnStart.setOnClickListener(v -> {
            startService(new Intent(this, AudioService.class));
            Toast.makeText(this, "Playing system sound", Toast.LENGTH_SHORT).show();
        });

        btnStop.setOnClickListener(v -> {
            stopService(new Intent(this, AudioService.class));
            Toast.makeText(this, "Sound stopped", Toast.LENGTH_SHORT).show();
        });
    }
}