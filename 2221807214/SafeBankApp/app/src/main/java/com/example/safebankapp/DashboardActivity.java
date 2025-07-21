package com.example.safebankapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class DashboardActivity extends AppCompatActivity {

    LinearLayout btnOrders, btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ربط الأزرار
        btnOrders = findViewById(R.id.btnOrders);
        btnAccount = findViewById(R.id.btnAccount);

        // الضغط على زر خدمة الطلبات
        btnOrders.setOnClickListener(v -> showBiometricPrompt(
                OrdersActivity.class,
                "التحقق البيومتري",
                "يرجى استخدام بصمتك أو وجهك للدخول إلى خدمة الطلبات"
        ));

        // الضغط على زر الحساب
        btnAccount.setOnClickListener(v -> showBiometricPrompt(
                AccountActivity.class,
                "التحقق من الهوية",
                "يرجى استخدام بصمتك أو وجهك للدخول إلى الحساب"
        ));
    }

    private void showBiometricPrompt(Class<?> targetActivity, String title, String subtitle) {
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
                != BiometricManager.BIOMETRIC_SUCCESS) {
            Toast.makeText(this, "الجهاز لا يدعم التحقق بالبصمة أو الوجه", Toast.LENGTH_SHORT).show();
            return;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(getApplicationContext(), "تم التحقق بنجاح", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DashboardActivity.this, targetActivity);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(getApplicationContext(), "خطأ: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getApplicationContext(), "فشل التحقق، جرّب مجددًا", Toast.LENGTH_SHORT).show();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setNegativeButtonText("إلغاء")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}