package com.example.eecs4443_project;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Dialog dialog;
    private Button firstButton, secondButton, thirdButton, dialogButton;
private Boolean firstTaskComplete, secondTaskComplete, thirdTaskComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initialize
        firstTaskComplete = false;
        secondTaskComplete = false;
        thirdTaskComplete = false;
        firstButton = findViewById(R.id.firstTaskButton);
        secondButton = findViewById(R.id.secondTaskButton);
        thirdButton = findViewById(R.id.thirdTaskButton);


        //dialog
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.welcome_page);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogButton = dialog.findViewById(R.id.welcome_ok_button);
        dialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        //when buttons clicked
        firstButton.setOnClickListener(v -> {
            Intent firstIntent = new Intent(this, FirstTaskActivity.class);
            startActivity(firstIntent);
        });
        secondButton.setOnClickListener(v -> {
            Intent secondIntent = new Intent(this, SecondTaskActivity.class);
            startActivity(secondIntent);
        });
        thirdButton.setOnClickListener(v -> {
            Intent thirdIntent = new Intent(this, ThirdTaskActivity.class);
            startActivity(thirdIntent);
        });

    }

    /**
     * Method to check if all tasks completed
     * @param first
     * @param second
     * @param third
     * @return
     */
    private Boolean checkTaskComplete(Boolean first, Boolean second, Boolean third){
        if (first && second && third){
            return true;
        }
        return false;
    }


}