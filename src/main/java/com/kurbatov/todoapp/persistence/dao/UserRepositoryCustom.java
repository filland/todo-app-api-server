package com.kurbatov.todoapp.persistence.dao;

import com.kurbatov.todoapp.persistence.entity.User;

public interface UserRepositoryCustom {

    User findByUsername(String username);
}
