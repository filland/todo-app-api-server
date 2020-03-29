package com.kurbatov.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.kurbatov.todoapp.util.ValidationConstraints.MAX_USERNAME_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MAX_USER_FIRST_NAME_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MAX_USER_LAST_NAME_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MAX_USER_PASSWORD_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MIN_USERNAME_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MIN_USER_PASSWORD_LENGTH;

public class RegisterUserRQ {

    @Size(max = MAX_USER_FIRST_NAME_LENGTH)
    @Pattern(regexp = "[a-zA-Z]+")
    @JsonProperty("firstName")
    private String firstName;

    @Size(max = MAX_USER_LAST_NAME_LENGTH)
    @Pattern(regexp = "[a-zA-Z]+")
    @JsonProperty("lastName")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_.]+")
    @Size(min = MIN_USERNAME_LENGTH, max = MAX_USERNAME_LENGTH)
    private String username;

    @NotBlank
    @Email
//    @Pattern(regexp = "[a-zA-Z0-9_.]{3,100}@[a-zA-Z0-9_.]{2,30}.[a-zA-Z0-9_.]{2,7}")
    private String email;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!_]+")
    @Size(min = MIN_USER_PASSWORD_LENGTH, max = MAX_USER_PASSWORD_LENGTH)
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
