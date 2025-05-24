package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PatientAdmissionForm extends AppCompatActivity {

    EditText editAmka, editName, editSurname;
    Button btnVerifyPrescription, btnVerifyReferral;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_admission_form);

        // Συνδέουμε τα views με το layout
        editAmka = findViewById(R.id.editAmka);
        editName = findViewById(R.id.editName);
        editSurname = findViewById(R.id.editSurname);
        btnVerifyPrescription = findViewById(R.id.btnVerifyPrescription);
        btnVerifyReferral = findViewById(R.id.btnVerifyReferral);


        // Όταν πατηθεί "Επαλήθευση"
        DatabaseHelper dbHelper = new DatabaseHelper(PatientAdmissionForm.this);

        btnVerifyPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amka = editAmka.getText().toString().trim();
                String name = editName.getText().toString().trim();
                String surname = editSurname.getText().toString().trim();

                if (amka.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                    Toast.makeText(PatientAdmissionForm.this, "❌ Συμπλήρωσε όλα τα πεδία", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isValid = dbHelper.verifyPatient(amka, name, surname);

                    if (isValid) {
                        Toast.makeText(PatientAdmissionForm.this, "✅ Επαλήθευση επιτυχής", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PatientAdmissionForm.this, DoctorPrescriptionCreate.class);
                        intent.putExtra("amka", amka);
                        intent.putExtra("name", name);
                        intent.putExtra("surname", surname);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(PatientAdmissionForm.this, InvalidDataActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        btnVerifyReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amka = editAmka.getText().toString().trim();
                String name = editName.getText().toString().trim();
                String surname = editSurname.getText().toString().trim();

                if (amka.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                    Toast.makeText(PatientAdmissionForm.this, "❌ Συμπλήρωσε όλα τα πεδία", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isValid = dbHelper.verifyPatient(amka, name, surname);

                    if (isValid) {
                        Toast.makeText(PatientAdmissionForm.this, "✅ Επαλήθευση επιτυχής", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PatientAdmissionForm.this, DoctorReferralCreate.class);
                        intent.putExtra("amka", amka);
                        intent.putExtra("name", name);
                        intent.putExtra("surname", surname);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(PatientAdmissionForm.this, InvalidDataActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
