// com/example/smartmed1/ui/QuestionScreen.java
package com.example.smartmed1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartmed1.R;
import com.example.smartmed1.model.Answer;
import com.example.smartmed1.model.Question;
import com.example.smartmed1.model.Question.QuestionType;
import com.example.smartmed1.service.QuizEngine;

import java.util.List;

public class QuestionScreen extends AppCompatActivity {
    private QuizEngine quizEngine;
    private LinearLayout questionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_screen);

        quizEngine = new QuizEngine();
        questionsContainer = findViewById(R.id.questionsContainer);

        // 1) Fetch questions
        List<Question> questions = quizEngine.fetchQuestions();

        // 2) Inflate a view for each question
        LayoutInflater inflater = LayoutInflater.from(this);
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

        // 3) View Results button
        findViewById(R.id.btnViewResults).setOnClickListener(v -> {
            if (!quizEngine.validateAllAnswered()) {
                startActivity(new Intent(this, NoAnswerErrorScreen.class));
                return;
            }
            startActivity(new Intent(this, ResultSummaryScreen.class));
        });
    }

    private void bindTextQuestion(View view, Question q) {
        TextView tv = view.findViewById(R.id.tvQuestionText);
        EditText et = view.findViewById(R.id.etAnswerText);
        tv.setText(q.getText());

        // 1) Pre‐record a default (empty) answer so it counts as answered
        quizEngine.recordAnswer(new Answer(q.getId(), ""));

        // 2) Replace focus listener with a TextWatcher to capture text changes
        et.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                quizEngine.recordAnswer(new Answer(q.getId(), s.toString().trim()));
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void bindSpinnerQuestion(View view, Question q) {
        TextView tv = view.findViewById(R.id.tvQuestionText);
        Spinner sp = view.findViewById(R.id.spinnerAnswer);
        tv.setText(q.getText());

        // 1) Default answer = 0
        quizEngine.recordAnswer(new Answer(q.getId(), "0"));

        // 2) Populate spinner
        Integer[] vals = new Integer[11];
        for (int i = 0; i <= 10; i++) vals[i] = i;
        sp.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                vals));

        // 3) Record selections
        sp.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View v, int pos, long id) {
                quizEngine.recordAnswer(new Answer(q.getId(), String.valueOf(vals[pos])));
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void bindStarQuestion(View view, Question q) {
        TextView tv = view.findViewById(R.id.tvQuestionText);
        RatingBar rb = view.findViewById(R.id.ratingAnswer);
        tv.setText(q.getText());

        // 1) Pre-record a default of 0 stars so it counts as answered
        quizEngine.recordAnswer(new Answer(q.getId(), "0"));

        // 2) Now listen for changes
        rb.setNumStars(5);
        rb.setStepSize(1f);
        rb.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            // overwrite with the new value
            quizEngine.recordAnswer(new Answer(q.getId(), String.valueOf((int)rating)));
        });
    }
}
