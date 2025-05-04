package com.example.smartmed1;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DoctorHome.class);
            startActivity(intent);
        });


        signupButton.setOnClickListener(v -> {
            // TODO: Άνοιγμα SignUpActivity
        });
    }
}
