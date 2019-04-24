package com.example.android.videogamequiz;

import java.util.List;

public class Question {
    private int imageResid;
    private String message;
    private List<Option> options;
    private QuestionType type;
    private int correctOptionAmount;

    public Question(int imageResid, String message, List<Option> options, QuestionType type) {
        this.imageResid = imageResid;
        this.message = message;
        this.options = options;
        this.type = type;

        correctOptionAmount = calculateCorrectOptionAmount(options);
    }

    public int getCorrectOptionAmount() {
        return correctOptionAmount;
    }

    private int calculateCorrectOptionAmount(List<Option> options) {
        int amount = 0;

        for (Option option : options) {
            if (option.isCorrect()) {
                amount++;
            }
        }

        return amount;
    }

    public QuestionType getType() {
        return type;
    }

    public int getImageResid() {
        return imageResid;
    }

    public String getMessage() {
        return message;
    }

    public List<Option> getOptions() {
        return options;
    }


}
