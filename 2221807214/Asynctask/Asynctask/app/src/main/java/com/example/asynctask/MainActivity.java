package com.example.asynctask;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView txt;
    private Button btnStartTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.textView);
        btnStartTask = findViewById(R.id.btnStartTask);

        btnStartTask.setOnClickListener(v -> new SimpleTask().execute());
    }

    private class SimpleTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            txt.setText("Loading...");
            btnStartTask.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(3000); // Simulate 3-second task
            } catch (InterruptedException e) {
                return "Task Interrupted";
            }
            return "Task Completed!";
        }

        @Override
        protected void onPostExecute(String result) {
            txt.setText(result);
            btnStartTask.setEnabled(true);
        }
    }
}