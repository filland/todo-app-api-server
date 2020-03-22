package com.kurbatov.todoapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ChangeUserPasswordRQ {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!_]+")
    @Size(min = 6, max = 30)
    private String oldPassword;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!_]+")
    @Size(min = 6, max = 30)
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
