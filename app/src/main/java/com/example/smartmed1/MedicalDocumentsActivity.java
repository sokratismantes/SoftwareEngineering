package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MedicalDocumentsActivity extends AppCompatActivity {

    LinearLayout containerFiles;
    TextView textNoFiles;
    EditText searchInput;
    Button btnFilter;
    ImageView imgDiagnostic; // ✅ νέο

    List<String> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_documents);

        containerFiles = findViewById(R.id.containerFiles);
        textNoFiles = findViewById(R.id.textNoFiles);
        searchInput = findViewById(R.id.searchInput);
        btnFilter = findViewById(R.id.btnFilter);
        imgDiagnostic = findViewById(R.id.imgDiagnostic); // ✅ σύνδεση με το ImageView

        // ✅ Όταν πατηθεί η εικόνα "Διαγνωστικές Εξετάσεις"
        imgDiagnostic.setOnClickListener(v -> {
            Intent intent = new Intent(MedicalDocumentsActivity.this, DiagnosticExamsActivity.class);
            startActivity(intent);
        });

        // Βήμα 1: Έλεγχος αν υπάρχουν αρχεία
        if (!Files.fileExists()) {
            textNoFiles.setVisibility(View.VISIBLE);
            return;
        }

        // Βήμα 2: Ανάκτηση και εμφάνιση αρχείων
        fileList = Files.getFiles();
        showFiles(fileList);

        // Βήμα 3: Φιλτράρισμα
        btnFilter.setOnClickListener(v -> {
            List<String> filtered = FilterManager.arrangeFiles(fileList);
            showFiles(filtered);
        });

        // Βήμα 4: Αναζήτηση
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            String keyword = searchInput.getText().toString().trim();
            List<String> results = FilterManager.searchFiles(fileList, keyword);

            if (results.isEmpty()) {
                Toast.makeText(this, "Δεν βρέθηκαν αποτελέσματα", Toast.LENGTH_SHORT).show();
            } else {
                showFiles(results);
            }
            return true;
        });
    }

    private void showFiles(List<String> files) {
        containerFiles.removeAllViews();

        for (String name : files) {
            TextView tv = new TextView(this);
            tv.setText(name);
            tv.setPadding(20, 20, 20, 20);
            tv.setTextSize(16);
            tv.setOnClickListener(v -> {
                Intent intent = new Intent(this, OpenFileScreenActivity.class);
                intent.putExtra("fileName", name);
                startActivity(intent);
            });
            containerFiles.addView(tv);
        }
    }
}
