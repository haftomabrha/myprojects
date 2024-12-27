package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutDeveloperActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);

        Button buttonContactDeveloper = findViewById(R.id.buttonContactDeveloper);
        TextView textView=findViewById(R.id.email);

textView.setText("haftomabrha452@gmail.com");           // You could open an email intent here or a contact form

}}
