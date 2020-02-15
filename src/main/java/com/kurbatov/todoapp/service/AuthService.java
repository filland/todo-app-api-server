package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.CompleteRegistrationRQ;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.security.jwt.LoginRQ;
import com.kurbatov.todoapp.security.jwt.RegisterRQ;

public interface AuthService {

    JwtAuthenticationResponse loginUser(LoginRQ loginRQ);

    void registerUser(RegisterRQ registerRQ);

    /**
     * Complete user's registration
     *
     * @param completeRegistrationRQ
     */
    void completeRegistration(CompleteRegistrationRQ completeRegistrationRQ);
}
