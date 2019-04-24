package com.example.android.videogamequiz;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.android.videogamequiz.Constants.OPTION_LIMIT;
import static com.example.android.videogamequiz.Constants.QUESTION_LIMIT;

public class QuestionUtils {

    private static Random random = new Random();

    public static List<Question> generateQuestions(List<Company> companies, Context context) {
        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < Math.min(QUESTION_LIMIT, companies.size()); i++) {

            QuestionType type = getRandomType();

            if (type == QuestionType.OneCorrectOption) {
                questions.add(generateOneCorrectOptionQuestion(companies, i, context));
            } else {
                questions.add(generateManyCorrectOptionsQuestion(companies, i, context));
            }
        }

        shuffleList(questions);

        return questions;
    }

    private static <E> void shuffleList(List<E> list) {
        for (int i = 0; i < list.size(); i++) {

            int optionToSwapWithLocation = random.nextInt(list.size());
            swapElements(list, i, optionToSwapWithLocation);
        }
    }

    private static <E> void swapElements(List<E> list, int element1Location, int element2Location) {
        E temp = list.get(element1Location);

        list.set(element1Location, list.get(element2Location));
        list.set(element2Location, temp);
    }

    private static Question generateManyCorrectOptionsQuestion(List<Company> companies, int pickedCompanyLocation, Context context) {
        Company company = companies.get(pickedCompanyLocation);
        List<Option> options = generateOptionsForManyCorrectOptionsQuestion(companies, pickedCompanyLocation, company);

        return new Question(company.getCompanyLogoResid(), context.getString(R.string.header_many_options_question) + " " + company.getCompanyName(),
                options, QuestionType.ManyCorrectOptions);
    }

    private static List<Option> generateOptionsForManyCorrectOptionsQuestion(List<Company> companies, int pickedCompanyLocation, Company company) {
        List<Option> generatedOptions = new ArrayList<>();

        int correctOptionGameLocation = random.nextInt(company.getGames().size());
        Game correctOptionGame = company.getGames().get(correctOptionGameLocation);
        generatedOptions.add(new Option(correctOptionGame.getTitle(), true));
        List<Integer> checkedGamesOfPickedCompany = new ArrayList<>();
        checkedGamesOfPickedCompany.add(correctOptionGameLocation);

        List<Integer> checkedCompanyLocations = new ArrayList<>();
        checkedCompanyLocations.add(pickedCompanyLocation);

        for (int i = 1; i < OPTION_LIMIT; i++) {
            Game game;
            boolean isCorrect;
            if (random.nextBoolean()) {
                int randomCorrectOptionGameLocation = random.nextInt(company.getGames().size());
                while (checkedGamesOfPickedCompany.contains(randomCorrectOptionGameLocation)) {
                    randomCorrectOptionGameLocation = random.nextInt(company.getGames().size());
                }

                checkedGamesOfPickedCompany.add(randomCorrectOptionGameLocation);

                game = company.getGames().get(randomCorrectOptionGameLocation);
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

            generatedOptions.add(new Option(game.getTitle(), isCorrect));
        }

        shuffleList(generatedOptions);

        return generatedOptions;
    }

    private static Question generateOneCorrectOptionQuestion(List<Company> companies, int pickedCompanyLocation, Context context) {
        Company company = companies.get(pickedCompanyLocation);
        int randomGameLocation = random.nextInt(company.getGames().size());
        Game game = company.getGames().get(randomGameLocation);
        List<Option> options = generateOptionsForOneCorrectOptionQuestion(companies, pickedCompanyLocation, company);

        return new Question(game.getImageResid(), context.getString(R.string.header_one_option_question) + " " + game.getTitle(),
                options, QuestionType.OneCorrectOption);
    }

    private static List<Option> generateOptionsForOneCorrectOptionQuestion(List<Company> companies, int pickedCompanyLocation,
                                                                           Company company) {
        List<Option> generatedOptions = new ArrayList<>();

        generatedOptions.add(new Option(company.getCompanyName(), true));

        List<Integer> checkedCompanyLocations = new ArrayList<>();
        checkedCompanyLocations.add(pickedCompanyLocation);

        for (int i = 1; i < OPTION_LIMIT; i++) {
            int randomCompanyLocation = random.nextInt(companies.size());
            while (checkedCompanyLocations.contains(randomCompanyLocation)) {
                randomCompanyLocation = random.nextInt(companies.size());
            }

            checkedCompanyLocations.add(randomCompanyLocation);
            generatedOptions.add(new Option(companies.get(randomCompanyLocation).getCompanyName(), false));
        }

        shuffleList(generatedOptions);

        return generatedOptions;
    }

    private static QuestionType getRandomType() {
        if (random.nextBoolean()) {
            return QuestionType.OneCorrectOption;
        }

        return QuestionType.ManyCorrectOptions;
    }
}
