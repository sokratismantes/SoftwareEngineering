package com.example.smartmed1;


public class Appointments {
    String doctor, specialty, date, time, patientName, type, insurance, purpose, history, appointment_id;

    public Appointments(String doctor, String specialty, String date, String time,
                        String patientName, String type, String insurance,
                        String purpose, String history, String appointment_id) {
        this.doctor = doctor;
        this.specialty = specialty;
        this.date = date;
        this.time = time;
        this.patientName = patientName;
        this.type = type;
        this.insurance = insurance;
        this.purpose = purpose;
        this.history = history;
        this.appointment_id = appointment_id;
    }
}
