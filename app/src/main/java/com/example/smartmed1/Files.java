package com.example.smartmed1;

import java.util.ArrayList;
import java.util.List;

public class Files {

    private static List<String> fileList = new ArrayList<>();

    // Προσομοίωση αρχείων
    static {
        fileList.add("Αιματολογικές.pdf");
        fileList.add("Ακτινογραφίες.pdf");
        fileList.add("Συνταγή_Ιανουάριος.pdf");
    }

    public static boolean fileExists() {
        return !fileList.isEmpty();
    }

    public static List<String> getFiles() {
        return new ArrayList<>(fileList);
    }

    public static void openFile(String filename) {
        System.out.println("Άνοιγμα αρχείου: " + filename);
    }
}
