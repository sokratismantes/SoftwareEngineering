package com.example.smartmed1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class HealthAnalysisScreen extends AppCompatActivity {

    BarChart barChart;
    DatabaseHelper dbHelper;
    String category;
    TextView percentageText;
    LinearLayout insightContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_analysis_screen);

        barChart = findViewById(R.id.barChart);
        percentageText = findViewById(R.id.percentageChangeText);
        insightContainer = findViewById(R.id.weightContainer);  //
        dbHelper = new DatabaseHelper(this);
        category = getIntent().getStringExtra("category");

        // Περιορίζουμε το ύψος στο 1/3 της οθόνης
        ViewGroup.LayoutParams params = barChart.getLayoutParams();
        params.height = (int) (getResources().getDisplayMetrics().heightPixels / 3.0);
        barChart.setLayoutParams(params);

        CompareResults();
        ShowWeeklySummary();

        Button addCommentBtn = findViewById(R.id.btnAddComment);
        addCommentBtn.setOnClickListener(v -> ShowNotepad());

    }

    private void CompareResults() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + category + " FROM HealthData ORDER BY date ASC", null);
        int i = 0;
        while (cursor.moveToNext()) {
            float value = (float) cursor.getDouble(0);
            entries.add(new BarEntry(i++, value));
        }

        cursor.close();
        db.close();

        BarDataSet dataSet = new BarDataSet(entries, "Τιμές " + category);
        dataSet.setColor(Color.parseColor("#FF6B6B"));
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateY(1200, Easing.EaseInOutCubic);
    }

    private void ShowWeeklySummary() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT date, sleepDuration, heartRate, steps, stressLevel, mood, energyLevel, weight " +
                        "FROM HealthData ORDER BY date DESC LIMIT 7", null
        );

        List<Float> dataList = new ArrayList<>();
        int index = getIndexForCategory(category);
        if (index == -1) {
            cursor.close();
            db.close();
            return;
        }

        while (cursor.moveToNext()) {
            dataList.add(cursor.getFloat(index));
        }

        cursor.close();
        db.close();

        List<Float> changes = CompareResults(dataList);
        if (!changes.isEmpty()) {
            float avg = 0f;
            for (float c : changes) avg += c;
            avg /= changes.size();

            String sign = avg >= 0 ? "+" : "-";
            String formatted = String.format("%.1f", Math.abs(avg)) + "%";
            String fullText = "Εβδομαδιαία Μεταβολή (" + category + "): " + sign + formatted;

            SpannableString spannable = new SpannableString(fullText);
            int start = fullText.indexOf(sign);
            int color = avg >= 0 ? Color.parseColor("#4CAF50") : Color.RED;
            spannable.setSpan(new ForegroundColorSpan(color), start, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            percentageText.setText(spannable);
            percentageText.setTextColor(Color.WHITE);

            float latestValue = dataList.get(0);
            addInsightCard(category, latestValue);
        }
    }

    private void addInsightCard(String category, float value) {
        View cardView = getLayoutInflater().inflate(R.layout.card_summary, insightContainer, false);

        TextView title = cardView.findViewById(R.id.summaryTitle);
        TextView description = cardView.findViewById(R.id.summaryDescription);
        TextView feedback = cardView.findViewById(R.id.summaryFeedback);

        title.setText("📌 Κατηγορία: " + getCategoryLabel(category));
        description.setText(ReviewSummary(category, value));
        feedback.setText("Αποτίμηση: " + getFeedbackLabel(category, value));

        insightContainer.addView(cardView);
    }

    private int getIndexForCategory(String category) {
        switch (category) {
            case "sleepDuration": return 1;
            case "heartRate": return 2;
            case "steps": return 3;
            case "stressLevel": return 4;
            case "mood": return 5;
            case "energyLevel": return 6;
            case "weight": return 7;
            default: return -1;
        }
    }

    private List<Float> CompareResults(List<Float> values) {
        List<Float> changes = new ArrayList<>();
        for (int i = values.size() - 1; i > 0; i--) {
            float prev = values.get(i);
            float current = values.get(i - 1);
            if (prev != 0) {
                float percentChange = ((current - prev) / prev) * 100f;
                changes.add(percentChange);
            }
        }
        return changes;
    }

    private String getCategoryLabel(String category) {
        switch (category) {
            case "sleepDuration": return "Ύπνος";
            case "heartRate": return "Καρδιακός Ρυθμός";
            case "steps": return "Βήματα";
            case "stressLevel": return "Άγχος";
            case "mood": return "Διάθεση";
            case "energyLevel": return "Ενέργεια";
            case "weight": return "Βάρος";
            default: return category;
        }
    }

    private String getFeedbackLabel(String category, float value) {
        String summary = ReviewSummary(category, value);
        if (summary.contains("✅") || summary.contains("😄") || summary.contains("⚡")) return "Καλή Κατάσταση";
        else if (summary.contains("⚠️") || summary.contains("🔻")) return "Απαιτείται Προσοχή";
        else return "Μέτρια Κατάσταση";
    }

    private String ReviewSummary(String category, float value) {
        switch (category) {
            case "sleepDuration":
                if (value < 6) return "🔻 Πολύ λίγος ύπνος!";
                else if (value > 9) return "🔺 Πολύς ύπνος.";
                else return "✅ Ικανοποιητική διάρκεια ύπνου.";
            case "heartRate":
                if (value < 60) return "⚠️ Πολύ χαμηλός καρδιακός ρυθμός.";
                else if (value > 100) return "⚠️ Αυξημένος καρδιακός ρυθμός.";
                else return "✅ Φυσιολογικός ρυθμός.";
            case "steps":
                if (value < 3000) return "🔻 Περισσότερη κίνηση θα βοηθήσει.";
                else if (value >= 10000) return "🏃 Άψογη φυσική δραστηριότητα!";
                else return "👍 Καλή προσπάθεια, συνέχισε.";
            case "stressLevel":
                if (value > 7) return "⚠️ Αυξημένο άγχος.";
                else if (value < 3) return "😌 Χαμηλό άγχος.";
                else return "😐 Μέτριο επίπεδο άγχους.";
            case "mood":
                if (value < 4) return "😞 Χαμηλή διάθεση.";
                else if (value >= 8) return "😄 Εξαιρετική διάθεση!";
                else return "🙂 Καλή διάθεση.";
            case "energyLevel":
                if (value < 4) return "😴 Χαμηλή ενέργεια.";
                else if (value >= 8) return "⚡ Υψηλή ενέργεια!";
                else return "👌 Ικανοποιητικό επίπεδο.";
            case "weight":
                return "📊 Βάρος: παρακολούθηση συνεχίζεται.";
            default:
                return "";
        }
    }

    private void ShowNotepad() {
        Intent intent = new Intent(HealthAnalysisScreen.this, UserComments.class);
        intent.putExtra("category", category); //
        startActivity(intent);
    }

}
