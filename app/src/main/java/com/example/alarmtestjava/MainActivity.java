package com.example.alarmtestjava;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;
import android.os.SystemClock;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer alarm;
    //private long startTime;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //alarm = MediaPlayer.create(this,R.raw.alarm);
        alarm = MediaPlayer.create(this, soundResourceID(2));
        textView = findViewById(R.id.textView);

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
    private long startTime;
    private long stopTime;
    //private long elapsedTime;

        /* スタートボタン */
        public void onStart (View view){
            alarm.start();
        //}

            // 開始時刻を取得
            startTime = SystemClock.elapsedRealtime();
        }

        /* ストップボタン */
        public void onStop (View view) {
            alarm.stop();
            //}
            // 停止した時刻を取得
            stopTime = SystemClock.elapsedRealtime();

            // 経過時間を計算
            long elapsedTime = stopTime - startTime;

            // 経過時間を秒単位で表示
            long elapsedSeconds = elapsedTime / 1000;

            textView.setText(String.valueOf(elapsedSeconds)+"秒経ちました！");
        }
}