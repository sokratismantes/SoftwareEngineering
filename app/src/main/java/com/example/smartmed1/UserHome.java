package com.example.smartmed1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;


public class UserHome extends AppCompatActivity {

    // Δήλωση κουμπιών
    Button btnNewAppointment, btnMyAppointments, btnMyPrescriptions, btnHealthData, btnMedicalFiles;


    ImageView infoIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);

        // Σύνδεση κουμπιών με τα IDs από το layout

        btnMyPrescriptions = findViewById(R.id.btnMyPrescriptions);
        btnMedicalFiles = findViewById(R.id.btnMedicalFiles);
        btnNewAppointment = findViewById(R.id.button2);
        btnMyAppointments = findViewById(R.id.button3);
        btnHealthData = findViewById(R.id.button5);


        btnNewAppointment.setOnClickListener(v -> {

        });

        btnMyAppointments.setOnClickListener(v -> {
        });

        btnMyPrescriptions.setOnClickListener(v -> {
            startActivity(new Intent(UserHome.this, PrescriptionListActivity.class));
        });


        btnHealthData.setOnClickListener(v -> {

        });

        btnMedicalFiles.setOnClickListener(v -> {
            // Άνοιγμα της σελίδας MedicalDocumentsActivity
            Intent intent = new Intent(this, MedicalDocumentsActivity.class);
            startActivity(intent);
        });
        infoIcon = findViewById(R.id.infoIcon);

        infoIcon.setOnClickListener(v -> {
            Intent intent = new Intent(UserHome.this, HelpAndSupportUser.class);
            startActivity(intent);
        });

        btnMedicalFiles.setOnClickListener(v -> {
            startActivity(new Intent(this, MedicalDocumentsActivity.class));
        });
        infoIcon = findViewById(R.id.infoIcon);

        infoIcon.setOnClickListener(v -> {
            startActivity(new Intent(UserHome.this, HelpAndSupportUser.class));
        });

        btnNewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAvailabilityForm();  //
            }
        });

        btnMyAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetActiveAppointments(); //
            }
        });

        btnHealthData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUserChoices(); //
            }
        });

    }
    //Ανοίγει τη φόρμα ραντεβού
    private void ShowAvailabilityForm() {
        Intent intent = new Intent(UserHome.this, AvailabilityForm.class);
        startActivity(intent);
    }

    private void GetActiveAppointments() {
        Intent intent = new Intent(UserHome.this, ActiveAppointments.class);
        startActivity(intent);
    }

    private void LoadUserChoices() {
        Intent intent = new Intent(UserHome.this, UserChoices.class);
        startActivity(intent);
    }
    }

