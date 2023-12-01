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

        //alarm = MediaPlayer.create(this,R.raw.alarm);
        alarm = MediaPlayer.create(this, soundResourceID(2));
    }

    public static int soundResourceID(int penaltyValue) {

        // 適切な soundResourceID を設定
        switch (penaltyValue) {
            case 1:
                //soundResourceID = R.raw.alarm1;
                return R.raw.alarm1;
            //break;
            //case 2:
            //  soundResourceID = R.raw.alarm2;
            //break;
            // 必要に応じて他のケースを追加
            default:
                //soundResourceID = R.raw.alarm;
                return R.raw.alarm;
            //break;
        }
    }

        /* スタートボタン */
        public void onStart (View view){
            alarm.start();
        }

        /* ストップボタン */
        public void onStop (View view){
            alarm.stop();
        }

        /*aaaa*/
}