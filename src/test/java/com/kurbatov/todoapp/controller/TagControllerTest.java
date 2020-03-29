package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.BaseWebMvcTest;
import com.kurbatov.todoapp.dto.tag.TagResource;
import com.kurbatov.todoapp.dto.tag.UpdateTagRQ;
import com.kurbatov.todoapp.dto.todo.TodoConverter;
import com.kurbatov.todoapp.dto.todo.TodoResource;
import com.kurbatov.todoapp.dto.user.UserConverter;
import com.kurbatov.todoapp.dto.user.UserResource;
import com.kurbatov.todoapp.persistence.dao.TagRepository;
import com.kurbatov.todoapp.persistence.dao.TodoRepository;
import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.oauth2.AuthProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TagControllerTest extends BaseWebMvcTest {

    private String TAG_ENDPOINT = "/api/v1/tags";

    private static final String TOKEN_KEY = "token";
    private static final String USER_RESOURCE_KEY = "user_resource";
    private static final String TODO_RESOURCE_KEY = "todo_resource";

    private static final String EXISTING_USERNAME = "todoControllerTest";
    private static final String EXISTING_USER_EMAIL = "todocontroller@todoapp.com";
    private static final String COMMON_PASSWORD = "123qwerty";

    private LinkedList<Map<String, Object>> testData;

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private TagRepository tagRepository;

    @BeforeAll
    void beforeAll() {

        testData = new LinkedList<>();

        for (int i = 0; i < 5; i++) {

            Map<String, Object> map = new HashMap<>();

            User user = new User();
            user.setUsername(EXISTING_USERNAME + i);
            user.setEmail(i + EXISTING_USER_EMAIL);
            user.setFirstName("firstname");
            user.setLastName("lastname");
            user.setPassword(COMMON_PASSWORD);
            user.setActive(true);
            user.setEmailConfirmed(true);
            user.setProvider(AuthProvider.LOCAL);
            User newUser = createNewUser(user);

            Todo todo = new Todo();
            todo.setTitle("title" + i);
            todo.setDescription("desc" + i);
            todo.setOwner(newUser);
            todo.setDone(false);
            todo.setActive(true);
            Todo savedTodo = todoRepository.save(todo);

            Tag tag1 = new Tag("test_tag_" + i);
            tag1.setActive(true);
            tag1.setOwner(newUser);
            Tag savedTag1 = tagRepository.save(tag1);

            Tag tag2 = new Tag("test_tag_" + i);
            tag2.setActive(true);
            tag2.setOwner(newUser);
            Tag savedTag2 = tagRepository.save(tag2);

            savedTodo.getTags().add(savedTag1);
            savedTodo.getTags().add(savedTag2);
            Todo savedTodoWithTags = todoRepository.save(savedTodo);
            map.put(TODO_RESOURCE_KEY, TodoConverter.TO_RESOURCE.apply(savedTodoWithTags));

            UserResource userResource = UserConverter.TO_RESOURCE.apply(newUser);
            map.put(USER_RESOURCE_KEY, userResource);

            String jwtToken = generateJwtToken(user);
            map.put(TOKEN_KEY, jwtToken);

            testData.add(map);
        }

    }

    @Test
    void validData_updateTagTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);
        UserResource userResource = (UserResource) userData.get(USER_RESOURCE_KEY);

        TagResource tagResource = todo.getTags().get(0);
        long tagId = tagResource.getId();

        UpdateTagRQ updateTagRQ = new UpdateTagRQ();
        updateTagRQ.setName("validData_updateTagTest");
        updateTagRQ.setActive(false);

        MvcResult result = mockMvc.perform(put(TAG_ENDPOINT + "/{tagId}", tagId)
                .content(objectMapper.writeValueAsString(updateTagRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        TagResource response = fromMvcResultToObject(result, TagResource.class);

        assertEquals(updateTagRQ.getName(), response.getName());
        assertEquals(updateTagRQ.isActive(), response.isActive());
        assertEquals(response.getOwnerId(), userResource.getId());
    }

    @Test
    void invalidTagName_updateTagTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        TagResource tagResource = todo.getTags().get(0);
        long tagId = tagResource.getId();

        UpdateTagRQ updateTagRQ = new UpdateTagRQ();
        updateTagRQ.setName("invalid name ^%&#");
        updateTagRQ.setActive(false);

        mockMvc.perform(put(TAG_ENDPOINT + "/{tagId}", tagId)
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void userNotTagOwner_updateTagTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);
        TagResource tagResource = todo.getTags().get(0);
        long otherUserTagId = tagResource.getId();

        Map<String, Object> userData2 = testData.pop();
        String jwtToken = (String) userData2.get(TOKEN_KEY);

        UpdateTagRQ updateTagRQ = new UpdateTagRQ();
        updateTagRQ.setName("validData_updateTagTest");
        updateTagRQ.setActive(false);

        mockMvc.perform(put(TAG_ENDPOINT + "/{tagId}", otherUserTagId)
                .content(objectMapper.writeValueAsString(updateTagRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());

    }
}