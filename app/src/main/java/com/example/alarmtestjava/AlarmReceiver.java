package com.example.alarmtestjava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Alarm received!");
        //MainActivity.alarm = MediaPlayer.create(context, R.raw.alarm);
        MainActivity.alarm = MediaPlayer.create(context, MainActivity.soundResourceID(MainActivity.currentPenaltyValue));
        //MainActivity.alarm.setVolume((float)0.2, (float)0.2);


        int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);//分
        int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);//時

        if(MainActivity.hour == nowHour && MainActivity.minute == nowMinute) {
            // MediaPlayerを作成して音楽を再生
            MainActivity.startMusic();
            Intent serviceIntent = new Intent(context, MusicService.class);
            context.startService(serviceIntent);
            //MainActivity.alarm.start();
            Log.d("AlarmReceiver", "Alarm start!");
        }


        // アラームが鳴ったときに行いたい処理を追加できます


        // アプリケーションがバックグラウンドで動作している場合、
        // メディアプレーヤーのリソースを解放することを忘れないでください。
        MainActivity.alarm.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
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

    public void soundVolume(int penaltyValue, MediaPlayer alarm) {
        if(penaltyValue <= 0) {
            alarm.setVolume(1, 1);
        }else if(penaltyValue > 0 && penaltyValue <= 1){
        }
    }


}
