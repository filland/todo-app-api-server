package com.kurbatov.todoapp.persistence.dao;

import com.kurbatov.todoapp.persistence.entity.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {

}
