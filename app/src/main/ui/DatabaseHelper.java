package com.example.smartmed.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SmartMed.db";
    public static final int DATABASE_VERSION = 6;

    // Πίνακας Appointments
    public static final String TABLE_APPOINTMENTS = "Appointments";
    public static final String COL_ID = "id";
    public static final String COL_DOCTOR_NAME = "doctorName";
    public static final String COL_SPECIALTY = "specialty";
    public static final String COL_DATE = "date";
    public static final String COL_TIME = "time";
    public static final String COL_PATIENT_NAME = "patientName";
    public static final String COL_APPOINTMENT_TYPE = "appointmentType";
    public static final String COL_INSURANCE = "insurance";
    public static final String COL_REASON = "reason";
    public static final String COL_HISTORY = "history";
    public static final String COL_URGENT = "urgent";

    // Πίνακας TimeSlots
    public static final String TABLE_TIMESLOTS = "TimeSlots";
    public static final String COL_SLOT_ID = "id";
    public static final String COL_SLOT_TIME = "slotTime";

    // Πίνακας HealthData
    public static final String TABLE_HEALTH = "HealthData";
    public static final String COL_HEALTH_ID = "id";
    public static final String COL_HEALTH_DATE = "date";
    public static final String COL_SLEEP_DURATION = "sleepDuration";
    public static final String COL_SLEEP_QUALITY = "sleepQuality";
    public static final String COL_BLOOD_PRESSURE = "bloodPressure";
    public static final String COL_HEART_RATE = "heartRate";
    public static final String COL_STEPS = "steps";
    public static final String COL_STRESS = "stressLevel";
    public static final String COL_MOOD = "mood";
    public static final String COL_ENERGY = "energyLevel";
    public static final String COL_WEIGHT = "weight";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DEBUG_SMARTMED", "🔥 onCreate: Η βάση δημιουργείται από την αρχή!");

        // Δημιουργία πίνακα Appointments
        String createAppointmentsTable = "CREATE TABLE " + TABLE_APPOINTMENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DOCTOR_NAME + " TEXT, " +
                COL_SPECIALTY + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_PATIENT_NAME + " TEXT, " +
                COL_APPOINTMENT_TYPE + " TEXT, " +
                COL_INSURANCE + " TEXT, " +
                COL_REASON + " TEXT, " +
                COL_HISTORY + " TEXT, " +
                COL_URGENT + " TEXT)";
        db.execSQL(createAppointmentsTable);

        // Δημιουργία πίνακα TimeSlots
        String createTimeSlotsTable = "CREATE TABLE " + TABLE_TIMESLOTS + " (" +
                COL_SLOT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SLOT_TIME + " TEXT)";
        db.execSQL(createTimeSlotsTable);

        // Default ώρες
        String[] timeSlots = {
                "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
                "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
                "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
                "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00"
        };
        for (String slot : timeSlots) {
            db.execSQL("INSERT INTO " + TABLE_TIMESLOTS + " (" + COL_SLOT_TIME + ") VALUES ('" + slot + "')");
        }

        // Δημιουργία πίνακα HealthData
        String createHealthTable = "CREATE TABLE " + TABLE_HEALTH + " (" +
                COL_HEALTH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_HEALTH_DATE + " TEXT, " +
                COL_SLEEP_DURATION + " REAL, " +
                COL_SLEEP_QUALITY + " REAL, " +
                COL_BLOOD_PRESSURE + " TEXT, " +
                COL_HEART_RATE + " REAL, " +
                COL_STEPS + " INTEGER, " +
                COL_STRESS + " REAL, " +
                COL_MOOD + " REAL, " +
                COL_ENERGY + " REAL, " +
                COL_WEIGHT + " REAL)";
        db.execSQL(createHealthTable);

        Log.d("DEBUG_SMARTMED", "✅ Όλοι οι πίνακες δημιουργήθηκαν!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DEBUG_SMARTMED", "📦 Αναβάθμιση βάσης από " + oldVersion + " ➜ " + newVersion);

        if (oldVersion < 6) {
            String createHealthTable = "CREATE TABLE IF NOT EXISTS " + TABLE_HEALTH + " (" +
                    COL_HEALTH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_HEALTH_DATE + " TEXT, " +
                    COL_SLEEP_DURATION + " REAL, " +
                    COL_SLEEP_QUALITY + " REAL, " +
                    COL_BLOOD_PRESSURE + " TEXT, " +
                    COL_HEART_RATE + " REAL, " +
                    COL_STEPS + " INTEGER, " +
                    COL_STRESS + " REAL, " +
                    COL_MOOD + " REAL, " +
                    COL_ENERGY + " REAL, " +
                    COL_WEIGHT + " REAL)";
            db.execSQL(createHealthTable);

            Log.d("DEBUG_SMARTMED", "✅ Πίνακας HealthData δημιουργήθηκε μέσω onUpgrade()");
        }
    }

    public int getAppointmentsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_APPOINTMENTS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
}
