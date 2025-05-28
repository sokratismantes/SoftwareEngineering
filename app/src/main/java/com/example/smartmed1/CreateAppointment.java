package com.example.smartmed1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


public class CreateAppointment extends AppCompatActivity {

    EditText editFullName, editInsurance, editReason, editHistory;
    RadioGroup radioTypeGroup, radioUrgencyGroup;
    Button confirmButton;
    DatabaseHelper dbHelper;

    // Τα 4 πρώτα έρχονται από προηγούμενη activity
    String doctorName, specialty, date, time;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_appointment);

        // Λήψη δεδομένων από Intent
        doctorName = getIntent().getStringExtra("doctorName");
        specialty = getIntent().getStringExtra("specialty");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");

        // Σύνδεση υπόλοιπων πεδίων
        editFullName = findViewById(R.id.editFullName);
        editInsurance = findViewById(R.id.editInsurance);
        editReason = findViewById(R.id.editReason);
        editHistory = findViewById(R.id.editHistory);
        radioTypeGroup = findViewById(R.id.radioTypeGroup);
        radioUrgencyGroup = findViewById(R.id.radioUrgencyGroup);
        confirmButton = findViewById(R.id.confirmButton);

        dbHelper = new DatabaseHelper(this);

        // Έλεγχος ασφαλιστικού φορέα όταν ο χρήστης αφήσει το πεδίο
        editInsurance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) { // μόλις φύγει από το πεδίο
                    String input = editInsurance.getText().toString().trim();
                    Toast.makeText(CreateAppointment.this, "✅ 'Εγκυρος ασφαλιστικός φορέας!", Toast.LENGTH_SHORT).show();
                    if (!input.isEmpty() && !CheckInsurance(input)) {
                        Toast.makeText(CreateAppointment.this, "❌ Μη έγκυρος ασφαλιστικός φορέας!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckFinalData()) {
                    ShowFinalForm();  // ✅ Τρέχει ΜΟΝΟ αν όλα είναι σωστά
                }
            }
        });
    }

    private boolean CheckInsurance(String insuranceInput) {
        String[] validProviders = {"EOPYY", "IKA", "OAEE", "OGA", "NAT", "ATE"};

        for (String provider : validProviders) {
            if (provider.equalsIgnoreCase(insuranceInput)) {
                return true;
            }
        }
        return false;
    }

    private boolean CheckFinalData() {
        String fullName = editFullName.getText().toString().trim();
        String insurance = editInsurance.getText().toString().trim();
        String reason = editReason.getText().toString().trim();
        String history = editHistory.getText().toString().trim();

        int selectedTypeId = radioTypeGroup.getCheckedRadioButtonId();
        int selectedUrgentId = radioUrgencyGroup.getCheckedRadioButtonId();

        if (fullName.isEmpty() || insurance.isEmpty() || reason.isEmpty() || history.isEmpty()) {
            Toast.makeText(this, "❗️ Συμπληρώστε όλα τα πεδία!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedTypeId == -1 || selectedUrgentId == -1) {
            Toast.makeText(this, "❗ Επιλέξτε τύπο ρσντεβού και επείγον περιστατικό!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void ShowFinalForm() {
        String patientName = editFullName.getText().toString().trim();
        String insurance = editInsurance.getText().toString().trim();
        String reason = editReason.getText().toString().trim();
        String history = editHistory.getText().toString().trim();

        int selectedTypeId = radioTypeGroup.getCheckedRadioButtonId();
        String appointmentType = selectedTypeId != -1
                ? ((RadioButton) findViewById(selectedTypeId)).getText().toString()
                : "";

        int selectedUrgentId = radioUrgencyGroup.getCheckedRadioButtonId();
        String urgent = selectedUrgentId != -1
                ? ((RadioButton) findViewById(selectedUrgentId)).getText().toString()
                : "";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_DOCTOR_NAME, doctorName);
        values.put(DatabaseHelper.COL_SPECIALTY, specialty);
        values.put(DatabaseHelper.COL_DATE, date);
        values.put(DatabaseHelper.COL_TIME, time);
        values.put(DatabaseHelper.COL_PATIENT_NAME, patientName);
        values.put(DatabaseHelper.COL_APPOINTMENT_TYPE, appointmentType);
        values.put(DatabaseHelper.COL_INSURANCE, insurance);
        values.put(DatabaseHelper.COL_REASON, reason);
        values.put(DatabaseHelper.COL_HISTORY, history);
        values.put(DatabaseHelper.COL_URGENT, urgent);

        long rowId = db.insert(DatabaseHelper.TABLE_APPOINTMENTS, null, values);

        if (rowId != -1) {
            Toast.makeText(this, "✅ Το ραντεβού καταχωρήθηκε!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(CreateAppointment.this, AppointmentPreview.class);
            intent.putExtra("doctorName", doctorName);
            intent.putExtra("specialty", specialty);
            intent.putExtra("date", date);
            intent.putExtra("time", time);
            intent.putExtra("patientName", patientName);
            intent.putExtra("appointmentType", appointmentType);
            intent.putExtra("insurance", insurance);
            intent.putExtra("reason", reason);
            intent.putExtra("history", history);
            intent.putExtra("urgent", urgent);
            startActivity(intent);

        } else {
            Toast.makeText(this, "❌ Αποτυχία καταχώρησης.", Toast.LENGTH_LONG).show();
        }

        db.close();
        int count = dbHelper.getAppointmentsCount();
        Toast.makeText(this, "🧾 Συνολικά ραντεβού στη βάση: " + count, Toast.LENGTH_LONG).show();

    }
}
