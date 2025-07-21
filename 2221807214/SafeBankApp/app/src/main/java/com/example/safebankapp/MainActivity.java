package com.example.safebankapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnOrders, btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View rootView = findViewById(R.id.main);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize buttons
        btnOrders = findViewById(R.id.btnOrders);
        btnAccount = findViewById(R.id.btnAccount);

        // Set click listeners
        btnOrders.setOnClickListener(v -> showBiometricPrompt(
                OrdersActivity.class,
                "التحقق البيومتري",
                "يرجى استخدام بصمتك أو وجهك للدخول إلى خدمة الطلبات"
        ));

        btnAccount.setOnClickListener(v -> showBiometricPrompt(
                AccountActivity.class,
                "التحقق من الهوية",
                "يرجى استخدام بصمتك أو وجهك للدخول إلى الحساب"
        ));
    }

    private void showBiometricPrompt(Class<?> targetActivity, String title, String subtitle) {
        BiometricManager biometricManager = BiometricManager.from(this);

        // Check available authentication methods
        boolean hasFingerprint = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                == BiometricManager.BIOMETRIC_SUCCESS;
        boolean hasFaceId = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
                == BiometricManager.BIOMETRIC_SUCCESS;

        if (!hasFingerprint && !hasFaceId) {
            Toast.makeText(this, "لا يوجد مستشعر بيومتري متاح", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use the only available method
        if (hasFingerprint && !hasFaceId) {
            startBiometricAuth(targetActivity, title, subtitle,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG);
        } else if (!hasFingerprint && hasFaceId) {
            startBiometricAuth(targetActivity, title, subtitle,
                    BiometricManager.Authenticators.BIOMETRIC_WEAK);
        } else {
            // Both methods available - show selection dialog
            showAuthenticationMethodDialog(targetActivity, title, subtitle);
        }
    }

    private void showAuthenticationMethodDialog(Class<?> targetActivity,
                                                String title,
                                                String subtitle) {
        new AlertDialog.Builder(this)
                .setTitle("اختر طريقة التحقق")
                .setItems(new String[]{"بصمة الإصبع", "التعرف على الوجه"}, (dialog, which) -> {
                    int authenticator = (which == 0)
                            ? BiometricManager.Authenticators.BIOMETRIC_STRONG
                            : BiometricManager.Authenticators.BIOMETRIC_WEAK;
                    startBiometricAuth(targetActivity, title, subtitle, authenticator);
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    private void startBiometricAuth(Class<?> targetActivity,
                                    String title,
                                    String subtitle,
                                    int authenticators) {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(getApplicationContext(), "تم التحقق بنجاح", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, targetActivity));
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
                .setAllowedAuthenticators(authenticators) // Strictly enforce selected method
                .setConfirmationRequired(authenticators != BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
