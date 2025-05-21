package com.example.smartmed1.model;

public class ScoreResult {
    private final int anxietyScore;
    private final int depressionScore;

    public ScoreResult(int anxietyScore, int depressionScore) {
        this.anxietyScore = anxietyScore;
        this.depressionScore = depressionScore;
    }

    public int getAnxietyScore() {
        return anxietyScore;
    }

    public int getDepressionScore() {
        return depressionScore;
    }

    // optionally you can add helper methods, warnings logic, etc.
}
