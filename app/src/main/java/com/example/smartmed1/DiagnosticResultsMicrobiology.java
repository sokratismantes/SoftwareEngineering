package com.example.smartmed1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticResultsMicrobiology extends AppCompatActivity {

    LinearLayout resultsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_results_microbiology);

        resultsContainer = findViewById(R.id.resultsContainer);

        List<String> results = getMicrobiologyResults();
        for (String res : results) {
            resultsContainer.addView(createMicroResultView(res));
        }
    }

    private View createMicroResultView(String titleText) {
        // Γονικό layout
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.HORIZONTAL);
        parentLayout.setBackgroundResource(R.drawable.rounded_button1);
        parentLayout.setPadding(24, 24, 24, 24);
        parentLayout.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 24);
        parentLayout.setLayoutParams(layoutParams);

        // Text
        TextView title = new TextView(this);
        title.setText(titleText);
        title.setTextSize(16);
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
        parentLayout.addView(title);

        // Eye icon
        ImageView eyeIcon = new ImageView(this);
        eyeIcon.setImageResource(R.drawable.eye_diagnostic_exams);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(64, 64);
        iconParams.setMargins(16, 0, 16, 0);
        eyeIcon.setLayoutParams(iconParams);
        eyeIcon.setContentDescription("Προβολή");
        parentLayout.addView(eyeIcon);

        // Download icon
        ImageView downloadIcon = new ImageView(this);
        downloadIcon.setImageResource(R.drawable.download_image);
        downloadIcon.setLayoutParams(new LinearLayout.LayoutParams(64, 64));
        downloadIcon.setContentDescription("Λήψη");
        parentLayout.addView(downloadIcon);

        return parentLayout;
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
}
