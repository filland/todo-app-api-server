package com.kurbatov.todoapp.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {

    INCORRECT_REQUEST(4001, "Incorrect Request. {}"),
    USER_WITH_EMAIL_EXISTS(4002, "User with the email {} already exists"),
    USER_WITH_USERNAME_EXISTS(4003, "User with the username {} already exists"),
    USER_ACCOUNT_WAS_NOT_CREATED(4004, "User account was not created"),
    AUTH_OAUTH2_WRONG_AUTH_MECHANISM(4005, "You're signed up with {} account"),
    INVALID_INFORMATION(4006, "The request contained invalid fields"),

    AUTH_OAUTH2_UNAUTHORIZED_REDIRECT(4011, "An unauthorized redirect URI"),

    ACCESS_DENIED(4031, "You do not have enough permissions. {}"),
    ADDRESS_LOCKED(4032, "Address is locked due to several incorrect login attempts"),

    RESOURCE_NOT_FOUND(4041, "The {} resource not found"),
    AUTH_OAUTH2_EMAIL_NOT_FOUND(4042, "Email not found from OAuth2 provider"),
    CONFIRMATION_TOKEN_NOT_FOUND(4043, "Confirmation token not found"),

    AUTH_OAUTH2_PROVIDER_NOT_SUPPORTED(4051, "Login with {} is not supported yet"),

    /**
     * Use it If there are no any other exceptions. There should by no such
     * exception
     */
    UNCLASSIFIED_ERROR(5001, "Unclassified error");

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
