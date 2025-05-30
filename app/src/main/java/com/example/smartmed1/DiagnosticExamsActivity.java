package com.example.smartmed1;

// Εισαγωγή απαραίτητων Android κλάσεων για τη διαχείριση UI και intents
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;




// Κλάση για την οθόνη επιλογής κατηγορίας διαγνωστικών εξετάσεων
public class DiagnosticExamsActivity extends AppCompatActivity {

    AutoCompleteTextView searchInput;
    TextView textNoFilteredFiles;


    Button btnFilter;

    LinearLayout btnHematology, btnMRI, btnMicrobiology, btnCardiology, btnGeneral, resultsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_files);

        // Συνδέσεις views
        searchInput = findViewById(R.id.searchInput);
        btnHematology = findViewById(R.id.btnHematology);
        btnMRI = findViewById(R.id.btnMRI);
        btnMicrobiology = findViewById(R.id.btnMicrobiology);
        btnCardiology = findViewById(R.id.btnCardiology);
        btnGeneral = findViewById(R.id.btnGeneral);
        btnFilter = findViewById(R.id.btnFilter);
        resultsContainer = findViewById(R.id.resultsContainer);  // ΝΕΟ: container για εμφάνιση εξετάσεων
        textNoFilteredFiles = findViewById(R.id.textNoFilteredFiles); // ✅ αυτό
        textNoFilteredFiles.setVisibility(View.GONE);

        btnFilter = findViewById(R.id.btnFilter);

        btnFilter.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim().toLowerCase();
            if (query.isEmpty()) {
                textNoFilteredFiles.setVisibility(View.GONE);
                return;
            }

            int matches = 0;
            for (int i = 0; i < resultsContainer.getChildCount(); i++) {
                View child = resultsContainer.getChildAt(i);
                if (child instanceof LinearLayout) {
                    LinearLayout layout = (LinearLayout) child;
                    TextView textView = (TextView) layout.getChildAt(0);
                    String title = textView.getText().toString().toLowerCase();
                    if (title.contains(query)) {
                        layout.setVisibility(View.VISIBLE);
                        matches++;
                    } else {
                        layout.setVisibility(View.GONE);
                    }
                }
            }

            if (matches == 0) {
                //  Φορτώνουμε τη σελίδα χωρίς αποτελέσματα
                setContentView(R.layout.no_filtered_files);

                AutoCompleteTextView searchInput = findViewById(R.id.searchInput);
                Button retryButton = new Button(this);
                retryButton.setText("Επιστροφή");
                retryButton.setOnClickListener(back -> recreate());

                LinearLayout containerFiles = findViewById(R.id.containerFiles);
                containerFiles.addView(retryButton);
            } else {
                textNoFilteredFiles.setVisibility(View.GONE);
            }
        });





        // Παράδειγμα action
        btnHematology.setOnClickListener(v -> {
            resultsContainer.removeAllViews();
            List<String> results = getHematologyResults();
            for (String res : results) {
                resultsContainer.addView(createHematologyResultView(res));
            }
        });
        btnMRI.setOnClickListener(v -> {
            resultsContainer.removeAllViews();
            List<String> results = getMRIResults();
            for (String res : results) {
                resultsContainer.addView(createMRIResultView(res));
            }
        });


        btnMicrobiology.setOnClickListener(v -> {
            resultsContainer.removeAllViews();
            List<String> results = getMicrobiologyResults();
            for (String res : results) {
                resultsContainer.addView(createMicrobiologyResultView(res));
            }
        });


        btnCardiology.setOnClickListener(v -> {
            resultsContainer.removeAllViews(); // καθαρισμός
            List<String> results = getCardiologyResults();
            for (String res : results) {
                resultsContainer.addView(createCardiologyResultView(res));
            }
        });
        btnGeneral.setOnClickListener(v -> {
            resultsContainer.removeAllViews();
            List<String> results = getMolecularResults();
            for (String res : results) {
                resultsContainer.addView(createMolecularResultView(res));
            }
        });
    }
    private View createHematologyResultView(String title) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setBackgroundResource(R.drawable.rounded_button1);
        itemLayout.setPadding(24, 24, 24, 24);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        itemParams.setMargins(0, 0, 0, 24);
        itemLayout.setLayoutParams(itemParams);

        TextView text = new TextView(this);
        text.setText(title);
        text.setTextSize(16f);
        text.setTextColor(0xFF000000);
        text.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        ));
        itemLayout.addView(text);

        ImageView deleteIcon = new ImageView(this);
        deleteIcon.setImageResource(R.drawable.delete);  // άλλαξέ το αν χρειάζεται
        deleteIcon.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
        itemLayout.addView(deleteIcon);

        ImageView downloadIcon = new ImageView(this);
        downloadIcon.setImageResource(R.drawable.download_image);
        downloadIcon.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
        downloadIcon.setPadding(24, 0, 0, 0);
        itemLayout.addView(downloadIcon);

        return itemLayout;
    }

    private View createMicrobiologyResultView(String titleText) {
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.HORIZONTAL);
        parentLayout.setBackgroundResource(R.drawable.rounded_button1);
        parentLayout.setPadding(24, 24, 24, 24);
        parentLayout.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 24);
        parentLayout.setLayoutParams(layoutParams);

        TextView title = new TextView(this);
        title.setText(titleText);
        title.setTextSize(16);
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        parentLayout.addView(title);

        ImageView eyeIcon = new ImageView(this);
        eyeIcon.setImageResource(R.drawable.eye_diagnostic_exams);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(64, 64);
        iconParams.setMargins(16, 0, 16, 0);
        eyeIcon.setLayoutParams(iconParams);
        eyeIcon.setContentDescription("Προβολή");
        parentLayout.addView(eyeIcon);

        ImageView downloadIcon = new ImageView(this);
        downloadIcon.setImageResource(R.drawable.download_image);
        downloadIcon.setLayoutParams(new LinearLayout.LayoutParams(64, 64));
        downloadIcon.setContentDescription("Λήψη");
        parentLayout.addView(downloadIcon);

        return parentLayout;
    }
    private View createMolecularResultView(String titleText) {
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.HORIZONTAL);
        parentLayout.setBackgroundResource(R.drawable.rounded_button1);
        parentLayout.setPadding(24, 24, 24, 24);
        parentLayout.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 24);
        parentLayout.setLayoutParams(layoutParams);

        TextView title = new TextView(this);
        title.setText(titleText);
        title.setTextSize(16);
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        parentLayout.addView(title);

        ImageView eyeIcon = new ImageView(this);
        eyeIcon.setImageResource(R.drawable.eye_diagnostic_exams);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(64, 64);
        iconParams.setMargins(16, 0, 16, 0);
        eyeIcon.setLayoutParams(iconParams);
        eyeIcon.setContentDescription("Προβολή");
        parentLayout.addView(eyeIcon);

        ImageView downloadIcon = new ImageView(this);
        downloadIcon.setImageResource(R.drawable.download_image);
        downloadIcon.setLayoutParams(new LinearLayout.LayoutParams(64, 64));
        downloadIcon.setContentDescription("Λήψη");
        parentLayout.addView(downloadIcon);

        return parentLayout;
    }
    private View createMRIResultView(String titleText) {
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.HORIZONTAL);
        parentLayout.setBackgroundResource(R.drawable.rounded_button1);
        parentLayout.setPadding(24, 24, 24, 24);
        parentLayout.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 24);
        parentLayout.setLayoutParams(layoutParams);

        TextView title = new TextView(this);
        title.setText(titleText);
        title.setTextSize(16);
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        parentLayout.addView(title);

        ImageView eyeIcon = new ImageView(this);
        eyeIcon.setImageResource(R.drawable.eye_diagnostic_exams);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(64, 64);
        iconParams.setMargins(16, 0, 16, 0);
        eyeIcon.setLayoutParams(iconParams);
        eyeIcon.setContentDescription("Προβολή");
        parentLayout.addView(eyeIcon);

        ImageView downloadIcon = new ImageView(this);
        downloadIcon.setImageResource(R.drawable.download_image);
        downloadIcon.setLayoutParams(new LinearLayout.LayoutParams(64, 64));
        downloadIcon.setContentDescription("Λήψη");
        parentLayout.addView(downloadIcon);

        return parentLayout;
    }


    // 🔽 Δημιουργία κάρτας αποτελέσματος
    private View createCardiologyResultView(String titleText) {
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.HORIZONTAL);
        parentLayout.setBackgroundResource(R.drawable.rounded_button1);
        parentLayout.setPadding(24, 24, 24, 24);
        parentLayout.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 24);
        parentLayout.setLayoutParams(layoutParams);

        TextView title = new TextView(this);
        title.setText(titleText);
        title.setTextSize(16);
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        parentLayout.addView(title);

        ImageView eyeIcon = new ImageView(this);
        eyeIcon.setImageResource(R.drawable.eye_diagnostic_exams);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(64, 64);
        iconParams.setMargins(16, 0, 16, 0);
        eyeIcon.setLayoutParams(iconParams);
        parentLayout.addView(eyeIcon);

        ImageView downloadIcon = new ImageView(this);
        downloadIcon.setImageResource(R.drawable.download_image);
        downloadIcon.setLayoutParams(new LinearLayout.LayoutParams(64, 64));
        parentLayout.addView(downloadIcon);

        return parentLayout;
    }
    private List<String> getCardiologyResults() {
        List<String> list = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title FROM CardiologyExams", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    private List<String> getHematologyResults() {
        List<String> list = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title FROM HematologyExams", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    private List<String> getMicrobiologyResults() {
        List<String> list = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title FROM MicrobiologyExams", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    private List<String> getMolecularResults() {
        List<String> list = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title FROM MolecularExams", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    private List<String> getMRIResults() {
        List<String> list = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title FROM MRIExams", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

}


