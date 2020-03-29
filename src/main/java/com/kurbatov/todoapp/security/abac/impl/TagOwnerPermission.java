package com.kurbatov.todoapp.security.abac.impl;

import com.kurbatov.todoapp.dto.tag.TagResource;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.security.abac.LookupPermission;
import com.kurbatov.todoapp.security.abac.Permission;
import com.kurbatov.todoapp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.kurbatov.todoapp.security.abac.AppPermission.TAG_OWNER_PERMISSION_NAME;

@Component
@LookupPermission(TAG_OWNER_PERMISSION_NAME)
public class TagOwnerPermission implements Permission {

    @Autowired
    private TagService tagService;

    @Override
    public boolean isAllowed(Authentication authentication, Object tagId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        TagResource tag = tagService.findById((Long) tagId);
        return userDetails.getUserId().equals(tag.getOwnerId());
    }
}
