package com.example.smartmed1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

public class PrescriptionListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPrescriptions;
    private PrescriptionAdapter prescriptionAdapter;
    private List<Prescriptions> prescriptionList;
    private List<Prescriptions> originalPrescriptionList;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_list);

        recyclerViewPrescriptions = findViewById(R.id.recyclerViewPrescriptions);
        recyclerViewPrescriptions.setLayoutManager(new LinearLayoutManager(this));

        editTextSearch = findViewById(R.id.editTextSearch);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        originalPrescriptionList = dbHelper.getAllPrescriptions();


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
        ImageView infoIcon = findViewById(R.id.infoIcon);

        infoIcon.setOnClickListener(v -> {
            startActivity(new Intent(PrescriptionListActivity.this, HelpAndSupportUser.class));
        });

    }

    private void filterPrescriptions(String text) {
        List<Prescriptions> filteredList = new ArrayList<>();
        for (Prescriptions prescription : originalPrescriptionList) {
            if (prescription.getCode().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(prescription);
            }
        }
        prescriptionAdapter.updateList(filteredList);
    }
} 