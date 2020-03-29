package com.kurbatov.todoapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kurbatov.todoapp.BaseWebMvcTest;
import com.kurbatov.todoapp.dto.PageableRS;
import com.kurbatov.todoapp.dto.tag.CreateTagRQ;
import com.kurbatov.todoapp.dto.tag.TagResource;
import com.kurbatov.todoapp.dto.todo.CreateTodoRQ;
import com.kurbatov.todoapp.dto.todo.TodoConverter;
import com.kurbatov.todoapp.dto.todo.TodoResource;
import com.kurbatov.todoapp.dto.todo.UpdateTodoRQ;
import com.kurbatov.todoapp.dto.user.UserConverter;
import com.kurbatov.todoapp.dto.user.UserResource;
import com.kurbatov.todoapp.persistence.dao.TagRepository;
import com.kurbatov.todoapp.persistence.dao.TodoRepository;
import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.oauth2.AuthProvider;
import com.kurbatov.todoapp.util.ValidationConstraints;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TodoControllerTest extends BaseWebMvcTest {

    private static final String TODO_ENDPOINT = "/api/v1/todos";

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
    void setUp() {

        testData = new LinkedList<>();

        User user = new User();
        user.setUsername(EXISTING_USERNAME);
        user.setEmail(EXISTING_USER_EMAIL);
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword(COMMON_PASSWORD);
        user.setActive(true);
        user.setEmailConfirmed(true);
        user.setProvider(AuthProvider.LOCAL);
        User newUser = createNewUser(user);

        for (int i = 0; i < 30; i++) {

            Map<String, Object> map = new HashMap<>();

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
    void todoExistAndUserIsOwner_findTodoTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);
        UserResource userResource = (UserResource) userData.get(USER_RESOURCE_KEY);

        Long existingTodoId = todo.getId();

        MvcResult result = mockMvc.perform(get(TODO_ENDPOINT + "/{todoId}", existingTodoId)
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        TodoResource todoResource = fromMvcResultToObject(result, TodoResource.class);

        assertEquals(todo.getId(), todoResource.getId());
        assertEquals(todo.getTitle(), todoResource.getTitle());
        assertEquals(todo.getDescription(), todoResource.getDescription());
        assertEquals(todo.isDone(), todoResource.isDone());
        assertEquals(todo.isActive(), todoResource.isActive());
        assertEquals(todo.getOwnerId(), todoResource.getOwnerId());
        assertEquals(todo.getTags().size(), todoResource.getTags().size());

        // todo this check could be implemented using soft assertion...
        for (int i = 0; i < todo.getTags().size(); i++) {
            TagResource todoTag = todo.getTags().get(i);
            TagResource tagResource = todoResource.getTags().get(i);
            assertEquals(todoTag.getId(), tagResource.getId());
            assertEquals(todoTag.getName(), tagResource.getName());
            assertEquals(todoTag.getOwnerId(), tagResource.getOwnerId());
        }
    }

    @Test
    void todoDoesNotExist_findTodoTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        Long todoIdThatNotExist = 999999L;

        mockMvc.perform(get(TODO_ENDPOINT + "/{todoId}", todoIdThatNotExist)
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());

    }


    @Test
    void userNotTodoOwner_findTodoTest() throws Exception {

        // preparation
        // create user
        User user = new User();
        user.setUsername(EXISTING_USERNAME + "findTodo");
        user.setEmail("findTodo" + EXISTING_USER_EMAIL);
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword(COMMON_PASSWORD);
        user.setActive(true);
        user.setEmailConfirmed(true);
        user.setProvider(AuthProvider.LOCAL);
        User newUser = createNewUser(user);
        String jwtTokenUser1 = generateJwtToken(newUser);

        Map<String, Object> user2 = testData.pop();
        TodoResource todoResourceUser2 = (TodoResource) user2.get(TODO_RESOURCE_KEY);

        Long todoIdUser2 = todoResourceUser2.getId();

        mockMvc.perform(get(TODO_ENDPOINT + "/{todoId}", todoIdUser2)
                .with(csrf())
                .with(token(jwtTokenUser1))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());

    }


    @Test
    void validData_findSeveralTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        UserResource userResource = (UserResource) userData.get(USER_RESOURCE_KEY);

        MvcResult result = mockMvc.perform(get(TODO_ENDPOINT)
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        PageableRS<TodoResource> pageableRS = objectMapper
                .readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<PageableRS<TodoResource>>() {
                        }
                );
        for (TodoResource resource : pageableRS.getList()) {
            assertEquals(resource.getOwnerId(), userResource.getId());
            for (TagResource tag : resource.getTags()) {
                assertEquals(tag.getOwnerId(), userResource.getId());
            }
        }

    }


    @Test
    void validData_createTodoTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        UserResource userResource = (UserResource) userData.get(USER_RESOURCE_KEY);

        CreateTodoRQ createTodoRQ = new CreateTodoRQ();
        createTodoRQ.setTitle("Motivation todo.");
        createTodoRQ.setDescription("Just do it!");

        MvcResult result = mockMvc.perform(post(TODO_ENDPOINT)
                .content(objectMapper.writeValueAsString(createTodoRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        TodoResource todoResource = fromMvcResultToObject(result, TodoResource.class);
        assertEquals(createTodoRQ.getTitle(), todoResource.getTitle());
        assertEquals(createTodoRQ.getDescription(), todoResource.getDescription());
        assertEquals(userResource.getId(), todoResource.getOwnerId());
        assertNotNull(todoResource.getId());
        assertTrue(todoResource.isActive());
        assertFalse(todoResource.isDone());
    }


    @Test
    void todoTitleIsNull_createTodoTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        CreateTodoRQ createTodoRQ = new CreateTodoRQ();
        createTodoRQ.setTitle(null);
        createTodoRQ.setDescription("Just do it!");

        mockMvc.perform(post(TODO_ENDPOINT)
                .content(objectMapper.writeValueAsString(createTodoRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }


    @Test
    void todoTitleTooLong_createTodoTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        final int tooLongTitleSize = ValidationConstraints.MAX_TODO_TITLE_LENGTH + 10;
        StringBuilder tooLongTitle = new StringBuilder();
        for (int i = 0; i < tooLongTitleSize; i++) {
            tooLongTitle.append("a");
        }

        CreateTodoRQ createTodoRQ = new CreateTodoRQ();
        createTodoRQ.setTitle(tooLongTitle.toString());
        createTodoRQ.setDescription("Just do it!");

        mockMvc.perform(post(TODO_ENDPOINT)
                .content(objectMapper.writeValueAsString(createTodoRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void todoDescriptionIsNull_createTodoTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        CreateTodoRQ createTodoRQ = new CreateTodoRQ();
        createTodoRQ.setTitle("todo title");
        createTodoRQ.setDescription(null);

        mockMvc.perform(post(TODO_ENDPOINT)
                .content(objectMapper.writeValueAsString(createTodoRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }


    @Test
    void todoDescriptionTooLong_createTodoTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        final int tooLongDescriptionLength = ValidationConstraints.MAX_TODO_DESC_LENGTH + 10;
        StringBuilder tooLongDescription = new StringBuilder();
        for (int i = 0; i < tooLongDescriptionLength; i++) {
            tooLongDescription.append("a");
        }

        CreateTodoRQ createTodoRQ = new CreateTodoRQ();
        createTodoRQ.setTitle("todo title");
        createTodoRQ.setDescription(tooLongDescription.toString());

        mockMvc.perform(post(TODO_ENDPOINT)
                .content(objectMapper.writeValueAsString(createTodoRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }


    @Test
    void validData_updateTodoTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        UpdateTodoRQ updateTodoRQ = new UpdateTodoRQ();
        updateTodoRQ.setTitle("validData_updateTodoTest");
        updateTodoRQ.setDescription("validData_updateTodoTest");
        updateTodoRQ.setActive(true);
        updateTodoRQ.setDone(true);

        MvcResult result = mockMvc.perform(put(TODO_ENDPOINT + "/{todoId}", todo.getId())
                .content(objectMapper.writeValueAsString(updateTodoRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        TodoResource todoResource = fromMvcResultToObject(result, TodoResource.class);
        assertEquals(updateTodoRQ.getTitle(), todoResource.getTitle());
        assertEquals(updateTodoRQ.getDescription(), todoResource.getDescription());
        assertEquals(updateTodoRQ.isActive(), todoResource.isActive());
        assertEquals(updateTodoRQ.isDone(), todoResource.isDone());
    }


    @Test
    void userNotTodoOwner_updateTodoTest() throws Exception {
        // preparation
        // create user
        User user = new User();
        user.setUsername(EXISTING_USERNAME + "updateTodo");
        user.setEmail("updateTodo" + EXISTING_USER_EMAIL);
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword(COMMON_PASSWORD);
        user.setActive(true);
        user.setEmailConfirmed(true);
        user.setProvider(AuthProvider.LOCAL);
        User newUser = createNewUser(user);
        String jwtTokenUser1 = generateJwtToken(newUser);

        Map<String, Object> userData = testData.pop();
        TodoResource todoUser2 = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        UpdateTodoRQ updateTodoRQUser2 = new UpdateTodoRQ();
        updateTodoRQUser2.setTitle("validData_updateTodoTest");
        updateTodoRQUser2.setDescription("validData_updateTodoTest");
        updateTodoRQUser2.setActive(true);
        updateTodoRQUser2.setDone(true);

        mockMvc.perform(put(TODO_ENDPOINT + "/{todoId}", todoUser2.getId())
                .content(objectMapper.writeValueAsString(updateTodoRQUser2))
                .with(csrf())
                .with(token(jwtTokenUser1))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }


    @Test
    void todoWithIdNotExist_updateTodoTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        TodoResource todoResource = (TodoResource) userData.get(TODO_RESOURCE_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        UpdateTodoRQ updateTodoRQUser2 = new UpdateTodoRQ();
        updateTodoRQUser2.setTitle("validData_updateTodoTest");
        updateTodoRQUser2.setDescription("validData_updateTodoTest");
        updateTodoRQUser2.setActive(true);
        updateTodoRQUser2.setDone(true);

        int todoIdThatNotExist = 99999;

        mockMvc.perform(put(TODO_ENDPOINT + "/{todoId}", todoIdThatNotExist)
                .content(objectMapper.writeValueAsString(updateTodoRQUser2))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }


    @Test
    void validData_markAsDoneTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        mockMvc.perform(put(TODO_ENDPOINT + "/{todoId}/done", todo.getId())
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }


    @Test
    void userNotTodoOwner_markAsDoneTest() throws Exception {
        // preparation
        // create user
        User user = new User();
        user.setUsername(EXISTING_USERNAME + "markAsDone");
        user.setEmail("markAsDone" + EXISTING_USER_EMAIL);
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword(COMMON_PASSWORD);
        user.setActive(true);
        user.setEmailConfirmed(true);
        user.setProvider(AuthProvider.LOCAL);
        User newUser = createNewUser(user);
        String jwtTokenUser1 = generateJwtToken(newUser);

        Map<String, Object> userData2 = testData.pop();
        TodoResource todo2 = (TodoResource) userData2.get(TODO_RESOURCE_KEY);

        mockMvc.perform(put(TODO_ENDPOINT + "/{todoId}/done", todo2.getId())
                .with(csrf())
                .with(token(jwtTokenUser1))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }


    @Test
    void noTodoWithSuchId_markTodoAsDoneTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        int todoIdThatNotExist = 99999;

        mockMvc.perform(put(TODO_ENDPOINT + "/{todoId}/done", todoIdThatNotExist)
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }


    @Test
    void validData_deleteTodoTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        mockMvc.perform(delete(TODO_ENDPOINT + "/{todoId}", todo.getId())
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @Test
    void userNotTodoOwner_deleteTodoTest() throws Exception {

        // preparation
        // create user
        User user = new User();
        user.setUsername(EXISTING_USERNAME + "deleteTodo");
        user.setEmail("deleteTodo" + EXISTING_USER_EMAIL);
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword(COMMON_PASSWORD);
        user.setActive(true);
        user.setEmailConfirmed(true);
        user.setProvider(AuthProvider.LOCAL);
        User newUser = createNewUser(user);
        String jwtTokenUser1 = generateJwtToken(newUser);

        Map<String, Object> userData = testData.pop();
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        mockMvc.perform(delete(TODO_ENDPOINT + "/{todoId}", todo.getId())
                .with(csrf())
                .with(token(jwtTokenUser1))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());

    }


    @Test
    void noTodoWithSuchId_deleteTodoTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        int todoIdThatNotExist = 99999;

        mockMvc.perform(delete(TODO_ENDPOINT + "/{todoId}", todoIdThatNotExist)
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }


    @Test
    void validData_addNewTagTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        CreateTagRQ createTagRQ = new CreateTagRQ();
        createTagRQ.setName("tagName");

        MvcResult mvcResult = mockMvc.perform(post(TODO_ENDPOINT + "/{todoId}/tags", todo.getId())
                .content(objectMapper.writeValueAsString(createTagRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        TodoResource todoResource = fromMvcResultToObject(mvcResult, TodoResource.class);

        assertEquals(todo.getTags().size() + 1, todoResource.getTags().size());

        boolean tagNameFound = false;
        for (TagResource tag : todoResource.getTags()) {
            if (createTagRQ.getName().equals(tag.getName())) {
                tagNameFound = true;
                break;
            }
        }
        assertTrue(tagNameFound);
    }


    @Test
    void validTagAndUserNotTodoOwner_addNewTagTest() throws Exception {

        // preparation
        // create user
        User user = new User();
        user.setUsername(EXISTING_USERNAME + "addNewTag");
        user.setEmail("addNewTag" + EXISTING_USER_EMAIL);
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword(COMMON_PASSWORD);
        user.setActive(true);
        user.setEmailConfirmed(true);
        user.setProvider(AuthProvider.LOCAL);
        User newUser = createNewUser(user);
        String otherUserJwtToken = generateJwtToken(newUser);

        Map<String, Object> userData = testData.pop();
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        CreateTagRQ createTagRQ = new CreateTagRQ();
        createTagRQ.setName("tagName");

        mockMvc.perform(post(TODO_ENDPOINT + "/{todoId}/tags", todo.getId())
                .content(objectMapper.writeValueAsString(createTagRQ))
                .with(csrf())
                .with(token(otherUserJwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void tagNameNotValid_addNewTagTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        TodoResource todo = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        CreateTagRQ createTagRQ = new CreateTagRQ();
        createTagRQ.setName("invalidTagName^&***");

        mockMvc.perform(post(TODO_ENDPOINT + "/{todoId}/tags", todo.getId())
                .content(objectMapper.writeValueAsString(createTagRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }


    @Test
    void tagOkAndTodoNotExist_addNewTagTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        CreateTagRQ createTagRQ = new CreateTagRQ();
        createTagRQ.setName("tagName");

        int todoIdNotExist = 9999999;

        mockMvc.perform(post(TODO_ENDPOINT + "/{todoId}/tags", todoIdNotExist)
                .content(objectMapper.writeValueAsString(createTagRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }


    @Test
    void validData_addExistingTagTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        TodoResource todoResource1 = (TodoResource) userData.get(TODO_RESOURCE_KEY);
        TagResource tagResource1 = todoResource1.getTags().get(0);

        Map<String, Object> userData2 = testData.pop();
        TodoResource todoResource2 = (TodoResource) userData2.get(TODO_RESOURCE_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        long tagId = tagResource1.getId();

        MvcResult mvcResult = mockMvc
                .perform(post(TODO_ENDPOINT + "/{todoId}/tags/{tagId}", todoResource2.getId(), tagId)
                        .with(csrf())
                        .with(token(jwtToken))
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        TodoResource responseTodo = fromMvcResultToObject(mvcResult, TodoResource.class);

        assertEquals(todoResource2.getTags().size() + 1, responseTodo.getTags().size());

        boolean tagNameFound = false;
        for (TagResource tag : responseTodo.getTags()) {
            if (tagId == tag.getId()) {
                tagNameFound = true;
                break;
            }
        }
        assertTrue(tagNameFound);
    }


    @Test
    void userNotTodoOwner_addExistingTagTest() throws Exception {
        // preparation
        // create user and todo_
        User user = new User();
        user.setUsername("userNowTagOwner_addExistingTagTest");
        user.setEmail("userNowTagOwner_addExistingTagTest@todoapp.com");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword(COMMON_PASSWORD);
        user.setActive(true);
        user.setEmailConfirmed(true);
        user.setProvider(AuthProvider.LOCAL);
        User newUser = createNewUser(user);
        Todo todo = new Todo();
        todo.setTitle("addExistingTag title");
        todo.setDescription("addExistingTag desc");
        todo.setOwner(newUser);
        todo.setDone(false);
        todo.setActive(true);
        Todo otherUserTodo = todoRepository.save(todo);

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        TodoResource userTodo = (TodoResource) userData.get(TODO_RESOURCE_KEY);
        long userTagId = userTodo.getTags().get(0).getId();

        mockMvc.perform(post(TODO_ENDPOINT + "/{todoId}/tags/{tagId}", otherUserTodo.getId(), userTagId)
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());

    }

    @Test
    void userNotTagOwner_addExistingTagTest() throws Exception {
        // preparation
        // create user, todo_ and tag
        User user = new User();
        user.setUsername("userNotTodoOwner_addExistingTagTest");
        user.setEmail("userNotTodoOwner_addExistingTagTest@todoapp.com");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword(COMMON_PASSWORD);
        user.setActive(true);
        user.setEmailConfirmed(true);
        user.setProvider(AuthProvider.LOCAL);
        User newUser = createNewUser(user);
        Todo todo = new Todo();
        todo.setTitle("addExistingTag title");
        todo.setDescription("addExistingTag desc");
        todo.setOwner(newUser);
        todo.setDone(false);
        todo.setActive(true);
        Todo otherUserTodo = todoRepository.save(todo);
        Tag tag = new Tag();
        tag.setName("tag name");
        tag.setActive(true);
        tag.setOwner(newUser);
        Tag savedTag = tagRepository.save(tag);
        otherUserTodo.getTags().add(savedTag);
        todoRepository.save(otherUserTodo);

        long otherUserTagId = savedTag.getId();

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        TodoResource userTodo = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        mockMvc.perform(post(TODO_ENDPOINT + "/{todoId}/tags/{tagId}", userTodo.getId(), otherUserTagId)
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }


    @Test
    void tagNotExist_addExistingTagTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        TodoResource todoResource = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        long notExistingTagId = 9999999;

        mockMvc
                .perform(post(TODO_ENDPOINT + "/{todoId}/tags/{tagId}", todoResource.getId(), notExistingTagId)
                        .with(csrf())
                        .with(token(jwtToken))
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }


    @Test
    void todoNotExist_addExistingTagTest() throws Exception {

        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);
        TodoResource todoResource = (TodoResource) userData.get(TODO_RESOURCE_KEY);

        long tagId = todoResource.getTags().get(0).getId();
        long notExistingTodoId = 9999999;

        mockMvc
                .perform(post(TODO_ENDPOINT + "/{todoId}/tags/{tagId}", notExistingTodoId, tagId)
                        .with(csrf())
                        .with(token(jwtToken))
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

    }
}