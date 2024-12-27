package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_settings);

                Button buttonChangeTheme = findViewById(R.id.buttonChangeTheme);
                Button buttonClearData = findViewById(R.id.buttonClearData);
                TextView textView=findViewById(R.id.result);

                buttonChangeTheme.setOnClickListener(v -> {
                        textView.setText("\uF0D8 Entering operator before number is not allowed.\n"+
                                "\uF0D8Entering Operator followed by Operator is not allowed.\n"+
                                "\uF0D8Any number division by zero is not allowed.");
                });

                buttonClearData.setOnClickListener(v -> {
textView.setText("");                        // Implement data clearing logic here
                });
        }
}
