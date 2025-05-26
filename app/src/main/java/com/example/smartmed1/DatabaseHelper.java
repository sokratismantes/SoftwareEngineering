package com.example.smartmed1;

// Εισαγωγή απαραίτητων κλάσεων για χρήση SQLite και συλλογών
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import com.example.smartmed1.model.Answer;
import com.example.smartmed1.model.Doctor;
import com.example.smartmed1.model.Question;
import com.example.smartmed1.model.Question.QuestionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Κλάση βοηθού βάσης δεδομένων που επεκτείνει τη SQLiteOpenHelper
public class DatabaseHelper extends SQLiteOpenHelper {

    // Σταθερές για όνομα και έκδοση βάσης δεδομένων
    public static final String DATABASE_NAME = "SmartMed.db";
    public static final int DATABASE_VERSION =24; // Αυξήθηκε η έκδοση

    // Ορισμός στηλών πίνακα χρηστών
    public static final String TABLE_USERS = "Users";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";
    public static final String COL_FULLNAME = "fullname";
    public static final String COL_AMKA = "amka";
    public static final String COL_EMAIL = "email";

    // ─────── NEW HISTORY TABLE ───────
    public static final String TABLE_ANSWERS     = "Answers";
    private static final String TABLE_HISTORY      = "QuizHistory";
    private static final String COL_HIST_ID        = "id";
    private static final String COL_HIST_TS        = "timestamp";
    private static final String COL_HIST_ANXIETY   = "anxiety";
    private static final String COL_HIST_DEPRESS   = "depression";
    private static final String COL_HIST_WELLBEING = "wellbeing";
    // Πίνακας FAQs
    public static final String TABLE_FAQ = "FAQs";

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
                "doκse TEXT," +
                "instructions TEXT," +
                "duration TEXT," +
                "pharmacy TEXT" +
                ")";
        db.execSQL(createPrescriptionsTable);

        String createReferralTable = "CREATE TABLE IF NOT EXISTS Referrals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "amka TEXT," +
                "name TEXT," +
                "diagnosis TEXT," +
                "examination_type TEXT," +
                "duration TEXT," +
                "diagnostic_center TEXT" +
                ")";
        db.execSQL(createReferralTable);

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

        // Δημιουργία πίνακα διαγνωστικων κεντρων
        String createDiagTable = "CREATE TABLE IF NOT EXISTS Diag_centers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "address TEXT NOT NULL)";  //
        db.execSQL(createDiagTable);

