package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InvalidDataActivity extends AppCompatActivity {
    Button btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invalid_data);

        btnReturn = findViewById(R.id.btnReturn);

        btnReturn.setOnClickListener(v -> GoToAdmissionForm());
    }

    private void GoToAdmissionForm() {
        Intent intent = new Intent(InvalidDataActivity.this, PatientAdmissionForm.class);
        startActivity(intent);
        finish(); // Κλείνει την τρέχουσα activity
    }
}
