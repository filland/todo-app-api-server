package com.kurbatov.todoapp.dto.todo;

import com.kurbatov.todoapp.dto.tag.TagResource;

import java.sql.Timestamp;
import java.util.List;

/**
 * Representation of Todo_ entity used for GET requests
 */
public class TodoResource {

    private Long id;
    private String title;
    private String description;
    private Boolean done;
    private Boolean active;
    private List<TagResource> tags;
    private Long ownerId;
    private Timestamp updated;

    public TodoResource() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<TagResource> getTags() {
        return tags;
    }

    public void setTags(List<TagResource> tags) {
        this.tags = tags;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
}
