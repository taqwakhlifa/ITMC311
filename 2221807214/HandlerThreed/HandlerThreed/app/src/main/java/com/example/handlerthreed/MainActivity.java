// File: MainActivity.java
package com.example.handlerthreed;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity {
    private ProgressBar bar1;
    private ProgressBar bar2;
    private TextView msgWorking;
    private TextView msgReturned;
    private ImageView statusImage;
    private Button restartButton;
    private boolean isRunning = false;
    private static final int MAX_SEC = 60;
    private String strTest = "global value seen by all threads ";
    private int intTest = 0;

    private final Handler handler = new Handler(msg -> {
        String returnedValue = (String) msg.obj;
        msgReturned.setText("Returned by background thread:\n\n" + returnedValue);
        bar1.incrementProgressBy(1);

        if (bar1.getProgress() >= MAX_SEC) {
            msgReturned.setText("Done. Background thread has been stopped.");
            isRunning = false;
        }

        if (bar1.getProgress() == bar1.getMax()) {
            msgWorking.setText("Done");
            bar1.setVisibility(View.INVISIBLE);
            bar2.setVisibility(View.INVISIBLE);
            statusImage.setVisibility(View.VISIBLE);
            restartButton.setVisibility(View.VISIBLE);
        } else {
            msgWorking.setText("Working... " + bar1.getProgress());
        }
        return true;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar1 = findViewById(R.id.progress);
        bar2 = findViewById(R.id.progress2);
        msgWorking = findViewById(R.id.TextView01);
        msgReturned = findViewById(R.id.TextView02);
        statusImage = findViewById(R.id.statusImage);
        restartButton = findViewById(R.id.restartButton);

        statusImage.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.INVISIBLE);

        restartButton.setOnClickListener(v -> restartThread());

        bar1.setMax(MAX_SEC);
        bar1.setProgress(0);

        strTest += "-01";
        intTest = 1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        startThread();
    }

    private void startThread() {
        isRunning = true;
        Thread background = new Thread(() -> {
            try {
                for (int i = 0; i < MAX_SEC && isRunning; i++) {
                    Thread.sleep(1000);
                    Random rnd = new Random();
                    String data = "Thread Value: " + rnd.nextInt(101);
                    data += "\n" + strTest + " " + intTest;
                    intTest++;
                    Message msg = handler.obtainMessage(1, data);
                    if (isRunning) {
                        handler.sendMessage(msg);
                    }
                }
            } catch (InterruptedException ignored) {
            }
        });
        background.start();
    }

    private void restartThread() {
        bar1.setProgress(0);
        bar1.setVisibility(View.VISIBLE);
        bar2.setVisibility(View.VISIBLE);
        statusImage.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.INVISIBLE);
        msgWorking.setText("Working...");
        msgReturned.setText("");
        intTest = 1;
        startThread();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }
}

