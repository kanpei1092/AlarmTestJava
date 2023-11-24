package com.example.alarmtestjava;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm = MediaPlayer.create(this,R.raw.alarm);
    }

    /* スタートボタン */
    public void onStart(View view){
        alarm.start();
    }

    /* ストップボタン */
    public void onStop(View view){
        alarm.stop();
    }
}