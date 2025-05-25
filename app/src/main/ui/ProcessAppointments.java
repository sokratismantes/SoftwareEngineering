package com.example.smartmed.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed.R;

public class ProcessAppointments extends AppCompatActivity {

    EditText editFullName, editInsurance, editReason, editHistory;
    RadioGroup radioTypeGroup, radioUrgencyGroup;
    Button confirmButton;

    DatabaseHelper dbHelper;

    Button feedbackText;

    String appointmentId; // <-- εδώ αποθηκεύουμε το id

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_appointments);

        editFullName = findViewById(R.id.editFullName);
        editInsurance = findViewById(R.id.editInsurance);
        editReason = findViewById(R.id.editReason);
        editHistory = findViewById(R.id.editHistory);
        radioTypeGroup = findViewById(R.id.radioTypeGroup);
        radioUrgencyGroup = findViewById(R.id.radioUrgencyGroup);
        confirmButton = findViewById(R.id.confirmButton);

        dbHelper = new DatabaseHelper(this);

        // Πάρε το appointmentId από το intent
        appointmentId = getIntent().getStringExtra("appointmentId");

        LoadProcessForm(appointmentId);

        confirmButton.setOnClickListener(v -> UpdateAppointment());

    }

    private void LoadProcessForm(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_APPOINTMENTS +
                " WHERE id = ?", new String[]{id});

        if (cursor.moveToFirst()) {
            editFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PATIENT_NAME)));
            editInsurance.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_INSURANCE)));
            editReason.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_REASON)));
            editHistory.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HISTORY)));

            String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_APPOINTMENT_TYPE));
            String urgent = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_URGENT));

            // Εύρεση και επιλογή RadioButton τύπου
            for (int i = 0; i < radioTypeGroup.getChildCount(); i++) {
                RadioButton rb = (RadioButton) radioTypeGroup.getChildAt(i);
                if (rb.getText().toString().equalsIgnoreCase(type)) {
                    rb.setChecked(true);
                    break;
                }
            }

            // Εύρεση και επιλογή RadioButton επείγοντος
            for (int i = 0; i < radioUrgencyGroup.getChildCount(); i++) {
                RadioButton rb = (RadioButton) radioUrgencyGroup.getChildAt(i);
                if (rb.getText().toString().equalsIgnoreCase(urgent)) {
                    rb.setChecked(true);
                    break;
                }
            }
        }

        cursor.close();
        db.close();
    }

    private void UpdateAppointment() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_PATIENT_NAME, editFullName.getText().toString().trim());
        values.put(DatabaseHelper.COL_INSURANCE, editInsurance.getText().toString().trim());
        values.put(DatabaseHelper.COL_REASON, editReason.getText().toString().trim());
        values.put(DatabaseHelper.COL_HISTORY, editHistory.getText().toString().trim());

        int typeId = radioTypeGroup.getCheckedRadioButtonId();
        int urgentId = radioUrgencyGroup.getCheckedRadioButtonId();

        if (typeId != -1) {
            RadioButton selectedType = findViewById(typeId);
            values.put(DatabaseHelper.COL_APPOINTMENT_TYPE, selectedType.getText().toString());
        }

        if (urgentId != -1) {
            RadioButton selectedUrgent = findViewById(urgentId);
            values.put(DatabaseHelper.COL_URGENT, selectedUrgent.getText().toString());
        }

        int rowsAffected = db.update(DatabaseHelper.TABLE_APPOINTMENTS, values, "id = ?", new String[]{appointmentId});
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "✅ Ενημερώθηκε με επιτυχία!", Toast.LENGTH_SHORT).show();
            SaveChanges();
        } else {
            Toast.makeText(this, "❌ Κάτι πήγε στραβά.", Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveChanges() {
        Intent intent = new Intent(ProcessAppointments.this, ActiveAppointments.class);
        startActivity(intent);
        finish();
    }
}
