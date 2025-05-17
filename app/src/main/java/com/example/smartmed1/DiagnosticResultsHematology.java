package com.example.smartmed1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import androidx.appcompat.app.AppCompatActivity;

public class DiagnosticResultsHematology extends AppCompatActivity {

    LinearLayout resultsContainer;
    TextView resultsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_results_hematology);

        resultsTitle = findViewById(R.id.resultsTitle);
        resultsContainer = findViewById(R.id.resultsContainer);

        // Παίρνουμε την κατηγορία από το intent
        String category = getIntent().getStringExtra("category");
        resultsTitle.setText(category + " Εξετάσεις");

        // Βήμα 1: Σύνδεση με τη βάση
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Βήμα 2: Εκτέλεση ερωτήματος
        Cursor cursor = db.rawQuery("SELECT title FROM HematologyExams", null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);

                // Βήμα 3: Δημιουργία δυναμικής διάταξης
                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setBackgroundResource(R.drawable.rounded_button);
                itemLayout.setPadding(24, 24, 24, 24);
                LayoutParams itemParams = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                );
                itemParams.setMargins(0, 0, 0, 24);
                itemLayout.setLayoutParams(itemParams);


                // Τίτλος αρχείου
                TextView text = new TextView(this);
                text.setText(title);
                text.setTextSize(16f);
                text.setTextColor(0xFF000000);
                text.setLayoutParams(new LayoutParams(
                        0, LayoutParams.WRAP_CONTENT, 1
                ));
                itemLayout.addView(text);

                // Εικονίδιο διαγραφής
                ImageView deleteIcon = new ImageView(this);
                deleteIcon.setImageResource(R.drawable.delete); // δικό σου icon
                deleteIcon.setLayoutParams(new LayoutParams(48, 48));
                itemLayout.addView(deleteIcon);

                // Εικονίδιο λήψης
                ImageView downloadIcon = new ImageView(this);
                downloadIcon.setImageResource(R.drawable.download_image);
                downloadIcon.setLayoutParams(new LayoutParams(48, 48));
                downloadIcon.setPadding(24, 0, 0, 0);
                itemLayout.addView(downloadIcon);

                // Προσθήκη του item στο container
                resultsContainer.addView(itemLayout);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }
}
