package com.kurbatov.todoapp.persistence;

import java.util.List;

public interface TodoService {

    Todo find(long id);

    long save(Todo todo);

    Todo update(Todo todo);

    void delete(long todoID);

    List findSeveral(int page, int limit);
}
