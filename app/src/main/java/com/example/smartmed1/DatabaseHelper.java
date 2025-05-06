package com.example.smartmed1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SmartMed.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_USERS = "Users";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";
    public static final String COL_FULLNAME = "fullname";
    public static final String COL_AMKA = "amka";
    public static final String COL_EMAIL = "email";

    public static final String TABLE_FAQ = "FAQs";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Πίνακας χρηστών
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT, " +
                COL_FULLNAME + " TEXT, " +
                COL_AMKA + " TEXT, " +
                COL_EMAIL + " TEXT)";
        db.execSQL(createUserTable);

        // Εισαγωγή αρχικών χρηστών
        db.execSQL("INSERT INTO " + TABLE_USERS + " (" +
                COL_USERNAME + ", " + COL_PASSWORD + ", " + COL_ROLE + ", " +
                COL_FULLNAME + ", " + COL_AMKA + ", " + COL_EMAIL + ") VALUES " +
                "('doctor1', '1234', 'doctor', 'Δρ. Νίκος Παπαδόπουλος', '12345678901', 'nikos@smartmed.gr')," +
                "('doctor2', '5678', 'doctor', 'Δρ. Μαρία Κωνσταντίνου', '98765432100', 'maria@smartmed.gr')," +
                "('user1', '1234', 'user', 'Γιάννης Πετρίδης', '11122233344', 'giannis@smartmed.gr')," +
                "('user2', '5678', 'user', 'Αναστασία Λαμπρή', '55566677788', 'anastasia@smartmed.gr')");

        // Πίνακας FAQ
        String createFaqTable = "CREATE TABLE FAQs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "question TEXT NOT NULL, " +
                "answer TEXT NOT NULL)";
        db.execSQL(createFaqTable);

        // Εισαγωγή αρχικών ερωταπαντήσεων
        db.execSQL("INSERT INTO FAQs (question, answer) VALUES " +
                "('Μπορώ από την εφαρμογή να δω τα παραπεμπτικά μου;', 'Ναι, στην ενότητα ''Τα παραπεμπτικά μου'' από την αρχική οθόνη.')," +
                "('Πόσο συχνά πρέπει να κάνω εξετάσεις αίματος;', 'Οι συστάσεις ποικίλλουν ανάλογα με το ιστορικό σας. Συμβουλευτείτε τον γιατρό σας.')," +
                "('Που μπορώ να δω τις συνταγές μου;', 'Στην ενότητα ''Οι Συνταγές Μου'' από το μενού χρήστη.')," +
                "('Μπορώ να κλείσω ραντεβού μέσω της εφαρμογής;', 'Ναι, στην ενότητα ''Νέο Ραντεβού'' μπορείτε να προγραμματίσετε νέο ραντεβού.')," +
                "('Μπορώ να αλλάξω τον προσωπικό μου γιατρό;', 'Ναι, μέσω της διαχείρισης προφίλ.');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS FAQs");
        onCreate(db);
    }
}
