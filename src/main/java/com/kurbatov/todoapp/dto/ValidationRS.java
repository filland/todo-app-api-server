package com.kurbatov.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kurbatov.todoapp.exception.RestException;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationRS extends RestException {

    @JsonProperty("errors")
    private Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
