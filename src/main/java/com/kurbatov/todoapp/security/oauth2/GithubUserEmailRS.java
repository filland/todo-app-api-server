package com.kurbatov.todoapp.security.oauth2;

/**
 * DTO for the response for the call of github API endpoint
 * https://api.github.com/user/emails
 */
public class GithubUserEmailRS {
    private String email;
    private Boolean primary;
    private Boolean verified;
    private String visibility;

    public GithubUserEmailRS() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
