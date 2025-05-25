package com.example.smartmed.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed.R;

public class AddHealthData extends AppCompatActivity {

    EditText editDate, editSleepDuration, editSleepQuality, editBloodPressure,
            editHeartRate, editSteps, editStress, editMood, editEnergy, editWeight;

    Button submitButton;
    DatabaseHelper dbHelper;

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_healthdata); // το XML που μου έστειλες

        dbHelper = new DatabaseHelper(this);

        // Σύνδεση πεδίων
        editDate = findViewById(R.id.editDate);
        editSleepDuration = findViewById(R.id.editSleepDuration);
        editSleepQuality = findViewById(R.id.editSleepQuality);
        editBloodPressure = findViewById(R.id.editBloodPressure);
        editHeartRate = findViewById(R.id.editHeartRate);
        editSteps = findViewById(R.id.editSteps);
        editStress = findViewById(R.id.editStress);
        editMood = findViewById(R.id.editMood);
        editEnergy = findViewById(R.id.editEnergy);
        editWeight = findViewById(R.id.editWeight);

        submitButton = findViewById(R.id.submitButton); //
        editSleepQuality.setOnTouchListener((v, event) -> {
            if (!editSleepQuality.hasFocus()) {
                showScaleHint();
            }
            return false; // αφήνει το focus να συνεχιστεί
        });
        editStress.setOnTouchListener((v, e) -> {
            if (!editStress.hasFocus()) showScaleHint();
            return false;
        });

        editMood.setOnTouchListener((v, e) -> {
            if (!editMood.hasFocus()) showScaleHint();
            return false;
        });

        editEnergy.setOnTouchListener((v, e) -> {
            if (!editEnergy.hasFocus()) showScaleHint();
            return false;
        });

        submitButton.setOnClickListener(v -> saveHealthData());
    }

    private void saveHealthData() {
        if (!validateFields()) {
            return;
        }
        try {
            String date = editDate.getText().toString().trim();
            String sleepDuration = editSleepDuration.getText().toString().trim();
            String sleepQuality = editSleepQuality.getText().toString().trim();
            String bloodPressure = editBloodPressure.getText().toString().trim();
            String heartRate = editHeartRate.getText().toString().trim();
            String steps = editSteps.getText().toString().trim();
            String stress = editStress.getText().toString().trim();
            String mood = editMood.getText().toString().trim();
            String energy = editEnergy.getText().toString().trim();
            String weight = editWeight.getText().toString().trim();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("date", date);
            values.put("sleepDuration", Double.parseDouble(safe(sleepDuration)));
            values.put("sleepQuality", Double.parseDouble(safe(sleepQuality)));
            values.put("bloodPressure", bloodPressure);
            values.put("heartRate", Double.parseDouble(safe(heartRate)));
            values.put("steps", Integer.parseInt(safe(steps)));
            values.put("stressLevel", Double.parseDouble(safe(stress)));
            values.put("mood", Double.parseDouble(safe(mood)));
            values.put("energyLevel", Double.parseDouble(safe(energy)));
            values.put("weight", Double.parseDouble(safe(weight)));

            long result = db.insert("HealthData", null, values);
            db.close();

            if (result != -1) {
                Toast.makeText(this, "✅ Τα δεδομένα αποθηκεύτηκαν!", Toast.LENGTH_LONG).show();
                clearFields();
            } else {
                Toast.makeText(this, "❌ Αποτυχία αποθήκευσης!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "⚠️ Σφάλμα: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void showScaleHint() {
        Toast.makeText(this, "Παρακαλώ συμπληρώστε βαθμολογώντας από το 1 έως το 10", Toast.LENGTH_SHORT).show();
    }


    private String safe(String s) {
        return s.isEmpty() ? "0" : s;
    }

    private void clearFields() {
        editDate.setText("");
        editSleepDuration.setText("");
        editSleepQuality.setText("");
        editBloodPressure.setText("");
        editHeartRate.setText("");
        editSteps.setText("");
        editStress.setText("");
        editMood.setText("");
        editEnergy.setText("");
        editWeight.setText("");
    }

    private boolean validateFields() {
        if (editDate.getText().toString().trim().isEmpty() ||
                editSleepDuration.getText().toString().trim().isEmpty() ||
                editSleepQuality.getText().toString().trim().isEmpty() ||
                editBloodPressure.getText().toString().trim().isEmpty() ||
                editHeartRate.getText().toString().trim().isEmpty() ||
                editSteps.getText().toString().trim().isEmpty() ||
                editStress.getText().toString().trim().isEmpty() ||
                editMood.getText().toString().trim().isEmpty() ||
                editEnergy.getText().toString().trim().isEmpty() ||
                editWeight.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "❗ Συμπληρώστε όλα τα πεδία.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
