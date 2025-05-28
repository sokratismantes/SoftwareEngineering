package com.example.smartmed1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class
SupportQuestions extends AppCompatActivity {

    private static final int FILE_PICKER_REQUEST = 1;

    EditText etSubject, etDescription;
    Button btnAttach, btnFaq, btnSubmit;

    Uri attachedFileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_form_screen);

        // Σύνδεση μεταβλητών με τα στοιχεία του layout
        etSubject = findViewById(R.id.etSubject);
        etDescription = findViewById(R.id.etDescription);
        btnAttach = findViewById(R.id.btnAttach);
        btnFaq = findViewById(R.id.btnFaq);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Επιλογή αρχείου
        btnAttach.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, FILE_PICKER_REQUEST);
        });

        // FAQs button
        btnFaq.setOnClickListener(v -> {
            Intent intent = new Intent(SupportQuestions.this, FAQsActivity.class);
            startActivity(intent);
        });


        // Υποβολή ερωτήματος
        btnSubmit.setOnClickListener(v -> {
            String subject = etSubject.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (subject.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Παρακαλώ συμπλήρωσε τα υποχρεωτικά πεδία", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Αποθήκευση δεδομένων και συνημμένου
            Toast.makeText(this, "Το ερώτημα υποβλήθηκε επιτυχώς", Toast.LENGTH_LONG).show();

            // Μετάβαση στην οθόνη επιβεβαίωσης (form_sent)
            // startActivity(new Intent(this, FormSentScreen.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST && resultCode == RESULT_OK && data != null) {
            attachedFileUri = data.getData();
            Toast.makeText(this, "Επισυνάφθηκε αρχείο", Toast.LENGTH_SHORT).show();
        }
    }
}
