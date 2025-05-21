// app/src/main/java/com/example/smartmed1/service/NotificationService.java
package com.example.smartmed1.service;

import com.example.smartmed1.service.EmailService;

public class NotificationService {
    private final EmailService emailService;

    public NotificationService() {
        emailService = new EmailService();
    }

    /**
     * Send an email with an access link to the doctor.
     *
     * @param resultId     the ID (or placeholder) of the quiz result
     * @param doctorId     the doctor’s internal ID
     * @param doctorEmail  the doctor’s email address
     */
    public void sendAccessLink(int resultId, int doctorId, String doctorEmail) {
        String link = generateAccessLink(resultId, doctorId);
        String subject = "Πρόσβαση στα αποτελέσματά σας";
        String body = "Ο ασθενής σας μοιράστηκε τα αποτελέσματά του. "
                + "Δείτε τα εδώ: " + link;
        emailService.sendEmail(doctorEmail, subject, body);
    }

    /** Build the URL (customize to your backend) */
    private String generateAccessLink(int resultId, int doctorId) {
        return "https://yourapp.example.com/results?resultId="
                + resultId + "&doctorId=" + doctorId;
    }
}
