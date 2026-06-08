package com.greenloop.service;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailService {

    private static final String FROM_EMAIL = "vidushasriyananda@gmail.com";
    private static final String APP_PASSWORD = "kxdfztyhszyouiyd";

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });
    }

    // Task 8 — notify client when order is dispatched
    public void sendDispatchEmail(String toEmail, String clientName, String orderId) {
        try {
            Session session = createSession();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your GreenLoop Order #" + orderId + " Has Been Dispatched");
            message.setText(
                    "Dear " + clientName + ",\n\n" +
                            "Great news! Your order #" + orderId + " has been dispatched and is on its way.\n\n" +
                            "Thank you for choosing GreenLoop.\n\nBest regards,\nGreenLoop Team"
            );
            Transport.send(message);
            System.out.println("Dispatch email sent to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Task 9 — notify delivery agent when assigned to an order
    public void sendAssignmentEmail(String toEmail, String agentName, String orderId) {
        try {
            Session session = createSession();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("New Delivery Assignment — Order #" + orderId);
            message.setText(
                    "Dear " + agentName + ",\n\n" +
                            "You have been assigned to deliver Order #" + orderId + ".\n" +
                            "Please log in to the GreenLoop system for full details.\n\n" +
                            "Best regards,\nGreenLoop Team"
            );
            Transport.send(message);
            System.out.println("Assignment email sent to " + agentName);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
