package com.example.smartmed1.model;

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
}
