package com.kurbatov.todoapp.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.kurbatov.todoapp.util.ValidationConstraints.MAX_USER_PASSWORD_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MIN_USER_PASSWORD_LENGTH;

public class ChangeUserPasswordRQ {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!_]+")
    @Size(min = MIN_USER_PASSWORD_LENGTH, max = MAX_USER_PASSWORD_LENGTH)
    private String oldPassword;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!_]+")
    @Size(min = MIN_USER_PASSWORD_LENGTH, max = MAX_USER_PASSWORD_LENGTH)
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
