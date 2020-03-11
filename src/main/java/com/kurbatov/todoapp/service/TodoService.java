package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.common.PageableRS;
import com.kurbatov.todoapp.persistence.entity.Todo;
import org.springframework.data.domain.Pageable;
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

    PageableRS<Todo> findSeveral(Long userId, Pageable pageable);
}
