package com.kurbatov.todoapp.dto.tag;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.kurbatov.todoapp.util.ValidationConstraints.MAX_TAG_NAME_LENGTH;
import static com.kurbatov.todoapp.util.ValidationConstraints.MIN_TAG_NAME_LENGTH;

public class UpdateTagRQ {

    @Size(min = MIN_TAG_NAME_LENGTH, max = MAX_TAG_NAME_LENGTH)
    @Pattern(regexp = "[a-zA-Z0-9!_]+")
    private String name;

    @NotNull
    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
