package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TodoService {

    Todo find(long id);

    Long save(Todo todo);

    Long save(Todo todo, UserDetails userDetails);

    Todo update(Todo todo);

    Todo update(Todo todo, UserDetails userDetails);

    void markTodoAsDone(Long todoId);

    void delete(long todoId);

    List findSeveral(int page, int limit, Long userId);
}
