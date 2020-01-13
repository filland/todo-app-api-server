package com.kurbatov.todoapp.persistence.dao;

import com.kurbatov.todoapp.persistence.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findByUsername(String username) {

        Query query = entityManager.createQuery("FROM User WHERE username = :username AND active = :active");
        query.setParameter("username", username);
        query.setParameter("active", true);
        return (User) query.getSingleResult();
    }
}
