package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.security.CustomUserDetails;

public interface TagService {

    Tag findById(Long tagId);

    Tag save(Tag tag);

    Tag update(Tag tag, Long tagId, CustomUserDetails userDetails);
}
