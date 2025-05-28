package com.example.smartmed1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button loginButton, signupButton;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Σύνδεση με τα views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Συμπλήρωσε όλα τα πεδία", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT role FROM Users WHERE username=? AND password=?", new String[]{username, password});

            if (cursor.moveToFirst()) {
                String role = cursor.getString(0);
                cursor.close();

                if (role.equals("doctor")) {
                    startActivity(new Intent(this, DoctorHome.class));
                } else if (role.equals("user")) {
                    startActivity(new Intent(this, UserHome.class));
                }

                finish();
            } else {
                cursor.close(); // ❗️Χρειαζόταν εδώ
                Toast.makeText(this, "Λανθασμένα στοιχεία", Toast.LENGTH_SHORT).show();
            }
        });

        signupButton.setOnClickListener(v -> {
            // TODO: Άνοιγμα SignUpActivity
        });
    }
}
