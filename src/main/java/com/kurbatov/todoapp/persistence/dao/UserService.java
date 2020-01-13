package com.kurbatov.todoapp.persistence.dao;

import com.kurbatov.todoapp.persistence.entity.User;

public interface UserService {

    User findByUsername(String username);

    void saveUser(User user);
}