// Προσθήκη παραδειγμάτων
        db.execSQL("INSERT INTO Diag_centers (address) VALUES " +
                "('Αμερικης 13')," +
                "('Πλακας 108')," +
                "('Επαναστασεως 78')");
        String createHistory =
                "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + " (" +
                        COL_HIST_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_HIST_TS        + " INTEGER NOT NULL, " +
                        COL_HIST_ANXIETY   + " INTEGER NOT NULL, " +
                        COL_HIST_DEPRESS   + " INTEGER NOT NULL, " +
                        COL_HIST_WELLBEING + " INTEGER NOT NULL" +
                        ");";
        db.execSQL(createHistory);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ANSWERS + "(" +
                "question_id INTEGER PRIMARY KEY, " + // <--- MODIFIED HERE
                "value TEXT, " +
                "timestamp INTEGER)");
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        // Κλήση δημιουργίας πινάκων ξανά
        onCreate(db);
    }

    // ─────── Public API: save a quiz result ───────
    public void saveQuizResult(int anxiety, int depression, int wellbeing) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_HIST_TS,        System.currentTimeMillis());
        cv.put(COL_HIST_ANXIETY,   anxiety);
        cv.put(COL_HIST_DEPRESS,   depression);
        cv.put(COL_HIST_WELLBEING, wellbeing);
        db.insert(TABLE_HISTORY, null, cv);
    }

    public List<QuizResultRecord> getQuizHistory(long startTs, long endTs) {
        List<QuizResultRecord> out = new ArrayList<>();

        // 1) get a readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // 2) build your selection and args
        String selection = COL_HIST_TS + " >= ? AND " + COL_HIST_TS + " <= ?";
        String[] selectionArgs = {
                String.valueOf(startTs),
                String.valueOf(endTs)
        };

        // 3) run the query
        Cursor c = db.query(
                TABLE_HISTORY,                                   // table
                new String[]{                                  // columns
                        COL_HIST_TS,
                        COL_HIST_ANXIETY,
                        COL_HIST_DEPRESS,
                        COL_HIST_WELLBEING
                },
                selection,                                      // WHERE ts>=? AND ts<=?
                selectionArgs,                                  // the “?” values
                null, null,                                     // no GROUP BY or HAVING
                COL_HIST_TS + " DESC"                           // ORDER BY ts DESC
        );

        // 4) iterate the results
        while (c.moveToNext()) {
            long ts = c.getLong(c.getColumnIndexOrThrow(COL_HIST_TS));
            int  a  = c.getInt(c.getColumnIndexOrThrow(COL_HIST_ANXIETY));
            int  d  = c.getInt(c.getColumnIndexOrThrow(COL_HIST_DEPRESS));
            int  w  = c.getInt(c.getColumnIndexOrThrow(COL_HIST_WELLBEING));
            out.add(new QuizResultRecord(ts, a, d, w));
        }
        c.close();

        return out;
    }
    public static class QuizResultRecord {
        public final long   timestamp;
        public final int    anxiety, depression, wellbeing;
        public QuizResultRecord(long ts, int a, int d, int w) {
            timestamp = ts;
            anxiety   = a;
            depression= d;
            wellbeing = w;
        }
    }
    /** In-memory stub for questions */
    private static final Question[] STUB_QUESTIONS = new Question[] {
            new Question(1, "Πόσο συχνά νιώθετε άγχος;", Question.QuestionType.SPINNER),
            new Question(2, "Πόσο έντονο είναι το στρες σας;", Question.QuestionType.STAR),
            new Question(3, "Περιγράψτε την διάθεσή σας σήμερα.", Question.QuestionType.TEXT),
            new Question(4, "Πόσες ώρες κοιμηθήκατε εχθές το βράδυ;", Question.QuestionType.SPINNER),
            new Question(5, "Σε ποιο βαθμό δυσκολεύεστε να συγκεντρωθείτε;", Question.QuestionType.STAR),
            new Question(6, "Αναφέρετε κάτι που σας έκανε να χαμογελάσετε σήμερα.", Question.QuestionType.TEXT),
            new Question(7, "Πόσο συχνά αισθάνεστε κόπωση χωρίς λόγο;", Question.QuestionType.SPINNER),
            new Question(8, "Βαθμολογήστε την ποιότητα της διατροφής σας σήμερα.", Question.QuestionType.STAR),
            new Question(9, "Υπάρχει κάτι συγκεκριμένο που σας προκαλεί άγχος αυτή την περίοδο;", Question.QuestionType.TEXT),
            new Question(10, "Πόσο ικανοποιημένοι είστε με τον ύπνο σας την τελευταία εβδομάδα;", Question.QuestionType.STAR)
    };

    // ───── QUIZ QUESTIONS ─────
    public List<Question> getMentalHealthQuestions() {
        return Arrays.asList(STUB_QUESTIONS);
    }

    // ───── SAVE ONE ANSWER ─────
    public void saveAnswer(Answer a) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("question_id", a.getQuestionId());
        values.put("value", a.getValue());
        values.put("timestamp", System.currentTimeMillis());

        // This will INSERT if question_id doesn't exist,
        // or UPDATE the existing row if question_id (being PRIMARY KEY) already exists.
        db.replace(TABLE_ANSWERS, null, values);
    }

    // ───── FIND DOCTOR BY AMKA ─────
    public Doctor findDoctorByAMKA(String amka) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id, fullname, amka, email FROM " + TABLE_USERS +
                        " WHERE " + COL_AMKA + " = ?", new String[]{ amka }
        );
        Doctor doc = null;
        if (c.moveToFirst()) {
            doc = new Doctor(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3)
            );
        }
        c.close();
        return doc;
    }
    /**
     * Fetch quiz‐history records whose timestamp is between startTs and endTs (inclusive),
     * ordered newest first.
     */

    /** Read back every saved answer row. */
    public List<Answer> getAllSavedAnswers() {
        List<Answer> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_ANSWERS,
                new String[]{"question_id","value"},
                null, null, null, null, null
        );
        while(c.moveToNext()) {
            int qid = c.getInt(0);
            String val = c.getString(1);
            out.add(new Answer(qid, val));
        }
        c.close();
        return out;
    }

    /** Wipe out the in-progress answers (e.g. on “Restart Quiz”). */
    public void clearAllSavedAnswers() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ANSWERS, null, null);
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

    public void insertReferral(String amka, String name, String diagnosis,
                               String examination_type, String duration, String diagnostic_center) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO Referrals (amka, name, diagnosis, examination_type, duration, diagnostic_center) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                new Object[]{amka, name, diagnosis, examination_type, duration, diagnostic_center});
    }

    // Έλεγχος αν υπάρχει φαρμακείο με τη δοθείσα διεύθυνση
    public boolean getPharmacyByAddress(String address) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Pharmacies WHERE LOWER(address) = ?", new String[]{address.toLowerCase()});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Έλεγχος αν υπάρχει Διαγνωστικο κεντρο με τη δοθείσα διεύθυνση
    public boolean getDiagnosticCenterByAddress(String address) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Diag_centers WHERE LOWER(address) = ?", new String[]{address.toLowerCase()});
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
    public List<Referral> getAllReferrals() {
        List<Referral> referrals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Referrals", null);

        if (cursor.moveToFirst()) {
            do {
                referrals.add(new Referral(
                        cursor.getString(cursor.getColumnIndexOrThrow("amka")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("diagnosis")),
                        cursor.getString(cursor.getColumnIndexOrThrow("examination_type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("duration")),
                        cursor.getString(cursor.getColumnIndexOrThrow("diagnostic_center"))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return referrals;
    }



}
