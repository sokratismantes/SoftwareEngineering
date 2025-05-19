package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorPrescriptionCreate extends AppCompatActivity {
    EditText etDiagnosis, etMedicine, etCode, etDosage, etInstructions, etDuration;
    Button btnDownload, btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_prescription_create);

        // Σύνδεση με τα views
        etDiagnosis = findViewById(R.id.etDiagnosis);
        etMedicine = findViewById(R.id.etMedicine);
        etCode = findViewById(R.id.etCode);
        etDosage = findViewById(R.id.etDosage);
        etInstructions = findViewById(R.id.etInstructions);
        etDuration = findViewById(R.id.etDuration);

        btnDownload = findViewById(R.id.btnDownload);
        btnConfirm = findViewById(R.id.btnConfirm);

        btnDownload.setOnClickListener(v -> {
            // TODO: Δημιουργία PDF και αποθήκευση
        });

        btnConfirm.setOnClickListener(v -> {
            // Λήψη τιμών από τα πεδία
            String diagnosis = etDiagnosis.getText().toString().trim();
            String drug = etMedicine.getText().toString().trim();
            String code = etCode.getText().toString().trim();
            String dose = etDosage.getText().toString().trim();
            String instructions = etInstructions.getText().toString().trim();
            String duration = etDuration.getText().toString().trim();

            String amka = getIntent().getStringExtra("amka");
            String name = getIntent().getStringExtra("name") + " " + getIntent().getStringExtra("surname");

            if (diagnosis.isEmpty() || drug.isEmpty() || code.isEmpty() ||
                    dose.isEmpty() || instructions.isEmpty() || duration.isEmpty()) {
                Toast.makeText(this, "❌ Συμπλήρωσε όλα τα πεδία", Toast.LENGTH_SHORT).show();
                return;
            }

            // Intent για ConfirmPrescription
            Intent intent = new Intent(DoctorPrescriptionCreate.this, ConfirmPrescription.class);
            intent.putExtra("amka", amka);
            intent.putExtra("name", name);
            intent.putExtra("diagnosis", diagnosis);
            intent.putExtra("drug", drug);
            intent.putExtra("code", code);
            intent.putExtra("dose", dose);
            intent.putExtra("instructions", instructions);
            intent.putExtra("duration", duration); // ✅ περνάμε και το duration
            startActivity(intent);
        });
    }
}
