package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.CompleteRegistrationRQ;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.security.jwt.SignInRQ;
import com.kurbatov.todoapp.security.jwt.SignUpRQ;

public interface AuthService {

    JwtAuthenticationResponse loginUser(SignInRQ signInRQ);

    void registerUser(SignUpRQ signUpRQ);

    /**
     * Complete user's registration
     *
     * @param completeRegistrationRQ
     */
    void completeRegistration(CompleteRegistrationRQ completeRegistrationRQ);
}
