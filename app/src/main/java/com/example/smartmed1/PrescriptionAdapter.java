package com.example.smartmed1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder> {
    private List<Prescription> originalList;
    private List<Prescription> filteredList;

    public PrescriptionAdapter(List<Prescription> list) {
        this.originalList = list;
        this.filteredList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription_card, parent, false);
        return new PrescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
        Prescription p = filteredList.get(position);
        holder.code.setText("Κωδικός Συνταγής : " + p.code);
        holder.expiry.setText("Ημερομηνία Λήξης : " + p.expiryDate);
        holder.doctor.setText("Γιατρός : " + p.doctor);
        holder.status.setText("Κατάσταση : " + p.status);
        holder.compliance.setText(p.compliance + "%");
        holder.compliance.setTextColor(p.compliance == 100 ? Color.RED : Color.BLACK);
        holder.heart.setImageResource(p.expired ? R.drawable.heart_filled : R.drawable.heart_outline);
        holder.details.setOnClickListener(v -> {
            android.content.Context context = holder.itemView.getContext();
            android.content.Intent intent = new android.content.Intent(context, PrescriptionDetailsActivity.class);
            intent.putExtra("code", p.code);
            intent.putExtra("diagnosis", p.status.equals("Ενεργή") ? "Λοιμώδης Φαρυγγίτιδα" : "Υπέρταση");
            intent.putExtra("medicines", p.code.equals("#1230099") ?
                "1. Αμοξικιλλίνη 500mg – 1 κάψουλα κάθε 8 ώρες για 7 ημέρες\n2. Ιβουπροφαίνη 400mg – 1 δισκίο κάθε 8 ώρες, εφόσον υπάρχει πόνος ή πυρετός\n3. Αντισηπτικό στοματικό διάλυμα – Γαργάρες 3 φορές την ημέρα μετά τα γεύματα"
                :
                "1. Λισινοπρίλη 10mg – 1 δισκίο κάθε πρωί\n2. Αμλοδιπίνη 5mg – 1 δισκίο κάθε βράδυ");
            intent.putExtra("instructions", p.code.equals("#1230099") ?
                "• Λήψη φαρμάκων με ένα ποτήρι νερό.\n• Αποφυγή αλκοόλ και ερεθιστικών τροφών.\n• Επανεξέταση σε 7 ημέρες, εάν τα συμπτώματα επιμένουν."
                :
                "• Τα φάρμακα να λαμβάνονται την ίδια ώρα κάθε μέρα.\n• Παρακολούθηση της αρτηριακής πίεσης καθημερινά.\n• Επικοινωνήστε με τον γιατρό σε περίπτωση ζάλης ή αδυναμίας.");
            intent.putExtra("doctor", p.doctor);
            context.startActivity(intent);
        });
        holder.heart.setOnClickListener(v -> {
            p.expired = !p.expired;
            holder.heart.setImageResource(p.expired ? R.drawable.heart_filled : R.drawable.heart_outline);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (Prescription p : originalList) {
                if (p.code.contains(query) || p.doctor.contains(query) || p.status.contains(query)) {
                    filteredList.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class PrescriptionViewHolder extends RecyclerView.ViewHolder {
        TextView code, expiry, doctor, status, compliance;
        ImageView heart;
        Button details;
        public PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.tvCode);
            expiry = itemView.findViewById(R.id.tvExpiry);
            doctor = itemView.findViewById(R.id.tvDoctor);
            status = itemView.findViewById(R.id.tvStatus);
            compliance = itemView.findViewById(R.id.tvCompliance);
            heart = itemView.findViewById(R.id.ivHeart);
            details = itemView.findViewById(R.id.btnDetails);
        }
    }
} 