package com.kurbatov.todoapp.security.jwt;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterRQ {

    private String name;

    @NotBlank
    @Size(min = 5, max = 15)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    // TODO create a property instead because such things can be discussed and never changed afterwards
    /**
     * The url that should be used in the email for confirmation
     * email and completing the registration
     */
    private String emailConfirmationBrowserUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailConfirmationBrowserUrl() {
        return emailConfirmationBrowserUrl;
    }

    public void setEmailConfirmationBrowserUrl(String emailConfirmationBrowserUrl) {
        this.emailConfirmationBrowserUrl = emailConfirmationBrowserUrl;
    }
}
