package com.kurbatov.todoapp.persistence.dao;

import com.kurbatov.todoapp.persistence.entity.Todo;
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
        return todoRepository.save(todo).getTodoID();
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
    public List<Todo> findSeveral(int page, int limit, Long userID) {
        return todoRepository.findSeveral(page, limit, userID);
    }
}
