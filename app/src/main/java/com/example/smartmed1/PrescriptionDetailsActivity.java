package com.example.smartmed1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class PrescriptionDetailsActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private TextView prescriptionCodeTextView;
    private TextView doctorNameTextView;
    private TextView diagnosisTextView;
    private TextView medicationsTextView;
    private TextView instructionsTextView;
    private CheckBox checkBoxMonday;
    private CheckBox checkBoxTuesday;
    private CheckBox checkBoxWednesday;
    private CheckBox checkBoxThursday;
    private CheckBox checkBoxFriday;
    private CheckBox checkBoxSaturday;
    private CheckBox checkBoxSunday;
    private EditText editTextSideEffects;
    private Button buttonSubmitSideEffects;
    private LinearLayout linearLayoutContactDoctor;
    private Button buttonRenewPrescription;
    private Button buttonDownloadDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_details);

        // Initialize TextViews
        prescriptionCodeTextView = findViewById(R.id.textViewPrescriptionCode);
        doctorNameTextView = findViewById(R.id.textViewDoctorName);
        diagnosisTextView = findViewById(R.id.textViewDiagnosis);
        medicationsTextView = findViewById(R.id.textViewMedications);
        instructionsTextView = findViewById(R.id.textViewInstructions);

        // Initialize CheckBoxes
        checkBoxMonday = findViewById(R.id.checkBoxMonday);
        checkBoxTuesday = findViewById(R.id.checkBoxTuesday);
        checkBoxWednesday = findViewById(R.id.checkBoxWednesday);
        checkBoxThursday = findViewById(R.id.checkBoxThursday);
        checkBoxFriday = findViewById(R.id.checkBoxFriday);
        checkBoxSaturday = findViewById(R.id.checkBoxSaturday);
        checkBoxSunday = findViewById(R.id.checkBoxSunday);

        // Initialize Side Effects components
        editTextSideEffects = findViewById(R.id.editTextSideEffects);
        buttonSubmitSideEffects = findViewById(R.id.buttonSubmitSideEffects);

        // Initialize Contact Doctor section
        linearLayoutContactDoctor = findViewById(R.id.linearLayoutContactDoctor);

        // Initialize Renew Prescription button
        buttonRenewPrescription = findViewById(R.id.buttonRenewPrescription);

        // Initialize Download Document button
        buttonDownloadDocument = findViewById(R.id.buttonDownloadDocument);

        // Get data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            String prescriptionCode = intent.getStringExtra("prescription_code");
            String doctorName = intent.getStringExtra("doctor_name");
            String diagnosis = intent.getStringExtra("diagnosis");
            String medications = intent.getStringExtra("medications");
            String instructions = intent.getStringExtra("instructions");

            // Display data
            prescriptionCodeTextView.setText(prescriptionCode);
            doctorNameTextView.setText("Ιατρός: " + doctorName);
            diagnosisTextView.setText(diagnosis);
            medicationsTextView.setText(medications);
            instructionsTextView.setText(instructions);
        }

        // Add click listeners to checkboxes
        setupCheckboxListeners();

        // Add click listener to submit button
        buttonSubmitSideEffects.setOnClickListener(v -> {
            String sideEffects = editTextSideEffects.getText().toString().trim();
            if (!sideEffects.isEmpty()) {
                Toast.makeText(this, "Οι παρενέργειες υποβλήθηκαν επιτυχώς", Toast.LENGTH_SHORT).show();
                editTextSideEffects.setText("");
            } else {
                Toast.makeText(this, "Παρακαλώ εισάγετε τις παρενέργειες", Toast.LENGTH_SHORT).show();
            }
        });

        // Add click listener to renew prescription button
        buttonRenewPrescription.setOnClickListener(v -> {
            Intent renewIntent = new Intent(PrescriptionDetailsActivity.this, Renew_Prescriptions.class);
            renewIntent.putExtra("prescription_code", prescriptionCodeTextView.getText().toString());
            renewIntent.putExtra("doctor_name", doctorNameTextView.getText().toString().replace("Ιατρός: ", ""));
            startActivity(renewIntent);
        });

        // Add click listener to download document button
        buttonDownloadDocument.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                generateAndSavePdf();
            } else {
                requestStoragePermission();
            }
        });

        // Add click listener to contact doctor section
        linearLayoutContactDoctor.setOnClickListener(v -> {
            String doctorName = doctorNameTextView.getText().toString().replace("Ιατρός: ", "");
            String phoneNumber;
            switch (doctorName) {
                case "Δημήτριος Αντωνίου":
                    phoneNumber = "2101234567";
                    break;
                case "Γεώργιος Πατάπης":
                    phoneNumber = "2102345678";
                    break;
                case "Παναγιώτης Παπαδόπουλος":
                    phoneNumber = "2103456789";
                    break;
                case "Ελένη Βασιλείου":
                    phoneNumber = "2104567890";
                    break;
                default:
                    phoneNumber = "2100000000";
            }
            Toast.makeText(this, "Τηλέφωνο γιατρού: " + phoneNumber, Toast.LENGTH_LONG).show();
        });
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generateAndSavePdf();
            } else {
                Toast.makeText(this, "Απαιτείται άδεια για την αποθήκευση του PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generateAndSavePdf() {
        String prescriptionCode = prescriptionCodeTextView.getText().toString();
        String doctorName = doctorNameTextView.getText().toString().replace("Ιατρός: ", "");
        String diagnosis = diagnosisTextView.getText().toString();
        String medications = medicationsTextView.getText().toString();
        String instructions = instructionsTextView.getText().toString();

        PdfGenerator pdfGenerator = new PdfGenerator(this);
        pdfGenerator.generatePrescriptionPdf(prescriptionCode, doctorName, diagnosis, medications, instructions);
    }

    private void setupCheckboxListeners() {
        CheckBox[] checkboxes = {
            checkBoxMonday, checkBoxTuesday, checkBoxWednesday,
            checkBoxThursday, checkBoxFriday, checkBoxSaturday, checkBoxSunday
        };

        for (CheckBox checkbox : checkboxes) {
            checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    updateRemainingDoses();
                }
            });
        }
    }

    private void updateRemainingDoses() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int remainingDays = 7 - (currentDay - 1);

        int checkedCount = 0;
        if (checkBoxMonday.isChecked()) checkedCount++;
        if (checkBoxTuesday.isChecked()) checkedCount++;
        if (checkBoxWednesday.isChecked()) checkedCount++;
        if (checkBoxThursday.isChecked()) checkedCount++;
        if (checkBoxFriday.isChecked()) checkedCount++;
        if (checkBoxSaturday.isChecked()) checkedCount++;
        if (checkBoxSunday.isChecked()) checkedCount++;

        int remainingDoses = (remainingDays * 3) - checkedCount;
        String message = "Απομένουν " + remainingDoses + " δόσεις μέχρι το τέλος της εβδομάδας";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
} 
