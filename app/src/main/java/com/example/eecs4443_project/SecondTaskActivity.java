package com.example.eecs4443_project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SecondTaskActivity extends AppCompatActivity {
    private static final int VOICE_PERMISSION = 100;
    private TextView textView;
    private Button speak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second_task);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.alarm_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textViewAlarm);
        speak = findViewById(R.id.buttonSetAlarm);

        speak.setOnClickListener(v -> speak());
    }

    public void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say time to set alarm (e.g., 'Set alarm for 7:30 AM')");
        startActivityForResult(intent, VOICE_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_PERMISSION && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String command = result.get(0).toLowerCase();
                textView.setText(command);
                setAlarm(command);
            }
        }
    }

    private void setAlarm(String command) {
        String[] words = command.split(" ");
        int hour = -1, minute = -1;
        boolean isPM = false;

        for (String word : words) {
            if (word.contains(":")) {
                String[] timeParts = word.split(":");
                try {
                    hour = Integer.parseInt(timeParts[0]);
                    minute = Integer.parseInt(timeParts[1]);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (word.equals("p.m.") || word.equals("pm")) {
                isPM = true;
            }
        }

        if (hour != -1 && minute != -1) {
            if (isPM && hour < 12) {
                hour += 12;
            } else if (!isPM && hour == 12) {
                hour = 0;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(this, "Alarm set for " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Could not parse time", Toast.LENGTH_SHORT).show();
        }
    }
}
