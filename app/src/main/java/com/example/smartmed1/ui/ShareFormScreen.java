package com.example.smartmed1.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.smartmed1.R;
import com.example.smartmed1.Files;
import com.example.smartmed1.model.Doctor;
import com.example.smartmed1.model.ScoreResult;
import com.example.smartmed1.service.NotificationService;
import com.example.smartmed1.service.QuizEngine;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ShareFormScreen extends AppCompatActivity {
    private static final String EXTRA_RESULT_ID = "extra_result_id";

    private QuizEngine quizEngine;
    private EditText edtAmka;
    private RadioGroup rgPerms;
    private int resultId;

    // Carry the timeframe through
    private long startTimestamp;
    private long endTimestamp;
    private static final String EXTRA_PDF_PATH    = "extra_pdf_path";


    public static void start(Context ctx,
                             int resultId,
                             long startTimestamp,
                             long endTimestamp,
                             String pdfPath) {
        Intent i = new Intent(ctx, ShareFormScreen.class);
        i.putExtra(EXTRA_RESULT_ID,    resultId);
        i.putExtra("extra_start_ts",   startTimestamp);
        i.putExtra("extra_end_ts",     endTimestamp);
        i.putExtra(EXTRA_PDF_PATH,     pdfPath);
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
        String pdfPath = getIntent().getStringExtra(EXTRA_PDF_PATH);

        edtAmka = findViewById(R.id.edtDoctorAmka);
        rgPerms = findViewById(R.id.rgPermissions);
        Button btnSend = findViewById(R.id.btnSendShare);

        RadioButton rbReadOnly  = findViewById(R.id.rbReadOnly);
        RadioButton rbReadWrite = findViewById(R.id.rbReadWrite);

        btnSend.setOnClickListener(v -> {
            String amka = edtAmka.getText().toString().trim();
            Doctor doc = quizEngine.validateDoctorAMKA(amka);
            if (doc == null) {
                startActivity(new Intent(this, InvalidAMKAScreen.class));
                return;
            }

            // stub link
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
                        "Σφάλμα κατά την αποστολή συνδέσμου.", Toast.LENGTH_LONG
                ).show();
                return;
            }

            // now attach the *same* PDF we just generated
            File pdfFile = new File(pdfPath);
            Uri uri = FileProvider.getUriForFile(
                    this,
                    "com.example.smartmed1.fileprovider",
                    pdfFile
            );

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{ doc.getEmail() });
            share.putExtra(Intent.EXTRA_SUBJECT,
                    "Αποτελέσματα Ψυχικής Υγείας");
            share.putExtra(Intent.EXTRA_TEXT,
                    "Παρακαλώ βρείτε τα αποτελέσματα επισυναπτόμενα.");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            // always grant read
            int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;

            // if “Προβολή και επεξεργασία” is checked, also grant write
            if (rbReadWrite.isChecked()) {
                flags |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
            }

            share.addFlags(flags);

            startActivity(Intent.createChooser(
                    share,
                    "Αποστολή αποτελεσμάτων"
            ));
        });
    }
}
