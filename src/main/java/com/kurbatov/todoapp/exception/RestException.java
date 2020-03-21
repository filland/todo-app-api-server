package com.kurbatov.todoapp.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestException implements Serializable {

    @JsonProperty("status")
    private int status;

    @JsonProperty("message")
    private String errorMessage;

    @JsonProperty("code")
    private int errorTypeCode;

    public RestException() {
    }

    public RestException(HttpStatus httpStatus, String errorMessage) {
        this.status = httpStatus.value();
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(status);
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.status = httpStatus.value();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorCode(int errorTypeCode) {

        this.errorTypeCode = errorTypeCode;
    }

    public int getErrorCode() {
        return errorTypeCode;
    }
}
