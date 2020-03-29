package com.kurbatov.todoapp.dto.user;

import com.kurbatov.todoapp.persistence.entity.User;

import java.util.function.Function;

public class UserConverter {

    private UserConverter() {
        //static only
    }

    public static final Function<User, UserResource> TO_RESOURCE = user -> {
        UserResource userResource = new UserResource();
        userResource.setId(user.getUserId());
        userResource.setUsername(user.getUsername());
        userResource.setEmail(user.getEmail());
        userResource.setFirstName(user.getFirstName());
        userResource.setLastName(user.getLastName());
        userResource.setActive(user.isActive());
        return userResource;
    };

}
