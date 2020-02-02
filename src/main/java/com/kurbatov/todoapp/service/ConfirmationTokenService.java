package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.persistence.entity.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {

    Optional<ConfirmationToken> find(long id);

    ConfirmationToken save(ConfirmationToken token);

    Optional<ConfirmationToken> findByToken(String confirmationToken);
}
