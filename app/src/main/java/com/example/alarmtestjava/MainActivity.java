package com.example.alarmtestjava;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private TimePicker timePicker;

    static MediaPlayer mediaPlayer;



    private MediaPlayer alarm;
    private TextView textView;
    private int currentPenaltyValue = 0; // 初期のpenaltyValueを設定

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        timePicker = findViewById(R.id.timePicker);
        Log.d("MainActivity", "get timePicker!");

        Button setAlarmButton = findViewById(R.id.setAlarmButton);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm(v);
            }
        });
    }

    public void setAlarm(View view) {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        Log.d("MainActivity", "get time!");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        setAlarmNotification(calendar.getTimeInMillis());
        Log.d("MainActivity", "set!");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Default Channel";
            String description = "Default Channel Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("default_channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setAlarmNotification(long triggerTime) {
        createNotificationChannel(); // 通知チャネルを作成


        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);

        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show();
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