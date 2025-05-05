package com.example.smartmed1.survey;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartmed1.R;
import com.example.smartmed1.model.Question;

import java.util.ArrayList;
import java.util.List;

import static com.example.smartmed1.model.Question.Type.*;

public class QuestionAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Question> questions;
    private final SparseArray<Object> answers = new SparseArray<>();

    public QuestionAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public int getItemViewType(int position) {
        switch (questions.get(position).getType()) {
            case TEXT:   return 0;
            case SCALE:  return 1;
            case RATING: return 2;
            default:     return -1;
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View v = inf.inflate(R.layout.item_question_text, parent, false);
            return new TextVH(v);
        } else if (viewType == 1) {
            View v = inf.inflate(R.layout.item_question_scale, parent, false);
            return new ScaleVH(v);
        } else {
            View v = inf.inflate(R.layout.item_question_rating, parent, false);
            return new RatingVH(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int pos) {
        Question q = questions.get(pos);
        if (vh instanceof TextVH) {
            ((TextVH) vh).bind(q);
        } else if (vh instanceof ScaleVH) {
            ((ScaleVH) vh).bind(q);
        } else {
            ((RatingVH) vh).bind(q);
        }
    }

    // ─── ViewHolders ─────────────────────────────────────────────────────────────

    class TextVH extends RecyclerView.ViewHolder {
        TextView tv;
        EditText et;

        TextVH(View v) {
            super(v);
            tv = v.findViewById(R.id.tvPrompt);
            et = v.findViewById(R.id.etAnswer);
        }

        void bind(Question q) {
            tv.setText(q.getPrompt());
            et.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
                @Override public void afterTextChanged(Editable e) {}
                @Override
                public void onTextChanged(CharSequence s, int st, int b, int c) {
                    // store the String answer
                    answers.put(q.getId(), s.toString());
                }
            });
        }
    }

    class ScaleVH extends RecyclerView.ViewHolder {
        TextView tv;
        Spinner sp;

        ScaleVH(View v) {
            super(v);
            tv = v.findViewById(R.id.tvPrompt);
            sp = v.findViewById(R.id.spinnerScale);
        }

        void bind(Question q) {
            tv.setText(q.getPrompt());

            // You need to set an adapter on the Spinner somewhere, e.g.
            // ArrayAdapter<String> adapter = ...
            // sp.setAdapter(adapter);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // store the selected index
                    answers.put(q.getId(), position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // optional: remove answer or set to default
                    answers.put(q.getId(), -1);
                }
            });
        }
    }

    class RatingVH extends RecyclerView.ViewHolder {
        TextView tv;
        RatingBar rb;

        RatingVH(View v) {
            super(v);
            tv = v.findViewById(R.id.tvPrompt);
            rb = v.findViewById(R.id.ratingBar);
        }

        void bind(Question q) {
            tv.setText(q.getPrompt());
            rb.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
                // store the integer rating 0–5
                answers.put(q.getId(), (int) rating);
            });
        }
    }

    // ─── Helpers ────────────────────────────────────────────────────────────────

    /** Ensure every question has an answer before submit **/
    public boolean allAnswered() {
        return answers.size() == questions.size();
    }

    /** Collect in-order answers for scoring **/
    public List<Object> getAnswersInOrder() {
        List<Object> out = new ArrayList<>();
        for (Question q : questions) {
            out.add(answers.get(q.getId()));
        }
        return out;
    }
}
