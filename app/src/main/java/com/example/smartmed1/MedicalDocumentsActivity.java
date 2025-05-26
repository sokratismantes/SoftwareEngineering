package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MedicalDocumentsActivity extends AppCompatActivity {

    LinearLayout containerFiles;
    TextView textNoFiles;
    AutoCompleteTextView searchInput;
    Button btnFilter;
    ImageView imgDiagnostic;

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

        containerFiles.setVisibility(View.GONE); // Απόκρυψη αρχείων στην αρχή

        imgDiagnostic.setOnClickListener(v -> {
            Intent intent = new Intent(MedicalDocumentsActivity.this, DiagnosticExamsActivity.class);
            startActivity(intent);
        });

        if (!Files.fileExists(this)) {
            textNoFiles.setVisibility(View.VISIBLE);
            return;
        }

        fileList = Files.getFiles(this);

        // Adapter για το dropdown της αναζήτησης
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                fileList
        );
        searchInput.setAdapter(adapter);
        searchInput.setThreshold(1);

        // Τι συμβαίνει όταν επιλεγεί κάποιο στοιχείο από τη λίστα
        searchInput.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            showFiles(List.of(selected));
            containerFiles.setVisibility(View.VISIBLE);
        });

        // Αν σβήσει το κείμενο -> κρύψε τη λίστα
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    containerFiles.setVisibility(View.GONE);
                    containerFiles.removeAllViews();
                }
            }
        });

        // Φιλτράρισμα με κουμπί
        btnFilter.setOnClickListener(v -> {
            List<String> filtered = FilterManager.arrangeFiles(fileList);
            showFiles(filtered);
            containerFiles.setVisibility(View.VISIBLE);
        });
        ImageView infoIcon = findViewById(R.id.infoIcon);

        infoIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MedicalDocumentsActivity.this, HelpAndSupportUser.class);
            startActivity(intent);
        });

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
}
