package com.example.smartmed1;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserHome extends AppCompatActivity {

    // Δήλωση κουμπιών
    Button btnNewAppointment, btnMyAppointments, btnMyPrescriptions, btnHealthData, btnMedicalFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);  // Σιγουρέψου ότι αυτό είναι το σωστό όνομα του XML

        // Σύνδεση κουμπιών με τα IDs από το layout
        btnNewAppointment = findViewById(R.id.btnNewAppointment);
        btnMyAppointments = findViewById(R.id.btnMyAppointments);
        btnMyPrescriptions = findViewById(R.id.btnMyPrescriptions);
        btnHealthData = findViewById(R.id.btnHealthData);
        btnMedicalFiles = findViewById(R.id.btnMedicalFiles);

        // Προσωρινά listeners με TODO
        btnNewAppointment.setOnClickListener(v -> {
            // TODO: Άνοιγμα σελίδας δημιουργίας νέου ραντεβού
            // startActivity(new Intent(this, NewAppointmentActivity.class));
        });

        btnMyAppointments.setOnClickListener(v -> {
            // TODO: Άνοιγμα σελίδας ραντεβού
        });

        btnMyPrescriptions.setOnClickListener(v -> {
            // TODO: Άνοιγμα σελίδας με τις συνταγές
        });

        btnHealthData.setOnClickListener(v -> {
            // TODO: Άνοιγμα σελίδας με τα δεδομένα υγείας
        });

        btnMedicalFiles.setOnClickListener(v -> {
            // TODO: Άνοιγμα σελίδας με τα ιατρικά αρχεία
        });
    }
}
