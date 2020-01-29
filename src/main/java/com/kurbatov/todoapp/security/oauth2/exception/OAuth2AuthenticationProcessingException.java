package com.kurbatov.todoapp.security.oauth2.exception;

import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.exception.TodoAppException;

public class OAuth2AuthenticationProcessingException extends TodoAppException {

    public OAuth2AuthenticationProcessingException(String message) {
        super(message);
    }

    public OAuth2AuthenticationProcessingException(ErrorType errorType, String... params) {
        super(errorType, params);
    }

    public OAuth2AuthenticationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
