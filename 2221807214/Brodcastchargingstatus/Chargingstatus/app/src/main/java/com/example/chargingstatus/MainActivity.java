package com.example.chargingstatus;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView chargingIcon;
    private TextView chargingStatusText;
    private TextView batteryPercentageText;
    private ChargingReceiver chargingReceiver;

    private final BroadcastReceiver uiUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("UPDATE_CHARGING_UI".equals(intent.getAction())) {
                boolean isCharging = intent.getBooleanExtra("isCharging", false);
                int batteryPercent = intent.getIntExtra("batteryPercent", -1);
                updateChargingUI(isCharging, batteryPercent);
            }
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chargingIcon = findViewById(R.id.chargingIcon);
        chargingStatusText = findViewById(R.id.chargingStatusText);
        batteryPercentageText = findViewById(R.id.batteryPercentageText);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(uiUpdateReceiver,
                    new IntentFilter("UPDATE_CHARGING_UI"),
                    Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(uiUpdateReceiver,
                    new IntentFilter("UPDATE_CHARGING_UI"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register the charging receiver
        chargingReceiver = new ChargingReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(chargingReceiver, filter);

        // Get initial battery status
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryIntent != null) {
            chargingReceiver.onReceive(this, batteryIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister receivers
        if (chargingReceiver != null) {
            unregisterReceiver(chargingReceiver);
        }
        unregisterReceiver(uiUpdateReceiver);
    }

    public void updateChargingUI(boolean isCharging, int batteryPercent) {
        runOnUiThread(() -> {
            if (isCharging) {
                chargingIcon.setImageResource(R.drawable.ic_battery_charging);
                chargingStatusText.setText(R.string.charging_status_charging);
            } else {
                chargingIcon.setImageResource(R.drawable.ic_battery_unplugged);
                chargingStatusText.setText(R.string.charging_status_not_charging);
            }

            if (batteryPercent >= 0) {
                batteryPercentageText.setText(getString(R.string.battery_percentage, batteryPercent));
            }
        });
    }
}