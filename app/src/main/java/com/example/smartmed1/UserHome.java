package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class UserHome extends AppCompatActivity {

    // Δήλωση κουμπιών
    Button btnNewAppointment, btnMyAppointments, btnMyPrescriptions, btnHealthData, btnMedicalFiles;
    ImageView infoIcon;  // Προσθήκη

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Σύνδεση κουμπιών
        btnNewAppointment = findViewById(R.id.btnNewAppointment);
        btnMyAppointments = findViewById(R.id.btnMyAppointments);
        btnMyPrescriptions = findViewById(R.id.btnMyPrescriptions);
        btnHealthData = findViewById(R.id.btnHealthData);
        btnMedicalFiles = findViewById(R.id.btnMedicalFiles);

        // Σύνδεση icon
        infoIcon = findViewById(R.id.infoIcon);

        infoIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, HelpAndSupportUser.class));
        });

        // Προσωρινά listeners
        btnNewAppointment.setOnClickListener(v -> {});
        btnMyAppointments.setOnClickListener(v -> {});
        btnMyPrescriptions.setOnClickListener(v -> {});
        btnHealthData.setOnClickListener(v -> {});

        btnMedicalFiles.setOnClickListener(v -> {
            startActivity(new Intent(this, MedicalDocumentsActivity.class));
        });
    }
}
