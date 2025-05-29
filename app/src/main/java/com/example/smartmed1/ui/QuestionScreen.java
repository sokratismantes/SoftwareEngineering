package com.example.smartmed1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed1.R;
import com.example.smartmed1.model.Answer;
import com.example.smartmed1.model.Question;
import com.example.smartmed1.service.QuizEngine;

import java.util.List;

public class QuestionScreen extends AppCompatActivity {
    private QuizEngine quizEngine;
    private LinearLayout questionsContainer;

    private final ActivityResultLauncher<Intent> resultsLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        // no automatic clear here—we let the user explicitly restart if desired
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quizEngine = new QuizEngine(this);

        setContentView(R.layout.questionscreen);

        questionsContainer = findViewById(R.id.questionsContainer);

        inflateQuestions();

        findViewById(R.id.btnViewResults).setOnClickListener(v -> {

            for (int i = 0; i < questionsContainer.getChildCount(); i++) {
                View questionItem = questionsContainer.getChildAt(i);

                // TEXT questions
                EditText et = questionItem.findViewById(R.id.etAnswerText);
                if (et != null) {
                    String answer = et.getText().toString().trim();
                    int qid = (int) et.getTag();            // assume you tagged it with question ID
                    quizEngine.recordAnswer(new Answer(qid, answer));
                }

                // SPINNER questions
                Spinner spinner = questionItem.findViewById(R.id.spinnerAnswer);
                if (spinner != null) {
                    int qid = (int) spinner.getTag();
                    String val = spinner.getSelectedItem().toString();
                    quizEngine.recordAnswer(new Answer(qid, val));
                }

                // STAR questions
                RatingBar rb = questionItem.findViewById(R.id.ratingAnswer);
                if (rb != null) {
                    int qid = (int) rb.getTag();
                    int rating = (int) rb.getRating();
                    quizEngine.recordAnswer(new Answer(qid, String.valueOf(rating)));
                }
            }


            if (!quizEngine.validateAllAnswered()) {
                startActivity(new Intent(this, NoAnswerErrorScreen.class));
                return;
            }

            // 2) Launch results
            Intent i = new Intent(this, ResultSummaryScreen.class);
            resultsLauncher.launch(i);
        });

        // Example of a "Restart Quiz" button
        findViewById(R.id.btnRestartQuiz).setOnClickListener(v -> {
            quizEngine.clearAnswers();
            questionsContainer.removeAllViews();
            inflateQuestions();
        });
    }

    private void inflateQuestions() {
        LayoutInflater inflater = LayoutInflater.from(this);
        List<Question> questions = quizEngine.fetchQuestions();

        for (Question q : questions) {
            View itemView;
            switch (q.getType()) {
                case TEXT:
                    itemView = inflater.inflate(R.layout.item_question_text, questionsContainer, false);
                    bindTextQuestion(itemView, q);
                    break;
                case SPINNER:
                    itemView = inflater.inflate(R.layout.item_question_spinner, questionsContainer, false);
                    bindSpinnerQuestion(itemView, q);
                    break;
                case STAR:
                default:
                    itemView = inflater.inflate(R.layout.item_question_star, questionsContainer, false);
                    bindStarQuestion(itemView, q);
                    break;
            }
            questionsContainer.addView(itemView);
        }
    }

    private void bindTextQuestion(View view, Question q) {
        TextView tv = view.findViewById(R.id.tvQuestionText);
        EditText et = view.findViewById(R.id.etAnswerText);
        tv.setText(q.getText());
        et.setTag(q.getId());

        et.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                quizEngine.recordAnswer(new Answer(q.getId(), s.toString().trim()));
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void bindSpinnerQuestion(View view, Question q) {
        TextView tv = view.findViewById(R.id.tvQuestionText);
        Spinner sp = view.findViewById(R.id.spinnerAnswer);
        tv.setText(q.getText());
        sp.setTag(q.getId());

        Integer[] vals = new Integer[11];
        for (int i = 0; i <= 10; i++) vals[i] = i;
        sp.setAdapter(new android.widget.ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, vals));

        final boolean[] userTouched = { false };
        sp.setOnTouchListener((v, e) -> {
            userTouched[0] = true;
            return false;
        });
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                if (!userTouched[0]) return;  // skip the initial set
                quizEngine.recordAnswer(new Answer(q.getId(), String.valueOf(vals[pos])));
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void bindStarQuestion(View view, Question q) {
        TextView tv = view.findViewById(R.id.tvQuestionText);
        RatingBar rb = view.findViewById(R.id.ratingAnswer);
        tv.setText(q.getText());
        rb.setTag(q.getId());

        rb.setNumStars(5);
        rb.setStepSize(1f);
        rb.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (!fromUser) return;            // ← ignore the initial system callback
            quizEngine.recordAnswer(
                    new Answer(q.getId(), String.valueOf((int) rating))
            );
        });
    }
}
