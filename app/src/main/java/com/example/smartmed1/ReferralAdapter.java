package com.example.smartmed1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReferralAdapter extends RecyclerView.Adapter<ReferralAdapter.ReferralViewHolder> {

    private List<Referral> referralList;

    public ReferralAdapter(List<Referral> referralList) {
        this.referralList = referralList;
    }

    @NonNull
    @Override
    public ReferralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_referral, parent, false);
        return new ReferralViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferralViewHolder holder, int position) {
        Referral r = referralList.get(position);
        String info = "ΑΜΚΑ: " + r.getAmka() + "\n"
                + "Όνομα: " + r.getName() + "\n"
                + "Διάγνωση: " + r.getDiagnosis() + "\n"
                + "Εξέταση: " + r.getExaminationType() + "\n"
                + "Ισχύει: " + r.getDuration() + " ημέρες\n"
                + "Κέντρο: " + r.getDiagnosticCenter();
        holder.textDetails.setText(info);
    }

    @Override
    public int getItemCount() {
        return referralList.size();
    }

    public void updateList(List<Referral> newList) {
        this.referralList = newList;
        notifyDataSetChanged();
    }

    static class ReferralViewHolder extends RecyclerView.ViewHolder {
        TextView textDetails;

        public ReferralViewHolder(@NonNull View itemView) {
            super(itemView);
            textDetails = itemView.findViewById(R.id.textDetails);
        }
    }
}
