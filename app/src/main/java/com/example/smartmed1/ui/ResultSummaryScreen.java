package com.example.smartmed1.ui;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed1.DatabaseHelper;
import com.example.smartmed1.Files;
import com.example.smartmed1.R;
import com.example.smartmed1.model.Answer;
import com.example.smartmed1.model.ScoreResult;
import com.example.smartmed1.service.QuizEngine;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Collections;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

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
        setContentView(R.layout.activity_result_summary_screen);

        quizEngine = new QuizEngine(this);
        dbh = new DatabaseHelper(this);

        // Read the resultId if you need it for "Share" flow
        resultId = getIntent().getIntExtra(EXTRA_RESULT_ID, -1);

        // 1) compute & persist this run
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
        chartTrends    = findViewById(R.id.chartTrends);

        btnPickStart   = findViewById(R.id.btnPickStart);
        btnPickEnd     = findViewById(R.id.btnPickEnd);
        btnApplyFilter = findViewById(R.id.btnApplyFilter);
        Button btnShare   = findViewById(R.id.btnShareResults);
        Button btnExport  = findViewById(R.id.btnExportPdf);
        ScoreResult result       = quizEngine.calculateScore();       // or quizEngine.getResultById(resultId)
        long        startTs      = this.startTimestamp;
        long        endTs        = this.endTimestamp;
        String      doctorEmail  = null;
        LineChart   chart        = chartTrends;

        // 6) share/export
        // 1) “Share” just invokes your existing chooser flow:
        btnShare.setOnClickListener(v ->
                Files.exportAndShareResults(
                        /* ctx: */        ResultSummaryScreen.this,
                        /* result: */     result,
                        /* doctorEmail: */doctorEmail,
                        /* startTs: */    startTs,
                        /* endTs: */      endTs,
                        /* chart: */      chart
                )
        );

