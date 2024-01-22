package com.example.alarmtestjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;


/*penalty
        ~20 何もなし
        21~31
        ...
        ~99
        で四則演算ランダム。*/

public class Calculation extends AppCompatActivity {

    private TextView questionTextView;
    private EditText answerEditText;
    private String correctAnswer;
    private int penalty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculation);

        penalty = MainActivity.getCurrentPenaltyValue();

        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);
        answerEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        Button submitButton = findViewById(R.id.submitAnswerButton);

        inflictPenalty();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    private void inflictPenalty(){
        int level = penalty/10;
        generateNewQuestion(level);
    }

    private void generateNewQuestion(int level) {
        switch (level) {
            case 1:
            case 2: level1(); break;
            case 3:
            case 4: level2(); break;
            case 5: level3(); break;
            case 6: level4(); break;
            case 7:
            case 8: level5();break;
            case 9:
            default: generateDefaultCalculation(); break;
        }
    }
    private void level1() {
        // 簡単な加減算
        Random random = new Random();
        int a = random.nextInt(20);  // 0から19までの数字
        int b = random.nextInt(20);
        boolean add = random.nextBoolean();  // true で加算、false で減算

        correctAnswer = String.valueOf(add ? a + b : a - b);
        questionTextView.setText(a + (add ? " + " : " - ") + b + " = ?");
    }

    private void level2() {
        // 乗算の問題を生成
        Random random = new Random();
        int a = random.nextInt(10);  // 0から9までの数字
        int b = random.nextInt(10);
        correctAnswer = String.valueOf(a * b);
        questionTextView.setText(a + " * " + b + " = ?");
    }

    private void level3() {
        // 除算の問題を生成
        Random random = new Random();
        int a = random.nextInt(20) + 1;  // 1から20までの数字
        int b = random.nextInt(9) + 1;    // 1から9までの数字
        BigDecimal bdA = new BigDecimal(a);
        BigDecimal bdB = new BigDecimal(b);
        BigDecimal result = bdA.divide(bdB, 2, RoundingMode.HALF_UP).stripTrailingZeros();
        correctAnswer = result.toPlainString();
        questionTextView.setText(a + " / " + b + " = ?");
    }

    private void level4() {
        Random random = new Random();
        int a = random.nextInt(19) + 1;  // 1から19までの数字
        int b = random.nextInt(19) + 1;  // 1から19までの数字
        int c = random.nextInt(19) + 1;  // 1から19までの数字
        String[] operations = {"+", "-", "*", "/"};
        String operation1 = operations[random.nextInt(operations.length)];
        String operation2 = operations[random.nextInt(operations.length)];

        BigDecimal bdA = new BigDecimal(a);
        BigDecimal bdB = new BigDecimal(b);
        BigDecimal bdC = new BigDecimal(c);
        BigDecimal result;

        // 計算の実行

        // 最初の演算
        if (operation1.equals("+")) {
            result = bdA.add(bdB);
        } else if (operation1.equals("-")) {
            result = bdA.subtract(bdB);
        } else if (operation1.equals("*")) {
            result = bdA.multiply(bdB);
        } else { // "/"
            result = bdA.divide(bdB, 2, RoundingMode.HALF_UP);
        }

        // 二番目の演算
        if (operation2.equals("+")) {
            result = result.add(bdC);
        } else if (operation2.equals("-")) {
            result = result.subtract(bdC);
        } else if (operation2.equals("*")) {
            result = result.multiply(bdC);
        } else { // "/"
            result = result.divide(bdC, 2, RoundingMode.HALF_UP);
        }

        correctAnswer = result.stripTrailingZeros().toPlainString();
        questionTextView.setText(a + " " + operation1 + " " + b + " " + operation2 + " " + c + " = ?");
    }

    private void level5() {
        Random random = new Random();
        int a = random.nextInt(90) + 10;  // 10から99までの数字
        int b = random.nextInt(90) + 10;
        int c = random.nextInt(90) + 10;
        int d = random.nextInt(90) + 10;

        String[] multDivOperations = {"*", "/"}; // 乗除
        String[] addSubMultDivOperations = {"+", "-", "*", "/"}; // 加減乗除
        String[] addSubOperations = {"+", "-"}; // 加減

        String operation1 = multDivOperations[random.nextInt(multDivOperations.length)];
        String operation2 = addSubMultDivOperations[random.nextInt(addSubMultDivOperations.length)];
        String operation3 = addSubOperations[random.nextInt(addSubOperations.length)];

        BigDecimal bdA = new BigDecimal(a);
        BigDecimal bdB = new BigDecimal(b);
        BigDecimal bdC = new BigDecimal(c);
        BigDecimal bdD = new BigDecimal(d);
        BigDecimal result;

        // 最初の演算
        result = performOperation(bdA, bdB, operation1);

        // 二番目の演算
        result = performOperation(result, bdC, operation2);

        // 三番目の演算
        result = performOperation(result, bdD, operation3);

        correctAnswer = result.stripTrailingZeros().toPlainString();
        questionTextView.setText(a + " " + operation1 + " " + b + " " + operation2 + " " + c + " " + operation3 + " " + d + " = ?");
    }

    private BigDecimal performOperation(BigDecimal operand1, BigDecimal operand2, String operation) {
        switch (operation) {
            case "+":
                return operand1.add(operand2);
            case "-":
                return operand1.subtract(operand2);
            case "*":
                return operand1.multiply(operand2);
            case "/":
                // 除算の場合、ゼロ除算を避ける
                return operand2.compareTo(BigDecimal.ZERO) != 0 ? operand1.divide(operand2, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            default:
                return BigDecimal.ZERO;  // デフォルトの場合
        }
    }

    private void generateDefaultCalculation() {
        Random random = new Random();
        int a = random.nextInt(10);  // 0から9までの数字
        int b = random.nextInt(10);
        correctAnswer = String.valueOf(a + b);
        questionTextView.setText(a + " + " + b + " = ?");
    }

    private void checkAnswer() {
        try {
            String userAnswer = answerEditText.getText().toString();
            if (userAnswer.equals(correctAnswer)) {
                Toast.makeText(Calculation.this, "Correct!", Toast.LENGTH_SHORT).show();
                Intent serviceIntent = new Intent(this, MusicService.class);
                stopService(serviceIntent);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(Calculation.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                inflictPenalty();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(Calculation.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }

        // ユーザー入力欄をリセット
        answerEditText.setText("");
    }
}
