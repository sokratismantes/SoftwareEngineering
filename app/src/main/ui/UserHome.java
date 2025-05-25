package com.example.smartmed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed.R;

public class UserHome extends AppCompatActivity {

    Button nextButton;
    Button activeAppointmentsButton;

    Button healthDataButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);

        nextButton = findViewById(R.id.button2);
        activeAppointmentsButton = findViewById(R.id.button3);
        healthDataButton = findViewById(R.id.button5);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAvailabilityForm();  //
            }
        });

        activeAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetActiveAppointments(); //
            }
        });

        healthDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadUserChoices(); //
            }
        });

    }

    //Ανοίγει τη φόρμα ραντεβού
    private void ShowAvailabilityForm() {
        Intent intent = new Intent(UserHome.this, AvailabilityForm.class);
        startActivity(intent);
    }

    private void GetActiveAppointments() {
        Intent intent = new Intent(UserHome.this, ActiveAppointments.class);
        startActivity(intent);
    }

    private void LoadUserChoices() {
        Intent intent = new Intent(UserHome.this, UserChoices.class);
        startActivity(intent);
    }

}
