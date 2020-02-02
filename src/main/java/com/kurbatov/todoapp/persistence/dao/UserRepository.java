package com.kurbatov.todoapp.persistence.dao;

import com.kurbatov.todoapp.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    @Query("from User where userID = :id AND active = true")
    @Override
    Optional<User> findById(Long id);

    @Query("FROM User WHERE username = :username AND active = true")
    Optional<User> findByUsername(String username);

    @Query("from  User where email = :email and active = true")
    Optional<User> findByEmail(String email);
}
