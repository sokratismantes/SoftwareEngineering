package com.example.smartmed2.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartmed2.R;

public class AppointmentPreview extends AppCompatActivity {

    TextView fullNameText, dateTimeText, doctorText, typeText, insuranceText,
            reasonText, historyText, urgencyText;

    TextView chargeText, discountText, finalAmountText;

    String insuranceProvider;

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_preview);

        // --- TextViews για preview ---
        fullNameText = findViewById(R.id.fullNameText);
        dateTimeText = findViewById(R.id.dateTimeText);
        doctorText = findViewById(R.id.doctorText);
        typeText = findViewById(R.id.typeText);
        insuranceText = findViewById(R.id.insuranceText);
        reasonText = findViewById(R.id.reasonText);
        historyText = findViewById(R.id.historyText);
        urgencyText = findViewById(R.id.urgencyText);

        // --- TextViews για Πληρωμή ---
        chargeText = findViewById(R.id.chargeText);         // βάλε στο XML αυτό το id
        discountText = findViewById(R.id.discountText);     // και αυτό
        finalAmountText = findViewById(R.id.finalAmountText); // και αυτό

        LinearLayout cashContainer = findViewById(R.id.cashContainer);
        LinearLayout cardContainer = findViewById(R.id.cardContainer);
        LinearLayout bankContainer = findViewById(R.id.bankContainer);

        cashContainer.setOnClickListener(v -> ShowPaymentMethods("Μετρητά"));
        cardContainer.setOnClickListener(v -> ShowPaymentMethods("Κάρτα"));
        bankContainer.setOnClickListener(v -> ShowPaymentMethods("Κατάθεση"));


        // --- Λήψη στοιχείων τελευταίου ραντεβού ---
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_APPOINTMENTS +
                        " ORDER BY " + DatabaseHelper.COL_ID + " DESC LIMIT 1",
                null
        );

        if (cursor.moveToFirst()) {
            fullNameText.setText("Ονοματεπώνυμο: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PATIENT_NAME)));
            dateTimeText.setText("Ημερομηνία: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DATE)) +
                    "\nΏρα: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TIME)));
            doctorText.setText("Ιατρός: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DOCTOR_NAME)));
            typeText.setText("Τύπος Ραντεβού: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_APPOINTMENT_TYPE)));
            insuranceProvider = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_INSURANCE));
            insuranceText.setText("Ασφαλιστικός Φορέας: " + insuranceProvider);
            reasonText.setText("Αιτία Επίσκεψης:\n" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_REASON)));
            historyText.setText("Προγενέστερο Ιστορικό Υγείας:\n" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_HISTORY)));
            urgencyText.setText("Είναι επείγον περιστατικό: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_URGENT)));
        }

        cursor.close();
        db.close();
    }

    // --- Launcher για αποτελέσματα από PaymentMethods ---
    private final ActivityResultLauncher<Intent> paymentResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    double charge = result.getData().getDoubleExtra("baseCharge", 0);
                    double discount = result.getData().getDoubleExtra("discount", 0);
                    double finalAmount = result.getData().getDoubleExtra("finalAmount", 0);

                    chargeText.setText("Χρέωση Υπηρεσίας: " + String.format("%.2f", charge) + " $");
                    discountText.setText("Έκπτωση Ασφαλιστικού Φορέα: -" + String.format("%.2f", discount) + " $");
                    finalAmountText.setText("Τελική Τιμή: " + String.format("%.2f", finalAmount) + " $");
                }
            });

    private void ShowPaymentMethods(String method) {
        Intent intent = new Intent(AppointmentPreview.this, PaymentMethods.class);
        intent.putExtra("selectedMethod", method);
        intent.putExtra("insurance", insuranceProvider); // Στέλνουμε τον ασφαλιστικό φορέα
        paymentResultLauncher.launch(intent);
    }
}
