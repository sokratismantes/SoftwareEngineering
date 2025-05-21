package com.example.smartmed1.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed1.R;
import com.example.smartmed1.model.ScoreResult;
import com.example.smartmed1.service.QuizEngine;

import java.util.List;

public class ResultSummaryScreen extends AppCompatActivity {
    private ProgressBar anxietyBar, depressionBar, wellbeingBar;
    private TextView tvAnxietyScore, tvDepressionScore, tvWellbeingScore;
    private LinearLayout warningsContainer;
    private Button btnShare, btnExport;
    private QuizEngine quizEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_summary_screen);

        // ensure we'll return RESULT_OK when this finishes
        setResult(RESULT_OK);

        quizEngine = QuizEngine.getInstance();
        ScoreResult result = quizEngine.calculateScore();

        // bind views
        anxietyBar      = findViewById(R.id.progressAnxiety);
        depressionBar   = findViewById(R.id.progressDepression);
        wellbeingBar    = findViewById(R.id.progressWellbeing);

        tvAnxietyScore    = findViewById(R.id.tvAnxietyScore);
        tvDepressionScore = findViewById(R.id.tvMoodScore);
        tvWellbeingScore  = findViewById(R.id.tvWellbeingScore);

        warningsContainer = findViewById(R.id.warningsContainer);

        btnShare         = findViewById(R.id.btnShareResults);
        btnExport        = findViewById(R.id.btnExportPdf);

        // populate bars & labels
        int a = result.getAnxietyScore();
        int d = result.getDepressionScore();
        int w = result.getWellbeingScore();

        anxietyBar.setProgress(a);
        depressionBar.setProgress(d);
        wellbeingBar.setProgress(w);

        tvAnxietyScore.setText(a + "/100");
        tvDepressionScore.setText(d + "/100");
        tvWellbeingScore.setText(w + "/100");

        // dynamic warnings
        warningsContainer.removeAllViews();
        List<String> warnings = result.getWarnings();
        for (String warn : warnings) {
            TextView tv = new TextView(this);
            tv.setText("• " + warn);
            tv.setTextColor(getColor(android.R.color.white));
            tv.setTextSize(14f);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 8, 0, 0);
            tv.setLayoutParams(lp);
            warningsContainer.addView(tv);
        }

        // optional “More info” link
        findViewById(R.id.tvMoreInfo).setOnClickListener(v -> {
            // TODO
        });

        // share/export listeners…
        btnShare.setOnClickListener(v ->
                ShareFormScreen.start(this, /* resultId */ 0)
        );
        btnExport.setOnClickListener(v -> {
            // …
        });
    }

    @Override
    public void finish() {
        // reaffirm RESULT_OK on any exit
        setResult(RESULT_OK);
        super.finish();
    }
}
