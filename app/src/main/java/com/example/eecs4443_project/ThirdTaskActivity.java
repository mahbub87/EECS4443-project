package com.example.eecs4443_project;

import static java.lang.Character.toLowerCase;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ThirdTaskActivity extends AppCompatActivity {
    private static final int VOICE_PERMISSION = 100;
    private Button speak;
    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_third_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.third), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speak = findViewById(R.id.thirdtasksteponebutton);
        view = findViewById(R.id.textViewsteponethird);

        speak.setOnClickListener(v ->{
            speak();
        });
    }

    public void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speak");
        startActivityForResult(intent, VOICE_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == VOICE_PERMISSION && resultCode == RESULT_OK){
            //textView.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
            String results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            if (results != null && !results.isEmpty()){
                try{
                    double resultW = calculate(results);
                    Log.e("MYDEBUG", "onActivityResult: " + resultW);
                    view.setText("" + resultW);

                }catch (Exception e){
                    //couldnt calculate txt
                }
            }

        }
    }

    private double calculate(String  input) throws Exception{
        Log.e("MYDEBUG", "calculate: CALLED AND THE INPUT IS " + input);
        input = input.toLowerCase();
        String[] words = input.split("\\s+");
        double num1 = 0, num2 = 0;
        String operator = "";

        for (String word : words) {
            if (word.matches("-?\\d+(\\.\\d+)?")) { // Check if it's a number
                if (operator.isEmpty()) {
                    num1 = Double.parseDouble(word);
                } else {
                    num2 = Double.parseDouble(word);
                }
            } else if (word.equals("plus") ||  word.equals("add") || word.equals("+") ) {
                operator = "+";
            } else if (word.equals("minus") || word.equals("subtract") || word.equals("-")) {
                operator = "-";
            } else if (word.equals("times") || word.equals("multiply") || word.equals("*")) {
                operator = "*";
            } else if (word.equals("divide") || word.equals("over") || word.equals("/")) {
                operator = "/";
            }
        }
        Log.e("MYDEBUG", "calculate: CALLED AND THE NUM 1 is" + num1 +"while NUM 2 is" + num2 + "and operator is" + operator);
        // Perform calculation using if-else
        if (operator.equals("+")) {
            return num1 + num2;
        } else if (operator.equals("-")) {
            return num1 - num2;
        } else if (operator.equals("*")) {
            return num1 * num2;
        } else if (operator.equals("/")) {
            if (num2 != 0) {
                return num1 / num2;
            } else {
                throw new Exception("Cannot divide by zero");
            }
        } else {
            throw new Exception("Invalid operator");
        }
    }
    }

