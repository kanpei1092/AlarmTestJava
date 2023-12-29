package com.example.alarmtestjava;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // MainActivity.alarmを初期化して音楽を再生
        MainActivity.alarm = MainActivity.alarm.create(this, MainActivity.soundResourceID(MainActivity.currentPenaltyValue));
        MainActivity.alarm.start();

        return START_STICKY;
    }

    private void stopMusic() {
        if (MainActivity.alarm != null) {
            MainActivity.alarm.stop();
            MainActivity.alarm.release();
            MainActivity.alarm = null;
            Log.d("MainActivity", "stopMusicが呼ばれました");
        }
    }

    @Override
    public void onDestroy() {
        stopMusic();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
