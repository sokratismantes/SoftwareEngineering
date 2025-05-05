package com.example.smartmed1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartmed1.model.Question;
import com.example.smartmed1.survey.QuestionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MentalHealthSurveyActivity extends AppCompatActivity {
    private QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        RecyclerView rv = findViewById(R.id.recyclerQuestions);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Question> questions = loadQuestions(); // from JSON
        adapter = new QuestionAdapter(questions);
        rv.setAdapter(adapter);

        findViewById(R.id.btnSubmit).setOnClickListener(v -> onSubmit());
    }

    private void onSubmit() {
        if (!adapter.allAnswered()) {
            new AlertDialog.Builder(this)
                    .setMessage("Παρακαλώ απαντήστε σε όλες τις ερωτήσεις πριν συνεχίσετε.")
                    .setPositiveButton("Εντάξει", null)
                    .show();
            return;
        }
        List<Object> answers = adapter.getAnswersInOrder();
        int anxietyScore    = computeAnxiety(answers);
        int depressionScore = computeDepression(answers);

        Intent i = new Intent(this, ResultsActivity.class);
        i.putExtra("ANX", anxietyScore);
        i.putExtra("DEP", depressionScore);
        startActivity(i);
    }

    private List<Question> loadQuestions() {
        try (InputStream is = getAssets().open("questions.json")) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONArray arr = new JSONArray(json);
            List<Question> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                int    id     = obj.getInt("id");
                String prompt = obj.getString("prompt");
                Question.Type type = Question.Type.valueOf(obj.getString("type"));
                list.add(new Question(id, prompt, type));
            }
            return list;
        } catch (IOException | JSONException e) {
            Log.e("Survey", "Failed to load questions.json", e);
            return Collections.emptyList();
        }
    }

    /** Example scoring: sum first half for anxiety */
    private int computeAnxiety(List<Object> answers) {
        int sum = 0;
        for (int i = 0; i < answers.size() / 2; i++) {
            Object a = answers.get(i);
            if (a instanceof Integer) sum += (Integer) a;
            else if (a instanceof String) sum += ((String) a).length();  // placeholder
        }
        return Math.min(sum, 100);  // cap at 100
    }

    /** Example scoring: sum second half for depression */
    private int computeDepression(List<Object> answers) {
        int sum = 0;
        for (int i = answers.size() / 2; i < answers.size(); i++) {
            Object a = answers.get(i);
            if (a instanceof Integer) sum += (Integer) a;
            else if (a instanceof String) sum += ((String) a).length();  // placeholder
        }
        return Math.min(sum, 100);
    }
}
