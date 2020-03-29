package com.kurbatov.todoapp.dto.tag;

import com.kurbatov.todoapp.persistence.entity.Tag;

import java.util.function.Function;

public class TagConverter {

    private TagConverter() {
        //static only
    }

    public static final Function<Tag, TagResource> TO_RESOURCE = tag -> {
        TagResource tagResource = new TagResource();
        tagResource.setId(tag.getId());
        tagResource.setName(tag.getName());
        tagResource.setOwnerId(tag.getOwner().getUserId());
        tagResource.setActive(tag.isActive());
        return tagResource;
    };
}
