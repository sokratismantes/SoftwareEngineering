package com.example.smartmed1;

// Εισαγωγή απαραίτητων κλάσεων για τη λειτουργία της διεπαφής και της βάσης
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmPrescription extends AppCompatActivity {

    // Στοιχεία προβολής των πληροφοριών της συνταγής
    TextView txtAmka, txtName, txtDiagnosis, txtDrug, txtPharmaCode, txtDose, txtInstructions, txtValidity;

    // Πεδίο εισόδου για καταχώρηση της διεύθυνσης φαρμακείου από τον γιατρό
    EditText editPharmacy;

    // Κουμπί ολοκλήρωσης διαδικασίας αποθήκευσης συνταγής
    Button btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_prescription);  // Σύνδεση με το αντίστοιχο XML layout

        // Αντιστοίχιση στοιχείων UI με μεταβλητές Java μέσω ID
        txtAmka = findViewById(R.id.txtAmka);
        txtName = findViewById(R.id.txtName);
        txtDiagnosis = findViewById(R.id.txtDiagnosis);
        txtDrug = findViewById(R.id.txtDrug);
        txtPharmaCode = findViewById(R.id.txtPharmaCode);
        txtDose = findViewById(R.id.txtDose);
        txtInstructions = findViewById(R.id.txtInstructions);
        txtValidity = findViewById(R.id.txtValidity);
        editPharmacy = findViewById(R.id.editPharmacy);
        btnComplete = findViewById(R.id.btnComplete);

        // Ανάκτηση δεδομένων από το προηγούμενο activity (DoctorPrescriptionCreate) μέσω του Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Εμφάνιση των δεδομένων που είχαν καταχωρηθεί από τον γιατρό
            txtAmka.setText("ΑΜΚΑ Ασθενούς : " + extras.getString("amka"));
            txtName.setText("Ονοματεπώνυμο : " + extras.getString("name"));
            txtDiagnosis.setText("Διάγνωση : " + extras.getString("diagnosis"));
            txtDrug.setText("Φαρμακευτική Αγωγή : " + extras.getString("drug"));
            txtPharmaCode.setText("Κωδικός Φαρμάκου : " + extras.getString("code"));
            txtDose.setText("Δοσολογία : " + extras.getString("dose"));
            txtInstructions.setText("Οδηγίες Εκτέλεσης : " + extras.getString("instructions"));

            // Εμφάνιση ισχύος της συνταγής σε ημέρες
            String duration = extras.getString("duration");
            txtValidity.setText("Η συνταγή θα είναι ενεργή για " + duration + " ημέρες.");
        }

        // Ορισμός λειτουργίας όταν πατηθεί το κουμπί "ΟΛΟΚΛΗΡΩΣΗ"
        btnComplete.setOnClickListener(v -> {

            // Λήψη όλων των στοιχείων της συνταγής από το intent
            String amka = getIntent().getStringExtra("amka");
            String name = getIntent().getStringExtra("name");
            String diagnosis = getIntent().getStringExtra("diagnosis");
            String drug = getIntent().getStringExtra("drug");
            String code = getIntent().getStringExtra("code");
            String dose = getIntent().getStringExtra("dose");
            String instructions = getIntent().getStringExtra("instructions");
            String duration = getIntent().getStringExtra("duration");

            // Λήψη της τιμής του φαρμακείου που καταχώρησε ο γιατρός
            String pharmacy = editPharmacy.getText().toString().trim();

            // Δημιουργία αντικειμένου χειρισμού της βάσης δεδομένων
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            // Έλεγχος αν η διεύθυνση φαρμακείου υπάρχει καταχωρημένη στη βάση
            if (!dbHelper.getPharmacyByAddress(pharmacy)) {
                Toast.makeText(this, "❌ Δεν βρέθηκε φαρμακείο με αυτή τη διεύθυνση!", Toast.LENGTH_LONG).show();
                return; // Τερματισμός λειτουργίας σε περίπτωση μη έγκυρης διεύθυνσης
            }

            // Αν η διεύθυνση είναι έγκυρη, αποθήκευση της συνταγής στη βάση
            dbHelper.insertPrescription(amka, name, diagnosis, drug, code, dose, instructions, duration, pharmacy);
            Toast.makeText(this, "✅ Η συνταγή αποθηκεύτηκε με επιτυχία!", Toast.LENGTH_SHORT).show();

            // Κλείσιμο του activity και επιστροφή στην προηγούμενη οθόνη
            finish();
        });
    }
}
