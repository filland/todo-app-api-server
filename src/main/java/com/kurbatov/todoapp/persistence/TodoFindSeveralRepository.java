package com.kurbatov.todoapp.persistence;

import java.util.List;

public interface TodoFindSeveralRepository {

    List<Todo> findSeveral(int page, int limit);
}
