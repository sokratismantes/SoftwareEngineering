// ShareFormScreen.java
package com.example.smartmed1.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartmed1.R;
import com.example.smartmed1.model.Doctor;
import com.example.smartmed1.service.QuizEngine;
import com.example.smartmed1.service.NotificationService;

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

        resultId   = getIntent().getIntExtra(EXTRA_RESULT_ID, -1);
        quizEngine = new QuizEngine();

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
            new NotificationService()
                    .sendAccessLink(resultId, doc.getId(), doc.getEmail());
            // TODO: show success UI (Toast or finish())
        });
    }
}
