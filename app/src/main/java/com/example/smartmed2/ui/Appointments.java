package com.example.smartmed2.ui;


public class Appointments {
    String doctor, specialty, date, time, patientName, type, insurance, reason, history, urgent;

    public Appointments(String doctor, String specialty, String date, String time,
                       String patientName, String type, String insurance,
                       String reason, String history, String urgent) {
        this.doctor = doctor;
        this.specialty = specialty;
        this.date = date;
        this.time = time;
        this.patientName = patientName;
        this.type = type;
        this.insurance = insurance;
        this.reason = reason;
        this.history = history;
        this.urgent = urgent;
    }

    // Μπορείς να προσθέσεις getters αν θες
}
