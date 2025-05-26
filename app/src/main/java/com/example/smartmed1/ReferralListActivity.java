package com.example.smartmed1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReferralListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReferrals;
    private ReferralAdapter referralAdapter;
    private List<Referral> referralList;
    private List<Referral> originalReferralList;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_list);

        recyclerViewReferrals = findViewById(R.id.recyclerViewReferrals);
        recyclerViewReferrals.setLayoutManager(new LinearLayoutManager(this));

        editTextSearch = findViewById(R.id.editTextSearch);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        originalReferralList = dbHelper.getAllReferrals();  // ΠΡΟΣΘΗΚΗ ΜΕΘΟΔΟΥ ΑΝ ΔΕΝ ΥΠΑΡΧΕΙ

        referralList = new ArrayList<>(originalReferralList);
        referralAdapter = new ReferralAdapter(referralList);
        recyclerViewReferrals.setAdapter(referralAdapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterReferrals(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        ImageView infoIcon = findViewById(R.id.infoIcon);
        infoIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, HelpAndSupportUser.class));
        });
    }

    private void filterReferrals(String text) {
        List<Referral> filteredList = new ArrayList<>();
        for (Referral r : originalReferralList) {
            if (r.getDiagnosis().toLowerCase().contains(text.toLowerCase()) ||
                    r.getExaminationType().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(r);
            }
        }
        referralAdapter.updateList(filteredList);
    }
}
