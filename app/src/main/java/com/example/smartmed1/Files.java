package com.example.smartmed1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.widget.Toast;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.example.smartmed1.model.ScoreResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.graphics.Bitmap;
import android.os.Environment;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.smartmed1.model.ScoreResult;
import com.example.smartmed1.model.Question;
import com.example.smartmed1.model.Answer;
import com.github.mikephil.charting.charts.LineChart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Files {

    private static final List<String> fileList = new ArrayList<>();

    static {
        fileList.add("Αιματολογικές.pdf");
        fileList.add("Ακτινογραφίες.pdf");
        fileList.add("Συνταγή_Ιανουάριος.pdf");
    }

    // ─── Existing methods ─────────────────────────────────────────────────────

    public static boolean fileExists(Context context) {
        return !fileList.isEmpty();
    }

    public static List<String> getFiles(Context context) {
        return new ArrayList<>(fileList);
    }

    public static void openFile(String filename) {
        System.out.println("Άνοιγμα αρχείου: " + filename);
    }

    // ─── New combined export & email logic ───────────────────────────────────

    /**
     * 1) Generates a PDF of the mental‐health questionnaire results.
     * 2) Fires an email Intent to send it to the given address.
     *
     * @param ctx         a valid Context (e.g. your Activity)
     * @param result      the ScoreResult containing scores
     * @param doctorEmail recipient’s email (from ShareFormScreen)
     * @param startTs     interval start
     * @param endTs       interval end
     * @param chart       the LineChart you want embedded
     */
    public static void exportAndShareResults(Context ctx,
                                             ScoreResult result,
                                             String doctorEmail,
                                             long startTs,
                                             long endTs,
                                             @Nullable LineChart chart) {
        try {
            File pdf = generateResultsPdf(ctx, result, startTs, endTs, chart);

            Uri contentUri = FileProvider.getUriForFile(
                    ctx,
                    "com.example.smartmed1.fileprovider",
                    pdf
            );

            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:" + doctorEmail));
            email.putExtra(Intent.EXTRA_SUBJECT, "Αποτελέσματα Ψυχικής Υγείας");
            email.putExtra(Intent.EXTRA_TEXT, "Παρακαλώ βρείτε τα αποτελέσματα επισυναπτόμενα.");
            email.putExtra(Intent.EXTRA_STREAM, contentUri);
            email.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent generic = new Intent(Intent.ACTION_SEND);
            generic.setType("application/pdf");
            generic.putExtra(Intent.EXTRA_STREAM, contentUri);
            generic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent chooser = Intent.createChooser(email, "Αποστολή αποτελεσμάτων");
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{ generic });

            ctx.startActivity(chooser);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ctx,
                    "Σφάλμα κατά τη δημιουργία/κοινοποίηση PDF.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    /** Service‐side entry: no chart, just delegate to the above with null */
    public static void exportAndShareResults(Context ctx,
                                             ScoreResult result,
                                             String doctorEmail,
                                             long startTs,
                                             long endTs) {
        exportAndShareResults(ctx, result, doctorEmail, startTs, endTs, /*chart=*/ null);
    }

    /**
     * Generates a PDF file containing:
     *  - Title + core scores
     *  - (Optional) the LineChart bitmap
     *  - All Q&A within [startTs, endTs]
     *
     * @return the File that was written
     */
    public static File generateResultsPdf(Context ctx,
                                          ScoreResult result,
                                          long startTs,
                                          long endTs,
                                          @Nullable LineChart chart) throws IOException {
        PdfDocument doc = new PdfDocument();
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = doc.startPage(info);
        Canvas c = page.getCanvas();

        // Background
        c.drawColor(Color.WHITE);

        // Title
        Paint titlePaint = new Paint();
        titlePaint.setTextSize(18f);
        titlePaint.setColor(Color.BLACK);
        c.drawText("Αποτελέσματα Ερωτηματολογίου Ψυχικής Υγείας", 20, 40, titlePaint);

        // Core scores
        Paint bodyPaint = new Paint();
        bodyPaint.setTextSize(14f);
        bodyPaint.setColor(Color.DKGRAY);
        int y = 80;
        c.drawText("Άγχος: " + result.getAnxietyScore(), 20, y, bodyPaint);
        y += 30;
        c.drawText("Κατάθλιψη: " + result.getDepressionScore(), 20, y, bodyPaint);
        y += 30;
        c.drawText("Ευεξία: " + result.getWellbeingScore(), 20, y, bodyPaint);
        y += 40;

        // Chart
        if (chart != null) {
            Bitmap bmp = chart.getChartBitmap();
            float wScale = (info.getPageWidth() - 40f) / bmp.getWidth();
            float hScale = 200f / bmp.getHeight();
            float scale = Math.min(wScale, hScale);
            int bmpW = (int)(bmp.getWidth() * scale);
            int bmpH = (int)(bmp.getHeight() * scale);
            Rect dst = new Rect(20, y, 20 + bmpW, y + bmpH);
            c.drawBitmap(bmp, null, dst, null);
            y += bmpH + 30;
        }

        // Q&A details
        DatabaseHelper dbh = new DatabaseHelper(ctx);
        List<Answer> answers = dbh.getAllSavedAnswersInRange(startTs, endTs);

        // Build question‐text map
        Map<Integer,String> qMap = new HashMap<>();
        for (Question q : dbh.getMentalHealthQuestions()) {
            qMap.put(q.getId(), q.getText());
        }

        for (Answer a : answers) {
            String qText = qMap.getOrDefault(a.getQuestionId(), "Ερώτηση #" + a.getQuestionId());
            c.drawText(qText, 20, y, bodyPaint);
            y += 18;
            c.drawText("→ " + a.getValue(), 30, y, bodyPaint);
            y += 28;

            // If you exceed the page height, call doc.finishPage(page),
            // start a new page, reset y, etc.
        }

        doc.finishPage(page);

        // — instead of getExternalFilesDir, write to PUBLIC Downloads folder:
        File downloads = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File folder = new File(downloads, "SmartMed");
        if (!folder.exists()) folder.mkdirs();

        File out = new File(folder, "mhq_results_" + System.currentTimeMillis() + ".pdf");

        try (FileOutputStream fos = new FileOutputStream(out)) {
            doc.writeTo(fos);
        }
        doc.close();

        return out;
    }
    private static String fmtDate(long ts) {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date(ts));
    }

    /**
     * helper for consistent indent
     */
    private static float thirtyTwoPx() {
        return 32f;
    }

    public static File generateResultsPdf(
            Context ctx,
            ScoreResult result,
            List<Answer> answers,
            Bitmap chartBmp,
            long startTs,
            long endTs
    ) throws IOException {
        // 1. Create document
        PdfDocument doc = new PdfDocument();
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = doc.startPage(info);
        Canvas c = page.getCanvas();

        // 2. Draw header
        Paint titleP = new Paint();
        titleP.setTextSize(18);
        c.drawText("Αποτελέσματα Ερωτηματολογίου", 20, 40, titleP);

        // 3. Draw the chart bitmap
        if (chartBmp != null) {
            // scale down if too large
            float left = 20, top = 60;
            c.drawBitmap(chartBmp, left, top, null);
            top += chartBmp.getHeight() + 20;
        }

        // 4. Draw each question + answer
        DatabaseHelper dbh = new DatabaseHelper(ctx);
        Paint bodyP = new Paint();
        bodyP.setTextSize(12);
        int y = 60 + (chartBmp != null ? chartBmp.getHeight() + 20 : 0);
        for (Answer a : answers) {
            String q = a.getQuestionText(dbh);    // you’ll need to add this helper
            String v = a.getValue();
            c.drawText(q + ": " + v, 20, y, bodyP);
            y += 20;
            if (y > 800) {
                doc.finishPage(page);
                page = doc.startPage(info);
                c = page.getCanvas();
                y = 40;
            }
        }

        // finish & write to file
        doc.finishPage(page);
        File pdfFile = new File(ctx.getCacheDir(), "mhq_results.pdf");
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            doc.writeTo(fos);
        }
        doc.close();
        return pdfFile;
    }
}