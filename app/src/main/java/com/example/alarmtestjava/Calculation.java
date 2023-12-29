package com.example.alarmtestjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Calculation extends AppCompatActivity {

    private TextView questionTextView;
    private EditText answerEditText;
    private Button submitButton;
    private int correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculation);

        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);
        submitButton = findViewById(R.id.submitAnswerButton);

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
        int userAnswer = Integer.parseInt(answerEditText.getText().toString());
        if (userAnswer == correctAnswer) {
            Toast.makeText(Calculation.this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Calculation.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
        }
        generateNewQuestion();
    }
}