package com.example.smartmed1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPrescriptions;
    private PrescriptionAdapter prescriptionAdapter;
    private List<Prescription> prescriptionList;
    private List<Prescription> originalPrescriptionList;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        recyclerViewPrescriptions = findViewById(R.id.recyclerViewPrescriptions);
        recyclerViewPrescriptions.setLayoutManager(new LinearLayoutManager(this));

        editTextSearch = findViewById(R.id.editTextSearch);

        // Create some dummy data
        originalPrescriptionList = new ArrayList<>();
        originalPrescriptionList.add(new Prescription("#1230099", "31/12/2025", "Δημήτριος Αντωνίου", "Ενεργή", 75, false,
                "Λοιμώδης Φαρυγγίτιδα",
                "1. Αμοξικιλλίνη 500mg – 1 κάψουλα κάθε 8 \n    ώρες για 7 ημέρες\n2. Ιβουπροφαίνη 400mg – 1 δισκίο κάθε 8 \n    ώρες, εφόσον υπάρχει πόνος ή πυρετός\n3. Αντισηπτικό στοματικό διάλυμα – Γαργάρες \n    3 φορές την ημέρα μετά τα γεύματα",
                "• Λήψη φαρμάκων με ένα ποτήρι νερό.\n• Αποφυγή αλκοόλ και ερεθιστικών τροφών.\n• Επανεξέταση σε 7 ημέρες, εάν τα \n  συμπτώματα επιμένουν."));
        originalPrescriptionList.add(new Prescription("#5021108", "08/06/2024", "Γεώργιος Πατάπης", "Έχει Λήξει", 100, true,
                "Υπέρταση",
                "1. Λαβηταλόλη 100mg - 1 δισκίο κάθε 12 ώρες\n2. Υδροχλωροθειαζίδη 25mg - 1 δισκίο το πρωί",
                "• Παρακολούθηση αρτηριακής πίεσης.\n• Αποφυγή υπερβολικού αλατιού."));
        originalPrescriptionList.add(new Prescription("#1230001", "15/07/2025", "Παναγιώτης Παπαδόπουλος", "Ενεργή", 90, false,
                "Δερματίτιδα εξ επαφής",
                "1. Κορινκοστεροειδές κρέμα 0.05% - εφαρμογή 2 φορές την ημέρα για 10 ημέρες",
                "• Καθαρισμός περιοχής με ήπιο σαπούνι.\n• Αποφυγή επαφής με ερεθιστικές ουσίες."));
        originalPrescriptionList.add(new Prescription("#6005502", "20/11/2024", "Ελένη Βασιλείου", "Έχει Λήξει", 85, false,
                "Αυχενικό σύνδρομο",
                "1. Παρακεταμόλη 500mg - 1-2 δισκία κάθε 4-6 ώρες\n2. Φυσικοθεραπεία",
                "• Ασκήσεις αυχένα.\n• Αποφυγή απότομων κινήσεων."));
        originalPrescriptionList.add(new Prescription("#1230099", "10/01/2026", "Δημήτριος Αντωνίου", "Ενεργή", 70, true,
                "Λοιμώδης Φαρυγγίτιδα (Επανάληψη)",
                "1. Αμοξικιλλίνη 500mg – 1 κάψουλα κάθε 8 \n    ώρες για 7 ημέρες\n2. Αντισηπτικό στοματικό διάλυμα – Γαργάρες \n    3 φορές την ημέρα μετά τα γεύματα",
                "• Ολοκλήρωση αγωγής.\n• Επανεξέταση αν τα συμπτώματα επιμείνουν."));
        // Add more dummy data as needed

        prescriptionList = new ArrayList<>(originalPrescriptionList);
        prescriptionAdapter = new PrescriptionAdapter(prescriptionList);
        recyclerViewPrescriptions.setAdapter(prescriptionAdapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPrescriptions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this implementation
            }
        });
    }

    private void filterPrescriptions(String text) {
        List<Prescription> filteredList = new ArrayList<>();
        for (Prescription prescription : originalPrescriptionList) {
            if (prescription.getCode().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(prescription);
            }
        }
        prescriptionAdapter.updateList(filteredList);
    }
} 