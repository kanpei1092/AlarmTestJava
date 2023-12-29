package com.example.alarmtestjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Calculation extends AppCompatActivity {

    private TextView questionTextView;
    private EditText answerEditText;
    private int correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculation);

        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);
        Button submitButton = findViewById(R.id.submitAnswerButton);

        generateNewQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    private void generateNewQuestion() {
        Random random = new Random();
        int a = random.nextInt(10);
        int b = random.nextInt(10);
        correctAnswer = a + b;
        questionTextView.setText(a + " + " + b + " = ?");
    }

    private void checkAnswer() {
        try {
            int userAnswer = Integer.parseInt(answerEditText.getText().toString());
            if (userAnswer == correctAnswer) {
                Toast.makeText(Calculation.this, "Correct!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(Calculation.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                generateNewQuestion();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(Calculation.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }

        // ユーザー入力欄をリセット
        answerEditText.setText("");
    }
}
