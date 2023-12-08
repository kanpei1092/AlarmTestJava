package com.example.alarmtestjava;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;
import android.nfc.NfcAdapter;
import android.content.Intent;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer alarm;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    /* スタートボタン */
    public void onStart(View view) {
        alarm.start();
        Toast.makeText(this, "音を流します", Toast.LENGTH_LONG).show();

    }

    /* ストップボタン */
    public void onStop(View view) {
        alarm.stop();
    }

    /* 起床判定メソッド */
    public void wakeUp(){
        alarm.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm = MediaPlayer.create(this, R.raw.alarm);

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

    /* 現在の（フォアグラウンド）アクティビティがNFCの意図を傍受し、アプリ内と他のアプリの両方で他のすべてのアクティビティよりも優先権を主張できるようにする */
    @Override
    protected void onResume() {
        super.onResume();
        assert nfcAdapter != null;
        //nfcAdapter.enableForegroundDispatch(context,pendingIntent,
        //                                    intentFilterArray,
        //                                    techListsArray)
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
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if ( !(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action))) {
            Toast.makeText(this, "if", Toast.LENGTH_LONG).show();
            wakeUp();
        } else {
            Toast.makeText(this, "何かエラーあるよ", Toast.LENGTH_LONG).show();
        }
    }
}