package com.example.smartmed.ui;


public class TimeSlot {

    private String date;
    private String start_time;
    private String end_time;

    // Constructor
    public TimeSlot(String date, String start_time, String end_time) {
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    // Getter μέθοδος που επιστρέφει το slot σε μορφή string
    public String GetTimeSlot() {
        return start_time + " - " + end_time + " (" + date + ")";
    }

    // Getters & Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "Time_Slot{" +
                "date='" + date + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                '}';
    }
}
