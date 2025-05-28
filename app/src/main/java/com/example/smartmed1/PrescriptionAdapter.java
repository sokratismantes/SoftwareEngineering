package com.example.smartmed1;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder> {

    private List<Prescriptions> prescriptionList;

    public PrescriptionAdapter(List<Prescriptions> prescriptionList) {
        this.prescriptionList = prescriptionList;
    }

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_prescription, parent, false);
        return new PrescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
        Prescriptions prescription = prescriptionList.get(position);
        holder.textViewPrescriptionCode.setText("Κωδικός Συνταγής: " + prescription.getCode());
        holder.textViewExpirationDate.setText("Ημερομηνία Λήξης: " + prescription.getExpirationDate());
        holder.textViewDoctorName.setText("Γιατρός: " + prescription.getDoctorName());
        holder.textViewStatus.setText("Κατάσταση: " + prescription.getStatus());
        holder.textViewAdherencePercentage.setText(prescription.getAdherencePercentage() + "%");
        // Set heart icon based on isFavorite
        holder.imageViewHeart.setImageResource(prescription.isFavorite() ? R.drawable.heart_filled : R.drawable.heart_outline);
        // Set heart icon color based on isFavorite
        if (prescription.isFavorite()) {
            holder.imageViewHeart.setColorFilter(Color.RED);
        } else {
            holder.imageViewHeart.clearColorFilter();
        }

        // Add click listener for the View Details button
        holder.buttonViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PrescriptionDetailsActivity.class);
            // Pass prescription details as extras
            intent.putExtra("prescription_code", prescription.getCode());
            intent.putExtra("doctor_name", prescription.getDoctorName());
            intent.putExtra("diagnosis", prescription.getDiagnosis());
            intent.putExtra("medications", prescription.getMedications());
            intent.putExtra("instructions", prescription.getInstructions());

            v.getContext().startActivity(intent);
        });

        holder.imageViewHeart.setOnClickListener(v -> {
            boolean isFavorite = prescription.isFavorite();
            prescription.setIsFavorite(!isFavorite);

            // Update icon and color
            holder.imageViewHeart.setImageResource(prescription.isFavorite() ? R.drawable.heart_filled : R.drawable.heart_outline);
            if (prescription.isFavorite()) {
                holder.imageViewHeart.setColorFilter(Color.RED);
                // Show toast when saved to favorites
                Toast.makeText(v.getContext(), "Η συνταγή αποθηκεύτηκε στα αγαπημένα", Toast.LENGTH_SHORT).show();
            } else {
                holder.imageViewHeart.clearColorFilter();
            }

            // Optionally, notify adapter for a single item change
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    // Method to update the list in the adapter
    public void updateList(List<Prescriptions> newList) {
        prescriptionList = newList;
        notifyDataSetChanged();
    }

    public static class PrescriptionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPrescriptionCode;
        TextView textViewExpirationDate;
        TextView textViewDoctorName;
        TextView textViewStatus;
        TextView textViewAdherenceLabel;
        TextView textViewAdherencePercentage;
        ImageView imageViewHeart;
        Button buttonViewDetails;

        public PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPrescriptionCode = itemView.findViewById(R.id.textViewPrescriptionCode);
            textViewExpirationDate = itemView.findViewById(R.id.textViewExpirationDate);
            textViewDoctorName = itemView.findViewById(R.id.textViewDoctorName);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewAdherenceLabel = itemView.findViewById(R.id.textViewAdherenceLabel);
            textViewAdherencePercentage = itemView.findViewById(R.id.textViewAdherencePercentage);
            imageViewHeart = itemView.findViewById(R.id.imageViewHeart);
            buttonViewDetails = itemView.findViewById(R.id.buttonViewDetails);
        }
    }
} 