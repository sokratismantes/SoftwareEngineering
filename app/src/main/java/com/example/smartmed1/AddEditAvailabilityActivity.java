package com.example.smartmed1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AddEditAvailabilityActivity extends AppCompatActivity {

    private Button buttonDay;
    private Button buttonStartTime;
    private Button buttonEndTime;
    private Button buttonConfirm;
    private Button buttonCancel;
    private TextView textViewSelectedDay;
    private TextView textViewSelectedStartTime;
    private TextView textViewSelectedEndTime;
    private TextView titleTextView;

    private boolean isEditMode = false;
    private int originalIndex = -1;
    private String originalDay = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_availability);

        // Get references to UI elements
        buttonDay = findViewById(R.id.buttonDay);
        buttonStartTime = findViewById(R.id.buttonStartTime);
        buttonEndTime = findViewById(R.id.buttonEndTime);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonCancel = findViewById(R.id.buttonCancel);
        textViewSelectedDay = findViewById(R.id.textViewSelectedDay);
        textViewSelectedStartTime = findViewById(R.id.textViewSelectedStartTime);
        textViewSelectedEndTime = findViewById(R.id.textViewSelectedEndTime);
        titleTextView = findViewById(R.id.title); // Get reference to the title TextView

        // Check if opened for editing
        Intent intent = getIntent();
        if (intent.hasExtra("is_edit") && intent.getBooleanExtra("is_edit", false)) {
            isEditMode = true;
            originalIndex = intent.getIntExtra("index", -1);
            originalDay = intent.getStringExtra("originalDay");

            // Populate fields with existing data
            String day = intent.getStringExtra("day");
            String startTime = intent.getStringExtra("startTime");
            String endTime = intent.getStringExtra("endTime");

            textViewSelectedDay.setText(day);
            textViewSelectedStartTime.setText(startTime);
            textViewSelectedEndTime.setText(endTime);

            // Update UI for edit mode
            titleTextView.setText("ΤΡΟΠΟΠΟΙΗΣΗ ΔΙΑΘΕΣΙΜΟΤΗΤΑΣ");
            buttonConfirm.setText("ΑΠΟΘΗΚΕΥΣΗ ΑΛΛΑΓΩΝ");
        } else {
            // Default text for add mode (already set in layout, but good to be explicit)
            titleTextView.setText("ΠΡΟΣΘΗΚΗ ΔΙΑΘΕΣΙΜΟΤΗΤΑΣ");
            buttonConfirm.setText("ΕΠΙΒΕΒΑΙΩΣΗ");
        }

        buttonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDayPickerDialog();
            }
        });

        buttonStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(textViewSelectedStartTime);
            }
        });

        buttonEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(textViewSelectedEndTime);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                String selectedDay = textViewSelectedDay.getText().toString();
                String selectedStartTime = textViewSelectedStartTime.getText().toString();
                String selectedEndTime = textViewSelectedEndTime.getText().toString();

                resultIntent.putExtra("selectedDay", selectedDay);
                resultIntent.putExtra("selectedStartTime", selectedStartTime);
                resultIntent.putExtra("selectedEndTime", selectedEndTime);

                // Include original index and day if in edit mode
                if (isEditMode) {
                    resultIntent.putExtra("originalIndex", originalIndex);
                    resultIntent.putExtra("originalDay", originalDay);
                }

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void showDayPickerDialog() {
        final String[] daysOfWeek = {"Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή", "Σάββατο", "Κυριακή"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Επιλέξτε Ημέρα");
        builder.setItems(daysOfWeek, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedDay = daysOfWeek[which];
                textViewSelectedDay.setText(selectedDay);
                // You might want to store the selected day in a variable here
            }
        });
        builder.show();
    }

    private void showTimePickerDialog(final TextView targetTextView) {
        // Get current time to set as default in the picker
        // TODO: Use the existing time in targetTextView if available
        int hour = 12;
        int minute = 0;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Format the time and set it to the target TextView
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        targetTextView.setText(selectedTime);
                    }
                },
                hour, // default hour
                minute, // default minute
                true); // 24 hour format
        timePickerDialog.show();
    }
} 