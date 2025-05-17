package com.example.smartmed1;

import android.content.Context;
import java.util.List;

public class Files {

    // Πλέον δεν αποθηκεύουμε λίστα εσωτερικά — τα διαβάζουμε live από SQLite

    public static boolean fileExists(Context context) {
        DatabaseHelper db = new DatabaseHelper(context);
        List<String> files = db.getAllMedicalFiles();
        return !files.isEmpty();
    }

    public static List<String> getFiles(Context context) {
        DatabaseHelper db = new DatabaseHelper(context);
        return db.getAllMedicalFiles();
    }

    public static void openFile(String filename) {
        System.out.println("Άνοιγμα αρχείου: " + filename);
        // Αν θέλεις στο μέλλον: μπορείς να προσθέσεις άνοιγμα pdf κτλ εδώ
    }
}
