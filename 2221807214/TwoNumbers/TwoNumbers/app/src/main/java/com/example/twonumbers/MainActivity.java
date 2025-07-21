package com.example.twonumbers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText number1EditText, number2EditText;
    private Button compareButton;
    private TextView scoreText;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number1EditText = findViewById(R.id.number1);
        number2EditText = findViewById(R.id.number2);
        compareButton = findViewById(R.id.compareButton);
        scoreText = findViewById(R.id.scoreText);

        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num1Str = number1EditText.getText().toString();
                String num2Str = number2EditText.getText().toString();

                if (num1Str.isEmpty() || num2Str.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter both numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                int num1 = Integer.parseInt(num1Str);
                int num2 = Integer.parseInt(num2Str);

                if (num1 > num2) {
                    Toast.makeText(MainActivity.this, "Correct! First number is greater.", Toast.LENGTH_SHORT).show();
                    score++;
                } else {
                    Toast.makeText(MainActivity.this, "Wrong! First number is not greater.", Toast.LENGTH_SHORT).show();
                }

                scoreText.setText("Score: " + score);
            }
        });
    }
}
