package com.kurbatov.todoapp.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterRQ {

    private String name;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_.]+")
    @Size(min = 5, max = 30)
    private String username;

    @NotBlank
    @Email
//    @Pattern(regexp = "[a-zA-Z0-9_.]{3,100}@[a-zA-Z0-9_.]{2,30}.[a-zA-Z0-9_.]{2,7}")
    private String email;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!_]+")
    @Size(min = 6, max = 20)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
