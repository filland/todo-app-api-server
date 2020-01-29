package com.kurbatov.todoapp.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {

    /**
     * Incorrect TodoApp API Request
     */
    INCORRECT_REQUEST(4001, "Incorrect Request. {}"),
    USER_WITH_EMAIL_EXISTS(4001, "User with the email {} already exists"),
    USER_WITH_USERNAME_EXISTS(4002, "User with the username {} already exists"),
    USER_ACCOUNT_WAS_NOT_CREATED(4003, "User account was not created"),
    AUTH_OAUTH2_UNAUTHORIZED_REDIRECT(4011, "An unauthorized redirect URI"),
    AUTH_OAUTH2_EMAIL_NOT_FOUND(4041, "Email not found from OAuth2 provider"),
    AUTH_OAUTH2_WRONG_AUTH_MECHANISM(4004, "You're signed up with {} account"),
    AUTH_OAUTH2_PROVIDER_NOT_SUPPORTED(4051, "Login with {} is not supported yet");

    private int code;
    private String description;

    ErrorType(int code, String description) {

        this.code = code;
        this.description = description;
    }

    public ErrorType getByCode(int code) {
        for (ErrorType errorType : values()) {
            if (errorType.getCode() == code) {
                return errorType;
            }
        }
        throw new IllegalArgumentException("Unable to find Error with code '" + code + "'");
    }

    public HttpStatus getStatusFromCode(int code) {
        int statusCode = code / 10;
        return HttpStatus.valueOf(statusCode);
    }

    public HttpStatus getHttpStatus() {
        return getStatusFromCode(this.code);
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}