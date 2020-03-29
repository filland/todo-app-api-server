package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.ConfirmEmailRQ;
import com.kurbatov.todoapp.dto.LoginRQ;
import com.kurbatov.todoapp.dto.RegisterUserRQ;
import com.kurbatov.todoapp.dto.RegisterUserRS;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;

public interface AuthService {

    JwtAuthenticationResponse loginUser(LoginRQ loginRQ);

    RegisterUserRS registerUser(RegisterUserRQ registerUserRQ);

    /**
     * Complete user's registration
     *
     * @param confirmEmailRQ
     */
    void confirmEmail(ConfirmEmailRQ confirmEmailRQ);
}
