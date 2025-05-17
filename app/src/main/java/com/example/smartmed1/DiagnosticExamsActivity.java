package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class DiagnosticExamsActivity extends AppCompatActivity {

    EditText searchField;
    LinearLayout btnHematology, btnMRI, btnMicrobiology, btnCardiology, btnGeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_exams);

        // Συνδέσεις views
        searchField = findViewById(R.id.searchField);
        btnHematology = findViewById(R.id.btnHematology);
        btnMRI = findViewById(R.id.btnMRI);
        btnMicrobiology = findViewById(R.id.btnMicrobiology);
        btnCardiology = findViewById(R.id.btnCardiology);
        btnGeneral = findViewById(R.id.btnGeneral);

        // Παράδειγμα action
        btnHematology.setOnClickListener(v -> {
            Intent intent = new Intent(DiagnosticExamsActivity.this, DiagnosticResultsHematology.class);
            intent.putExtra("category", "Αιματολογικές");
            startActivity(intent);
        });

        btnMRI.setOnClickListener(v -> {
            Intent intent = new Intent(DiagnosticExamsActivity.this, DiagnosticResultsMRI.class);
            intent.putExtra("category", "Μαγνητικές");
            startActivity(intent);
        });

        btnMicrobiology.setOnClickListener(v -> {
            Intent intent = new Intent(DiagnosticExamsActivity.this, DiagnosticResultsMicrobiology.class);
            intent.putExtra("category", "Μικροβιολογικές");
            startActivity(intent);
        });

        btnCardiology.setOnClickListener(v -> {
            Intent intent = new Intent(DiagnosticExamsActivity.this, DiagnosticResultsCardiology.class);
            intent.putExtra("category", "Καρδιολογικές");
            startActivity(intent);
        });

        btnGeneral.setOnClickListener(v -> {
            Intent intent = new Intent(DiagnosticExamsActivity.this, DiagnosticResultsMolecular.class);
            intent.putExtra("category", "Μοριακές");
            startActivity(intent);
        });

    }
}
