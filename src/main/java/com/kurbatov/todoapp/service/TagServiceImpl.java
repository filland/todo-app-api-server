package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.exception.TodoAppException;
import com.kurbatov.todoapp.persistence.dao.TagRepository;
import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag findById(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "Tag"));
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag update(Tag tag, Long tagId, CustomUserDetails userDetails) {
        Tag dbTag = this.findById(tagId);
        dbTag.setName(tag.getName());
        dbTag.setActive(tag.isActive());
        return tagRepository.save(dbTag);
    }

}
