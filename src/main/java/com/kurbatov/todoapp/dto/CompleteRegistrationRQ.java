package com.kurbatov.todoapp.dto;

public class CompleteRegistrationRQ {

    /**
     * Token to complete user's registration
     */
    private String token;

    public CompleteRegistrationRQ() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
