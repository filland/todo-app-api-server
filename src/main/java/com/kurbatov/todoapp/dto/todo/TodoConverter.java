package com.kurbatov.todoapp.dto.todo;

import com.kurbatov.todoapp.dto.tag.TagConverter;
import com.kurbatov.todoapp.dto.tag.TagResource;
import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.persistence.entity.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TodoConverter {

    private TodoConverter() {
        //static only
    }

    public static final Function<Todo, TodoResource> TO_RESOURCE = todo -> {
        TodoResource todoResource = new TodoResource();
        todoResource.setId(todo.getId());
        todoResource.setTitle(todo.getTitle());
        todoResource.setDescription(todo.getDescription());
        todoResource.setDone(todo.isDone());
        todoResource.setActive(todo.isActive());
        todoResource.setOwnerId(todo.getOwner().getUserId());
        todoResource.setUpdated(todo.getUpdated());

        List<TagResource> tagResources = new ArrayList<>();
        for (Tag tag : todo.getTags()) {
            tagResources.add(TagConverter.TO_RESOURCE.apply(tag));
        }
        todoResource.setTags(tagResources);

        return todoResource;
    };
}
