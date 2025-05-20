package com.example.smartmed1;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmPrescription extends AppCompatActivity {

    // Δηλώσεις μεταβλητών για εμφάνιση στοιχείων και εισαγωγή φαρμακείου
    TextView txtAmka, txtName, txtDiagnosis, txtDrug, txtPharmaCode, txtDose, txtInstructions, txtValidity;
    EditText editPharmacy;
    Button btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_prescription);

        // Αντιστοίχιση των στοιχείων της διεπαφής με τις μεταβλητές
        txtAmka = findViewById(R.id.txtAmka);
        txtName = findViewById(R.id.txtName);
        txtDiagnosis = findViewById(R.id.txtDiagnosis);
        txtDrug = findViewById(R.id.txtDrug);
        txtPharmaCode = findViewById(R.id.txtPharmaCode);
        txtDose = findViewById(R.id.txtDose);
        txtInstructions = findViewById(R.id.txtInstructions);
        txtValidity = findViewById(R.id.txtValidity);
        btnComplete = findViewById(R.id.btnComplete);
        editPharmacy = findViewById(R.id.editPharmacy);  // πεδίο στο οποίο ο γιατρός εισάγει τη διεύθυνση φαρμακείου

        // Ανάκτηση δεδομένων από το προηγούμενο activity (DoctorPrescriptionCreate)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Εμφάνιση των στοιχείων της συνταγής στην προεπισκόπηση
            txtAmka.setText("ΑΜΚΑ Ασθενούς : " + extras.getString("amka"));
            txtName.setText("Ονοματεπώνυμο : " + extras.getString("name"));
            txtDiagnosis.setText("Διάγνωση : " + extras.getString("diagnosis"));
            txtDrug.setText("Φαρμακευτική Αγωγή : " + extras.getString("drug"));
            txtPharmaCode.setText("Κωδικός Φαρμάκου : " + extras.getString("code"));
            txtDose.setText("Δοσολογία : " + extras.getString("dose"));
            txtInstructions.setText("Οδηγίες Εκτέλεσης : " + extras.getString("instructions"));

            // Χρονική ισχύς συνταγής
            String duration = extras.getString("duration");
            txtValidity.setText("Η συνταγή θα είναι ενεργή για " + duration + " ημέρες.");
        }

        // Ενέργεια όταν πατηθεί το κουμπί "ΟΛΟΚΛΗΡΩΣΗ"
        btnComplete.setOnClickListener(v -> {
            // Ξανά ανάκτηση δεδομένων από τα extras (θα μπορούσε να γίνει και μία φορά στην αρχή)
            String amka = getIntent().getStringExtra("amka");
            String name = getIntent().getStringExtra("name");
            String diagnosis = getIntent().getStringExtra("diagnosis");
            String drug = getIntent().getStringExtra("drug");
            String code = getIntent().getStringExtra("code");
            String dose = getIntent().getStringExtra("dose");
            String instructions = getIntent().getStringExtra("instructions");
            String duration = getIntent().getStringExtra("duration");
            String pharmacy = editPharmacy.getText().toString().trim(); // Τιμή που εισήγαγε ο χρήστης

            // Δημιουργία αντικειμένου βάσης και αποθήκευση της συνταγής
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            if (!dbHelper.getPharmacyByAddress(pharmacy)) {
                Toast.makeText(this, "❌ Δεν βρέθηκε φαρμακείο με αυτή τη διεύθυνση!", Toast.LENGTH_LONG).show();
                return;
            }

// Αν υπάρχει, αποθήκευση συνταγής
            dbHelper.insertPrescription(amka, name, diagnosis, drug, code, dose, instructions, duration, pharmacy);
            Toast.makeText(this, "✅ Η συνταγή αποθηκεύτηκε με επιτυχία!", Toast.LENGTH_SHORT).show();
            finish();


            // Επιστροφή (κλείσιμο του activity)
            finish();
        });
    }
}
