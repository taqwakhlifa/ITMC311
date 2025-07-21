package com.example.safebankapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn;



    // بيانات ثابتة للمثال، بعدين ممكن تربطيها بقاعدة بيانات أو API
    private final String correctUsername = "12345";
    private final String correctPassword = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);


        btnSignIn.setOnClickListener(v -> {
            String username = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (TextUtils.isEmpty(username)) {
                etEmail.setError("من فضلك أدخل رقم المستخدم");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("من فضلك أدخل كلمة المرور");
                return;
            }

            if (username.equals(correctUsername) && password.equals(correctPassword)) {
                // تسجيل دخول ناجح - نفتح MainActivity (أو شاشة أخرى)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // تمنع الرجوع لشاشة الدخول بالزر رجوع
            } else {
                Toast.makeText(LoginActivity.this, "بيانات الدخول غير صحيحة", Toast.LENGTH_SHORT).show();
            }
        });
    }
}