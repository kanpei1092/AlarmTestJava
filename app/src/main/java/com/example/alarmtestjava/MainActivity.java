package com.example.alarmtestjava;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;
import android.os.SystemClock;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer alarm;
    private TextView textView;
    private int currentPenaltyValue = 0; // 初期のpenaltyValueを設定

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //alarm = MediaPlayer.create(this,R.raw.alarm);
        //alarm = MediaPlayer.create(this, soundResourceID(2));
        textView = findViewById(R.id.textView);

    }


    public static int soundResourceID(int penaltyValue) {

        // 適切な soundResourceID を設定
        if (penaltyValue > 0){
            return R.raw.alarm1;
        } else{
            return R.raw.alarm;
        }
    }

    // penaltyValueを取得する関数
    public int getCurrentPenaltyValue() {
        return currentPenaltyValue;
    }

    // elapsedSecondsの値によってpenaltyValueを変化させる関数
    public void updatePenaltyValue(long elapsedSeconds) {
        if (elapsedSeconds >= 10) {
            currentPenaltyValue = currentPenaltyValue + 10; // 10秒以上経過した場合、penaltyValueを1に変更
            alarm = MediaPlayer.create(this, soundResourceID(currentPenaltyValue)); // MediaPlayerを更新
        }
        // 他にも条件があれば追加
        if (elapsedSeconds < 10) {
            currentPenaltyValue = currentPenaltyValue - 10; // 10秒以上経過した場合、penaltyValueを1に変更
            alarm = MediaPlayer.create(this, soundResourceID(currentPenaltyValue)); // MediaPlayerを更新
        }
    }
    private long startTime;
    private long stopTime;

        /* スタートボタン */
        public void onStart (View view){
            if (alarm != null) {
                alarm.release();
            }
            alarm = MediaPlayer.create(this, soundResourceID(currentPenaltyValue)); // 初期のpenaltyValueを使用してMediaPlayerを作成

            alarm.start();

            // 開始時刻を取得
            startTime = SystemClock.elapsedRealtime();
        }

        /* ストップボタン */
        public void onStop (View view) {
            if (alarm != null && alarm.isPlaying()) {
                alarm.stop();

                // 停止した時刻を取得
                stopTime = SystemClock.elapsedRealtime();

                // 経過時間を計算
                long elapsedTime = stopTime - startTime;

                // 経過時間を秒単位で表示
                long elapsedSeconds = elapsedTime / 1000;

                // elapsedSecondsに基づいてpenaltyValueを更新
                updatePenaltyValue(elapsedSeconds);

                textView.setText(String.valueOf(elapsedSeconds) + "秒経ちました！" +
                        "ペナルティ値は"+String.valueOf(getCurrentPenaltyValue())+"です！");
            }
        }
    @Override
    protected void onDestroy() {
        // Release MediaPlayer resources when the activity is destroyed
        if (alarm != null) {
            alarm.release();
        }
        super.onDestroy();
    }
}