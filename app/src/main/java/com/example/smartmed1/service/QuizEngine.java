package com.example.smartmed1.service;

import com.example.smartmed1.data.DB;
import com.example.smartmed1.model.Answer;
import com.example.smartmed1.model.Question;
import com.example.smartmed1.model.ScoreResult;
import com.example.smartmed1.model.Doctor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizEngine {
    private final DB db;
    private final Map<Integer, Answer> answers = new HashMap<>();
    private List<Question> questions;

    public QuizEngine() {
        db = DB.getInstance();
    }

    /** Load & cache questions once */
    public List<Question> fetchQuestions() {
        if (questions == null) {
            questions = db.getMentalHealthQuestions();
        }
        return questions;
    }

    /** Record (or overwrite) an answer */
    public void recordAnswer(Answer answer) {
        answers.put(answer.getQuestionId(), answer);
        db.saveAnswer(answer);
    }

    /** Total number of questions loaded */
    public int totalQuestions() {
        return fetchQuestions().size();
    }

    /** Number of questions answered so far */
    public int answeredCount() {
        return answers.size();
    }

    /** True if every question has at least one answer */
    public boolean validateAllAnswered() {
        return answeredCount() == totalQuestions();
    }

    /** Get all answers for scoring or persistence */
    public List<Answer> getAllAnswers() {
        return List.copyOf(answers.values());
    }

    /** Stub for scoring logic */
    /**
     * Score mapping:
     *   Anxiety questions: IDs 1 (spinner), 2 (star), 7 (spinner)
     *   Depression questions: IDs 5 (star), 8 (star), 10 (star)
     * Each group is summed and normalized to 0–100.
     */
    public ScoreResult calculateScore() {
        // Pull recorded answers
        Map<Integer, Answer> ansMap = answers; // questionId -> Answer

        // Anxiety group: Q1 (0–10), Q2 (0–5), Q7 (0–10)  → max 25
        int a1 = parseInt(ansMap.getOrDefault(1, new Answer(1, "0")).getValue(), 10);
        int a2 = parseInt(ansMap.getOrDefault(2, new Answer(2, "0")).getValue(), 5);
        int a7 = parseInt(ansMap.getOrDefault(7, new Answer(7, "0")).getValue(), 10);
        int anxietySum = a1 + a2 + a7;
        int anxietyScore = Math.round(anxietySum / 25f * 100);

        // Depression group: Q5 (0–5), Q8 (0–5), Q10 (0–5) → max 15
        int d5  = parseInt(ansMap.getOrDefault(5, new Answer(5, "0")).getValue(), 5);
        int d8  = parseInt(ansMap.getOrDefault(8, new Answer(8, "0")).getValue(), 5);
        int d10 = parseInt(ansMap.getOrDefault(10, new Answer(10, "0")).getValue(), 5);
        int depressionSum = d5 + d8 + d10;
        int depressionScore = Math.round(depressionSum / 15f * 100);

        return new ScoreResult(anxietyScore, depressionScore);
    }

    /** Safely parse the integer, clamped to [0,maxVal] on error. */
    private int parseInt(String s, int maxVal) {
        try {
            int v = Integer.parseInt(s);
            if (v < 0) return 0;
            if (v > maxVal) return maxVal;
            return v;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** Validate that a doctor with this AMKA exists */
    public Doctor validateDoctorAMKA(String amka) {
        return db.findDoctorByAMKA(amka);
    }

    /** Clear recorded answers (e.g. on quiz restart) */
    public void clearAnswers() {
        answers.clear();
    }
}
