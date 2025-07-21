package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvStatus;
    private Button btnStart, btnCancel;
    private WorkerThread workerThread;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);
        btnStart = findViewById(R.id.btnStart);
        btnCancel = findViewById(R.id.btnCancel);

        btnStart.setOnClickListener(v -> startTask());
        btnCancel.setOnClickListener(v -> cancelTask());
    }

    private void startTask() {
        tvStatus.setText("Working...");
        btnStart.setEnabled(false);
        btnCancel.setEnabled(true);
        progressBar.setProgress(0);

        workerThread = new WorkerThread();
        workerThread.start();
    }

    private void cancelTask() {
        if (workerThread != null) {
            workerThread.interrupt();
            workerThread = null;
        }
        resetUI();
        Toast.makeText(this, "Task cancelled", Toast.LENGTH_SHORT).show();
    }

    private void resetUI() {
        tvStatus.setText("Press Start");
        btnStart.setEnabled(true);
        btnCancel.setEnabled(false);
    }

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i <= 100; i++) {
                    if (isInterrupted()) {
                        throw new InterruptedException();
                    }

                    final int progress = i;
                    mainHandler.post(() -> {
                        progressBar.setProgress(progress);
                        tvStatus.setText("Progress: " + progress + "%");
                    });

                    Thread.sleep(50); // Simulate work
                }

                mainHandler.post(() -> {
                    Toast.makeText(MainActivity.this,
                            "Task completed!", Toast.LENGTH_SHORT).show();
                    resetUI();
                });
            } catch (InterruptedException e) {
                // Thread was interrupted (cancelled)
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (workerThread != null && workerThread.isAlive()) {
            workerThread.interrupt();
        }
    }
}