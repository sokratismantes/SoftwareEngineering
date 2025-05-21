package com.example.smartmed1;

// Εισαγωγή απαραίτητων κλάσεων για χρήση SQLite και συλλογών
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

// Κλάση βοηθού βάσης δεδομένων που επεκτείνει τη SQLiteOpenHelper
public class DatabaseHelper extends SQLiteOpenHelper {


    // Σταθερές για όνομα και έκδοση βάσης δεδομένων
    public static final String DATABASE_NAME = "SmartMed.db";
    public static final int DATABASE_VERSION =16; // Αυξήθηκε η έκδοση

    // Ορισμός στηλών πίνακα χρηστών
    public static final String TABLE_USERS = "Users";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";
    public static final String COL_FULLNAME = "fullname";
    public static final String COL_AMKA = "amka";
    public static final String COL_EMAIL = "email";

    // Πίνακας FAQs
    public static final String TABLE_FAQ = "FAQs";

    // Κατασκευαστής που αρχικοποιεί τη βάση δεδομένων
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Δημιουργία πινάκων κατά την αρχικοποίηση της βάσης
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

        // Δημιουργία πίνακα χρηστών
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

        db.execSQL("INSERT INTO FAQs (question, answer) VALUES " +
                "('Μπορώ από την εφαρμογή να δω τα παραπεμπτικά μου;', 'Ναι, στην ενότητα ''Τα παραπεμπτικά μου'' από την αρχική οθόνη.')," +
                "('Πόσο συχνά πρέπει να κάνω εξετάσεις αίματος;', 'Οι συστάσεις ποικίλλουν ανάλογα με το ιστορικό σας. Συμβουλευτείτε τον γιατρό σας.')," +
                "('Που μπορώ να δω τις συνταγές μου;', 'Στην ενότητα ''Οι Συνταγές Μου'' από το μενού χρήστη.')," +
                "('Μπορώ να κλείσω ραντεβού μέσω της εφαρμογής;', 'Ναι, στην ενότητα ''Νέο Ραντεβού'' μπορείτε να προγραμματίσετε νέο ραντεβού.')," +
                "('Μπορώ να αλλάξω τον προσωπικό μου γιατρό;', 'Ναι, μέσω της διαχείρισης προφίλ.');");

        // Πίνακας Αιματολογικών Εξετάσεων
        String createHematologyTable = "CREATE TABLE HematologyExams (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "filepath TEXT NOT NULL)";
        db.execSQL(createHematologyTable);

        db.execSQL("INSERT INTO HematologyExams (title, filepath) VALUES " +
                "('ΓΕΝΙΚΗ_ΑΙΜΑΤΟΣ_2023.pdf', 'path/to/geniki.pdf')," +
                "('Θυρεοειδής.docx', 'path/to/thyreo.docx')," +
                "('Βιοχημική_Σεπτέμβριος.docx', 'path/to/bioximiki.docx')," +
                "('TSH.png', 'path/to/tsh.png')");

        // ✅ Πίνακας Μαγνητικών Εξετάσεων (MRI)
        String createMRITable = "CREATE TABLE MRIExams (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "filepath TEXT NOT NULL)";
        db.execSQL(createMRITable);

        db.execSQL("INSERT INTO MRIExams (title, filepath) VALUES " +
                "('MRI_Εγκεφάλου.pdf', 'path/to/mri1.pdf')," +
                "('MRI_Σπονδυλικής_Στήλης.docx', 'path/to/mri2.docx')," +
                "('MRI_Γονάτου.png', 'path/to/mri3.png')");

        // ✅ Πίνακας MedicalFiles
        String createMedicalFiles = "CREATE TABLE IF NOT EXISTS MedicalFiles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)";
        db.execSQL(createMedicalFiles);

        db.execSQL("INSERT INTO MedicalFiles (name) VALUES ('Αιματολογικές.pdf')");
        db.execSQL("INSERT INTO MedicalFiles (name) VALUES ('Συνταγή_Μαρτίου.pdf')");
        db.execSQL("INSERT INTO MedicalFiles (name) VALUES ('Παραπεμπτικό_Ακτινογραφίας.pdf')");

        // Πίνακας Μικροβιολογικών Εξετάσεων
        String createMicrobiologyTable = "CREATE TABLE MicrobiologyExams (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "filepath TEXT NOT NULL)";
        db.execSQL(createMicrobiologyTable);

        db.execSQL("INSERT INTO MicrobiologyExams (title, filepath) VALUES " +
                "('Μικροβιολογική_Ούρων.pdf', 'path/to/urine.pdf')," +
                "('Καλλιέργεια_Λαιμού.docx', 'path/to/throat.docx')," +
                "('Καλλιέργεια_Ούρων.png', 'path/to/urine2.png')");

        // Καρδιολογικές Εξετάσεις
        String createCardiologyTable = "CREATE TABLE CardiologyExams (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "filepath TEXT NOT NULL)";
        db.execSQL(createCardiologyTable);

        db.execSQL("INSERT INTO CardiologyExams (title, filepath) VALUES " +
                "('ΗΚΓ_Ιανουάριος_2024.pdf', 'path/to/cardio1.pdf')," +
                "('Triplex_καρδιάς.docx', 'path/to/cardio2.docx')," +
                "('Πίεση_μετρήσεις.png', 'path/to/cardio3.png')");

        // Πίνακας Μοριακών Εξετάσεων
        String createMolecularTable = "CREATE TABLE MolecularExams (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "filepath TEXT NOT NULL)";
        db.execSQL(createMolecularTable);

        db.execSQL("INSERT INTO MolecularExams (title, filepath) VALUES " +
                "('PCR_COVID19_Αποτελέσματα.pdf', 'path/to/pcr1.pdf')," +
                "('Μοριακός_Έλεγχος_Ιού.docx', 'path/to/molecular2.docx')," +
                "('PCR_Γρίπης.png', 'path/to/pcr3.png')");

        String createPrescriptionsTable = "CREATE TABLE IF NOT EXISTS Prescriptions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "amka TEXT," +
                "name TEXT," +
                "diagnosis TEXT," +
                "drug TEXT," +
                "pharma_code TEXT," +
                "dose TEXT," +
                "instructions TEXT," +
                "duration TEXT," +
                "pharmacy TEXT" +
                ")";
        db.execSQL(createPrescriptionsTable);

        // Δημιουργία πίνακα φαρμακείων
        String createPharmacyTable = "CREATE TABLE IF NOT EXISTS Pharmacies (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "address TEXT NOT NULL, " +
                "available_codes TEXT)";  // κωδικοί φαρμάκων χωρισμένοι με κόμμα
        db.execSQL(createPharmacyTable);

