package com.example.smartmed1;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class FAQsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        listView = findViewById(R.id.listViewFAQs);
        dbHelper = new DatabaseHelper(this);

        loadFAQs();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                questions
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String question = questions.get(position);
            String answer = answers.get(position);
            showAnswerDialog(question, answer);
        });
    }

    private void loadFAQs() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT question, answer FROM FAQs", null);

        if (cursor.moveToFirst()) {
            do {
                questions.add(cursor.getString(0));
                answers.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    private void showAnswerDialog(String question, String answer) {
        new AlertDialog.Builder(this)
                .setTitle(question)
                .setMessage(answer)
                .setPositiveButton("OK", null)
                .show();
    }
}
