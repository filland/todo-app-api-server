package com.kurbatov.todoapp.persistence.dao;

import com.kurbatov.todoapp.persistence.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long>,
        ConfirmationTokenRepositoryCustom {

    @Query("FROM ConfirmationToken where token = :token and active = true")
    Optional<ConfirmationToken> findByToken(String token);
}
