package com.kurbatov.todoapp.security.abac.impl;

import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static com.kurbatov.todoapp.TestUtils.buildAuthentication;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagOwnerPermissionTest {

    @InjectMocks
    private TagOwnerPermission tagOwnerPermission;

    @Mock
    private TagService tagService;

    @Test
    public void isAllowed_tagOwnerEqualAuthenticationPrincipalTest() {

        User tagOwner = new User();
        tagOwner.setUserId(1L);

        Tag importantTag = new Tag("important");
        importantTag.setOwner(tagOwner);

        when(tagService.findById(1L)).thenReturn(importantTag);

        Authentication authentication = buildAuthentication(tagOwner);

        Long tagId = 1L;
        assertTrue(tagOwnerPermission.isAllowed(authentication, tagId));
    }


    @Test
    public void isAllowed_tagOwnerNotEqualAuthenticationPrincipalTest() {

        User tagOwner = new User();
        tagOwner.setUserId(2L);
        Tag importantTag = new Tag("important");
        importantTag.setOwner(tagOwner);
        when(tagService.findById(1L)).thenReturn(importantTag);

        User user = new User();
        user.setUserId(1L);
        Authentication authentication = buildAuthentication(user);

        Long tagId = 1L;
        assertFalse(tagOwnerPermission.isAllowed(authentication, tagId));
    }

}