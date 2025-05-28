package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed1.ui.QuestionScreen;

public class HelpAndSupportUser extends AppCompatActivity {

    Button btnQuestionForm, btnFeedback, btnMentalForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_support);
        btnQuestionForm = findViewById(R.id.btnQuestionForm);
        btnFeedback = findViewById(R.id.btnFeedback);
        btnMentalForm = findViewById(R.id.btnMentalForm);

        btnQuestionForm.setOnClickListener(v -> {
            startActivity(new Intent(this, SupportQuestions.class));
        });


        btnFeedback.setOnClickListener(v -> {
            // startActivity(new Intent(this, FeedbackScreen.class));
        });

        btnMentalForm.setOnClickListener(v -> {
            startActivity(new Intent(HelpAndSupportUser.this, QuestionScreen.class));
        });

    }
}
