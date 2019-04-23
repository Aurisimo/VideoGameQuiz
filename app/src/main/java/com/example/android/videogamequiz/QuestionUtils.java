package com.example.android.videogamequiz;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.android.videogamequiz.Constants.ANSWER_LIMIT;
import static com.example.android.videogamequiz.Constants.QUESTION_LIMIT;

public class QuestionUtils {

    private static Random random = new Random();

    public static List<Question> generateQuestions(List<Company> companies, Context context) {
        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < Math.min(QUESTION_LIMIT, companies.size()); i++) {

            QuestionType type = getRandomType();

            if (type == QuestionType.OneCorrectAnswer) {
                questions.add(generateOneCorrectAnswerQuestion(companies, i, context));
            } else {
                questions.add(generateManyCorrectAnswersQuestion(companies, i, context));
            }
        }

        shuffleList(questions);

        return questions;
    }

    private static <E> void shuffleList(List<E> list) {
        for (int i = 0; i < list.size(); i++) {

            int answerToSwapWithLocation = random.nextInt(list.size());
            swapElements(list, i, answerToSwapWithLocation);
        }
    }

    private static <E> void swapElements(List<E> list, int element1Location, int element2Location) {
        E temp = list.get(element1Location);

        list.set(element1Location, list.get(element2Location));
        list.set(element2Location, temp);
    }

    private static Question generateManyCorrectAnswersQuestion(List<Company> companies, int pickedCompanyLocation, Context context) {
        Company company = companies.get(pickedCompanyLocation);
        List<Answer> answers = generateAnswersForManyCorrectAnswersQuestion(companies, pickedCompanyLocation, company);

        return new Question(company.getCompanyLogoResid(), context.getString(R.string.header_many_answers_question) + " " + company.getCompanyName(),
                answers, QuestionType.ManyCorrectAnswers);
    }

    private static List<Answer> generateAnswersForManyCorrectAnswersQuestion(List<Company> companies, int pickedCompanyLocation, Company company) {
        List<Answer> generatedAnswers = new ArrayList<>();

        int correctAnswerGameLocation = random.nextInt(company.getGames().size());
        Game correctAnswerGame = company.getGames().get(correctAnswerGameLocation);
        generatedAnswers.add(new Answer(correctAnswerGame.getTitle(), true));
        List<Integer> checkedGamesOfPickedCompany = new ArrayList<>();
        checkedGamesOfPickedCompany.add(correctAnswerGameLocation);

        List<Integer> checkedCompanyLocations = new ArrayList<>();
        checkedCompanyLocations.add(pickedCompanyLocation);

        for (int i = 1; i < ANSWER_LIMIT; i++) {
            Game game;
            boolean isCorrect;
            if (random.nextInt(2) == 0) {
                int randomCorrectAnswerGameLocation = random.nextInt(company.getGames().size());
                while (checkedGamesOfPickedCompany.contains(randomCorrectAnswerGameLocation)) {
                    randomCorrectAnswerGameLocation = random.nextInt(company.getGames().size());
                }

                checkedGamesOfPickedCompany.add(randomCorrectAnswerGameLocation);

                game = company.getGames().get(randomCorrectAnswerGameLocation);
                isCorrect = true;
            } else {
                int randomCompanyLocation = random.nextInt(companies.size());
                while (checkedCompanyLocations.contains(randomCompanyLocation)) {
                    randomCompanyLocation = random.nextInt(companies.size());
                }

                checkedCompanyLocations.add(randomCompanyLocation);

                List<Game> games = companies.get(randomCompanyLocation).getGames();
                game = games.get(random.nextInt(games.size()));

                isCorrect = false;
            }

            generatedAnswers.add(new Answer(game.getTitle(), isCorrect));
        }

        shuffleList(generatedAnswers);

        return generatedAnswers;
    }

    private static Question generateOneCorrectAnswerQuestion(List<Company> companies, int pickedCompanyLocation, Context context) {
        Company company = companies.get(pickedCompanyLocation);
        int randomGameLocation = random.nextInt(company.getGames().size());
        Game game = company.getGames().get(randomGameLocation);
        List<Answer> answers = generateAnswersForOneCorrectAnswerQuestion(companies, pickedCompanyLocation, company);

        return new Question(game.getImageResid(), context.getString(R.string.header_one_answer_question) + " " + game.getTitle(),
                answers, QuestionType.OneCorrectAnswer);
    }

    private static List<Answer> generateAnswersForOneCorrectAnswerQuestion(List<Company> companies, int pickedCompanyLocation,
                                                                           Company company) {
        List<Answer> generatedAnswers = new ArrayList<>();

        generatedAnswers.add(new Answer(company.getCompanyName(), true));

        List<Integer> checkedCompanyLocations = new ArrayList<>();
        checkedCompanyLocations.add(pickedCompanyLocation);

        for (int i = 1; i < ANSWER_LIMIT; i++) {
            int randomCompanyLocation = random.nextInt(companies.size());
            while (checkedCompanyLocations.contains(randomCompanyLocation)) {
                randomCompanyLocation = random.nextInt(companies.size());
            }

            checkedCompanyLocations.add(randomCompanyLocation);
            generatedAnswers.add(new Answer(companies.get(randomCompanyLocation).getCompanyName(), false));
        }

        shuffleList(generatedAnswers);

        return generatedAnswers;
    }

    private static QuestionType getRandomType() {
        if (random.nextInt(2) == 0) {
            return QuestionType.OneCorrectAnswer;
        }

        return QuestionType.ManyCorrectAnswers;
    }
}
