package com.example.smartmed1;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrescriptionDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);

        android.widget.TextView tvCode = findViewById(R.id.tvCode);
        android.widget.TextView tvDiagnosis = findViewById(R.id.tvDiagnosis);
        android.widget.TextView tvMedicines = findViewById(R.id.tvMedicines);
        android.widget.TextView tvInstructions = findViewById(R.id.tvInstructions);
        android.widget.TextView tvDoctor = findViewById(R.id.tvDoctor);
        Button btnGetDocument = findViewById(R.id.btnGetDocument);
        Button btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);
        Button btnRenew = findViewById(R.id.btnRenew);

        android.content.Intent intent = getIntent();
        String code = intent.getStringExtra("code");
        String diagnosis = intent.getStringExtra("diagnosis");
        String medicines = intent.getStringExtra("medicines");
        String instructions = intent.getStringExtra("instructions");
        String doctor = intent.getStringExtra("doctor");

        if (code != null) tvCode.setText(code);
        if (diagnosis != null) tvDiagnosis.setText("Διάγνωση : " + diagnosis);
        if (medicines != null) tvMedicines.setText(medicines);
        if (instructions != null) tvInstructions.setText(instructions);
        if (doctor != null) tvDoctor.setText("Ιατρός: " + doctor);

        btnGetDocument.setOnClickListener(v -> {
            String codeText = tvCode.getText().toString();
            String diagnosisText = tvDiagnosis.getText().toString();
            String medicinesText = tvMedicines.getText().toString();
            String instructionsText = tvInstructions.getText().toString();
            String doctorText = tvDoctor.getText().toString();

            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            int x = 10, y = 25;
            android.graphics.Canvas canvas = page.getCanvas();
            android.graphics.Paint paint = new android.graphics.Paint();
            paint.setTextSize(12);

            canvas.drawText(codeText, x, y, paint); y += 20;
            canvas.drawText(diagnosisText, x, y, paint); y += 20;
            for (String line : medicinesText.split("\n")) { canvas.drawText(line, x, y, paint); y += 15; }
            y += 10;
            for (String line : instructionsText.split("\n")) { canvas.drawText(line, x, y, paint); y += 15; }
            y += 10;
            canvas.drawText(doctorText, x, y, paint);

            pdfDocument.finishPage(page);

            File pdfDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "prescriptions");
            if (!pdfDir.exists()) pdfDir.mkdirs();
            File pdfFile = new File(pdfDir, codeText.replace("#", "") + ".pdf");
            try (FileOutputStream out = new FileOutputStream(pdfFile)) {
                pdfDocument.writeTo(out);
                Toast.makeText(this, "Το PDF αποθηκεύτηκε: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "Σφάλμα κατά την αποθήκευση PDF", Toast.LENGTH_SHORT).show();
            }
            pdfDocument.close();
        });

        btnSubmitFeedback.setOnClickListener(v -> {
            Toast.makeText(this, "Υποβλήθηκε η αναφορά παρενεργειών!", Toast.LENGTH_SHORT).show();
        });

        btnRenew.setOnClickListener(v -> {
            android.content.Intent renewIntent = new android.content.Intent(this, RenewPrescriptionActivity.class);
            renewIntent.putExtra("code", tvCode.getText().toString());
            startActivity(renewIntent);
        });
    }
} 