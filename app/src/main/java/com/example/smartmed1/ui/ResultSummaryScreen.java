package com.example.smartmed1.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed1.DatabaseHelper;
import com.example.smartmed1.R;
import com.example.smartmed1.model.ScoreResult;
import com.example.smartmed1.service.QuizEngine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultSummaryScreen extends AppCompatActivity {
    private ProgressBar anxietyBar, depressionBar, wellbeingBar;
    private TextView tvAnxietyScore, tvDepressionScore, tvWellbeingScore;
    private LinearLayout warningsContainer;
    private Button btnShare, btnExport;
    private QuizEngine quizEngine;
    private TextView tvLevelSummary, tvAdvice;
    private DatabaseHelper dbh;
    private Button btnPickStart, btnPickEnd, btnApplyFilter;
    private TextView tvTrend;
    private long startTimestamp, endTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // at top of onCreate()
        quizEngine = new QuizEngine(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_summary_screen);
        setResult(RESULT_OK);

// 0) get your helper
        dbh = new DatabaseHelper(this);


        // 1) score & persist
        ScoreResult result = quizEngine.calculateScore();
        dbh.saveQuizResult(
                result.getAnxietyScore(),
                result.getDepressionScore(),
                result.getWellbeingScore()
        );

        // 2) find all views
        anxietyBar      = findViewById(R.id.progressAnxiety);
        depressionBar   = findViewById(R.id.progressDepression);
        wellbeingBar    = findViewById(R.id.progressWellbeing);
        tvAnxietyScore    = findViewById(R.id.tvAnxietyScore);
        tvDepressionScore = findViewById(R.id.tvMoodScore);
        tvWellbeingScore  = findViewById(R.id.tvWellbeingScore);
        tvLevelSummary    = findViewById(R.id.tvLevelSummary);
        tvAdvice          = findViewById(R.id.tvAdvice);
        warningsContainer = findViewById(R.id.warningsContainer);
        btnShare          = findViewById(R.id.btnShareResults);
        btnExport         = findViewById(R.id.btnExportPdf);

        // 3) populate bars & scores
        int a = result.getAnxietyScore();
        int d = result.getDepressionScore();
        int w = result.getWellbeingScore();
        anxietyBar.setProgress(a);
        depressionBar.setProgress(d);
        wellbeingBar.setProgress(w);
        tvAnxietyScore.setText(a + "/100");
        tvDepressionScore.setText(d + "/100");
        tvWellbeingScore.setText(w + "/100");

        // 4) show the level summary and advice
        String levels = String.format(
                "Άγχος: %s, Κατάθλιψη: %s, Ευεξία: %s",
                result.getAnxietyLevel(),
                result.getDepressionLevel(),
                result.getWellbeingLevel()
        );
        tvLevelSummary.setText(levels);
        tvAdvice.setText(result.getAdvice());

        btnPickStart   = findViewById(R.id.btnPickStart);
        btnPickEnd     = findViewById(R.id.btnPickEnd);
        btnApplyFilter = findViewById(R.id.btnApplyFilter);
        tvTrend        = findViewById(R.id.tvTrend);

        // default: show the last 7 days
        Calendar cal = Calendar.getInstance();
        endTimestamp   = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        startTimestamp = cal.getTimeInMillis();
        btnPickStart.setText("Από " + dateString(startTimestamp));
        btnPickEnd.setText("Έως " + dateString(endTimestamp));

        btnPickStart.setOnClickListener(v -> pickDate(true));
        btnPickEnd.setOnClickListener(v -> pickDate(false));
        btnApplyFilter.setOnClickListener(v -> computeAndShowTrend());

        // compute initial trend
        computeAndShowTrend();
    }

    private void pickDate(boolean isStart) {
        // initialize dialog with current selection
        Calendar cur = Calendar.getInstance();
        cur.setTimeInMillis(isStart ? startTimestamp : endTimestamp);

        new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar sel = Calendar.getInstance();
                    sel.set(year, month, dayOfMonth, 0, 0, 0);
                    long ts = sel.getTimeInMillis();
                    if (isStart) {
                        startTimestamp = ts;
                        btnPickStart.setText("Από " + dateString(ts));
                    } else {
                        // set to end-of-day
                        sel.set(year, month, dayOfMonth, 23,59,59);
                        endTimestamp = sel.getTimeInMillis();
                        btnPickEnd.setText("Έως " + dateString(ts));
                    }
                },
                cur.get(Calendar.YEAR),
                cur.get(Calendar.MONTH),
                cur.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private String dateString(long ts) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return fmt.format(new Date(ts));
    }

    private void computeAndShowTrend() {
        List<DatabaseHelper.QuizResultRecord> hist =
                dbh.getQuizHistory(startTimestamp, endTimestamp);

        if (hist.isEmpty()) {
            tvTrend.setText("Δεν βρέθηκαν δεδομένα σε αυτή την περίοδο.");
            return;
        }
        // latest vs. average of the rest
        DatabaseHelper.QuizResultRecord latest = hist.get(0);
        if (hist.size() == 1) {
            tvTrend.setText("Μόνο ένα αποτέλεσμα στη σε αυτή την περίοδο.");
            return;
        }
        int count = hist.size() - 1;
        float sumA = 0, sumD = 0, sumW = 0;
        for (int i = 1; i < hist.size(); i++) {
            sumA += hist.get(i).anxiety;
            sumD += hist.get(i).depression;
            sumW += hist.get(i).wellbeing;
        }
        float avgA = sumA / count, avgD = sumD / count, avgW = sumW / count;

        StringBuilder sb = new StringBuilder();
        // anxiety trend
        if (latest.anxiety > avgA + 5) {
            sb.append("Το άγχος έχει αυξηθεί σε σχέση με το μέσο όρο.");
        } else if (latest.anxiety < avgA - 5) {
            sb.append("Η ευεξία στο άγχος βελτιώθηκε σε σχέση με το μέσο όρο.");
        } else {
            sb.append("Το άγχος παραμένει σταθερό.");
        }
        sb.append("\n");

        // depression trend
        if (latest.depression > avgD + 5) {
            sb.append("Η κατάθλιψη έχει αυξηθεί σε σχέση με το μέσο όρο.");
        } else if (latest.depression < avgD - 5) {
            sb.append("Βελτίωση στα συμπτώματα κατάθλιψης σε σχέση με το μέσο όρο.");
        } else {
            sb.append("Τα συμπτώματα κατάθλιψης παραμένουν σταθερά.");
        }
        sb.append("\n");

        // wellbeing trend (inverted logic)
        if (latest.wellbeing < avgW - 5) {
            sb.append("Η ευεξία μειώθηκε σημαντικά.");
        } else if (latest.wellbeing > avgW + 5) {
            sb.append("Η ευεξία βελτιώθηκε σημαντικά.");
        } else {
            sb.append("Η ευεξία παραμένει σταθερή.");
        }

        tvTrend.setText(sb.toString());

        ScoreResult result = quizEngine.calculateScore();

        // 5) warnings as you had them…
        warningsContainer.removeAllViews();
        for (String warn : result.getWarnings()) {
            TextView tv = new TextView(this);
            tv.setText("• " + warn);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(14f);
            // Use the fully-qualified LayoutParams constants:
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(0, 8, 0, 0);
            tv.setLayoutParams(lp);
            warningsContainer.addView(tv);
        }

        // 6) share/export… unchanged
        btnShare.setOnClickListener(v ->
                ShareFormScreen.start(this, /* resultId */0)
        );
        btnExport.setOnClickListener(v -> { /* … */ });
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
