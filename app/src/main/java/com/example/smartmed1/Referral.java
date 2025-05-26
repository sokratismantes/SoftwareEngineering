package com.example.smartmed1;

public class Referral {
    private String amka;
    private String name;
    private String diagnosis;
    private String examinationType;
    private String duration;
    private String diagnosticCenter;

    public Referral(String amka, String name, String diagnosis, String examinationType, String duration, String diagnosticCenter) {
        this.amka = amka;
        this.name = name;
        this.diagnosis = diagnosis;
        this.examinationType = examinationType;
        this.duration = duration;
        this.diagnosticCenter = diagnosticCenter;
    }

    public String getAmka() {
        return amka;
    }

    public String getName() {
        return name;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getExaminationType() {
        return examinationType;
    }

    public String getDuration() {
        return duration;
    }

    public String getDiagnosticCenter() {
        return diagnosticCenter;
    }
}
