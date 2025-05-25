package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Δραστηριότητα όπου ο γιατρός συμπληρώνει νέο παραπεμτικο
public class DoctorReferralCreate extends AppCompatActivity {

    // Δήλωση πεδίων εισόδου για το παραπεμτικο
    EditText etDiagnosis, etExamtype, etDuration;

    // Κουμπιά: Λήψη σε PDF & Επιβεβαίωση
    Button btnDownload, btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_referral_create);

        // Αντιστοίχιση των views από το XML layout
        etDiagnosis = findViewById(R.id.etDiagnosis);
        etDuration = findViewById(R.id.etDuration);
        etExamtype=findViewById(R.id.etExamtype);

        btnDownload = findViewById(R.id.btnDownload);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Μέθοδος DownloadReferral()
        btnDownload.setOnClickListener(v -> {
            // Ανάκτηση τιμών από τα πεδία εισόδου
            String diagnosis = etDiagnosis.getText().toString().trim();
            String examination_type = etExamtype.getText().toString().trim();
            String duration = etDuration.getText().toString().trim();

            // Ανάκτηση AMKA και Ονόματος ασθενούς από το Intent
            String amka = getIntent().getStringExtra("amka");
            String name = getIntent().getStringExtra("name") + " " + getIntent().getStringExtra("surname");

            // Έλεγχος ότι όλα τα πεδία είναι συμπληρωμένα
            if (diagnosis.isEmpty() || examination_type.isEmpty() || duration.isEmpty()) {
                Toast.makeText(this, "❌ Συμπλήρωσε όλα τα πεδία", Toast.LENGTH_SHORT).show();
                return;
            }

            // Δημιουργία PDF (DownloadReferral UI operation)
            android.graphics.pdf.PdfDocument document = new android.graphics.pdf.PdfDocument();
            android.graphics.pdf.PdfDocument.PageInfo pageInfo =
                    new android.graphics.pdf.PdfDocument.PageInfo.Builder(300, 600, 1).create();
            android.graphics.pdf.PdfDocument.Page page = document.startPage(pageInfo);

            // Ζωγραφική του περιεχομένου στο PDF
            android.graphics.Canvas canvas = page.getCanvas();
            int x = 10, y = 25;
            android.graphics.Paint paint = new android.graphics.Paint();
            paint.setTextSize(12);

            canvas.drawText("📄 Παραπεμτικό", x, y, paint); y += 25;
            canvas.drawText("Όνομα: " + name, x, y, paint); y += 20;
            canvas.drawText("AMKA: " + amka, x, y, paint); y += 20;
            canvas.drawText("Διάγνωση: " + diagnosis, x, y, paint); y += 20;
            canvas.drawText("Τύπος Εξέτασης: " + examination_type, x, y, paint); y += 20;
            canvas.drawText("Διάρκεια: " + duration + " ημέρες", x, y, paint); y += 20;

            document.finishPage(page);

            try {
                // Αποθήκευση αρχείου PDF στον φάκελο Λήψεις
                java.io.File downloadsPath = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS);
                java.io.File file = new java.io.File(downloadsPath, "Referral_" + examination_type + ".pdf");
                java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                document.writeTo(fos);

                // Εμφάνιση επιβεβαίωσης στον χρήστη
                Toast.makeText(this, "✅ Το αρχείο δημιουργήθηκε:\n" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // Αν κάτι πάει στραβά
                Toast.makeText(this, "❌ Σφάλμα κατά τη δημιουργία PDF", Toast.LENGTH_SHORT).show();
            }

            document.close(); // Κλείσιμο εγγράφου
        });

        // Μέθοδος ConfirmPrescription()
        btnConfirm.setOnClickListener(v -> {
            // Ανάκτηση των τιμών
            String diagnosis = etDiagnosis.getText().toString().trim();
            String examination_type = etExamtype.getText().toString().trim();
            String duration = etDuration.getText().toString().trim();

            String amka = getIntent().getStringExtra("amka");
            String name = getIntent().getStringExtra("name") + " " + getIntent().getStringExtra("surname");

            // Έλεγχος για κενά πεδία
            if (diagnosis.isEmpty() || examination_type.isEmpty() || duration.isEmpty()) {
                Toast.makeText(this, "❌ Συμπλήρωσε όλα τα πεδία", Toast.LENGTH_SHORT).show();
                return;
            }

            // Δημιουργία Intent για ConfirmPrescription activity
            Intent intent = new Intent(DoctorReferralCreate.this, ConfirmReferral.class);
            intent.putExtra("amka", amka);
            intent.putExtra("name", name);
            intent.putExtra("diagnosis", diagnosis);
            intent.putExtra("examination_type", examination_type);
            intent.putExtra("duration", duration); // Χρονική ισχύς συνταγής
            startActivity(intent);
        });
    }
}
