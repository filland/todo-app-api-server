package com.kurbatov.todoapp.persistence.dao;

import com.kurbatov.todoapp.persistence.entity.Todo;

import java.util.List;

public interface TodoRepositoryCustom {

    List<Todo> findSeveral(int page, int limit, Long userId);
}
