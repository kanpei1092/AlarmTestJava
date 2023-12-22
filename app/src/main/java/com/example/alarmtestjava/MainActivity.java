package com.example.alarmtestjava;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
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
import android.util.Log;
import android.media.MediaPlayer;
import android.view.View;
import android.os.SystemClock;
import android.widget.TextView;
import android.nfc.NfcAdapter;
import android.content.Intent;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static MediaPlayer alarm;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView textView;
    private static int currentPenaltyValue = 0; // 初期のpenaltyValueを設定
    private static long startTime;
    private long stopTime;

    private AlarmManager alarmManager;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        timePicker = findViewById(R.id.timePicker);
        Log.d("MainActivity", "get timePicker!");

        //textView = findViewById(R.id.textView);

        /* NFCの初期化 */
        {
            // NFCアダプタの初期化
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);

            //NFCが利用可能か表示
            if (nfcAdapter == null) {
                Toast.makeText(this, "NFCは利用できません", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "NFCは利用できます", Toast.LENGTH_LONG).show();
                // スキャンされたときにタグの詳細情報でPendingIntentオブジェクトを準備するようにAndroidシステムに指示する
                pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);

            }
        }

        /* ボタンでアラームセット　*/
        {
            Button setAlarmButton = findViewById(R.id.setAlarmButton);
            setAlarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAlarm(v);
                }
            });
        }
    }

    // 適切な soundResourceID を設定
    public static int soundResourceID(int penaltyValue) {
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

    //時間設定
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



    private void doPenalty(int penalty) {
        switch (penalty) {
            case 1:
                // penaltyが1の場合の処理
                Toast.makeText(this, "Penalty 1", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                // penaltyが2の場合の処理
                Toast.makeText(this, "Penalty 2", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                // penaltyが3の場合の処理
                Toast.makeText(this, "Penalty 3", Toast.LENGTH_SHORT).show();
                break;
            default:
                // どのcaseにも当てはまらない場合の処理
                Toast.makeText(this, "Default Penalty", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    static void startMusic(){
        if (alarm == null) {
            alarm.release();
        }
        alarm.start();
        // 開始時刻を取得
        startTime = SystemClock.elapsedRealtime();
    }



    /* ストップボタン */
    public void onStop (View view) {
        wakeUp();
    }

    /* 起床判定メソッド */
    public void wakeUp(){
        if (alarm != null && alarm.isPlaying()) {
            Toast.makeText(this, "おはようございます", Toast.LENGTH_LONG).show();
            alarm.stop();

            stopTime = SystemClock.elapsedRealtime(); // 停止した時刻を取得
            long elapsedTime = stopTime - startTime; // 経過時間を計算
            long elapsedSeconds = elapsedTime / 1000; // 経過時間を秒単位で表示

            // elapsedSecondsに基づいてpenaltyValueを更新
            updatePenaltyValue(elapsedSeconds);
            Toast.makeText(this, String.valueOf(elapsedSeconds) + "秒経ちました！" + "\nペナルティ値は"+String.valueOf(getCurrentPenaltyValue())+"です！", Toast.LENGTH_LONG).show();
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

    /* 現在の（フォアグラウンド）アクティビティがNFCの意図を傍受し、アプリ内と他のアプリの両方で他のすべてのアクティビティよりも優先権を主張できるようにする */
    @Override
    protected void onResume() {
        super.onResume();
        // NFCアダプタがnullでない場合のみ、Foreground Dispatchを有効化
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // NFCアダプタがnullでない場合のみ、Foreground Dispatchを無効化
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        wakeUp();
    }
}