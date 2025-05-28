package com.example.smartmed1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AvailabilityForm extends AppCompatActivity {

    EditText editDoctorName, editSpecialty, editDate, editTime;
    Button nextButton;

    DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.availability_form);

        //deleteDatabase("SmartMed.db");

        dbHelper = new DatabaseHelper(this);

        editDoctorName = findViewById(R.id.editDoctorName);
        editSpecialty = findViewById(R.id.editSpecialty);
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        nextButton = findViewById(R.id.button7);


        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDate();
            }
        });


        // --- Picker για ώρα ---
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTimeSlot();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvailabilityForm.this, AvailableTimeSlots.class);
                intent.putExtra("doctorName", editDoctorName.getText().toString());
                intent.putExtra("specialty", editSpecialty.getText().toString());
                intent.putExtra("date", editDate.getText().toString());
                intent.putExtra("time", editTime.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void GetTimeSlot() {
        try {
            ArrayList<String> slots = new ArrayList<>();



            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                    "SELECT slotTime FROM " + DatabaseHelper.TABLE_TIMESLOTS, null);

            while (cursor.moveToNext()) {
                slots.add(cursor.getString(0));
            }
            cursor.close();

            CharSequence[] items = slots.toArray(new CharSequence[0]);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Επιλέξτε Ώρα Ραντεβού");
            builder.setItems(items, (dialog, which) -> {
                editTime.setText(items[which]);
            });

            builder.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetDate() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Athens"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Μορφοποιούμε την ημερομηνία σε "dd/MM/yyyy"
                    String date = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                            selectedDay, selectedMonth + 1, selectedYear);
                    editDate.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }
}