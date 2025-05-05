package com.example.smartmed1;

public class Prescription {
    public String code;
    public String expiryDate;
    public String doctor;
    public String status;
    public int compliance;
    public boolean expired;

    public Prescription(String code, String expiryDate, String doctor, String status, int compliance, boolean expired) {
        this.code = code;
        this.expiryDate = expiryDate;
        this.doctor = doctor;
        this.status = status;
        this.compliance = compliance;
        this.expired = expired;
    }
} 