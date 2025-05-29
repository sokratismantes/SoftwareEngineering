package com.example.smartmed1.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed1.DatabaseHelper;
import com.example.smartmed1.Files;
import com.example.smartmed1.R;
import com.example.smartmed1.model.ScoreResult;
import com.example.smartmed1.service.QuizEngine;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultSummaryScreen extends AppCompatActivity {
    private static final String EXTRA_RESULT_ID = "extra_result_id";

    private QuizEngine quizEngine;
    private DatabaseHelper dbh;
    private int resultId;

    private ProgressBar anxietyBar, depressionBar, wellbeingBar;
    private TextView tvAnxietyScore, tvDepressionScore, tvWellbeingScore;
    private TextView tvLevelSummary, tvAdvice, tvTrend;
    private LinearLayout warningsContainer;
    private Button btnPickStart, btnPickEnd, btnApplyFilter;
    private Button btnShare, btnExport;

    private LineChart chartTrends;

    private long startTimestamp, endTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultsummaryscreen);

        quizEngine = new QuizEngine(this);
        dbh = new DatabaseHelper(this);

        // Read the resultId if you need it for "Share" flow
        resultId = getIntent().getIntExtra(EXTRA_RESULT_ID, -1);


        ScoreResult initial = quizEngine.calculateScore();
        dbh.saveQuizResult(
                initial.getAnxietyScore(),
                initial.getDepressionScore(),
                initial.getWellbeingScore()
        );

        // 2) find views
        anxietyBar      = findViewById(R.id.progressAnxiety);
        depressionBar   = findViewById(R.id.progressDepression);
        wellbeingBar    = findViewById(R.id.progressWellbeing);
        tvAnxietyScore    = findViewById(R.id.tvAnxietyScore);
        tvDepressionScore = findViewById(R.id.tvMoodScore);
        tvWellbeingScore  = findViewById(R.id.tvWellbeingScore);
        tvLevelSummary  = findViewById(R.id.tvLevelSummary);
        tvAdvice        = findViewById(R.id.tvAdvice);
        tvTrend         = findViewById(R.id.tvTrend);
        warningsContainer = findViewById(R.id.warningsContainer);

        btnPickStart   = findViewById(R.id.btnPickStart);
        btnPickEnd     = findViewById(R.id.btnPickEnd);
        btnApplyFilter = findViewById(R.id.btnApplyFilter);
        btnShare       = findViewById(R.id.btnShareResults);
        btnExport      = findViewById(R.id.btnExportPdf);
        // 6) share/export
        btnShare.setOnClickListener(v ->
                ShareFormScreen.start(
                        ResultSummaryScreen.this,
                        resultId,
                        startTimestamp,
                        endTimestamp
                )
        );
        btnExport.setOnClickListener(v -> {
            ScoreResult result = quizEngine.calculateScore();

            String doctorEmail = null;
            Files.exportAndShareResults(
                    ResultSummaryScreen.this,
                    result,
                    doctorEmail,
                    startTimestamp,
                    endTimestamp
            );
        });

        chartTrends    = findViewById(R.id.chartTrends);

        // 3) populate the static bars & labels
        int a = initial.getAnxietyScore();
        int d = initial.getDepressionScore();
        int w = initial.getWellbeingScore();
        anxietyBar.setProgress(a);
        depressionBar.setProgress(d);
        wellbeingBar.setProgress(w);
        tvAnxietyScore   .setText(a + "/100");
        tvDepressionScore.setText(d + "/100");
        tvWellbeingScore .setText(w + "/100");

        String levels = String.format(
                "Άγχος: %s, Κατάθλιψη: %s, Ευεξία: %s",
                initial.getAnxietyLevel(),
                initial.getDepressionLevel(),
                initial.getWellbeingLevel()
        );
        tvLevelSummary.setText(levels);
        tvAdvice      .setText(initial.getAdvice());

        // 4) default date window = last 7 days
        Calendar cal = Calendar.getInstance();
        endTimestamp   = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        startTimestamp = cal.getTimeInMillis();
        btnPickStart.setText("Από " + dateString(startTimestamp));
        btnPickEnd  .setText("Έως " + dateString(endTimestamp));

        // 5) wire buttons
        btnPickStart .setOnClickListener(v -> pickDate(true));
        btnPickEnd   .setOnClickListener(v -> pickDate(false));
        btnApplyFilter.setOnClickListener(v -> computeAndShowTrend());

        chartTrends.animate().cancel();
        chartTrends.getAnimator().setPhaseX(1f);
        chartTrends.getAnimator().setPhaseY(1f);

        // initial render
        computeAndShowTrend();
    }

    private void pickDate(boolean isStart) {
        Calendar cur = Calendar.getInstance();
        cur.setTimeInMillis(isStart ? startTimestamp : endTimestamp);

        new DatePickerDialog(this,
                (view, year, month, day) -> {
                    Calendar sel = Calendar.getInstance();
                    sel.set(year, month, day, 0, 0, 0);
                    if (isStart) {
                        startTimestamp = sel.getTimeInMillis();
                        btnPickStart.setText("Από " + dateString(startTimestamp));
                    } else {
                        sel.set(year, month, day, 23, 59, 59);
                        endTimestamp = sel.getTimeInMillis();
                        btnPickEnd.setText("Έως " + dateString(endTimestamp));
                    }
                },
                cur.get(Calendar.YEAR),
                cur.get(Calendar.MONTH),
                cur.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private String dateString(long ts) {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date(ts));
    }

    private void computeAndShowTrend() {
        // 1) fetch history
        List<DatabaseHelper.QuizResultRecord> hist =
                dbh.getQuizHistory(startTimestamp, endTimestamp);

        // 2) require at least 2 points
        if (hist.size() < 2) {
            chartTrends.clear();
            chartTrends.setVisibility(View.GONE);
            tvTrend.setVisibility(View.VISIBLE);
            tvTrend.setText("Δεν υπάρχουν αρκετά δεδομένα για τον επιλεγμένο χρόνο.");
            return;
        }

        // 3) build entries
        long rawSpanMs = endTimestamp - startTimestamp;
        float spanDays = Math.max(rawSpanMs / (1000f*60*60*24), 0f);

        List<Entry> anxE = new ArrayList<>(), depE = new ArrayList<>(), welE = new ArrayList<>();
        for (DatabaseHelper.QuizResultRecord r : hist) {
            float x = (r.timestamp - startTimestamp) / (1000f*60*60*24);
            anxE.add(new Entry(x, r.anxiety));
            depE.add(new Entry(x, r.depression));
            welE.add(new Entry(x, r.wellbeing));
        }

        LineDataSet sAnx = new LineDataSet(anxE, "Άγχος");
        LineDataSet sDep = new LineDataSet(depE, "Κατάθλιψη");
        LineDataSet sWel = new LineDataSet(welE, "Ευεξία");
        // only cubic if 3+ points
        LineDataSet.Mode mode = (hist.size() >= 3)
                ? LineDataSet.Mode.CUBIC_BEZIER
                : LineDataSet.Mode.LINEAR;
        sAnx.setMode(mode);
        sDep.setMode(mode);
        sWel.setMode(mode);
        sAnx.setDrawValues(false);
        sDep.setDrawValues(false);
        sWel.setDrawValues(false);

        // 4) set data
        chartTrends.setData(new LineData(sAnx, sDep, sWel));

        // 5) configure X axis
        XAxis xAxis = chartTrends.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(spanDays);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat fmt =
                    new SimpleDateFormat("dd/MM", Locale.getDefault());
            @Override
            public String getFormattedValue(float value) {
                long millis = startTimestamp + (long)(value * 1000*60*60*24);
                return fmt.format(new Date(millis));
            }
        });

        chartTrends.getDescription().setText(
                "Τάσεις από " + dateString(startTimestamp) +
                        " έως " + dateString(endTimestamp)
        );

        // 6) finally try to draw safely
        try {
            chartTrends.invalidate();
            chartTrends.setVisibility(View.VISIBLE);
            tvTrend.setVisibility(View.GONE);
        } catch (NegativeArraySizeException ex) {
            Log.w("ResultSummary", "Chart draw skipped – invalid data range", ex);
            chartTrends.clear();
            chartTrends.setVisibility(View.GONE);
            tvTrend.setVisibility(View.VISIBLE);
            tvTrend.setText("Δεν υπάρχουν αρκετά έγκυρα δεδομένα για το διάστημα.");
        }

        // rest of your warnings + share/export wiring…
        warningsContainer.removeAllViews();
        for (String warn : quizEngine.calculateScore().getWarnings()) {
            TextView tv = new TextView(this);
            tv.setText("• " + warn);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(14f);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(0, 8, 0, 0);
            tv.setLayoutParams(lp);
            warningsContainer.addView(tv);
        }
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
