package com.example.smartmed1;

// Εισαγωγή απαραίτητων κλάσεων για τη λειτουργία της διεπαφής και της βάσης
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class

ConfirmReferral extends AppCompatActivity {

    // Στοιχεία προβολής των πληροφοριών του παραπεμτικού
    TextView txtAmka, txtName, txtDiagnosis,txtExamtype, txtValidity;

    // Πεδίο εισόδου για καταχώρηση της διεύθυνσης φαρμακείου από τον γιατρό
    EditText editDiagnostic_Center;

    // Κουμπί ολοκλήρωσης διαδικασίας αποθήκευσης παραπεμτικού
    Button btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_referral);  // Σύνδεση με το αντίστοιχο XML layout

        // Αντιστοίχιση στοιχείων UI με μεταβλητές Java μέσω ID
        txtAmka = findViewById(R.id.txtAmka);
        txtName = findViewById(R.id.txtName);
        txtDiagnosis = findViewById(R.id.txtDiagnosis);
        txtExamtype = findViewById(R.id.txtExamtype);
        txtValidity = findViewById(R.id.txtValidity);
        editDiagnostic_Center = findViewById(R.id.editDiagnostic_Center);
        btnComplete = findViewById(R.id.btnComplete);

        // Ανάκτηση δεδομένων από το προηγούμενο activity (DoctorReferralCreate) μέσω του Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Εμφάνιση των δεδομένων που είχαν καταχωρηθεί από τον γιατρό
            txtAmka.setText("ΑΜΚΑ Ασθενούς : " + extras.getString("amka"));
            txtName.setText("Ονοματεπώνυμο : " + extras.getString("name"));
            txtDiagnosis.setText("Διάγνωση : " + extras.getString("diagnosis"));
            txtExamtype.setText("Τύπος Εξέτασης : " + extras.getString("examination_type"));

            // Εμφάνιση ισχύος του παραπεμτικού σε ημέρες
            String duration = extras.getString("duration");
            txtValidity.setText("Η συνταγή θα είναι ενεργή για " + duration + " ημέρες.");
        }

        // Ορισμός λειτουργίας όταν πατηθεί το κουμπί "ΟΛΟΚΛΗΡΩΣΗ"
        btnComplete.setOnClickListener(v -> {

            // Λήψη όλων των στοιχείων της συνταγής από το intent
            String amka = getIntent().getStringExtra("amka");
            String name = getIntent().getStringExtra("name");
            String diagnosis = getIntent().getStringExtra("diagnosis");
            String examination_type = getIntent().getStringExtra("examination_type");
            String duration = getIntent().getStringExtra("duration");

            // Λήψη της τιμής του διαγνωστικού κέντρου που καταχώρησε ο γιατρός
            String diagnostic_center = editDiagnostic_Center.getText().toString().trim();

            // Δημιουργία αντικειμένου χειρισμού της βάσης δεδομένων
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            // Έλεγχος αν η διεύθυνση φαρμακείου υπάρχει καταχωρημένη στη βάση
            if (!dbHelper.getPharmacyByAddress(diagnostic_center)) {
                Toast.makeText(this, "❌ Δεν βρέθηκε Διαγνωστικό κέντρο με αυτή τη διεύθυνση!", Toast.LENGTH_LONG).show();
                return; // Τερματισμός λειτουργίας σε περίπτωση μη έγκυρης διεύθυνσης
            }

            // Αν η διεύθυνση είναι έγκυρη, αποθήκευση του παραπεμτικού στη βάση
            dbHelper.insertReferral(amka, name, diagnosis,examination_type, duration, diagnostic_center);
            Toast.makeText(this, "✅ Η συνταγή αποθηκεύτηκε με επιτυχία!", Toast.LENGTH_SHORT).show();

            // Κλείσιμο του activity και επιστροφή στην προηγούμενη οθόνη
            finish();
        });
    }
}
