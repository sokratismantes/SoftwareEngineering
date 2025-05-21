// DB.java
package com.example.smartmed1.data;

import com.example.smartmed1.model.Question;
import com.example.smartmed1.model.Answer;
import com.example.smartmed1.model.Doctor;

import java.util.Arrays;
import java.util.List;

public class DB {
    private static DB instance;

    private DB() { /* open or get writable DB */ }

    public static synchronized DB getInstance() {
        if (instance == null) instance = new DB();
        return instance;
    }

    public List<Question> getMentalHealthQuestions() {
        // TODO: replace this stub with a real database query.
        return Arrays.asList(
                new Question(1, "Πόσο συχνά νιώθετε άγχος;", Question.QuestionType.SPINNER),
                new Question(2, "Πόσο έντονο είναι το στρες σας;", Question.QuestionType.STAR),
                new Question(3, "Περιγράψτε την διάθεσή σας σήμερα.", Question.QuestionType.TEXT),
                new Question(4, "Πόσες ώρες κοιμηθήκατε εχθές το βράδυ;", Question.QuestionType.SPINNER),
                new Question(5, "Σε ποιο βαθμό δυσκολεύεστε να συγκεντρωθείτε;", Question.QuestionType.STAR),
                new Question(6, "Αναφέρετε κάτι που σας έκανε να χαμογελάσετε σήμερα.", Question.QuestionType.TEXT),
                new Question(7, "Πόσο συχνά αισθάνεστε κόπωση χωρίς λόγο;", Question.QuestionType.SPINNER),
                new Question(8, "Βαθμολογήστε την ποιότητα της διατροφής σας σήμερα.", Question.QuestionType.STAR),
                new Question(9, "Υπάρχει κάτι συγκεκριμένο που σας προκαλεί άγχος αυτή την περίοδο;", Question.QuestionType.TEXT),
                new Question(10, "Πόσο ικανοποιημένοι είστε με τον ύπνο σας την τελευταία εβδομάδα;", Question.QuestionType.STAR)
        );
    }

    public boolean saveAnswer(Answer answer) {
        // TODO: insert into user_answers table
        return false;
    }

    public Doctor findDoctorByAMKA(String amka) {
        // TODO: query doctors table by AMKA
        return null;
    }
}
