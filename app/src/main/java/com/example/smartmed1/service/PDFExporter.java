// app/src/main/java/com/example/smartmed1/service/PDFExporter.java
package com.example.smartmed1.service;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

import com.example.smartmed1.model.ScoreResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFExporter {

    /**
     * Generates a PDF summarizing the mental health scores.
     * @param ctx     any Context (used to access cache directory)
     * @param result  the ScoreResult with anxiety & depression values
     * @return        a File pointing to the generated PDF
     * @throws IOException if writing fails
     */
    public File preparePDF(Context ctx, ScoreResult result) throws IOException {
        // 1. Create a new PdfDocument
        PdfDocument document = new PdfDocument();

        // 2. Define page size (A4-ish in points: 595×842)
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                .Builder(595, 842, 1)
                .create();

        // 3. Start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // 4. Draw content
        Paint titlePaint = new Paint();
        titlePaint.setTextSize(18);
        canvas.drawText("Αποτελέσματα Ερωτηματολογίου Ψυχικής Υγείας", 20, 40, titlePaint);

        Paint bodyPaint = new Paint();
        bodyPaint.setTextSize(14);
        int yPosition = 80;
        canvas.drawText("Βαθμολογία Άγχους: " + result.getAnxietyScore(), 20, yPosition, bodyPaint);
        yPosition += 30;
        canvas.drawText("Βαθμολογία Διάθεσης: " + result.getDepressionScore(), 20, yPosition, bodyPaint);

        // Optionally, add warnings or summaries here...

        // 5. Finish the page
        document.finishPage(page);

        // 6. Write the document to a file in cache
        File pdfFile = new File(ctx.getCacheDir(), "mhq_results.pdf");
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            document.writeTo(fos);
        } finally {
            document.close();
        }

        return pdfFile;
    }
}
