package com.example.smartmed1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed1.R;
import com.example.smartmed1.model.ScoreResult;
import com.example.smartmed1.service.PDFExporter;
import com.example.smartmed1.service.QuizEngine;

import java.io.File;

public class ResultSummaryScreen extends AppCompatActivity {
    private ProgressBar anxietyBar, depressionBar;
    private TextView tvAnxietyLabel, tvMoodLabel;
    private Button btnShare, btnExport;
    private QuizEngine quizEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_summary_screen);

        // 1) Initialize engine & compute
        quizEngine = new QuizEngine();
        ScoreResult result = quizEngine.calculateScore();

        // 2) Find views
        ImageView ivIcon           = findViewById(R.id.ivSummaryIcon);
        TextView tvTitle           = findViewById(R.id.tvSummaryTitle);
        anxietyBar                 = findViewById(R.id.progressAnxiety);
        depressionBar              = findViewById(R.id.progressDepression);
        TextView tvWarningsHeader  = findViewById(R.id.tvWarningsHeader);
        TextView tvWarn1           = findViewById(R.id.tvWarn1);
        TextView tvWarn2           = findViewById(R.id.tvWarn2);
        TextView tvMoreInfo        = findViewById(R.id.tvMoreInfo);
        btnShare                   = findViewById(R.id.btnShareResults);
        btnExport                  = findViewById(R.id.btnExportPdf);

        // after fetching result in onCreate()
        anxietyBar.setProgress(result.getAnxietyScore());
        depressionBar.setProgress(result.getDepressionScore());

        // 4) Populate warnings text
        tvWarn1.setText("• Οι απαντήσεις σας υποδεικνύουν ότι ενδέχεται να αντιμετωπίζετε συμπτώματα μέτριου άγχους.");
        tvWarn2.setText("• Οι απαντήσεις σας υποδεικνύουν ότι ενδέχεται να αντιμετωπίζετε συμπτώματα ήπιας κατάθλιψης.");

        // 5) Info link listener
        tvMoreInfo.setOnClickListener(v -> {
            // TODO: launch an info screen or webview
        });

        // 6) Button listeners
        btnShare.setOnClickListener(v ->
                ShareFormScreen.start(this, /* TODO: pass real result ID */ 0)
        );

        btnExport.setOnClickListener(v -> {
            PDFExporter exporter = new PDFExporter();
            try {
                File pdf = exporter.preparePDF(this, result);
                // TODO: share/view via FileProvider
                Intent view = new Intent(Intent.ACTION_VIEW);
                // configure view Intent here...
            } catch (Exception e) {
                startActivity(new Intent(this, NoValidExportScreen.class));
            }
        });
    }
}
