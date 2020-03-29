package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.BaseWebMvcTest;
import com.kurbatov.todoapp.dto.user.ChangeUserPasswordRQ;
import com.kurbatov.todoapp.dto.user.UpdateUserRQ;
import com.kurbatov.todoapp.dto.user.UserResource;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.oauth2.AuthProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends BaseWebMvcTest {

    private final String USER_ENDPOINT = "/api/v1/users";

    private static final String TOKEN_KEY = "token";
    private static final String USER_KEY = "user";

    private static final String COMMON_PASSWORD = "123qwerty";

    // queue of maps where map contains user and jwtToken for the user
    private LinkedList<Map<String, Object>> testData;

    @BeforeAll
    void beforeAll() {

        testData = new LinkedList<>();

        String existingUsername = "userControllerUser";
        String existingUserEmail = "usercontroller@todoapp.com";

        // create one user for each test and generate jwt token for this user
        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = new HashMap<>();

            User user = new User();
            user.setUsername(existingUsername + i);
            user.setEmail(i + existingUserEmail);
            user.setFirstName("firstname");
            user.setLastName("lastname");
            user.setPassword(COMMON_PASSWORD);
            user.setActive(true);
            user.setEmailConfirmed(true);
            user.setProvider(AuthProvider.LOCAL);
            User newUser = createNewUser(user);
            map.put(USER_KEY, newUser);

            String jwtToken = generateJwtToken(user);
            map.put(TOKEN_KEY, jwtToken);

            testData.push(map);
        }
    }

    @Test
    void validRequest_getCurrentUserTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        User user = (User) userData.get(USER_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        MvcResult result = mockMvc.perform(get(USER_ENDPOINT + "/me")
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        UserResource userFromResponse = objectMapper
                .readValue(result.getResponse().getContentAsString(), UserResource.class);
        assertEquals(user.getEmail(), userFromResponse.getEmail());
        assertEquals(user.getUsername(), userFromResponse.getUsername());
        assertEquals(user.getFirstName(), userFromResponse.getFirstName());
        assertEquals(user.getLastName(), userFromResponse.getLastName());
    }


    @Test
    void validUserData_updateCurrentUserTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        User user = (User) userData.get(USER_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        UpdateUserRQ updateUserRQ = new UpdateUserRQ();
        updateUserRQ.setEmail("t" + user.getEmail());
        updateUserRQ.setUsername("t" + user.getUsername());
        updateUserRQ.setFirstName("new" + user.getFirstName());
        updateUserRQ.setLastName("new" + user.getLastName());

        MvcResult result = mockMvc.perform(post(USER_ENDPOINT + "/me")
                .content(objectMapper.writeValueAsString(updateUserRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        UserResource userFromResponse = objectMapper
                .readValue(result.getResponse().getContentAsString(), UserResource.class);
        assertEquals(updateUserRQ.getEmail(), userFromResponse.getEmail());
        assertEquals(updateUserRQ.getUsername(), userFromResponse.getUsername());
        assertEquals(updateUserRQ.getFirstName(), userFromResponse.getFirstName());
        assertEquals(updateUserRQ.getLastName(), userFromResponse.getLastName());
    }


    @Test
    void invalidUsername_updateCurrentUserTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        User user = (User) userData.get(USER_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        UpdateUserRQ updateUserRQ = new UpdateUserRQ();
        updateUserRQ.setEmail("new" + user.getEmail());
        updateUserRQ.setUsername("invalid&?#" + user.getUsername());
        updateUserRQ.setFirstName("new" + user.getFirstName());
        updateUserRQ.setLastName("new" + user.getLastName());

        mockMvc.perform(post(USER_ENDPOINT + "/me")
                .content(objectMapper.writeValueAsString(updateUserRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidFirstName_updateCurrentUserTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        User user = (User) userData.get(USER_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        UpdateUserRQ updateUserRQ = new UpdateUserRQ();
        updateUserRQ.setEmail("new" + user.getEmail());
        updateUserRQ.setUsername("new" + user.getUsername());
        updateUserRQ.setFirstName("invalid%^&" + user.getFirstName());
        updateUserRQ.setLastName("new" + user.getLastName());

        mockMvc.perform(post(USER_ENDPOINT + "/me")
                .content(objectMapper.writeValueAsString(updateUserRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidLastName_updateCurrentUserTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        User user = (User) userData.get(USER_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        UpdateUserRQ updateUserRQ = new UpdateUserRQ();
        updateUserRQ.setEmail("new" + user.getEmail());
        updateUserRQ.setUsername("new" + user.getUsername());
        updateUserRQ.setFirstName("new" + user.getFirstName());
        updateUserRQ.setLastName("invalid%^&" + user.getLastName());

        mockMvc.perform(post(USER_ENDPOINT + "/me")
                .content(objectMapper.writeValueAsString(updateUserRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }


    @Test
    void invalidEmail_updateCurrentUserTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        User user = (User) userData.get(USER_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        UpdateUserRQ updateUserRQ = new UpdateUserRQ();
        updateUserRQ.setEmail("" + new Date().getTime());
        updateUserRQ.setUsername("new" + user.getUsername());
        updateUserRQ.setFirstName("new" + user.getFirstName());
        updateUserRQ.setLastName("new" + user.getLastName());

        mockMvc.perform(post(USER_ENDPOINT + "/me")
                .content(objectMapper.writeValueAsString(updateUserRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeEmail_updateCurrentUserTest() {
        // generate and send another confirmation email after updating email address ?
        assertTrue(false);
    }

    @Test
    void validData_changeCurrentPasswordTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        ChangeUserPasswordRQ changeUserPasswordRQ = new ChangeUserPasswordRQ();
        changeUserPasswordRQ.setOldPassword(COMMON_PASSWORD);
        changeUserPasswordRQ.setNewPassword(COMMON_PASSWORD + "123");

        mockMvc.perform(post(USER_ENDPOINT + "/me/password")
                .content(objectMapper.writeValueAsString(changeUserPasswordRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @Test
    void wrongOldPassword_changeCurrentPasswordTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        ChangeUserPasswordRQ changeUserPasswordRQ = new ChangeUserPasswordRQ();
        changeUserPasswordRQ.setOldPassword("wrongOldPassword");
        changeUserPasswordRQ.setNewPassword(COMMON_PASSWORD + "123");

        mockMvc.perform(post(USER_ENDPOINT + "/me/password")
                .content(objectMapper.writeValueAsString(changeUserPasswordRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void invalidOldPassword_changeCurrentPasswordTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        ChangeUserPasswordRQ changeUserPasswordRQ = new ChangeUserPasswordRQ();
        changeUserPasswordRQ.setOldPassword(COMMON_PASSWORD + "%&*");
        changeUserPasswordRQ.setNewPassword(COMMON_PASSWORD + "123");

        mockMvc.perform(post(USER_ENDPOINT + "/me/password")
                .content(objectMapper.writeValueAsString(changeUserPasswordRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidNewPassword_changeCurrentPasswordTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        ChangeUserPasswordRQ changeUserPasswordRQ = new ChangeUserPasswordRQ();
        changeUserPasswordRQ.setOldPassword(COMMON_PASSWORD);
        changeUserPasswordRQ.setNewPassword(COMMON_PASSWORD + "invalid%&*");

        mockMvc.perform(post(USER_ENDPOINT + "/me/password")
                .content(objectMapper.writeValueAsString(changeUserPasswordRQ))
                .with(csrf())
                .with(token(jwtToken))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }


    @Test
    void usernameExists_checkIfUsernameAlreadyUsedTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        User user = (User) userData.get(USER_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        mockMvc.perform(
                get(USER_ENDPOINT + "/check-username?username={usernamePlaceholder}", user.getUsername())
                        .with(csrf())
                        .with(token(jwtToken))
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void usernameNotExist_checkIfUsernameAlreadyUsedTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        String usernameWhatDoesNotExist = "newUsername" + new Date().getTime();

        mockMvc.perform(
                get(USER_ENDPOINT + "/check-username?username={usernamePlaceholder}", usernameWhatDoesNotExist)
                        .with(csrf())
                        .with(token(jwtToken))
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

    }


    @Test
    void emailExists_checkIfEmailAlreadyUsedTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        String jwtToken = (String) userData.get(TOKEN_KEY);

        String emailThatDoesNotExist = new Date().getTime() + "mail@todoapp.com";

        mockMvc.perform(
                get(USER_ENDPOINT + "/check-email?email={emailPlaceholder}", emailThatDoesNotExist)
                        .with(csrf())
                        .with(token(jwtToken))
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @Test
    void emailDoesNotExist_checkIfEmailAlreadyUsedTest() throws Exception {
        Map<String, Object> userData = testData.pop();
        User user = (User) userData.get(USER_KEY);
        String jwtToken = (String) userData.get(TOKEN_KEY);

        mockMvc.perform(
                get(USER_ENDPOINT + "/check-email?email={emailPlaceholder}", user.getEmail())
                        .with(csrf())
                        .with(token(jwtToken))
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

}