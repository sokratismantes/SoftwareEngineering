package com.example.smartmed1.model;

public class Question {

    public enum Type { TEXT, SCALE, RATING }

    private final int id;
    private final String prompt;
    private final Type type;

    public Question(int id, String prompt, Type type) {
        this.id = id;
        this.prompt = prompt;
        this.type = type;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    /** Unique identifier for ordering and mapping answers */
    public int getId() {
        return id;
    }

    /** The text of the question to display */
    public String getPrompt() {
        return prompt;
    }

    /** One of TEXT, SCALE or RATING */
    public Type getType() {
        return type;
    }
}
