package com.example.smartmed1.model;

import com.example.smartmed1.DatabaseHelper;

public class Answer {
    private final int questionId;
    private final String value;

    public Answer(int questionId, String value) {
        this.questionId = questionId;
        this.value = value;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getValue() {
        return value;
    }
    public String getQuestionText(DatabaseHelper dbh) {
        for (Question q : dbh.getMentalHealthQuestions()) {
            if (q.getId() == questionId) {
                return q.getText();
            }
        }
        return null;
    }
}
