package com.example.alarmtestjava;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import java.util.Calendar;
import android.widget.Button;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity{

    private MediaPlayer alarm;
    private static final String CHANNEL_ID = "alarm_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_PERMISSION_CODE = 123; // 任意の値
    private int selectedHour;
    private int selectedMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 時のSpinnerを取得
        Spinner hourSpinner = findViewById(R.id.hourSpinner);
        // 分のSpinnerを取得
        Spinner minuteSpinner = findViewById(R.id.minuteSpinner);

        Button setAlarmButton = findViewById(R.id.setAlarmButton);

        // 時の選択肢のリストを作成
        List<Integer> hourOptions = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourOptions.add(i);
        }

        // 分の選択肢のリストを作成
        List<Integer> minuteOptions = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minuteOptions.add(i);
        }

        // ArrayAdapterを使用して時のSpinnerに選択肢を追加
        ArrayAdapter<Integer> hourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hourOptions);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        // ArrayAdapterを使用して分のSpinnerに選択肢を追加
        ArrayAdapter<Integer> minuteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, minuteOptions);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(minuteAdapter);

        // Spinnerの選択イベントを処理
        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 時と分の選択されたアイテムの処理
                selectedHour = (int) hourSpinner.getSelectedItem();
                selectedMinute = (int) minuteSpinner.getSelectedItem();
                Toast.makeText(MainActivity.this, "選択された時刻: " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 何も選択されなかった場合の処理
            }
        };

        // 時と分のSpinnerの選択イベントを設定
        hourSpinner.setOnItemSelectedListener(onItemSelectedListener);
        minuteSpinner.setOnItemSelectedListener(onItemSelectedListener);

        // ボタンがクリックされたときの処理を設定
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHour = (int) hourSpinner.getSelectedItem();
                selectedMinute = (int) minuteSpinner.getSelectedItem();
                Toast.makeText(MainActivity.this, "アラームを設定: " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();

                // 権限をリクエスト
                requestExactAlarmPermission();
            }
        });
    }

    private void requestExactAlarmPermission() {
        // 権限が既に付与されているか確認
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_EXACT_ALARM)
                == PackageManager.PERMISSION_GRANTED) {
            // 権限が既に付与されている場合はアラームを設定
            setAlarm();
        } else {
            // 権限をリクエスト
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.USE_EXACT_ALARM},
                    REQUEST_PERMISSION_CODE);
        }
    }

    // 権限リクエスト結果を受け取る
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 権限が付与された場合はアラームを設定
                setAlarm();
            } else {
                Toast.makeText(this, "アラームを設定するためには権限が必要です。", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setAlarm() {
        // アラームを設定するコード
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);

        Calendar now = Calendar.getInstance();

        // アラームのトリガー時刻が過去の場合は翌日に設定
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Android 12以降の新しいバックグラウンド制約に対応
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                handleExactAlarmSchedulingError();
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    // アプリの制約に合わせて適切な処理を行う
    private void handleExactAlarmSchedulingError() {
        // エラー処理のためのコードを追加
        // 例: Toastでユーザーにメッセージを表示するなど
        Toast.makeText(this, "アラームを設定できませんでした。", Toast.LENGTH_SHORT).show();
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

    private void createNotification(Context context, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }



}