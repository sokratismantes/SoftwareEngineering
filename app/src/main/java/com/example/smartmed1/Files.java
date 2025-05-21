package com.example.smartmed1;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Files {

    private static final List<String> fileList = new ArrayList<>();

    static {
        fileList.add("Αιματολογικές.pdf");
        fileList.add("Ακτινογραφίες.pdf");
        fileList.add("Συνταγή_Ιανουάριος.pdf");
    }

    // ✅ Χρησιμοποιείται στο MedicalDocumentsActivity
    public static boolean fileExists(Context context) {
        return !fileList.isEmpty();
    }

    public static List<String> getFiles(Context context) {
        return new ArrayList<>(fileList); // επιστρέφουμε αντίγραφο
    }

    // Μπορεί να συνδεθεί αργότερα με πραγματικό αρχείο
    public static void openFile(String filename) {
        System.out.println("Άνοιγμα αρχείου: " + filename);
    }
}
