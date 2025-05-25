package com.example.smartmed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed.R;

public class PaymentMethods extends AppCompatActivity {

    Button addCardButton;

    double baseCharge = 0.0;
    double discount = 0.0;
    double finalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_methods);

        addCardButton = findViewById(R.id.addCardButton); // Βεβαιώσου ότι υπάρχει στο XML

        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insurance = getIntent().getStringExtra("insurance");

                if (insurance != null && !insurance.isEmpty()) {
                    CalculateSum(insurance);  // Υπολογισμός
                    ShowFinalSubmit();          // Επιστροφή δεδομένων
                } else {
                    Toast.makeText(PaymentMethods.this, "❌ Δεν βρέθηκε ασφαλιστικός φορέας", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Υπολογίζει ποσά με βάση τον ασφαλιστικό φορέα
    private void CalculateSum(String insuranceProvider) {
        baseCharge = 50.00;

        switch (insuranceProvider.toUpperCase()) {
            case "EOPYY":
                discount = 38.95;
                break;
            case "IKA":
                discount = 30.00;
                break;
            case "OAEE":
                discount = 25.00;
                break;
            case "OGA":
                discount = 20.00;
                break;
            case "NAT":
                discount = 15.00;
                break;
            case "ATE":
                discount = 10.00;
                break;
            default:
                discount = 0.00;
                break;
        }

        finalAmount = baseCharge - discount;
    }

    // Επιστρέφει τις τιμές στην AppointmentPreview
    private void ShowFinalSubmit() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("baseCharge", baseCharge);
        resultIntent.putExtra("discount", discount);
        resultIntent.putExtra("finalAmount", finalAmount);
        setResult(RESULT_OK, resultIntent);
        finish(); // Κλείνει την PaymentMethods και επιστρέφει στην AppointmentPreview
    }
}
