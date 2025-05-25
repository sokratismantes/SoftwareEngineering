package com.example.smartmed1.model;

public class Question {
    private final int id;
    private final String text;
    private final QuestionType type;

    public Question(int id, String text, QuestionType type) {
        this.id = id;
        this.text = text;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public QuestionType getType() {
        return type;
    }

    public enum QuestionType {
        TEXT,
        SPINNER,
        STAR
    }
}
