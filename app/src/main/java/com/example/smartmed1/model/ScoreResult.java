package com.example.smartmed1.model;

import java.util.ArrayList;
import java.util.List;

public class ScoreResult {
    private final int anxietyScore;
    private final int depressionScore;
    private final int wellbeingScore;
    private final List<String> warnings;

    public ScoreResult(int anxietyScore, int depressionScore) {
        this.anxietyScore    = anxietyScore;
        this.depressionScore = depressionScore;
        this.wellbeingScore  = 100 - Math.round((anxietyScore + depressionScore) / 2f);

        warnings = new ArrayList<>();
        if (anxietyScore >= 75) {
            warnings.add("Πολύ υψηλό άγχος – σκεφθείτε επαφή με ειδικό.");
        } else if (anxietyScore >= 50) {
            warnings.add("Μέτριο επίπεδο άγχους – προσέξτε σημάδια κλιμάκωσης.");
        }

        if (depressionScore >= 75) {
            warnings.add("Πολύ χαμηλή διάθεση – ίσως χρειάζεστε υποστήριξη.");
        } else if (depressionScore >= 50) {
            warnings.add("Ήπια μείωση διάθεσης – παρακολουθείστε την κατάσταση.");
        }

        if (wellbeingScore < 50) {
            warnings.add("Τα γενικά επίπεδα ευεξίας σας είναι κάτω του μέσου όρου.");
        }
    }

    public int getAnxietyScore()    { return anxietyScore; }
    public int getDepressionScore() { return depressionScore; }
    public int getWellbeingScore()  { return wellbeingScore; }
    public List<String> getWarnings() { return List.copyOf(warnings); }

    /** If you ever want a single text block summary */
    public String getSummaryText() {
        if (warnings.isEmpty()) {
            return "Τα επίπεδά σας είναι εντός φυσιολογικών ορίων. Συνέχισε έτσι!";
        }
        return String.join("\n", warnings);
    }
}
