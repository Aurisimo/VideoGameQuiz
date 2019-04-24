package com.example.android.videogamequiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import static com.example.android.videogamequiz.Constants.OPTION_LIMIT;

public class MainActivity extends AppCompatActivity {

    private List<Company> companies;
    private List<Question> questions;
    private int currentQuestionLocation;
    private int counterOfCorrectOptions;
    private boolean[] results;
    private Random random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Random();

        setupOptionViewListeners();

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
        
        enableOptions(true);
        
        resetOptions();
    }

    private void displayProgress() {
        TextView progressTextView = findViewById(R.id.textView_main_progress);
        progressTextView.setText((currentQuestionLocation + 1) + " " + getString(R.string.label_of) + " " + questions.size());
    }

    private void setupOptionViewListeners() {

        for (int i = 0; i < OPTION_LIMIT; i++) {
            int optionCheckBoxResid = getOptionCheckBoxResid(i);
            CheckBox optionCheckBox = findViewById(optionCheckBoxResid);
            optionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if ((boolean) buttonView.getTag()) {
                                counterOfCorrectOptions++;
                            } else {
                                counterOfCorrectOptions--;
                            }
                        } else {
                            if ((boolean) buttonView.getTag()) {
                                counterOfCorrectOptions--;
                            } else {
                                counterOfCorrectOptions++;
                            }
                        }

                        results[currentQuestionLocation] = questions.get(currentQuestionLocation).getCorrectOptionAmount() == counterOfCorrectOptions;
                }
            });

            int optionRadioButtonResid = getOptionRadioButtonResid(i);
            RadioButton optionRadioButton = findViewById(optionRadioButtonResid);

            optionRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if ((boolean) buttonView.getTag()) {
                        if (isChecked) {
                            counterOfCorrectOptions++;
                        } else {
                            counterOfCorrectOptions--;
                        }

                        results[currentQuestionLocation] = counterOfCorrectOptions == 1;
                    }
                }
            });
        }

        EditText optionEditText = findViewById(R.id.editText_main_option);
        optionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredOption = s.toString();
                EditText optionEditText = findViewById(R.id.editText_main_option);
                String correctOption = (String)optionEditText.getTag();
                results[currentQuestionLocation] = correctOption != null && enteredOption != null
                        && enteredOption.toLowerCase().equals(correctOption.toLowerCase());
            }
        });
    }

    private void displayQuestion(int questionLocation) {
        Question question = questions.get(questionLocation);

        TextView questionTextView = findViewById(R.id.textView_main_question);
        questionTextView.setText(question.getMessage());

        ImageView questionImageView = findViewById(R.id.imageView_main_question_image);
        questionImageView.setImageResource(question.getImageResid());

        LinearLayout manyOptionsQuestionLinearLayout = findViewById(R.id.linearLayout_main_many_options_question);
        LinearLayout oneOptionQuestionRadioGroup = findViewById(R.id.radioGroup_main_one_option_question);
        EditText optionEditText = findViewById(R.id.editText_main_option);

        if (question.getType() == QuestionType.ManyCorrectOptions) {
            int counter = 0;
            for (Option option : question.getOptions()) {
                manyOptionsQuestionLinearLayout.setVisibility(View.VISIBLE);
                oneOptionQuestionRadioGroup.setVisibility(View.GONE);
                optionEditText.setVisibility(View.GONE);

                int optionCheckBoxResid = getOptionCheckBoxResid(counter);
                CheckBox optionCheckBox = findViewById(optionCheckBoxResid);
                optionCheckBox.setText(option.getMessage());
                optionCheckBox.setTag(option.isCorrect());

                counter++;
            }

        } else {
            if (random.nextBoolean()) {
                int counter = 0;
                for (Option option : question.getOptions()) {
                    manyOptionsQuestionLinearLayout.setVisibility(View.GONE);
                    oneOptionQuestionRadioGroup.setVisibility(View.VISIBLE);
                    optionEditText.setVisibility(View.GONE);

                    int optionRadioButtonResid = getOptionRadioButtonResid(counter);
                    RadioButton optionRadioButton = findViewById(optionRadioButtonResid);

                    optionRadioButton.setText(option.getMessage());
                    optionRadioButton.setTag(option.isCorrect());

                    counter++;
                }
            } else {
                manyOptionsQuestionLinearLayout.setVisibility(View.GONE);
                oneOptionQuestionRadioGroup.setVisibility(View.GONE);
                optionEditText.setVisibility(View.VISIBLE);
                optionEditText.setText("");

                for (Option option : question.getOptions()) {
                   if (option.isCorrect()) {
                        optionEditText.setTag(option.getMessage());
                        break;
                    }
                }
            }
        }
    }

    private int getOptionCheckBoxResid(int counter) {
        switch (counter) {
            case 0:
                return R.id.checkBox_main_option0;
            case 1:
                return R.id.checkBox_main_option1;
            case 2:
                return R.id.checkBox_main_option2;
            default:
                return R.id.checkBox_main_option3;
        }
    }

    private int getOptionRadioButtonResid(int counter) {
        switch (counter) {
            case 0:
                return R.id.radioButton_main_option0;
            case 1:
                return R.id.radioButton_main_option1;
            case 2:
                return R.id.radioButton_main_option2;
            default:
                return R.id.radioButton_main_option3;
        }
    }

    public void nextQuestion(View view) {
        Toast.makeText(this, results[currentQuestionLocation] ? getString(R.string.message_correct) : getString(R.string.message_incorrect),
                Toast.LENGTH_SHORT).show();

        if (currentQuestionLocation < questions.size() - 1) {
            counterOfCorrectOptions = 0;
            currentQuestionLocation++;
            displayQuestion(currentQuestionLocation);
            displayProgress();
            resetOptions();
        } else {
            Button nextButton = findViewById(R.id.button_main_next);
            nextButton.setVisibility(View.GONE);

            Button resultsButton = findViewById(R.id.button_main_show_results);
            resultsButton.setVisibility(View.VISIBLE);

            Button resetButton = findViewById(R.id.button_main_reset);
            resetButton.setVisibility(View.VISIBLE);

            enableOptions(false);
        }
    }

    private void enableOptions(boolean enable) {
        for (int i = 0; i < OPTION_LIMIT; i++) {
            int optionCheckBoxResid = getOptionCheckBoxResid(i);
            CheckBox optionCheckBox = findViewById(optionCheckBoxResid);
            optionCheckBox.setEnabled(enable);

            int optionRadioButtonResid = getOptionRadioButtonResid(i);
            RadioButton optionRadioButton = findViewById(optionRadioButtonResid);
            optionRadioButton.setEnabled(enable);
        }

        EditText optionEditText = findViewById(R.id.editText_main_option);
        optionEditText.setEnabled(enable);
    }

    private void resetOptions() {
        for (int i = 0; i < OPTION_LIMIT; i++) {
            int optionCheckBoxResid = getOptionCheckBoxResid(i);
            CheckBox optionCheckBox = findViewById(optionCheckBoxResid);
            optionCheckBox.setChecked(false);
            optionCheckBox.jumpDrawablesToCurrentState();

            int optionRadioButtonResid = getOptionRadioButtonResid(i);
            RadioButton optionRadioButton = findViewById(optionRadioButtonResid);
            optionRadioButton.setChecked(false);
            optionRadioButton.jumpDrawablesToCurrentState();
        }

        EditText optionEditText = findViewById(R.id.editText_main_option);
        optionEditText.setText("");
        optionEditText.setInputType(InputType.TYPE_NULL);
    }

    public void showResults(View view) {
        String message = getString(R.string.message_results) + "\n\n";

        int totalCorrectOptionCounter = 0;
        for (int i = 0; i < results.length; i++) {

            if (results[i]) {
                totalCorrectOptionCounter++;
            }

            message += getString(R.string.message_question) + (i + 1)
                    + ": " + (results[i] ? getString(R.string.message_correct) : getString(R.string.message_incorrect)) + "\n";
        }

        message += "\n" + getString(R.string.message_total_options) + " " + totalCorrectOptionCounter;

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void again(View view) {
        initialize();
    }
}
