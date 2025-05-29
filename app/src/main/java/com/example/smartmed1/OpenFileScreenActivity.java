package com.example.smartmed1;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OpenFileScreenActivity extends AppCompatActivity {

    TextView fileNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_file_screen);

        fileNameText = findViewById(R.id.fileNameText);

        String fileName = getIntent().getStringExtra("fileName");

        if (fileName != null) {
            fileNameText.setText("Προβολή: " + fileName);
            Files.openFile(fileName);
        }
    }
}

