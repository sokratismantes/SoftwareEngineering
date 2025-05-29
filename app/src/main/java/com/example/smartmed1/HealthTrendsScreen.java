package com.example.smartmed1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



import java.util.HashMap;

public class HealthTrendsScreen extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_trends_screen);
        dbHelper = new DatabaseHelper(this);
        DisplayHealthMeans();
        setupClickListeners();

        Button btnTrends = findViewById(R.id.btnShowTrends);
        btnTrends.setOnClickListener(v -> ShowWarning());

    }

    private void DisplayHealthMeans() {
        HashMap<String, Double> means = ComputeMeanValues();

        FindVariances(R.id.sleepText, R.id.sleepContainer, "ΥΠΝΟΣ: ", means.get("sleepDuration"), " ώρες", 6.0, 9.0, false);
        FindVariances(R.id.heartRateText, R.id.heartRateContainer, "ΚΑΡΔΙΑΚΟΣ ΡΥΘΜΟΣ: ", means.get("heartRate"), " bpm", 50, 100, false);
        FindVariances(R.id.stepsText, R.id.stepsContainer, "ΒΗΜΑΤΑ: ", means.get("steps"), "", 3000, 50000, false);
        FindVariances(R.id.stressText, R.id.stressContainer, "ΑΓΧΟΣ: ", means.get("stressLevel"), "", 0.0, 7.0, true);
        FindVariances(R.id.moodText, R.id.moodContainer, "ΔΙΑΘΕΣΗ: ", means.get("mood"), "", 5.0, 10.0, true);
        FindVariances(R.id.energyText, R.id.energyContainer, "ΕΝΕΡΓΕΙΑ: ", means.get("energyLevel"), "", 5.0, 10.0, true);
        FindVariances(R.id.weightText, R.id.weightContainer, "ΒΑΡΟΣ: ", means.get("weight"), " kg", 45.0, 85.0, false);
    }

    private HashMap<String, Double> ComputeMeanValues() {
        HashMap<String, Double> averages = new HashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT AVG(sleepDuration), AVG(sleepQuality), AVG(heartRate), AVG(steps), " +
                        "AVG(stressLevel), AVG(mood), AVG(energyLevel), AVG(weight) FROM HealthData",
                null
        );

        if (cursor.moveToFirst()) {
            averages.put("sleepDuration", cursor.getDouble(0));
            averages.put("sleepQuality", cursor.getDouble(1));
            averages.put("heartRate", cursor.getDouble(2));
            averages.put("steps", cursor.getDouble(3));
            averages.put("stressLevel", cursor.getDouble(4));
            averages.put("mood", cursor.getDouble(5));
            averages.put("energyLevel", cursor.getDouble(6));
            averages.put("weight", cursor.getDouble(7));
        }

        cursor.close();
        db.close();
        return averages;
    }

    private void FindVariances(int textViewId, int containerId, String label, Double value,
                                 String suffix, double min, double max, boolean outOfTen) {
        TextView textView = findViewById(textViewId);
        LinearLayout container = findViewById(containerId);

        if (value == null || value == 0.0) {
            textView.setText(label + "--");
            return;
        }

        String extra = outOfTen ? "/10" : "";
        String formatted = String.format("%.1f", value);
        textView.setText(label + formatted + suffix + extra);

        if (value < min || value > max) {
            container.setBackgroundResource(R.drawable.rounded_alert_button);
        } else {
            container.setBackgroundResource(R.drawable.rounded_payment_button);
        }
    }

    private void setupClickListeners() {
        findViewById(R.id.sleepContainer).setOnClickListener(v -> openAnalysis("sleepDuration"));
        findViewById(R.id.heartRateContainer).setOnClickListener(v -> openAnalysis("heartRate"));
        findViewById(R.id.stepsContainer).setOnClickListener(v -> openAnalysis("steps"));
        findViewById(R.id.stressContainer).setOnClickListener(v -> openAnalysis("stressLevel"));
        findViewById(R.id.moodContainer).setOnClickListener(v -> openAnalysis("mood"));
        findViewById(R.id.energyContainer).setOnClickListener(v -> openAnalysis("energyLevel"));
        findViewById(R.id.weightContainer).setOnClickListener(v -> openAnalysis("weight"));
    }

    private void openAnalysis(String category) {
        Intent intent = new Intent(HealthTrendsScreen.this, Health_Analysis.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void CreateWarning() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT sleepDuration, heartRate, steps, stressLevel, mood, energyLevel, weight " +
                        "FROM HealthData ORDER BY date DESC LIMIT 1", null);

        int redCount = 0;

        if (cursor.moveToFirst()) {
            float sleep = cursor.getFloat(0);
            float heart = cursor.getFloat(1);
            float steps = cursor.getFloat(2);
            float stress = cursor.getFloat(3);
            float mood = cursor.getFloat(4);
            float energy = cursor.getFloat(5);
            float weight = cursor.getFloat(6);

            if (sleep < 6 || sleep > 9) redCount++;
            if (heart < 60 || heart > 100) redCount++;
            if (steps < 3000) redCount++;
            if (stress > 7) redCount++;
            if (mood < 4) redCount++;
            if (energy < 4) redCount++;
            // Βάρος δεν ελέγχεται αυστηρά ως "κόκκινο"
        }

        cursor.close();
        db.close();

        Intent intent = new Intent(HealthTrendsScreen.this, HealthWarningScreen.class);
        if (redCount >= 3) {
            intent.putExtra("message", "⚠️ Υπάρχουν πάνω από 3 προειδοποιητικά σημάδια στην υγεία σας. Προτείνεται προσοχή.");
        } else {
            intent.putExtra("message", "✅ Οι δείκτες υγείας σας είναι σε ικανοποιητικό επίπεδο. Συνεχίστε έτσι!");
        }

        startActivity(intent);
    }

    private void ShowWarning() {
        CreateWarning();
    }

}
