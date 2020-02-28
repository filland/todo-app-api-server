package com.kurbatov.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class LoginRQ {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_.]+")
    @JsonProperty("username")
    private String username;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!_]+")
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