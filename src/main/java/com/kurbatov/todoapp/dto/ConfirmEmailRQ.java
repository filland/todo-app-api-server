package com.kurbatov.todoapp.dto;

public class ConfirmEmailRQ {

    /**
     * Token to complete user's registration
     */
    private String token;

    public ConfirmEmailRQ() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
