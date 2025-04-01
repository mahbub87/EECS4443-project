package com.example.eecs4443_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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

import java.util.ArrayList;

public class FirstTaskActivity extends AppCompatActivity {
    private static final int VOICE_PERMISSION = 100;


    private ImageView check, check2;
    private TextView textView;
    StopWatch stopWatch = new StopWatch();
    private int tryagain, errorPercent, errorPercent2,  tryagain2;
    private long time, time2;
    private String activationWord = "Hey Guide!";
    private Button confirm, confirm2, next;
    private ImageButton speak, speak2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.first), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initializes
        textView = findViewById(R.id.textViewTEST);
        speak = findViewById(R.id.fs1_speak_button);
        speak2 = findViewById(R.id.fs2_speak_button);
        confirm = findViewById(R.id.fs1_confirm_button);
        confirm2 = findViewById(R.id.fs2_confirm_button);
        next = findViewById(R.id.f_next_button);
        check = findViewById(R.id.fs1_check);
        check2 = findViewById(R.id.fs2_check);
        tryagain = 0;
        tryagain2 = 0;
        errorPercent = 0;
        errorPercent2 = 0;
        time = 0;
        time2 = 0;

        //turn the buttons invisible first
        //myButton.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.INVISIBLE);
        confirm2.setVisibility(View.INVISIBLE);
        check.setVisibility(View.INVISIBLE);
        check2.setVisibility(View.INVISIBLE);

        //when clicked opens microphone intent
        speak.setOnClickListener(v ->{
            tryagain++; // tracks button click
            speak();
            stopWatch.reset();
            stopWatch.start();
            confirm.setVisibility(View.VISIBLE);
        });

        speak2.setOnClickListener(v ->{
            activationWord = "weather";
            tryagain2++; // tracks button click
            speak();
            stopWatch.reset();
            stopWatch.start();
            confirm2.setVisibility(View.VISIBLE);
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
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say what's the weather in New York?");
        startActivityForResult(intent, VOICE_PERMISSION);
    }

    /**
     * method that grabs microphone input and sets text
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == VOICE_PERMISSION && resultCode == RESULT_OK){
            //textView.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()){
                String query = results.get(0);
                //do a dialogue pop up here
                textView.setText("You said: " + query);
                withActivationWord(query);

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
                        stopWatch.stop();
                        time2 = stopWatch.getTime();
                        check2.setVisibility(View.VISIBLE);
                    }

                });

            }

        }
    }

    /**
     * method to check with the activation word
     * @param query
     */
    private void withActivationWord(String query) {
        // Check if the query contains the activation word
        if (query.toLowerCase().contains(activationWord)) {
          //open web browser
            int startIndex = query.indexOf(activationWord) + activationWord.length();

            // Extract everything after the activation word
            String command = query.substring(startIndex).trim();
            searchWeather(command);
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

    /**
     * method that opens web browser to look for weather + user input on google
     * @param query
     */
    private void searchWeather(String query){
        String searchurl = "https://www.google.com/search?q=weather+" + query;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchurl));
        startActivity(intent);
    }

}
