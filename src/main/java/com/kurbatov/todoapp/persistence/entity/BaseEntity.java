package com.kurbatov.todoapp.persistence.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity implements Identifier {

    @Column(name = "active", nullable = false, columnDefinition = "tinyint default true")
    private Boolean active;

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
