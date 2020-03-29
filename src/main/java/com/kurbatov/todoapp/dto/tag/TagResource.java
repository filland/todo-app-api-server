package com.kurbatov.todoapp.dto.tag;

/**
 * Representation of Tag entity used for GET requests
 */
public class TagResource {

    private Long id;
    private String name;
    private Long ownerId;
    private Boolean active;

    public TagResource() {
    }

    public TagResource(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
