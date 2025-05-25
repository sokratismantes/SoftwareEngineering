package com.example.smartmed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed.R;

public class UserChoices extends AppCompatActivity {

    Button btnHealthData, btnHealthTrends, btnHealthGoals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_choices);

        btnHealthData = findViewById(R.id.btnHealthData);
        btnHealthTrends = findViewById(R.id.btnHealthTrends);
        btnHealthGoals = findViewById(R.id.btnHealthGoals);

        btnHealthData.setOnClickListener(v -> {
            Intent intent = new Intent(UserChoices.this, AddHealthData.class);
            startActivity(intent);
        });

        btnHealthTrends.setOnClickListener(v -> {
            Intent intent = new Intent(UserChoices.this, HealthTrendsScreen.class);
            startActivity(intent);
        });

    }
}
