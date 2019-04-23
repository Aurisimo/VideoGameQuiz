package com.example.android.videogamequiz;

import java.util.List;

public class Question {
    private int imageResid;
    private String message;
    private List<Answer> answers;
    private QuestionType type;
    private int correctAnswerAmount;

    public Question(int imageResid, String message, List<Answer> answers, QuestionType type) {
        this.imageResid = imageResid;
        this.message = message;
        this.answers = answers;
        this.type = type;

        correctAnswerAmount = calculateCorrectAnswerAmount(answers);
    }

    public int getCorrectAnswerAmount() {
        return correctAnswerAmount;
    }

    private int calculateCorrectAnswerAmount(List<Answer> answers) {
        int amount = 0;

        for (Answer answer : answers) {
            if (answer.isCorrect()) {
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

    public List<Answer> getAnswers() {
        return answers;
    }


}
