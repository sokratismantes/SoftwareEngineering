package com.example.smartmed1;

public class Prescription {
    private String code;
    private String expirationDate;
    private String doctorName;
    private String status;
    private int adherencePercentage;
    private boolean isFavorite;
    private String diagnosis;
    private String medications;
    private String instructions;

    public Prescription(String code, String expirationDate, String doctorName, String status, int adherencePercentage, boolean isFavorite, String diagnosis, String medications, String instructions) {
        this.code = code;
        this.expirationDate = expirationDate;
        this.doctorName = doctorName;
        this.status = status;
        this.adherencePercentage = adherencePercentage;
        this.isFavorite = isFavorite;
        this.diagnosis = diagnosis;
        this.medications = medications;
        this.instructions = instructions;
    }

    public String getCode() {
        return code;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getStatus() {
        return status;
    }

    public int getAdherencePercentage() {
        return adherencePercentage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getMedications() {
        return medications;
    }

    public String getInstructions() {
        return instructions;
    }
} 