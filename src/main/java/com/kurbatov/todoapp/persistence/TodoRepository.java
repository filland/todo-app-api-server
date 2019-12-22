package com.kurbatov.todoapp.persistence;

import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long>, TodoFindSeveralRepository {

}
