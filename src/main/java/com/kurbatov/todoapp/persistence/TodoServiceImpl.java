package com.kurbatov.todoapp.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public Todo find(long id) {
        return todoRepository.findById(id).orElse(null);
    }

    @Override
    public long save(Todo todo) {
        return todoRepository.save(todo).getId();
    }

    @Override
    public Todo update(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public void delete(long todoID) {
        Todo todo = this.find(todoID);
        todo.setActive(false);
        todoRepository.save(todo);
    }

    @Override
    public List<Todo> findSeveral(int page, int limit) {
        return todoRepository.findSeveral(page, limit);
    }
}
