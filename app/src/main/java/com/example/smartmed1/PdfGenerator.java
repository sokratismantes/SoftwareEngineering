package com.example.smartmed1;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PdfGenerator {
    private Context context;

    public PdfGenerator(Context context) {
        this.context = context;
    }

    public void generatePrescriptionPdf(String prescriptionCode, String doctorName, 
                                      String diagnosis, String medications, String instructions) {
        try {
            // Create a new PDF document
            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            // Get the canvas to draw on
            android.graphics.Canvas canvas = page.getCanvas();
            android.graphics.Paint paint = new android.graphics.Paint();
            paint.setColor(android.graphics.Color.BLACK);
            paint.setTextSize(12);

            // Draw the content
            float y = 50;
            float lineHeight = 20;

            // Title
            paint.setTextSize(16);
            paint.setFakeBoldText(true);
            canvas.drawText("Συνταγή Φαρμάκων", 50, y, paint);
            y += lineHeight * 2;

            // Reset text size for content
            paint.setTextSize(12);
            paint.setFakeBoldText(false);

            // Prescription Code
            canvas.drawText("Κωδικός Συνταγής: " + prescriptionCode, 50, y, paint);
            y += lineHeight;

            // Doctor Name
            canvas.drawText("Ιατρός: " + doctorName, 50, y, paint);
            y += lineHeight;

            // Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            canvas.drawText("Ημερομηνία: " + sdf.format(new Date()), 50, y, paint);
            y += lineHeight * 2;

            // Diagnosis
            paint.setFakeBoldText(true);
            canvas.drawText("Διάγνωση:", 50, y, paint);
            y += lineHeight;
            paint.setFakeBoldText(false);
            canvas.drawText(diagnosis, 50, y, paint);
            y += lineHeight * 2;

            // Medications
            paint.setFakeBoldText(true);
            canvas.drawText("Φάρμακα:", 50, y, paint);
            y += lineHeight;
            paint.setFakeBoldText(false);
            for (String line : medications.split("\n")) {
                canvas.drawText(line, 50, y, paint);
                y += lineHeight;
            }
            y += lineHeight;

            // Instructions
            paint.setFakeBoldText(true);
            canvas.drawText("Οδηγίες:", 50, y, paint);
            y += lineHeight;
            paint.setFakeBoldText(false);
            for (String line : instructions.split("\n")) {
                canvas.drawText(line, 50, y, paint);
                y += lineHeight;
            }

            // Finish the page
            document.finishPage(page);

            // Create the file
            File downloadsDir;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            } else {
                downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            }

            if (downloadsDir != null && downloadsDir.exists()) {
                String fileName = "Prescription_" + prescriptionCode + "_" + sdf.format(new Date()) + ".pdf";
                File file = new File(downloadsDir, fileName);

                // Write the document to the file
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    document.writeTo(fos);
                    Toast.makeText(context, "Το PDF αποθηκεύτηκε στο φάκελο Downloads", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Δεν ήταν δυνατή η πρόσβαση στο φάκελο Downloads", Toast.LENGTH_SHORT).show();
            }

            // Close the document
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Σφάλμα κατά τη δημιουργία του PDF", Toast.LENGTH_SHORT).show();
        }
    }
} 