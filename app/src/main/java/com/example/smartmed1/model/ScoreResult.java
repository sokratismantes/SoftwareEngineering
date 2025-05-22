package com.example.smartmed1.model;

import java.util.ArrayList;
import java.util.List;

// ScoreResult.java
public class ScoreResult {
    private final int anxietyScore;
    private final int depressionScore;
    private final int wellbeingScore;
    private final List<String> warnings = new ArrayList<>();

    public ScoreResult(int anxiety, int depression) {
        this.anxietyScore    = anxiety;
        this.depressionScore = depression;
        this.wellbeingScore  = 100 - Math.round((anxiety + depression) / 2f);

        // no reassignment of warnings here—already initialized above

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

    public enum Level { LOW, MODERATE, HIGH }

    public Level getAnxietyLevel() {
        if (anxietyScore <= 33) return Level.LOW;
        if (anxietyScore <= 66) return Level.MODERATE;
        return Level.HIGH;
    }

    public Level getDepressionLevel() {
        if (depressionScore <= 33) return Level.LOW;
        if (depressionScore <= 66) return Level.MODERATE;
        return Level.HIGH;
    }

    /** For wellbeing we invert the buckets: high wellbeing is “LOW risk” */
    public Level getWellbeingLevel() {
        if (wellbeingScore <= 33) return Level.HIGH;
        if (wellbeingScore <= 66) return Level.MODERATE;
        return Level.LOW;
    }

    public String getAdvice() {
        switch (getAnxietyLevel()) {
            case LOW:
                return "Τα επίπεδα άγχους είναι χαμηλά. Κράτησε τη ρουτίνα σου.";
            case MODERATE:
                return "Μέτριο άγχος – δοκίμασε μικρές ασκήσεις αναπνοής.";
            default:
                return "Υψηλό άγχος – σκέψου επαφή με ειδικό.";
        }
    }

    public int getAnxietyScore()    { return anxietyScore; }
    public int getDepressionScore() { return depressionScore; }
    public int getWellbeingScore()  { return wellbeingScore; }

    public List<String> getWarnings() {
        return List.copyOf(warnings);
    }

    /** If you ever want a single text block summary */
    public String getSummaryText() {
        if (warnings.isEmpty()) {
            return "Τα επίπεδά σας είναι εντός φυσιολογικών ορίων. Συνέχισε έτσι!";
        }
        return String.join("\n", warnings);
    }
}
