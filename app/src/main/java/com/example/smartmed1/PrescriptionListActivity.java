package com.example.smartmed1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PrescriptionAdapter adapter;
    private EditText searchBar;
    private List<Prescription> prescriptionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        recyclerView = findViewById(R.id.prescriptionRecyclerView);
        searchBar = findViewById(R.id.searchBar);

        prescriptionList = getMockPrescriptions();
        adapter = new PrescriptionAdapter(prescriptionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private List<Prescription> getMockPrescriptions() {
        List<Prescription> list = new ArrayList<>();
        list.add(new Prescription("#1230099", "31/12/2025", "Δημήτριος Αντωνίου", "Ενεργή", 75, false));
        list.add(new Prescription("#5021108", "08/06/2024", "Γεώργιος Πατάπης", "Έχει Λήξει", 100, true));
        return list;
    }
} 