package com.example.android.videogamequiz;

public class Option {
    private String message;
    private boolean isCorrect;

    public Option(String message, boolean isCorrect) {
        this.message = message;
        this.isCorrect = isCorrect;
    }

    public String getMessage() {
        return message;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
