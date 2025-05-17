package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.List;

public class MedicalDocumentsActivity extends AppCompatActivity {

    LinearLayout containerFiles;
    TextView textNoFiles;
    AutoCompleteTextView searchInput;
    Button btnFilter;
    ImageView imgDiagnostic;
    LinearLayout folderContainer;

    List<String> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_documents);

        containerFiles = findViewById(R.id.containerFiles);
        textNoFiles = findViewById(R.id.textNoFiles);
        searchInput = findViewById(R.id.searchInput);
        btnFilter = findViewById(R.id.btnFilter);
        imgDiagnostic = findViewById(R.id.imgDiagnostic);
        folderContainer = findViewById(R.id.folderContainer);



        // Μετάβαση στις διαγνωστικές εξετάσεις
        imgDiagnostic.setOnClickListener(v -> {
            Intent intent = new Intent(MedicalDocumentsActivity.this, DiagnosticExamsActivity.class);
            startActivity(intent);
        });

        // Ανάκτηση αρχείων από SQLite μέσω Files
        if (!Files.fileExists(this)) {
            textNoFiles.setVisibility(View.VISIBLE);
            return;
        }
        fileList = Files.getFiles(this);
        if (fileList.isEmpty()) {
            textNoFiles.setVisibility(View.VISIBLE);
            return;
        }

        // Προετοιμασία dropdown autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                fileList
        );
        searchInput.setAdapter(adapter);
        searchInput.setThreshold(1);

        searchInput.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            showFiles(List.of(selected));
        });

        // Φιλτράρισμα μέσω κουμπιού
        btnFilter.setOnClickListener(v -> {
            List<String> filtered = FilterManager.arrangeFiles(fileList);
            showFiles(filtered);
        });

        // Φόρτωση φακέλων
        showFolders();
    }

    private void showFiles(List<String> files) {
        containerFiles.removeAllViews();
        for (String name : files) {
            TextView tv = new TextView(this);
            tv.setText(name);
            tv.setPadding(20, 20, 20, 20);
            tv.setTextSize(16);
            tv.setTextColor(getColor(android.R.color.black));
            tv.setOnClickListener(v -> {
                Intent intent = new Intent(this, OpenFileScreenActivity.class);
                intent.putExtra("fileName", name);
                startActivity(intent);
            });
            containerFiles.addView(tv);
        }
    }

    private void showFolders() {
        List<String> folders = Folders.getFolders();
        for (String folderName : folders) {
            Button folderBtn = new Button(this);
            folderBtn.setText(folderName);
            folderBtn.setAllCaps(false);
            folderBtn.setTextSize(14);
            folderBtn.setPadding(24, 10, 24, 10);
            folderBtn.setOnClickListener(v -> {
                List<String> filtered = FilterManager.searchFiles(fileList, folderName);
                if (filtered.isEmpty()) {
                    Toast.makeText(this, "Δεν υπάρχουν αρχεία για " + folderName, Toast.LENGTH_SHORT).show();
                } else {
                    showFiles(filtered);
                }
            });
            folderContainer.addView(folderBtn);
        }
    }
}
