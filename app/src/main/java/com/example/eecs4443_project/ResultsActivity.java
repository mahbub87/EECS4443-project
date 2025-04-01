package com.example.eecs4443_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultsActivity extends AppCompatActivity {

    private Button ok;
    private TextView timetext, errortext;
    private long time, time2;
    private  int error, error2;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.results), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //grab the data
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //String user = bundle.getString("key");
            time = bundle.getLong("time");
            time2 = bundle.getLong("time2");
            error = bundle.getInt("error");
            error2 = bundle.getInt("error2");
        }

        //init
        timetext = findViewById(R.id.resulttimetext);
        errortext = findViewById(R.id.resulterrortext);
        ok = findViewById(R.id.result_ok_button);

        timetext.setText(String.format("Step 1 total time: %dms\nStep 2 total time: %dms",
                time,time2));
        errortext.setText(String.format("Step 1 error rate: %d%%\nStep 2 error rate: %d%%",
                error,error2));
        ok.setOnClickListener(v -> {
            finish();
        });


    }

}
