package com.example.alarmtestjava;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        timePicker = findViewById(R.id.timePicker);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ボタンが押されたときの処理

                // MainActivityに戻るためのIntentを作成
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                intent.putExtra("key_data1", hour);
                intent.putExtra("key_data2", minute);

                // 画面遷移を開始
                startActivity(intent);
            }
        });
    }
}