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
        return todoRepository.save(todo).getTodoID();
    }

    @Override
    public Long save(Todo todo, UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        todo.setOwner(new User(customUserDetails.getUserID()));
        todo.setActive(true);
        Todo save = todoRepository.save(todo);
        return save.getTodoID();
    }

    @Override
    public Todo update(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Todo update(Todo todo, UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        todo.setOwner(new User(customUserDetails.getUserID()));
        return todoRepository.save(todo);
    }

    @Override
    public void delete(long todoID) {
        Todo todo = this.find(todoID);
        todo.setActive(false);
        todoRepository.save(todo);
    }

    @Override
    public List<Todo> findSeveral(int page, int limit, Long userID) {
        return todoRepository.findSeveral(page, limit, userID);
    }
}
