package com.example.smartmed.ui;

public class Payment {

    private String payer_id;
    private String payment_type;
    private String service_type;
    private String date;

    // Constructor
    public Payment(String payer_id, String payment_type, String service_type, String date) {
        this.payer_id = payer_id;
        this.payment_type = payment_type;
        this.service_type = service_type;
        this.date = date;
    }

    // Getters
    public String getPayer_id() {
        return payer_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getService_type() {
        return service_type;
    }

    public String getDate() {
        return date;
    }

    // Setters
    public void setPayer_id(String payer_id) {
        this.payer_id = payer_id;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Optional: toString for easy display
    @Override
    public String toString() {
        return "Payment{" +
                "payer_id='" + payer_id + '\'' +
                ", payment_type='" + payment_type + '\'' +
                ", service_type='" + service_type + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
