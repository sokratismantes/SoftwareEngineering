package com.example.smartmed1;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_results);

        int anx = getIntent().getIntExtra("ANX", 0);
        int dep = getIntent().getIntExtra("DEP", 0);

        ProgressBar pbAnx = findViewById(R.id.pbAnxiety);
        ProgressBar pbDep = findViewById(R.id.pbDepression);
        TextView  tvAnx = findViewById(R.id.tvAnxietyLabel);
        TextView  tvDep = findViewById(R.id.tvDepressionLabel);
        TextView  tvWarn= findViewById(R.id.tvWarnings);

        pbAnx.setProgress(anx);
        pbDep.setProgress(dep);
        tvAnx.setText("Άγχος: " + anx);
        tvDep.setText("Διάθεση: " + dep);

        // Simple warnings
        StringBuilder warns = new StringBuilder();
        if (anx > 70) warns.append("Υψηλό επίπεδο άγχους.\n");
        if (dep < 30) warns.append("Χαμηλή διάθεση.\n");
        tvWarn.setText(warns.toString());

        findViewById(R.id.btnShare).setOnClickListener(v -> shareResults(anx, dep));
        findViewById(R.id.btnPdf).setOnClickListener(v -> exportPdf(anx, dep));
    }

    private void shareResults(int anx, int dep) {
        String txt = "Αποτελέσματα Έρευνας:\nΆγχος: " + anx + "\nΔιάθεση: " + dep;
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_TEXT, txt);
        startActivity(Intent.createChooser(send, "Κοινοποίηση μέσω"));
    }

    private void exportPdf(int anx, int dep) {
        // Create a new PDF document
        PdfDocument doc = new PdfDocument();
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = doc.startPage(info);
        Canvas c = page.getCanvas();
        Paint p = new Paint();
        p.setTextSize(12);
        c.drawText("Έρευνα Ψυχικής Υγείας", 10, 25, p);
        c.drawText("Άγχος: " + anx, 10, 50, p);
        c.drawText("Διάθεση: " + dep, 10, 70, p);
        doc.finishPage(page);

        File out = new File(getExternalFilesDir(null), "survey_results.pdf");
        try (FileOutputStream fos = new FileOutputStream(out)) {
            doc.writeTo(fos);
            Toast.makeText(this,
                    "Αποθηκεύθηκε: " + out.getAbsolutePath(),
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Σφάλμα κατά την αποθήκευση PDF",
                    Toast.LENGTH_SHORT).show();
        } finally {
            doc.close();
        }
    }
}
