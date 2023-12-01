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

public class MainActivity extends AppCompatActivity {

    private MediaPlayer alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //alarm = MediaPlayer.create(this,R.raw.alarm);
        alarm = MediaPlayer.create(this, soundResourceID(2));

        // Spinnerを取得
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);

        // 選択肢のリストを作成
        List<Integer> options1 = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            options1.add(i);
        }
        List<Integer> options2 = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            options2.add(i);
        }

        // ArrayAdapterを使用してSpinnerに選択肢を追加
        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options1);
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options2);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);

        // Spinner1の選択イベントを処理
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 選択されたアイテムの処理
                int selectedOption = options1.get(position);
                Toast.makeText(MainActivity.this, "選択されたオプション: " + selectedOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 何も選択されなかった場合の処理
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 選択されたアイテムの処理
                int selectedOption = options2.get(position);
                Toast.makeText(MainActivity.this, "選択されたオプション: " + selectedOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 何も選択されなかった場合の処理
            }
        });


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



}