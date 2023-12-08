package com.example.alarmtestjava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Alarm received!");
        showNotification(context, "Alarm", "Wake up! It's time!");

        // ここでアラームが鳴ったときに行いたい処理を追加できます
        MediaPlayer alarm = MediaPlayer.create(this, R.raw.alarm);
        alarm.start();
    }

    private void showNotification(Context context, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default_channel")
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(null, true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(1, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
            // パーミッションがない場合の処理をここに追加
            // 例: ユーザーにパーミッションの許可を求めるダイアログを表示する、エラーメッセージを表示するなど
        }
    }
}
