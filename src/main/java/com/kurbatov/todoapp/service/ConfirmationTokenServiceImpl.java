package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.persistence.dao.ConfirmationTokenRepository;
import com.kurbatov.todoapp.persistence.entity.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    public Optional<ConfirmationToken> find(long id) {
        return confirmationTokenRepository.findById(id);
    }

    public ConfirmationToken save(ConfirmationToken token) {
        return confirmationTokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmationToken> findByToken(String confirmationToken) {
        return confirmationTokenRepository.findByToken(confirmationToken);
    }


}
