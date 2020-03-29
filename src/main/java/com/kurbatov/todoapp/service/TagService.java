package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.tag.TagResource;
import com.kurbatov.todoapp.dto.tag.UpdateTagRQ;
import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.security.CustomUserDetails;

public interface TagService {

    TagResource findById(Long tagId);

    Tag createTag(Tag tag);

    TagResource updateTag(UpdateTagRQ tag, Long tagId, CustomUserDetails userDetails);
}
