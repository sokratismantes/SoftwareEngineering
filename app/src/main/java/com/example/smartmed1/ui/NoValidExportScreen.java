package com.example.smartmed1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed1.Login;
import com.example.smartmed1.R;

public class NoValidExportScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novalidexportscreen);

        Button btnRetry = findViewById(R.id.btnRetryExport);
        Button btnCancel = findViewById(R.id.btnCancelExport);

        // Retry: just finish this screen so the user returns to ShareFormScreen
        btnRetry.setOnClickListener(v -> finish());

        // Cancel: go back to the Home (or wherever makes sense)
        btnCancel.setOnClickListener(v -> {
            // If you want to go to MainActivity:
            Intent i = new Intent(this, Login.class);
            // clear the back stack so user can't return here
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });
    }
}
