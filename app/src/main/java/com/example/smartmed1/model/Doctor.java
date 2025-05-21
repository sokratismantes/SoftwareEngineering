package com.example.smartmed1.model;

public class Doctor {
    private final int id;
    private final String name;
    private final String email;
    private final String amka;

    public Doctor(int id, String name, String email, String amka) {
        this.id    = id;
        this.name  = name;
        this.email = email;
        this.amka  = amka;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAmka() {
        return amka;
    }
}
