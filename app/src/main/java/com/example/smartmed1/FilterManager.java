package com.example.smartmed1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterManager {

    public static List<String> arrangeFiles(List<String> files) {
        // Π.χ. ταξινόμηση αλφαβητικά
        files.sort(String::compareTo);
        return files;
    }

    public static List<String> searchFiles(List<String> files, String keyword) {
        return files.stream()
                .filter(name -> name.toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
