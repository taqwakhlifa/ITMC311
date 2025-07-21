package com.example.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

public class AudioService extends Service {
    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (player == null) {
                player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
                player.setLooping(true);
                player.start();
                Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error playing sound", Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
    }
}
