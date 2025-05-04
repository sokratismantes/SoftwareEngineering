package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorHome extends AppCompatActivity {

    // Δήλωση μεταβλητών
    Button btnPrescription, btnReferral, btnAvailability, btnAppointments, btnPrescriptions, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        // Σύνδεση κουμπιών
        btnPrescription = findViewById(R.id.btnPrescription);
        btnReferral = findViewById(R.id.btnReferral);
        btnAvailability = findViewById(R.id.btnAvailability);
        btnAppointments = findViewById(R.id.btnAppointments);
        btnPrescriptions = findViewById(R.id.btnPrescriptions);
        btnHistory = findViewById(R.id.btnHistory);

        // Προσθήκη listeners (προς το παρόν με TODO)
        btnPrescription.setOnClickListener(v -> {
            // TODO: Ξεκίνα Activity για δημιουργία συνταγής
            // startActivity(new Intent(this, CreatePrescriptionActivity.class));
        });

        btnReferral.setOnClickListener(v -> {
            // TODO: Ξεκίνα Activity για δημιουργία παραπεμπτικού
        });

        btnAvailability.setOnClickListener(v -> {
            // TODO: Διαχείριση διαθεσιμότητας
        });

        btnAppointments.setOnClickListener(v -> {
            // TODO: Προβολή ραντεβού
        });

        btnPrescriptions.setOnClickListener(v -> {
            // TODO: Προβολή συνταγών
        });

        btnHistory.setOnClickListener(v -> {
            // TODO: Ιστορικό
        });
    }
}
