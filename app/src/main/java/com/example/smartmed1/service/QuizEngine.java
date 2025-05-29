package com.example.smartmed1.service;

import android.content.Context;
import android.util.Log;

import com.example.smartmed1.DatabaseHelper;
import com.example.smartmed1.model.Answer;
import com.example.smartmed1.model.Doctor;
import com.example.smartmed1.model.Question;
import com.example.smartmed1.model.ScoreResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizEngine {
    private final DatabaseHelper dbh;
    // private final Map<Integer,Answer> answers = new HashMap<>(); // Not strictly needed for Option B
    private List<Question> questions;

    public QuizEngine(Context ctx) {
        dbh = new DatabaseHelper(ctx);
    }

    public List<Question> fetchQuestions() {
        if (questions == null) {
            questions = dbh.getMentalHealthQuestions();
        }
        return questions;
    }

    public void recordAnswer(Answer a) {
        // Still write immediately
        dbh.saveAnswer(a);
    }

    public boolean validateAllAnswered() {
        List<Answer> saved = dbh.getAllSavedAnswers();
        List<Question> allQs = fetchQuestions();

        // must have exactly one answer per question
        if (saved.size() != allQs.size()) return false;

        // and each answer must be non-empty and non-zero
        for (Answer a : saved) {
            String val = a.getValue();
            if (val == null || val.trim().isEmpty() || "0".equals(val.trim())) {
                return false;
            }
        }
        return true;
    }

    public void clearAnswers() {
        // clear on-disk answers
        dbh.clearAllSavedAnswers();
    }
    public ScoreResult getResultById(int resultId) {
        // you could look up past ScoreResults here by ID,
        // but for now we just recalc the current one:
        return calculateScore();
    }

    public ScoreResult calculateScore() {
        // 1) load every answer back from the table
        List<Answer> saved = dbh.getAllSavedAnswers();
        Map<Integer, Answer> map = new HashMap<>();
        for (Answer a : saved) {
            map.put(a.getQuestionId(), a);
        }

        // 2) do exactly the same scoring logic, but using `map`:
        // Ensure default Answer objects have valid question IDs if those are used elsewhere,
        // though for getValue() and parseInt(), it might not matter immediately.
        int a1 = parseInt(map.getOrDefault(1, new Answer(1, "0")).getValue(), 10);
        int a2 = parseInt(map.getOrDefault(2, new Answer(2, "0")).getValue(), 5);
        int a7 = parseInt(map.getOrDefault(7, new Answer(7, "0")).getValue(), 10);
        int anxietySum = a1 + a2 + a7;
        int anxietyScore = Math.round(anxietySum / 25f * 100);

        int d5 = parseInt(map.getOrDefault(5, new Answer(5, "0")).getValue(), 5);
        int d8 = parseInt(map.getOrDefault(8, new Answer(8, "0")).getValue(), 5);
        int d10 = parseInt(map.getOrDefault(10, new Answer(10, "0")).getValue(), 5);
        int depressionSum = d5 + d8 + d10;
        int depressionScore = Math.round(depressionSum / 15f * 100);

        return new ScoreResult(anxietyScore, depressionScore);
    }

    private int parseInt(String s, int max) {
        if (s == null) return 0; // Guard against null string
        try {
            int v = Integer.parseInt(s);
            return Math.max(0, Math.min(max, v));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Doctor validateDoctorAMKA(String amka) {
        return dbh.findDoctorByAMKA(amka);
    }
}
