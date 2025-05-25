package com.example.smartmed1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;
import java.util.Calendar;
import android.widget.ImageView;


public class UserHome extends AppCompatActivity {

    // Δήλωση κουμπιών
    Button btnNewAppointment, btnMyAppointments, btnMyPrescriptions, btnHealthData, btnMedicalFiles;
    ImageView infoIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

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
            // TODO: Άνοιγμα σελίδας με τα ραντεβού
        });

        btnMyPrescriptions.setOnClickListener(v -> {
            // ανοιγμα σελιδας με τις συνταγες
            startActivity(new Intent(this, PrescriptionListActivity.class));
        });

        btnHealthData.setOnClickListener(v -> {
            // TODO: Άνοιγμα σελίδας με τα δεδομένα υγείας
        });

        btnMedicalFiles.setOnClickListener(v -> {
            // Άνοιγμα της σελίδας MedicalDocumentsActivity
            Intent intent = new Intent(this, MedicalDocumentsActivity.class);
            startActivity(intent);
        });
        infoIcon = findViewById(R.id.infoIcon);  // Υποθέτουμε ότι αυτό είναι το ID της εικόνας σου

        infoIcon.setOnClickListener(v -> {
            Intent intent = new Intent(UserHome.this, HelpAndSupportUser.class);
            startActivity(intent);
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
