package com.example.smartmed2.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed2.R;

public class AvailableTimeSlots extends AppCompatActivity {

    LinearLayout doctorInfoBox;
    Button proceedButton;

    String doctorName, specialty, date, time;

    DatabaseHelper dbHelper;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_time_slots);

        doctorInfoBox = findViewById(R.id.InfoText);
        proceedButton = findViewById(R.id.btnAddDetails);  // id στο κουμπί στο XML

        dbHelper = new DatabaseHelper(this);

        // Παίρνουμε τα στοιχεία που στάλθηκαν από την AvailabilityForm
        doctorName = getIntent().getStringExtra("doctorName");
        specialty = getIntent().getStringExtra("specialty");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");

        ReserveTimeSlot();  // καλούμε τη μέθοδο αμέσως

        proceedButton.setOnClickListener(v -> ShowCreateAppointment());
    }

    @SuppressLint("SetTextI18n")

    private void ReserveTimeSlot() {
        TextView doctorNameLabel = findViewById(R.id.doctorNameLabel);
        TextView specialtyLabel = findViewById(R.id.specialtyLabel);
        TextView dateLabel = findViewById(R.id.dateLabel);
        TextView timeLabel = findViewById(R.id.timeLabel);

        doctorNameLabel.setText("Όνομα Γιατρού: " + doctorName);
        specialtyLabel.setText("Ειδικότητα: " + specialty);
        dateLabel.setText("Ημερομηνία: " + date);
        timeLabel.setText("Ώρα: " + time);

        // Update status στη βάση
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        //ContentValues values = new ContentValues();
        //values.put(DatabaseHelper.COL_SLOT_STATUS, "Κλεισμένο");
        //db.update(DatabaseHelper.TABLE_TIMESLOTS, values, DatabaseHelper.COL_SLOT_TIME + " = ?", new String[]{time});
        //db.close();
    }

    private void ShowCreateAppointment() {
        Intent intent = new Intent(AvailableTimeSlots.this, CreateAppointment.class);
        intent.putExtra("doctorName", doctorName);
        intent.putExtra("specialty", specialty);
        intent.putExtra("date", date);
        intent.putExtra("time", time);
        startActivity(intent);
    }

}