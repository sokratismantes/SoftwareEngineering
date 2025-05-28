package com.example.smartmed1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Calendar;
import java.util.Locale;

public class Feedback extends AppCompatActivity {

    EditText editFeedbackDate, editFeedbackText;
    ImageView[] stars = new ImageView[5];
    int rating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        editFeedbackDate = findViewById(R.id.editFeedbackDate);
        editFeedbackText = findViewById(R.id.editFeedbackText);

        editFeedbackDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Feedback.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        editFeedbackDate.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });


        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);

        for (int i = 0; i < stars.length; i++) {
            final int index = i;
            stars[i].setOnClickListener(view -> {
                rating = index + 1;
                updateStars();
            });
        }

        findViewById(R.id.submitButton).setOnClickListener(v -> {
            String date = editFeedbackDate.getText().toString().trim();
            String feedback = editFeedbackText.getText().toString().trim();

            if (date.isEmpty() || feedback.isEmpty() || rating == 0) {
                Toast.makeText(this, "Συμπλήρωσε όλα τα πεδία και επίλεξε βαθμολογία.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "✅ Ευχαριστούμε για την αξιολόγηση!", Toast.LENGTH_LONG).show();
            finish(); // Κλείνει τη φόρμα και επιστρέφει πίσω
        });
    }

    private void updateStars() {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setImageResource(i < rating ? R.drawable.star_filled : R.drawable.star_empty);
        }
    }
}
