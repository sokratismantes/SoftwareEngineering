package com.example.smartmed1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class UserComments extends AppCompatActivity {

    private EditText editHealthComment;
    private Button btnSaveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_comments); // Βεβαιώσου ότι το XML λέγεται έτσι

        editHealthComment = findViewById(R.id.editHealthComment);
        btnSaveNote = findViewById(R.id.btnSaveNote);

        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editHealthComment.getText().toString().trim();

                if (comment.isEmpty()) {
                    Toast.makeText(UserComments.this, "Παρακαλώ γράψτε κάποιο σχόλιο.", Toast.LENGTH_SHORT).show();
                } else {
                    SaveNote(comment);
                }
            }
        });
    }

    private void SaveNote(String comment) {
        // Εδώ θα μπορούσες να το αποθηκεύσεις σε SQLite ή SharedPreferences
        // Για απλό feedback προς το χρήστη:
        Toast.makeText(this, "Το σχόλιο αποθηκεύτηκε!", Toast.LENGTH_SHORT).show();

        // Καθαρίζει το πεδίο
        editHealthComment.setText("");

        // Αν θέλεις να επιστρέφει στην προηγούμενη οθόνη
        finish();
    }
}