// 2) “Export” only generates & saves the PDF, then notifies the user:
        btnExport.setOnClickListener(v -> {
            try {
                File pdf = Files.generateResultsPdf(
                        ResultSummaryScreen.this,
                        initial,
                        startTimestamp,
                        endTimestamp,
                        chartTrends
                );

                Toast.makeText(
                        ResultSummaryScreen.this,
                        "PDF αποθηκεύτηκε στο: " + pdf.getAbsolutePath(),
                        Toast.LENGTH_LONG
                ).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(
                        ResultSummaryScreen.this,
                        "Σφάλμα κατά την αποθήκευση PDF:\n" + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });


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

        // DEBUG only: ten days of fake history
        long now = System.currentTimeMillis();
        long oneDay = 24L*60*60*1000;
        for (int i = 0; i < 10; i++) {
            long ts = now - (9 - i)*oneDay;
            int  anx = 20 + i*5;
            int  dep = 30 + i*3;
            int  wel = 100 - Math.round((anx+dep)/2f);
            dbh.insertQuizHistoryAt(anx, dep, wel, ts);
        }

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
        // 1) fetch history (newest first)
        List<DatabaseHelper.QuizResultRecord> hist =
                dbh.getQuizHistory(startTimestamp, endTimestamp);

        // 2) need at least 2 points
        if (hist.size() < 2) {
            chartTrends.clear();
            chartTrends.setVisibility(View.GONE);
            tvTrend.setVisibility(View.VISIBLE);
            tvTrend.setText("Δεν υπάρχουν αρκετά δεδομένα για τον επιλεγμένο χρόνο.");
            return;
        }

        // 3) reverse so oldest→newest
        Collections.reverse(hist);

        // 4) build entries
        final float msPerDay = 1000f * 60 * 60 * 24;
        long rawSpan = endTimestamp - startTimestamp;
        float spanDays = Math.max(rawSpan / msPerDay, 1f);

        List<Entry> anxE = new ArrayList<>(), depE = new ArrayList<>(), welE = new ArrayList<>();
        for (DatabaseHelper.QuizResultRecord r : hist) {
            float x = (r.timestamp - startTimestamp) / msPerDay;
            anxE.add(new Entry(x, r.anxiety));
            depE.add(new Entry(x, r.depression));
            welE.add(new Entry(x, r.wellbeing));
        }
        // 5) ensure each list is sorted by X
        Collections.sort(anxE, (a, b) -> Float.compare(a.getX(), b.getX()));
        Collections.sort(depE, (a, b) -> Float.compare(a.getX(), b.getX()));
        Collections.sort(welE, (a, b) -> Float.compare(a.getX(), b.getX()));

        // 6) make datasets
        LineDataSet sAnx = new LineDataSet(anxE, "Άγχος");
        LineDataSet sDep = new LineDataSet(depE, "Κατάθλιψη");
        LineDataSet sWel = new LineDataSet(welE, "Ευεξία");
        LineDataSet.Mode mode = hist.size() >= 3
                ? LineDataSet.Mode.CUBIC_BEZIER
                : LineDataSet.Mode.LINEAR;

        // bright, distinct colors
        int cAnx = Color.rgb(229, 57,  53);
        int cDep = Color.rgb(30,  136, 229);
        int cWel = Color.rgb(67,  160, 71);

        for (LineDataSet set : Arrays.asList(sAnx, sDep, sWel)) {
            set.setMode(mode);
            set.setLineWidth(3f);
            set.setCircleRadius(5f);
            set.setDrawCircles(true);
            set.setDrawValues(false);
        }
        sAnx.setColor(cAnx); sAnx.setCircleColor(cAnx);
        sDep.setColor(cDep); sDep.setCircleColor(cDep);
        sWel.setColor(cWel); sWel.setCircleColor(cWel);

        // 7) reset any zoom/pan
        chartTrends.fitScreen();

        // 8) feed data
        chartTrends.setData(new LineData(sAnx, sDep, sWel));

        // 9) style axes & legend
        chartTrends.getLegend().setTextColor(Color.WHITE);
        chartTrends.getLegend().setForm(Legend.LegendForm.CIRCLE);

        XAxis x = chartTrends.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setTextColor(Color.WHITE);
        x.setAxisLineColor(Color.WHITE);
        x.setGridColor(Color.argb(60,255,255,255));
        x.setAxisMinimum(0f);
        x.setAxisMaximum(spanDays);
        x.setGranularity(1f);
        x.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat fmt =
                    new SimpleDateFormat("dd/MM", Locale.getDefault());
            @Override
            public String getFormattedValue(float value) {
                long ms = startTimestamp + (long)(value * msPerDay);
                return fmt.format(new Date(ms));
            }
        });

        YAxis left = chartTrends.getAxisLeft();
        left.setTextColor(Color.WHITE);
        left.setAxisLineColor(Color.WHITE);
        left.setGridColor(Color.argb(60,255,255,255));
        left.setAxisMinimum(0f);
        left.setAxisMaximum(100f);
        chartTrends.getAxisRight().setEnabled(false);

        chartTrends.getDescription().setText(
                "Τάσεις από " + dateString(startTimestamp)
                        + " έως " + dateString(endTimestamp)
        );
        chartTrends.getDescription().setTextColor(Color.WHITE);

        // 10) disable zooming (so no NegativeArray crashes)
        chartTrends.setScaleXEnabled(false);
        chartTrends.setScaleYEnabled(false);
        chartTrends.setPinchZoom(false);
        chartTrends.setDoubleTapToZoomEnabled(false);

        // 11) draw it
        chartTrends.animateX(600);
        chartTrends.invalidate();
        chartTrends.setVisibility(View.VISIBLE);
        tvTrend.setVisibility(View.GONE);

        // 12) warnings (unchanged)
        warningsContainer.removeAllViews();
        for (String w : quizEngine.calculateScore().getWarnings()) {
            TextView tv = new TextView(this);
            tv.setText("• " + w);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(14f);
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
            lp.setMargins(0,8,0,0);
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
