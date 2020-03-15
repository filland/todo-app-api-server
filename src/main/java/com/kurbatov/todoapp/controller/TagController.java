package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.kurbatov.todoapp.security.abac.AppPermission.TAG_OWNER;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @PutMapping("/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(TAG_OWNER)
    public Tag updateTag(@RequestBody Tag tag,
                         @PathVariable Long tagId,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return tagService.update(tag, tagId, userDetails);
    }

}
