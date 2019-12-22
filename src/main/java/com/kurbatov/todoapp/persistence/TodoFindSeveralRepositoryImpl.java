package com.kurbatov.todoapp.persistence;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TodoFindSeveralRepositoryImpl implements TodoFindSeveralRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Todo> findSeveral(int page, int limit) {
        Query q = entityManager.createQuery("FROM Todo WHERE active = true ORDER BY id DESC");
        q.setFirstResult(calculateOffset(page, limit));
        q.setMaxResults(limit);
        return q.getResultList();
    }

    private static int calculateOffset(int page, int limit){
        return (page * limit) - limit;
    }
}
