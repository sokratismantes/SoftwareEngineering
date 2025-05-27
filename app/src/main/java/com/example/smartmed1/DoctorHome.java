package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// Αρχική οθόνη για τον γιατρό μετά το login
public class DoctorHome extends AppCompatActivity {

    // Δήλωση κουμπιών για τις διαθέσιμες λειτουργίες του γιατρού
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

        // Ρητές μέθοδοι μετάβασης
        btnPrescription.setOnClickListener(v -> GoToPatientForm());
        btnReferral.setOnClickListener(v -> GoToReferralForm());
        btnAvailability.setOnClickListener(v -> {
            startActivity(new Intent(this, DoctorAvailabilityActivity.class));
        });
        btnAppointments.setOnClickListener(v -> GoToAppointments());
        btnPrescriptions.setOnClickListener(v -> GoToPrescriptions());
        btnHistory.setOnClickListener(v -> GoToHistory());
    }

    // Ενεργοποίηση της σελίδας εισαγωγής ασθενούς για συνταγογράφηση
    private void GoToPatientForm() {
        Intent intent = new Intent(this, PatientAdmissionForm.class);
        startActivity(intent);
    }

    private void GoToReferralForm() {
        Intent intent = new Intent(this, PatientAdmissionForm.class);
        startActivity(intent);
    }


    private void GoToAppointments() {


    }

    private void GoToPrescriptions() {


    }

    private void GoToHistory() {


    }
}
