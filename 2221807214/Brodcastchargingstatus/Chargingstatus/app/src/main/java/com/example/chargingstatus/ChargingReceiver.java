package com.example.chargingstatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

public class ChargingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null) return;

        switch (action) {
            case Intent.ACTION_POWER_CONNECTED:
                updateUI(context, true, -1);
                Toast.makeText(context, R.string.device_charging, Toast.LENGTH_SHORT).show();
                break;

            case Intent.ACTION_POWER_DISCONNECTED:
                updateUI(context, false, -1);
                Toast.makeText(context, R.string.device_unplugged, Toast.LENGTH_SHORT).show();
                break;

            case Intent.ACTION_BATTERY_CHANGED:
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int batteryPct = (int) (level * 100 / (float) scale);

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;

                updateUI(context, isCharging, batteryPct);
                break;
        }
    }

    private void updateUI(Context context, boolean isCharging, int batteryPercent) {
        if (context instanceof MainActivity) {
            ((MainActivity) context).updateChargingUI(isCharging, batteryPercent);
        } else {
            Intent updateIntent = new Intent("UPDATE_CHARGING_UI");
            updateIntent.putExtra("isCharging", isCharging);
            updateIntent.putExtra("batteryPercent", batteryPercent);
            context.sendBroadcast(updateIntent);
        }
    }
}
