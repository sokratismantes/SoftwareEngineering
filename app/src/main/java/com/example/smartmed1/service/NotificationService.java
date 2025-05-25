// app/src/main/java/com/example/smartmed1/service/NotificationService.java
package com.example.smartmed1.service;

import android.content.Context;
import android.util.Log;

import com.example.smartmed1.Files;
import com.example.smartmed1.model.ScoreResult;

/**
 * Service responsible for notifying doctors with an access link
 * to the patient’s questionnaire results AND triggering the PDF/email flow.
 */
public class NotificationService {
    private static final String TAG = "NotificationService";
    private final Context ctx;
    private final QuizEngine quizEngine;

    public NotificationService(Context ctx) {
        this.ctx = ctx;
        this.quizEngine = new QuizEngine(ctx);
    }

    /**
     * “Sends” an access link and then fires off the PDF/email share.
     *
     * @param resultId    the internal ID of the questionnaire result
     * @param doctorId    the internal ID of the doctor
     * @param doctorEmail the doctor's email address
     * @return true if we successfully kicked off both steps
     */
    public boolean sendAccessLink(int resultId,
                                  int doctorId, String doctorEmail,
                                  long startTs,
                                  long endTs) {
        // 1) Stub: notify backend / send link (you can flesh this out later)
        Log.d(TAG, "Pretending to send link for result " + resultId
                + " to doctor " + doctorId + " at " + doctorEmail);

        // 2) Re‐calculate the ScoreResult
        ScoreResult result = quizEngine.getResultById(resultId);
        if (result == null) {
            Log.e(TAG, "No ScoreResult for id=" + resultId);
            return false;
        }

        // 3) Delegate to Files to build the PDF and open the email chooser
        Files.exportAndShareResults(ctx, result, doctorEmail, startTs, endTs);
        return true;
    }
}
