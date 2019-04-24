package com.example.android.videogamequiz;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import static com.example.android.videogamequiz.Constants.ANSWER_LIMIT;

public class MainActivity extends AppCompatActivity {

    private List<Company> companies;
    private List<Question> questions;
    private int currentQuestionLocation;
    private int counterOfCorrectAnswers;
    private boolean[] results;
    private Random random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Random();

        setupAnswerViewListeners();

        companies = CompanyRepository.getCompanies();

        initialize();
    }

    private void initialize() {
        questions = QuestionUtils.generateQuestions(companies, this);
        results = new boolean[questions.size()];

        currentQuestionLocation = 0;

        displayQuestion(currentQuestionLocation);
        displayProgress();

        Button nextButton = findViewById(R.id.button_main_next);
        nextButton.setVisibility(View.VISIBLE);

        Button resetButton = findViewById(R.id.button_main_reset);
        resetButton.setVisibility(View.INVISIBLE);

        Button resultsButton = findViewById(R.id.button_main_show_results);
        resultsButton.setVisibility(View.INVISIBLE);

        enableAnswers(true);
    }

    private void displayProgress() {
        TextView progressTextView = findViewById(R.id.textView_main_progress);
        progressTextView.setText((currentQuestionLocation + 1) + " " + getString(R.string.label_of) + " " + questions.size());
    }

    private void setupAnswerViewListeners() {

        for (int i = 0; i < ANSWER_LIMIT; i++) {
            int answerCheckBoxResid = getAnswerCheckBoxResid(i);
            CheckBox answerCheckBox = findViewById(answerCheckBoxResid);
            answerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if ((boolean) buttonView.getTag()) {
                        if (isChecked) {
                            counterOfCorrectAnswers++;
                        } else {
                            counterOfCorrectAnswers--;
                        }

                        results[currentQuestionLocation] = questions.get(currentQuestionLocation).getCorrectAnswerAmount() == counterOfCorrectAnswers;
                    }
                }
            });

            int answerRadioButtonResid = getAnswerRadioButtonResid(i);
            RadioButton answerRadioButton = findViewById(answerRadioButtonResid);

            answerRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if ((boolean) buttonView.getTag()) {
                        if (isChecked) {
                            counterOfCorrectAnswers++;
                        } else {
                            counterOfCorrectAnswers--;
                        }

                        results[currentQuestionLocation] = counterOfCorrectAnswers == 1;
                    }
                }
            });
        }

        EditText answerEditText = findViewById(R.id.editText_main_answer);
        answerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredAnswer = s.toString();
                EditText answerEditText = findViewById(R.id.editText_main_answer);
                String correctAnswer = (String)answerEditText.getTag();
                results[currentQuestionLocation] = correctAnswer != null && enteredAnswer != null
                        && enteredAnswer.toLowerCase().equals(correctAnswer.toLowerCase());
            }
        });
    }

    private void displayQuestion(int questionLocation) {
        Question question = questions.get(questionLocation);

        TextView questionTextView = findViewById(R.id.textView_main_question);
        questionTextView.setText(question.getMessage());

        ImageView questionImageView = findViewById(R.id.imageView_main_question_image);
        questionImageView.setImageResource(question.getImageResid());

        LinearLayout manyAnswersQuestionLinearLayout = findViewById(R.id.linearLayout_main_many_answers_question);
        LinearLayout oneAnswerQuestionRadioGroup = findViewById(R.id.radioGroup_main_one_answer_question);
        EditText answerEditText = findViewById(R.id.editText_main_answer);

        if (question.getType() == QuestionType.ManyCorrectAnswers) {
            int counter = 0;
            for (Answer answer : question.getAnswers()) {
                manyAnswersQuestionLinearLayout.setVisibility(View.VISIBLE);
                oneAnswerQuestionRadioGroup.setVisibility(View.GONE);
                answerEditText.setVisibility(View.GONE);

                int answerCheckBoxResid = getAnswerCheckBoxResid(counter);
                CheckBox answerCheckBox = findViewById(answerCheckBoxResid);
                answerCheckBox.setText(answer.getMessage());
                answerCheckBox.setTag(answer.isCorrect());

                counter++;
            }

        } else {
            if (random.nextBoolean()) {
                int counter = 0;
                for (Answer answer : question.getAnswers()) {
                    manyAnswersQuestionLinearLayout.setVisibility(View.GONE);
                    oneAnswerQuestionRadioGroup.setVisibility(View.VISIBLE);
                    answerEditText.setVisibility(View.GONE);

                    int answerRadioButtonResid = getAnswerRadioButtonResid(counter);
                    RadioButton answerRadioButton = findViewById(answerRadioButtonResid);

                    answerRadioButton.setText(answer.getMessage());
                    answerRadioButton.setTag(answer.isCorrect());

                    counter++;
                }
            } else {
                manyAnswersQuestionLinearLayout.setVisibility(View.GONE);
                oneAnswerQuestionRadioGroup.setVisibility(View.GONE);
                answerEditText.setVisibility(View.VISIBLE);
                answerEditText.setText("");

                for (Answer answer : question.getAnswers()) {
                   if (answer.isCorrect()) {
                        answerEditText.setTag(answer.getMessage());
                        break;
                    }
                }
            }
        }
    }

    private int getAnswerCheckBoxResid(int counter) {
        switch (counter) {
            case 0:
                return R.id.checkBox_main_answer0;
            case 1:
                return R.id.checkBox_main_answer1;
            case 2:
                return R.id.checkBox_main_answer2;
            default:
                return R.id.checkBox_main_answer3;
        }
    }

    private int getAnswerRadioButtonResid(int counter) {
        switch (counter) {
            case 0:
                return R.id.radioButton_main_answer0;
            case 1:
                return R.id.radioButton_main_answer1;
            case 2:
                return R.id.radioButton_main_answer2;
            default:
                return R.id.radioButton_main_answer3;
        }
    }

    public void nextQuestion(View view) {
        Toast.makeText(this, results[currentQuestionLocation] ? getString(R.string.message_correct) : getString(R.string.message_incorrect),
                Toast.LENGTH_SHORT).show();

        if (currentQuestionLocation < questions.size() - 1) {
            counterOfCorrectAnswers = 0;
            currentQuestionLocation++;
            displayQuestion(currentQuestionLocation);
            displayProgress();
            resetAnswers();
        } else {
            Button nextButton = findViewById(R.id.button_main_next);
            nextButton.setVisibility(View.GONE);

            Button resultsButton = findViewById(R.id.button_main_show_results);
            resultsButton.setVisibility(View.VISIBLE);

            Button resetButton = findViewById(R.id.button_main_reset);
            resetButton.setVisibility(View.VISIBLE);

            enableAnswers(false);
        }
    }

    private void enableAnswers(boolean enable) {
        for (int i = 0; i < ANSWER_LIMIT; i++) {
            int answerCheckBoxResid = getAnswerCheckBoxResid(i);
            CheckBox answerCheckBox = findViewById(answerCheckBoxResid);
            answerCheckBox.setEnabled(enable);

            int answerRadioButtonResid = getAnswerRadioButtonResid(i);
            RadioButton answerRadioButton = findViewById(answerRadioButtonResid);
            answerRadioButton.setEnabled(enable);
        }

        EditText answerEditText = findViewById(R.id.editText_main_answer);
        answerEditText.setEnabled(enable);
    }

    private void resetAnswers() {
        for (int i = 0; i < ANSWER_LIMIT; i++) {
            int answerCheckBoxResid = getAnswerCheckBoxResid(i);
            CheckBox answerCheckBox = findViewById(answerCheckBoxResid);
            answerCheckBox.setChecked(false);
            answerCheckBox.jumpDrawablesToCurrentState();

            int answerRadioButtonResid = getAnswerRadioButtonResid(i);
            RadioButton answerRadioButton = findViewById(answerRadioButtonResid);
            answerRadioButton.setChecked(false);
            answerRadioButton.jumpDrawablesToCurrentState();
        }

        EditText answerEditText = findViewById(R.id.editText_main_answer);
        answerEditText.setText("");
    }

    public void showResults(View view) {
        String message = getString(R.string.message_results) + "\n\n";

        int totalCorrectAnswerCounter = 0;
        for (int i = 0; i < results.length; i++) {

            if (results[i]) {
                totalCorrectAnswerCounter++;
            }

            message += getString(R.string.message_question) + (i + 1)
                    + ": " + (results[i] ? getString(R.string.message_correct) : getString(R.string.message_incorrect)) + "\n";
        }

        message += "\n" + getString(R.string.message_total_answers) + " " + totalCorrectAnswerCounter;

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void again(View view) {
        initialize();
    }
}
