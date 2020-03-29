package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.tag.TagConverter;
import com.kurbatov.todoapp.dto.tag.TagResource;
import com.kurbatov.todoapp.dto.tag.UpdateTagRQ;
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

    private Tag findEntityById(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "Tag"));
    }

    @Override
    public TagResource findById(Long tagId) {
        return TagConverter.TO_RESOURCE.apply(this.findEntityById(tagId));
    }

    @Override
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public TagResource updateTag(UpdateTagRQ tag, Long tagId, CustomUserDetails userDetails) {
        Tag dbTag = this.findEntityById(tagId);
        dbTag.setName(tag.getName());
        dbTag.setActive(tag.isActive());
        return TagConverter.TO_RESOURCE.apply(tagRepository.save(dbTag));
    }

}
