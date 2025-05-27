package com.example.smartmed1.ui;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartmed1.R;

public class InvalidAMKAScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invalid_amkascreen);

        Button btnRetry = findViewById(R.id.btnRetryAmka);
        btnRetry.setOnClickListener(v -> {

            finish();
        });
    }
}
