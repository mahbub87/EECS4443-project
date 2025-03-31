package com.example.eecs4443_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class FirstTaskActivity extends AppCompatActivity {
    private static final int VOICE_PERMISSION = 100;


    private TextView textView;
    Stopwatch
    private Button speak, confirm, tryagain;
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
        speak = findViewById(R.id.buttonTest);

        //when clicked opens microphone intent
        speak.setOnClickListener(v ->{
            speak();
        });

        //
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
                //two buttons, confirm or try again

                searchWeather(query);
            }

        }
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
