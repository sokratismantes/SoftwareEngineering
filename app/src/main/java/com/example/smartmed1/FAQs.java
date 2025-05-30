package com.example.smartmed1;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

// Δραστηριότητα εμφάνισης των Συχνών Ερωτήσεων (FAQs)
public class FAQs extends AppCompatActivity {

    // Στοιχείο λίστας και δομές αποθήκευσης ερωτήσεων/απαντήσεων
    ListView listView;
    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faqs_screen);

        listView = findViewById(R.id.listViewFAQs);
        dbHelper = new DatabaseHelper(this);

        loadFAQs();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                questions
        );
        listView.setAdapter(adapter);

        // Όταν ο χρήστης πατήσει σε μια ερώτηση → εμφάνιση σε ξεχωριστή σελίδα
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String question = questions.get(position);
            String answer = answers.get(position);

            // Εμφάνιση της answer_screen
            setContentView(R.layout.answer_screen);

            TextView tvQ = findViewById(R.id.tvQuestion);
            TextView tvA = findViewById(R.id.tvAnswer);
            tvQ.setText(question);
            tvA.setText(answer);
        }); // ✅ Κλείσιμο listener

    } // ✅ Κλείσιμο onCreate()

    // Μέθοδος ανάκτησης των ερωταπαντήσεων από τη βάση
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

    // Επιστροφή από τη σελίδα απάντησης πίσω στη λίστα
    public void returnToFAQs(View view) {
        recreate(); // Ξαναφορτώνει τη λίστα των FAQs
    }

    // [Προαιρετικό] Pop-up διάλογος που δεν χρησιμοποιείται πλέον, μπορεί να αφαιρεθεί
    private void showAnswerDialog(String question, String answer) {
        new AlertDialog.Builder(this)
                .setTitle(question)
                .setMessage(answer)
                .setPositiveButton("OK", null)
                .show();
    }
}
