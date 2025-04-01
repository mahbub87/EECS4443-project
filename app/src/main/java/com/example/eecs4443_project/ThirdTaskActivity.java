package com.example.eecs4443_project;

import static java.lang.Character.toLowerCase;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.util.ArrayList;

public class ThirdTaskActivity extends AppCompatActivity {
    private static final int VOICE_PERMISSION = 100;
    private TextView calcresult, calcresult2;
    private long time, time2;
    private Button confirm, confirm2, next;
    private ImageView check, check2;
    StopWatch stopWatch = new StopWatch();
    StopWatch stopWatch2 = new StopWatch();
    private int tryagain, errorPercent, errorPercent2,  tryagain2, currstep;
    private String activationWord = "Buddy";
    private ImageButton speak, speak2;
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

        //init
        speak = findViewById(R.id.ts1_speak_button);
        speak2 = findViewById(R.id.ts2_speak_button);
        confirm = findViewById(R.id.ts1_confirm_button);
        confirm2 = findViewById(R.id.ts2_confirm_button);
        next = findViewById(R.id.t_next_button);
        check = findViewById(R.id.ts1_check);
        check2 = findViewById(R.id.ts2_check);
        calcresult = findViewById(R.id.calc_result_view);
        calcresult2 = findViewById(R.id.calc_result_view2);
        tryagain = 0;
        tryagain2 = 0;
        errorPercent = 0;
        errorPercent2 = 0;
        time = 0;
        time2 = 0;
        currstep = 0;

        //turn the buttons invisible first
        //myButton.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.INVISIBLE);
        confirm2.setVisibility(View.INVISIBLE);
        check.setVisibility(View.INVISIBLE);
        check2.setVisibility(View.INVISIBLE);

        //when clicked opens microphone intent
        speak.setOnClickListener(v ->{
            currstep = 1;
            tryagain++; // tracks button click
            speak();
            stopWatch.reset();
            stopWatch.start();
            confirm.setVisibility(View.VISIBLE);
        });

        speak2.setOnClickListener(v ->{
            currstep = 2;
            activationWord = "weather";
            tryagain2++; // tracks button click
            speak();
            stopWatch2.reset();
            stopWatch2.start();
            confirm2.setVisibility(View.VISIBLE);
        });

        //confirm button if clicked, stopwtch stopped and method called
        confirm.setOnClickListener(v -> {
            if (tryagain != 0){
                errorPercent = errorPercentage(tryagain);
                stopWatch.stop();
                time = stopWatch.getTime();
                check.setVisibility(View.VISIBLE);
            }

        });



        confirm2.setOnClickListener(v -> {
            if(tryagain2 != 0){
                errorPercent2 = errorPercentage(tryagain2);
                stopWatch2.stop();
                time2 = stopWatch2.getTime();
                check2.setVisibility(View.VISIBLE);
            }

        });

        //if next button clicked
        next.setOnClickListener(v -> {
            if ((tryagain != 0 && tryagain2 != 0)){
                Intent next = new Intent(this, ResultsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("time", time);
                bundle.putLong("time2", time2);
                bundle.putInt("error", errorPercent);
                bundle.putInt("error2", errorPercent2);
                next.putExtras(bundle);
                startActivity(next);
                finish();
            }
        });
    }

    /**
     * method that launches microphone intent
     */
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
                try {
                    withActivationWord(results);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    /**
     * method to check with the activation word
     * @param query
     */
    private void withActivationWord(String query) throws Exception {
        // Check if the query contains the activation word
        if (query.toLowerCase().contains(activationWord.toLowerCase())) {
            //open web browser
            int startIndex = query.indexOf(activationWord) + activationWord.length();

            // Extract everything after the activation word
            String command = query.substring(startIndex).trim();
            try{
               double resultW = calculate(command);
               //set textview view.setText("" + resultW);
                if (currstep == 1){
                    calcresult.setText("" + resultW);
                } else if (currstep == 2) {
                    calcresult2.setText("" + resultW);
                }
            }catch (IOException e){
                //catch
            }

        }
    }

    /**
     * method that calculates error percentage
     * @param tries
     * @return
     */
    private int errorPercentage(int tries){
        int errors = tries - 1;
        int percent = (errors/tries) * 100;
        return percent;
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

