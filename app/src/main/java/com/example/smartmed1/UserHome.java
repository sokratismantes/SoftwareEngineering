package com.example.smartmed1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;
import java.util.Calendar;

public class UserHome extends AppCompatActivity {

    // Δήλωση κουμπιών
    Button btnNewAppointment, btnMyAppointments, btnMyPrescriptions, btnHealthData, btnMedicalFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Σύνδεση κουμπιών με τα IDs από το layout
        btnNewAppointment = findViewById(R.id.btnNewAppointment);
        btnMyAppointments = findViewById(R.id.btnMyAppointments);
        btnMyPrescriptions = findViewById(R.id.btnMyPrescriptions);
        btnHealthData = findViewById(R.id.btnHealthData);
        btnMedicalFiles = findViewById(R.id.btnMedicalFiles);

        // Προσωρινά listeners με TODO
        btnNewAppointment.setOnClickListener(v -> {
            // TODO: Άνοιγμα σελίδας δημιουργίας νέου ραντεβού
            // startActivity(new Intent(this, NewAppointmentActivity.class));
        });

        btnMyAppointments.setOnClickListener(v -> {
            // TODO: Άνοιγμα σελίδας με τα ραντεβού
        });

        btnMyPrescriptions.setOnClickListener(v -> {
            retrieveListPrescriptions();
        });

        btnHealthData.setOnClickListener(v -> {
            // TODO: Άνοιγμα σελίδας με τα δεδομένα υγείας
        });

        btnMedicalFiles.setOnClickListener(v -> {
            // TODO: Άνοιγμα σελίδας με τα ιατρικά αρχεία
        });
    }

    private void retrieveListPrescriptions() {
        // Καλούμε το showPrescriptionList για να εμφανίσουμε τη λίστα
        showPrescriptionList();
    }

    private void showPrescriptionList() {
        // Άνοιγμα του PrescriptionListActivity για εμφάνιση των συνταγών
        startActivity(new Intent(this, PrescriptionListActivity.class));
    }

    private void detailsRecovery() {
        // Ανακτά τις λεπτομέρειες της συνταγής και ελέγχει την ημερομηνία λήξης
        prescriptionExpirationDateCheck();
        showPrescriptionDetails();
    }

    private void prescriptionExpirationDateCheck() {
        // Έλεγχος αν η συνταγή έχει λήξει
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        
        // TODO: Πρέπει να προσθέσουμε τη λογική για τον έλεγχο της ημερομηνίας λήξης
        // Αυτό θα πρέπει να συνδεθεί με τη βάση δεδομένων
        
        if (isExpired(currentDate)) {
            Toast.makeText(this, "Η συνταγή έχει λήξει", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExpired(Date currentDate) {
        // TODO: Υλοποίηση του ελέγχου λήξης
        return false; // Προσωρινή τιμή
    }

    private void showPrescriptionDetails() {
        // Εμφάνιση των λεπτομερειών της συνταγής
        Intent intent = new Intent(this, PrescriptionDetailsActivity.class);
        startActivity(intent);
    }

    private void fileDownloaded() {
        // Χειρισμός επιτυχούς λήψης αρχείου
        Toast.makeText(this, "Το αρχείο κατέβηκε επιτυχώς", Toast.LENGTH_SHORT).show();
        // TODO: Ενημέρωση της βάσης δεδομένων για την κατάσταση λήψης
    }

    private void fileNotDownloaded() {
        // Χειρισμός αποτυχημένης λήψης αρχείου
        Toast.makeText(this, "Σφάλμα κατά τη λήψη του αρχείου", Toast.LENGTH_SHORT).show();
        // TODO: Καταγραφή του σφάλματος και προσπάθεια επανάληψης
    }

    private void dosagesLimitExcedeed() {
        // Χειρισμός περίπτωσης υπέρβασης ορίου δόσεων
        Toast.makeText(this, "Προειδοποίηση: Έχετε ξεπεράσει το όριο των δόσεων", Toast.LENGTH_LONG).show();
        // TODO: Καταγραφή του συμβάντος και ειδοποίηση του γιατρού
    }

    private void checkboxCompletionRecording() {
        // Καταγραφή της κατάστασης ολοκλήρωσης των checkboxes
        // TODO: Αποθήκευση της κατάστασης στη βάση δεδομένων
        Toast.makeText(this, "Η κατάσταση ολοκλήρωσης καταγράφηκε", Toast.LENGTH_SHORT).show();
    }
}
