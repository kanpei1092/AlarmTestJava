package com.example.alarmtestjava;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;
import android.os.SystemClock;
import android.widget.TextView;
import android.nfc.NfcAdapter;
import android.content.Intent;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer alarm;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    //private long startTime;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm = MediaPlayer.create(this, soundResourceID(2));
        textView = findViewById(R.id.textView);

        // NFCアダプタの初期化
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFCは利用できません", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "NFCは利用できます", Toast.LENGTH_LONG).show();
        }

        // スキャンされたときにタグの詳細情報でPendingIntentオブジェクトを準備するようにAndroidシステムに指示する
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);
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
        Toast.makeText(this, "音を流します", Toast.LENGTH_LONG).show();

        // 開始時刻を取得
        startTime = SystemClock.elapsedRealtime();
    }

    /* ストップボタン */
    public void onStop (View view) {
        alarm.stop();
        // 停止した時刻を取得
        stopTime = SystemClock.elapsedRealtime();

        // 経過時間を計算
        long elapsedTime = stopTime - startTime;

        // 経過時間を秒単位で表示
        long elapsedSeconds = elapsedTime / 1000;

        textView.setText(String.valueOf(elapsedSeconds)+"秒経ちました！");
    }

    /* 起床判定メソッド */
    public void wakeUp(){
        Toast.makeText(this, "おはようございます", Toast.LENGTH_LONG).show();
        alarm.stop();
    }



    /* 現在の（フォアグラウンド）アクティビティがNFCの意図を傍受し、アプリ内と他のアプリの両方で他のすべてのアクティビティよりも優先権を主張できるようにする */
    @Override
    protected void onResume() {
        super.onResume();
        assert nfcAdapter != null;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    protected void onPause() {
        super.onPause();
        //On pause stop listening
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