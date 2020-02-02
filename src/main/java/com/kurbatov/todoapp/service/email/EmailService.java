package com.kurbatov.todoapp.service.email;

import com.kurbatov.todoapp.security.CustomUserDetails;

/**
 * Service for sending emails
 */
public interface EmailService {

    /**
     * Send an email to the user after he/she signed up to complete registration
     * @param subject
     * @param recipient
     * @param confirmationLink
     */
    void sendRegistrationConfirmationEmail(String subject, String recipient, String confirmationLink);

    void confirmEmail(String confirmationToken);
}
