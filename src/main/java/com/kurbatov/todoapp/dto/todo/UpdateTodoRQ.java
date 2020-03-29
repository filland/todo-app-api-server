package com.kurbatov.todoapp.dto.todo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.kurbatov.todoapp.util.ValidationConstraints.MAX_TODO_DESC_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MAX_TODO_TITLE_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MIN_TODO_DESC_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MIN_TODO_TITLE_LENGTH;

public class UpdateTodoRQ {

    @NotNull
    @Size(min = MIN_TODO_TITLE_LENGTH, max = MAX_TODO_TITLE_LENGTH)
    private String title;

    @NotNull
    @Size(min = MIN_TODO_DESC_LENGTH, max = MAX_TODO_DESC_LENGTH)
    private String description;

    @NotNull
    private Boolean done;

    @NotNull
    private Boolean active;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
