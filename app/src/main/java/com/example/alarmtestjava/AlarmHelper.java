package com.example.alarmtestjava;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmHelper {
    public static void setAlarmAtTime(Context context, int hourOfDay, int minute) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 現在の日付と指定された時間と分を使用して Calendar インスタンスを作成
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // 指定された時間を過ぎていた場合は翌日に設定
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // アラームが発動するまでのミリ秒
        long triggerTimeMillis = calendar.getTimeInMillis();

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // アラームを設定
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
    }
}

