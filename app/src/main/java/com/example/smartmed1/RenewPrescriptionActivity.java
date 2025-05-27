package com.example.smartmed1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RenewPrescriptionActivity extends AppCompatActivity {

    private EditText editTextReason;
    private EditText editTextNotes;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_prescription);

        // Initialize views
        editTextReason = findViewById(R.id.editTextReason);
        editTextNotes = findViewById(R.id.editTextNotes);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Get prescription details from intent
        String prescriptionCode = getIntent().getStringExtra("prescription_code");
        String doctorName = getIntent().getStringExtra("doctor_name");

        // Set up submit button click listener
        buttonSubmit.setOnClickListener(v -> {
            String reason = editTextReason.getText().toString().trim();
            String notes = editTextNotes.getText().toString().trim();

            if (reason.isEmpty()) {
                Toast.makeText(this, "Παρακαλώ εισάγετε τον λόγο ανανέωσης", Toast.LENGTH_SHORT).show();
                return;
            }


            Toast.makeText(this, "Επιτυχής ανανέωση συνταγής", Toast.LENGTH_SHORT).show();
            
            // Close the activity and return to the previous screen
            finish();
        });
    }
} 