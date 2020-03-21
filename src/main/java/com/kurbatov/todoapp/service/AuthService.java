package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.ConfirmEmailRQ;
import com.kurbatov.todoapp.dto.LoginRQ;
import com.kurbatov.todoapp.dto.RegisterRQ;
import com.kurbatov.todoapp.dto.RegisterRS;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;

public interface AuthService {

    JwtAuthenticationResponse loginUser(LoginRQ loginRQ);

    RegisterRS registerUser(RegisterRQ registerRQ);

    /**
     * Complete user's registration
     *
     * @param confirmEmailRQ
     */
    void confirmEmail(ConfirmEmailRQ confirmEmailRQ);
}
