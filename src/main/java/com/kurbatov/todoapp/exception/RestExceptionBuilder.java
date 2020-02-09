package com.kurbatov.todoapp.exception;

import org.springframework.http.HttpStatus;

public class RestExceptionBuilder {

    private HttpStatus httpStatus;
    private ErrorType errorType;
    private String errorMessage;

    public RestExceptionBuilder() {
    }

    public RestExceptionBuilder setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public RestExceptionBuilder setErrorType(ErrorType errorType) {
        this.errorType = errorType;
        return this;
    }

    public RestExceptionBuilder setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public RestException build() {
        RestException restException = new RestException();
        restException.setErrorMessage(errorMessage);
        if (errorType != null) {
            restException.setHttpStatus(errorType.getHttpStatus());
            restException.setErrorCode(errorType.getCode());
        } else {
            restException.setHttpStatus(httpStatus);
        }
        return restException;
    }

}
