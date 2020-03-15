package com.kurbatov.todoapp.persistence.dao;

import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.persistence.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Todo> findSeveral(Integer page, Integer limit, Long userId) {
        Query q = entityManager.createQuery(
                "FROM Todo WHERE active = true AND owner_id = :userId ORDER BY todoId DESC"
        );
        q.setFirstResult(calculateOffset(page, limit));
        q.setMaxResults(limit);
        User user = new User();
        user.setUserId(userId);
        q.setParameter("userId", user);
        return q.getResultList();
    }

    private static int calculateOffset(int page, int limit) {
        return (page * limit) - limit;
    }
}
