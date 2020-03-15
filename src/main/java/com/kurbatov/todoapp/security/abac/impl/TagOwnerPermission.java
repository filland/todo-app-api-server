package com.kurbatov.todoapp.security.abac.impl;

import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.security.abac.LookupPermission;
import com.kurbatov.todoapp.security.abac.Permission;
import com.kurbatov.todoapp.service.TagService;
import com.kurbatov.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.kurbatov.todoapp.security.abac.AppPermission.TAG_OWNER_PERMISSION_NAME;

@Component
@LookupPermission(TAG_OWNER_PERMISSION_NAME)
public class TagOwnerPermission implements Permission {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Override
    public boolean isAllowed(Authentication authentication, Object tagId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findById(userDetails.getUserId());
        Tag tag = tagService.findById((Long) tagId);
        return tag.getOwner().getId().equals(user.getId());
    }
}