// Προσθήκη παραδειγμάτων
        db.execSQL("INSERT INTO Pharmacies (address, available_codes) VALUES " +
                "('Σταδίου 12', 'A123,B456')," +
                "('Αχαρνών 99', 'X999,Z777')," +
                "('Πατησίων 55', '123456,999999')");


    }

    // Σε περίπτωση αναβάθμισης της βάσης, γίνεται εκ νέου δημιουργία όλων των πινάκων
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS FAQs");
        db.execSQL("DROP TABLE IF EXISTS HematologyExams");
        db.execSQL("DROP TABLE IF EXISTS MRIExams");
        db.execSQL("DROP TABLE IF EXISTS MedicalFiles");
        db.execSQL("DROP TABLE IF EXISTS MicrobiologyExams");
        db.execSQL("DROP TABLE IF EXISTS CardiologyExams");
        db.execSQL("DROP TABLE IF EXISTS MolecularExams");
        db.execSQL("DROP TABLE IF EXISTS Prescriptions");

        // Κλήση δημιουργίας πινάκων ξανά
        onCreate(db);
    }


    // Μέθοδος για επαλήθευση ασθενούς με βάση ΑΜΚΑ και όνομα
    public boolean verifyPatient(String amka, String name, String surname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " +
                        COL_AMKA + " = ? AND " +
                        "LOWER(" + COL_FULLNAME + ") LIKE ?",
                new String[]{amka, "%" + name.toLowerCase() + "%" + surname.toLowerCase() + "%"}
        );

        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // Εισαγωγή νέας συνταγής στη βάση δεδομένων
    public void insertPrescription(String amka, String name, String diagnosis, String drug,
                                   String pharmaCode, String dose, String instructions,
                                   String duration, String pharmacy) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO Prescriptions (amka, name, diagnosis, drug, pharma_code, dose, instructions, duration, pharmacy) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{amka, name, diagnosis, drug, pharmaCode, dose, instructions, duration, pharmacy});
    }

    // Έλεγχος αν υπάρχει φαρμακείο με τη δοθείσα διεύθυνση
    public boolean getPharmacyByAddress(String address) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Pharmacies WHERE LOWER(address) = ?", new String[]{address.toLowerCase()});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Επιστροφή όλων των αποθηκευμένων συνταγών για εμφάνιση
    public List<Prescription> getAllPrescriptions() {
        List<Prescription> prescriptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Prescriptions", null);

        if (cursor.moveToFirst()) {
            do {
                // Δημιουργία αντικειμένου Prescription με παραμετρικά/στατικά πεδία
                String code = cursor.getString(cursor.getColumnIndexOrThrow("pharma_code"));
                String expiry = "31/12/2025"; // Μπορείς αργότερα να το περάσεις σωστά
                String doctor = "Γιατρός";    // Αν προσθέσεις πεδίο doctor στο μέλλον
                String status = "Ενεργή";     // Μπορείς να κάνεις έλεγχο με ημερομηνία
                int compliance = 100;         // Προσωρινή τιμή — αν θες μπορεί να υπολογίζεται
                boolean expired = false;

                prescriptions.add(new Prescription(
                        code,
                        expiry,
                        doctor,
                        status,
                        compliance,
                        expired,
                        cursor.getString(cursor.getColumnIndexOrThrow("diagnosis")),
                        cursor.getString(cursor.getColumnIndexOrThrow("drug")),
                        cursor.getString(cursor.getColumnIndexOrThrow("instructions"))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return prescriptions;
    }


}
