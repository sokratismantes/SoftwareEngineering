// ShareFormScreen.java
package com.example.smartmed1.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smartmed1.R;
import com.example.smartmed1.model.Doctor;
import com.example.smartmed1.service.QuizEngine;
import com.example.smartmed1.Files;
import com.example.smartmed1.model.ScoreResult;

public class ShareFormScreen extends AppCompatActivity {
    private static final String EXTRA_RESULT_ID = "extra_result_id";
    private QuizEngine quizEngine;
    private EditText edtAmka;
    private RadioGroup rgPerms;
    private int resultId;

    public static void start(Context ctx, int resultId) {
        Intent i = new Intent(ctx, ShareFormScreen.class);
        i.putExtra(EXTRA_RESULT_ID, resultId);
        ctx.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_form_screen);
        quizEngine = new QuizEngine(this);
        resultId   = getIntent().getIntExtra(EXTRA_RESULT_ID, -1);

        edtAmka  = findViewById(R.id.edtDoctorAmka);
        rgPerms  = findViewById(R.id.rgPermissions);
        Button btnSend = findViewById(R.id.btnSendShare);

        btnSend.setOnClickListener(v -> {
            String amka = edtAmka.getText().toString();
            Doctor doc = quizEngine.validateDoctorAMKA(amka);
            if (doc == null) {
                startActivity(new Intent(this, InvalidAMKAScreen.class));
                return;
            }
            ScoreResult result = quizEngine.getResultById(resultId);
            Files.exportAndShareResults(this, result, doc.getEmail());

            Toast.makeText(this,
          "Ετοιμάζεται το e-mail με τα αποτελέσματα…",
            Toast.LENGTH_SHORT).show();
        });
    }
}
