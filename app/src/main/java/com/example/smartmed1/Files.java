package com.example.smartmed1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.smartmed1.model.ScoreResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Files {

    private static final List<String> fileList = new ArrayList<>();
    static {
        fileList.add("Αιματολογικές.pdf");
        fileList.add("Ακτινογραφίες.pdf");
        fileList.add("Συνταγή_Ιανουάριος.pdf");
    }



    public static boolean fileExists(Context context) {
        return !fileList.isEmpty();
    }

    public static List<String> getFiles(Context context) {
        return new ArrayList<>(fileList);
    }

    public static void openFile(String filename) {
        System.out.println("Άνοιγμα αρχείου: " + filename);
    }



    /**
     * 1) Generates a PDF of the mental‐health questionnaire results.
     * 2) Fires an email Intent to send it to the given address.
     *
     * @param ctx          a valid Context (e.g. your Activity)
     * @param result       the ScoreResult containing scores
     * @param doctorEmail  recipient’s email (from ShareFormScreen)
     */
    public static void exportAndShareResults(Context ctx,
                                             ScoreResult result,
                                             String doctorEmail,
                                             long startTs,
                                             long endTs) {
        try {
            // ─── 1. Build the PDF page ────────────────────────────────────────
            PdfDocument doc = new PdfDocument();
            PdfDocument.PageInfo info = new PdfDocument.PageInfo
                    .Builder(595, 842, 1)
                    .create();
            PdfDocument.Page page = doc.startPage(info);
            Canvas c = page.getCanvas();

            // Title
            Paint titlePaint = new Paint();
            titlePaint.setTextSize(18);
            c.drawText(
                    "Αποτελέσματα Ερωτηματολογίου Ψυχικής Υγείας",
                    20, 40, titlePaint
            );

            // Scores
            Paint bodyPaint = new Paint();
            bodyPaint.setTextSize(14);
            int y = 80;
            c.drawText("Βαθμολογία Άγχους: " + result.getAnxietyScore(), 20, y, bodyPaint);
            y += 30;
            c.drawText("Βαθμολογία Διάθεσης: " + result.getDepressionScore(), 20, y, bodyPaint);
            y += 30;
            c.drawText("Ευεξία: " + result.getWellbeingScore(), 20, y, bodyPaint);
            y += 40;

            // ─── 2. Compute trends for timeframe ──────────────────────────────
            DatabaseHelper dbh = new DatabaseHelper(ctx);
            List<DatabaseHelper.QuizResultRecord> history =
                    dbh.getQuizHistory(startTs, endTs);

            String trendText;
            if (history.isEmpty()) {
                trendText = "Δεν βρέθηκαν δεδομένα σε αυτή την περίοδο.";
            } else if (history.size() == 1) {
                trendText = "Μόνο ένα αποτέλεσμα σε αυτή την περίοδο.";
            } else {
                DatabaseHelper.QuizResultRecord latest = history.get(0);
                int count = history.size() - 1;
                float sumA = 0, sumD = 0, sumW = 0;
                for (int i = 1; i < history.size(); i++) {
                    sumA += history.get(i).anxiety;
                    sumD += history.get(i).depression;
                    sumW += history.get(i).wellbeing;
                }
                float avgA = sumA / count;
                float avgD = sumD / count;
                float avgW = sumW / count;

                StringBuilder sb = new StringBuilder();
                // Anxiety trend
                if (latest.anxiety > avgA + 5) {
                    sb.append("Το άγχος έχει αυξηθεί σε σχέση με το μέσο όρο.");
                } else if (latest.anxiety < avgA - 5) {
                    sb.append("Το άγχος έχει βελτιωθεί σε σχέση με το μέσο όρο.");
                } else {
                    sb.append("Το άγχος παραμένει σταθερό.");
                }
                sb.append("\n");
                // Depression trend
                if (latest.depression > avgD + 5) {
                    sb.append("Η κατάθλιψη έχει αυξηθεί σε σχέση με το μέσο όρο.");
                } else if (latest.depression < avgD - 5) {
                    sb.append("Η κατάθλιψη έχει βελτιωθεί σε σχέση με το μέσο όρο.");
                } else {
                    sb.append("Τα συμπτώματα κατάθλιψης παραμένουν σταθερά.");
                }
                sb.append("\n");
                // Wellbeing trend
                if (latest.wellbeing < avgW - 5) {
                    sb.append("Η ευεξία μειώθηκε σημαντικά.");
                } else if (latest.wellbeing > avgW + 5) {
                    sb.append("Η ευεξία βελτιώθηκε σημαντικά.");
                } else {
                    sb.append("Η ευεξία παραμένει σταθερή.");
                }

                trendText = sb.toString();
            }

            // Draw the trend text
            Paint trendPaint = new Paint();
            trendPaint.setTextSize(12);
            for (String line : trendText.split("\n")) {
                c.drawText(line, 20, y, trendPaint);
                y += 20;
            }

            // Finish the page
            doc.finishPage(page);

            // ─── 3. Write PDF to cache ───────────────────────────────────────
            File pdfFile = new File(ctx.getCacheDir(), "mhq_results.pdf");
            try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                doc.writeTo(fos);
            } finally {
                doc.close();
            }

            // ─── 4. Build share Intents ──────────────────────────────────────
            Uri contentUri = FileProvider.getUriForFile(
                    ctx,
                    "com.example.smartmed1.fileprovider",
                    pdfFile
            );

            // a) Email-only chooser
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + doctorEmail));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "Αποτελέσματα Ψυχικής Υγείας");
            emailIntent.putExtra(Intent.EXTRA_TEXT,
                    "Παρακαλώ βρείτε τα αποτελέσματα επισυναπτόμενα.");
            emailIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // b) Generic PDF share
            Intent genericIntent = new Intent(Intent.ACTION_SEND);
            genericIntent.setType("application/pdf");
            genericIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            genericIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Combine into one chooser
            Intent chooser = Intent.createChooser(emailIntent,
                    "Αποστολή αποτελεσμάτων");
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    new Intent[]{ genericIntent });

            ctx.startActivity(chooser);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ctx,
                    "Σφάλμα κατά τη δημιουργία/κοινοποίηση PDF.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
