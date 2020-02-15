package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.persistence.dao.TodoRepository;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public Todo find(long id) {
        return todoRepository.findById(id).orElse(null);
    }

    @Override
    public Long save(Todo todo) {
        return todoRepository.save(todo).getTodoId();
    }

    @Override
    public Long save(Todo todo, UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        todo.setOwner(new User(customUserDetails.getUserId()));
        todo.setActive(true);
        Todo save = todoRepository.save(todo);
        return save.getTodoId();
    }

    @Override
    public Todo update(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Todo update(Todo todo, UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        todo.setOwner(new User(customUserDetails.getUserId()));
        return todoRepository.save(todo);
    }

    @Override
    public void delete(long todoId) {
        Todo todo = this.find(todoId);
        todo.setActive(false);
        todoRepository.save(todo);
    }

    @Override
    public List<Todo> findSeveral(int page, int limit, Long userId) {
        return todoRepository.findSeveral(page, limit, userId);
    }
}
