// NoAnswerErrorScreen.java
package com.example.smartmed1.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smartmed1.R;

public class NoAnswerErrorScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_answer_error_screen);
        Button btnRetry = findViewById(R.id.btnRetryQuiz);
        btnRetry.setOnClickListener(v -> finish());
    }
}
