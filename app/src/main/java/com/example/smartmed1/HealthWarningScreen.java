package com.example.smartmed1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class HealthWarningScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_warning_screen);

        TextView warningMessage = findViewById(R.id.warningText);
        String message = getIntent().getStringExtra("message");
        warningMessage.setText(message);
    }
}
