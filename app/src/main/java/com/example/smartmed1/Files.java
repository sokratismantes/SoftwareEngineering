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
     * @param ctx          a valid Context (e.g. your Activity)
     * @param result       the ScoreResult containing scores
     * @param doctorEmail  recipient’s email (from ShareFormScreen)
     */
    public static void exportAndShareResults(Context ctx,
                                             ScoreResult result,
                                             String doctorEmail) {
        try {
            // ─── 1. Build the PDF ────────────────────────────────────────────────
            PdfDocument doc = new PdfDocument();
            PdfDocument.PageInfo info = new PdfDocument.PageInfo
                    .Builder(595, 842, 1).create();
            PdfDocument.Page page = doc.startPage(info);
            Canvas c = page.getCanvas();

            // Title
            Paint title = new Paint();
            title.setTextSize(18);
            c.drawText(
                    "Αποτελέσματα Ερωτηματολογίου Ψυχικής Υγείας",
                    20, 40, title
            );

            // Body
            Paint body = new Paint();
            body.setTextSize(14);
            int y = 80;
            c.drawText("Βαθμολογία Άγχους: " + result.getAnxietyScore(), 20, y, body);
            y += 30;
            c.drawText("Βαθμολογία Διάθεσης: " + result.getDepressionScore(), 20, y, body);

            doc.finishPage(page);

            // Write out
            File pdf = new File(ctx.getCacheDir(), "mhq_results.pdf");
            try (FileOutputStream fos = new FileOutputStream(pdf)) {
                doc.writeTo(fos);
            }
            doc.close();

            // ─── 2. Send via email Intent ───────────────────────────────────────
            Uri contentUri = FileProvider.getUriForFile(
                    ctx,
                    "com.example.smartmed1.fileprovider",
                    pdf
            );

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_EMAIL, new String[]{ doctorEmail });
            share.putExtra(Intent.EXTRA_SUBJECT,
                    "Αποτελέσματα Ερωτηματολογίου Ψυχικής Υγείας");
            share.putExtra(Intent.EXTRA_TEXT,
                    "Παρακαλώ βρείτε τα αποτελέσματα επισυναπτόμενα.");
            share.putExtra(Intent.EXTRA_STREAM, contentUri);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            ctx.startActivity(
                    Intent.createChooser(share, "Αποστολή αποτελεσμάτων μέσω email")
            );

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ctx,
                    "Σφάλμα κατά την δημιουργία/αποστολή PDF.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
