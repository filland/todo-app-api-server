package com.kurbatov.todoapp.service.email;

/**
 * Service for handling emails
 */
public interface EmailService {

    /**
     * Send an email to the user after he/she signed up to complete registration
     *
     * @param subject
     * @param recipient
     * @param confirmationLink
     */
    void sendRegistrationConfirmationEmail(String subject, String recipient, String confirmationLink);
}
