package com.kurbatov.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.kurbatov.todoapp.util.ValidationConstraints.MAX_USERNAME_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MAX_USER_PASSWORD_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MIN_USERNAME_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MIN_USER_PASSWORD_LENGTH;

public class LoginRQ {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_.]+")
    @Size(min = MIN_USERNAME_LENGTH, max = MAX_USERNAME_LENGTH)
    @JsonProperty("username")
    private String username;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!_]+")
    @Size(min = MIN_USER_PASSWORD_LENGTH, max = MAX_USER_PASSWORD_LENGTH)
    @JsonProperty("password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}