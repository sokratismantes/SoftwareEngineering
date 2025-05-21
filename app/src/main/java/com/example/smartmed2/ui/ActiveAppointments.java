package com.example.smartmed2.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed2.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActiveAppointments extends AppCompatActivity {

    LinearLayout appointmentsContainer;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_appointments);

        appointmentsContainer = findViewById(R.id.appointmentsContainer);
        dbHelper = new DatabaseHelper(this);

        ShowActiveAppointments();
        SearchAppointment(); // Προσθήκη listener στην αναζήτηση
    }

    private void ShowActiveAppointments() {
        appointmentsContainer.removeAllViews(); // Καθάρισε τα προηγούμενα views

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_APPOINTMENTS +
                " ORDER BY " + DatabaseHelper.COL_DATE + " ASC", null);

        int count = 0;

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DATE));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TIME));
            @SuppressLint("Range") String doctor = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DOCTOR_NAME));
            @SuppressLint("Range") String method = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_INSURANCE));
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ID));
            final String finalId = id;

            int remainingDays = CheckRemainingTime(date);

            View card = LayoutInflater.from(this).inflate(R.layout.card_appointment, appointmentsContainer, false);

            TextView dateTimeText = card.findViewById(R.id.cardDateTime);
            TextView doctorText = card.findViewById(R.id.cardDoctor);
            TextView remainingText = card.findViewById(R.id.cardRemainingDays);
            Button manageButton = card.findViewById(R.id.manageButton); //


            dateTimeText.setText("Ημερομηνία : " + date + "\nΏρα : " + time);
            doctorText.setText("Ιατρός: " + doctor + "\nΜέθοδος Πληρωμής : " + method);
            remainingText.setText("Απομένουν " + remainingDays + " ημέρες για το ραντεβού σας.");

            manageButton.setOnClickListener(v -> ShowProcessAppointments(finalId));

            appointmentsContainer.addView(card);
            count++;
        }

        cursor.close();
        db.close();

        Toast.makeText(this, "📅 Φορτώθηκαν " + count + " ραντεβού", Toast.LENGTH_SHORT).show();
    }

    private void ShowProcessAppointments(String appointmentId) {
        Intent intent = new Intent(ActiveAppointments.this, ProcessAppointments.class);
        intent.putExtra("appointmentId", appointmentId);
        startActivity(intent);
    }

    private int CheckRemainingTime(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date appointmentDate = sdf.parse(dateString);
            long diff = appointmentDate.getTime() - System.currentTimeMillis();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            return 0;
        }
    }

    private void SearchAppointment() {
        EditText searchBar = findViewById(R.id.searchBar); // <-- id από το XML
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ShowSelectedAppointment(s.toString().trim());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void ShowSelectedAppointment(String query) {
        appointmentsContainer.removeAllViews();  // Καθαρίζει τα παλιά

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_APPOINTMENTS +
                        " WHERE " + DatabaseHelper.COL_DOCTOR_NAME + " LIKE ?",
                new String[]{"%" + query + "%"}
        );

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DATE));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TIME));
            @SuppressLint("Range") String doctor = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DOCTOR_NAME));
            @SuppressLint("Range") String method = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_INSURANCE));
            int remainingDays = CheckRemainingTime(date);

            View card = LayoutInflater.from(this).inflate(R.layout.card_appointment, appointmentsContainer, false);

            TextView dateTimeText = card.findViewById(R.id.cardDateTime);
            TextView doctorText = card.findViewById(R.id.cardDoctor);
            TextView remainingText = card.findViewById(R.id.cardRemainingDays);

            dateTimeText.setText("Ημερομηνία : " + date + "\nΏρα : " + time);
            doctorText.setText("Ιατρός: " + doctor + "\nΔιεύθυνση : " + "\nΜέθοδος Πληρωμής : " + method);
            remainingText.setText("Απομένουν " + remainingDays + " ημέρες για το ραντεβού σας.");

            appointmentsContainer.addView(card);
        }

        cursor.close();
        db.close();
    }
}

