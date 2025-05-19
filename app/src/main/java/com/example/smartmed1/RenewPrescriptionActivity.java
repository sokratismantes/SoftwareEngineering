package com.example.smartmed1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RenewPrescriptionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_prescription);

        TextView tvCodeValue = findViewById(R.id.tvCodeValue);
        EditText etReason = findViewById(R.id.etReason);
        EditText etComments = findViewById(R.id.etComments);
        Button btnSubmitRenewal = findViewById(R.id.btnSubmitRenewal);

        String code = getIntent().getStringExtra("code");
        if (code != null) {
            tvCodeValue.setText(code);
        }

        btnSubmitRenewal.setOnClickListener(v -> {
            String reason = etReason.getText().toString().trim();
            if (reason.isEmpty()) {
                Toast.makeText(this, "Συμπληρώστε τον λόγο ανανέωσης!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Εδώ μπορείς να στείλεις τα δεδομένα στον server ή να τα αποθηκεύσεις
            Toast.makeText(this, "Η αίτηση ανανέωσης υποβλήθηκε!", Toast.LENGTH_LONG).show();
            finish();
        });
    }
} 