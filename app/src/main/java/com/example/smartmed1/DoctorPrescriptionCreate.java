package com.example.smartmed1;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DoctorPrescriptionCreate extends AppCompatActivity {

    Button btnDownload, btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_prescription_create);

        btnDownload = findViewById(R.id.btnDownload);
        btnConfirm = findViewById(R.id.btnConfirm);

        btnDownload.setOnClickListener(v -> {
            // TODO: Δημιουργία PDF και αποθήκευση
        });

        btnConfirm.setOnClickListener(v -> {
            // TODO: Προς προεπισκόπηση και αποστολή συνταγής
        });
    }
}
