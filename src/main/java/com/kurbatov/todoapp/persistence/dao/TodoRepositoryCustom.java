package com.kurbatov.todoapp.persistence.dao;

import com.kurbatov.todoapp.persistence.entity.Todo;

import java.util.List;

public interface TodoRepositoryCustom {

    @Deprecated
    List<Todo> findSeveral(Integer page, Integer limit, Long userId);
}
