package com.example.smartmed1.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed1.R;
import com.example.smartmed1.Files;
import com.example.smartmed1.model.Doctor;
import com.example.smartmed1.model.ScoreResult;
import com.example.smartmed1.service.NotificationService;
import com.example.smartmed1.service.QuizEngine;

public class ShareFormScreen extends AppCompatActivity {
    private static final String EXTRA_RESULT_ID = "extra_result_id";

    private QuizEngine quizEngine;
    private EditText edtAmka;
    private RadioGroup rgPerms;
    private int resultId;

    // Carry the timeframe through
    private long startTimestamp;
    private long endTimestamp;

    public static void start(Context ctx,
                             int resultId,
                             long startTimestamp,
                             long endTimestamp) {
        Intent i = new Intent(ctx, ShareFormScreen.class);
        i.putExtra(EXTRA_RESULT_ID, resultId);
        i.putExtra("extra_start_ts", startTimestamp);
        i.putExtra("extra_end_ts",   endTimestamp);
        ctx.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareformscreen);

        quizEngine = new QuizEngine(this);

        // 1) Pull out your extras
        resultId        = getIntent().getIntExtra(EXTRA_RESULT_ID, -1);
        startTimestamp  = getIntent().getLongExtra("extra_start_ts", 0L);
        endTimestamp    = getIntent().getLongExtra("extra_end_ts",   0L);

        edtAmka = findViewById(R.id.edtDoctorAmka);
        rgPerms = findViewById(R.id.rgPermissions);
        Button btnSend = findViewById(R.id.btnSendShare);

        btnSend.setOnClickListener(v -> {
            // 2) Validate AMKA
            String amka = edtAmka.getText().toString().trim();
            Doctor doc = quizEngine.validateDoctorAMKA(amka);
            if (doc == null) {
                startActivity(new Intent(this, InvalidAMKAScreen.class));
                return;
            }

            // 3) Send the access link (stub + trend/PDF flow)
            boolean linkSent = new NotificationService(this)
                    .sendAccessLink(
                            resultId,
                            doc.getId(),
                            doc.getEmail(),
                            startTimestamp,
                            endTimestamp
                    );

            if (!linkSent) {
                Toast.makeText(this,
                        "Σφάλμα κατά την αποστολή συνδέσμου. Προσπαθήστε ξανά.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // 4) Re-calculate current ScoreResult
            ScoreResult result = quizEngine.getResultById(resultId);

            // 5) Export & share PDF for the same timeframe
            Files.exportAndShareResults(
                    this,
                    result,
                    doc.getEmail(),
                    startTimestamp,
                    endTimestamp
            );

            // 6) Let the user know
            Toast.makeText(this,
                    "Ο σύνδεσμος στάλθηκε. Ετοιμάζεται το email με το PDF…",
                    Toast.LENGTH_LONG).show();
        });
    }
}
