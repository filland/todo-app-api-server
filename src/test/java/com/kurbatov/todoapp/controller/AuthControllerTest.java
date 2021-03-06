package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.BaseWebMvcTest;
import com.kurbatov.todoapp.dto.LoginRQ;
import com.kurbatov.todoapp.dto.RegisterUserRQ;
import com.kurbatov.todoapp.dto.RegisterUserRS;
import com.kurbatov.todoapp.dto.ValidationRS;
import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.security.oauth2.AuthProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends BaseWebMvcTest {

    private final String existingUsername = "authControllerUser";
    private final String existingPassword = "123123";
    private final String existingUserEmail = "authcontroller@todoapp.com";

    @BeforeAll
    public void beforeAll() {
        User user = new User();
        user.setUsername(existingUsername);
        user.setEmail(existingUserEmail);
        user.setPassword(existingPassword);
        user.setActive(true);
        user.setEmailConfirmed(true);
        user.setProvider(AuthProvider.LOCAL);
        createNewUser(user);
    }

    @Test
    public void validUser_loginTest() throws Exception {
        LoginRQ loginRQ = new LoginRQ();
        loginRQ.setUsername(existingUsername);
        loginRQ.setPassword(existingPassword);
        MvcResult result = mockMvc.perform(post("/auth/login")
                .with(csrf())
                .content(objectMapper.writeValueAsString(loginRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        JwtAuthenticationResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), JwtAuthenticationResponse.class);
        String usernameFromJwt = jwtTokenProvider.getUsernameFromJwt(response.getAccessToken());
        assertEquals(existingUsername, usernameFromJwt);
    }

    @Test
    void rightUsernameWrongPassword_loginTest() throws Exception {

        LoginRQ loginRQ = new LoginRQ();
        loginRQ.setUsername(existingUsername);
        loginRQ.setPassword("wrongPassword123");

        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .content(objectMapper.writeValueAsString(loginRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void invalidSymbolsInPassword_loginTest() throws Exception {
        LoginRQ loginRQ = new LoginRQ();
        loginRQ.setUsername(existingUsername);
        loginRQ.setPassword("invalidSymbols_&?>%");

        MvcResult result = mockMvc.perform(post("/auth/login")
                .with(csrf())
                .content(objectMapper.writeValueAsString(loginRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationRS validationRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), ValidationRS.class);

        assertEquals(ErrorType.INVALID_INFORMATION.getCode(), validationRS.getErrorCode());
    }


    @Test
    void usernameDoesNotExist_loginTest() throws Exception {
        LoginRQ loginRQ = new LoginRQ();
        loginRQ.setUsername("fakeUsername");
        loginRQ.setPassword("123123");

        MvcResult result = mockMvc.perform(post("/auth/login")
                .with(csrf())
                .content(objectMapper.writeValueAsString(loginRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden())
                .andReturn();

        ValidationRS validationRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), ValidationRS.class);

        assertEquals(ErrorType.BAD_CREDENTIALS.getCode(), validationRS.getErrorCode());
    }

    @Test
    void userNotConfirmedEmailAddress_loginTest() throws Exception {

        // need to think how mandatory confirmation would affect
        assertTrue(false);
    }


    @Test
    void validUserInfo_registerTest() throws Exception {

        RegisterUserRQ registerUserRQ = new RegisterUserRQ();
        registerUserRQ.setUsername("user2");
        registerUserRQ.setEmail("todoapp@todoapp456.com");
        registerUserRQ.setPassword("123123");

        MvcResult result = mockMvc.perform(post("/auth/register")
                .with(csrf())
                .content(objectMapper.writeValueAsString(registerUserRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        RegisterUserRS registerUserRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), RegisterUserRS.class);

        assertNotNull(registerUserRS.getId());
    }


    @Test
    void invalidSymbolsInUsername_registerTest() throws Exception {
        RegisterUserRQ registerUserRQ = new RegisterUserRQ();
        registerUserRQ.setUsername("user2&?*");
        registerUserRQ.setEmail("invalidSymbolsInUsername_registerTest@todoapp456.com");
        registerUserRQ.setPassword("123123");

        MvcResult result = mockMvc.perform(post("/auth/register")
                .with(csrf())
                .content(objectMapper.writeValueAsString(registerUserRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationRS validationRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), ValidationRS.class);

        assertEquals(ErrorType.INVALID_INFORMATION.getCode(), validationRS.getErrorCode());
    }

    @Test
    void usernameIsTooShort_registerTest() throws Exception {

        RegisterUserRQ registerUserRQ = new RegisterUserRQ();
        registerUserRQ.setUsername("u");
        registerUserRQ.setEmail("usernameIsTooShort_registerTest@todoapp456.com");
        registerUserRQ.setPassword("123123");

        MvcResult result = mockMvc.perform(post("/auth/register")
                .with(csrf())
                .content(objectMapper.writeValueAsString(registerUserRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationRS validationRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), ValidationRS.class);

        assertEquals(ErrorType.INVALID_INFORMATION.getCode(), validationRS.getErrorCode());
    }

    @Test
    void usernameIsTooLong_registerTest() throws Exception {

        StringBuilder tooLongUsername = new StringBuilder();
        for (int i = 0; i < 35; i++) {
            tooLongUsername.append("a");
        }

        RegisterUserRQ registerUserRQ = new RegisterUserRQ();
        registerUserRQ.setUsername(tooLongUsername.toString());
        registerUserRQ.setEmail("usernameIsTooShort_registerTest@todoapp456.com");
        registerUserRQ.setPassword("123123");

        MvcResult result = mockMvc.perform(post("/auth/register")
                .with(csrf())
                .content(objectMapper.writeValueAsString(registerUserRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationRS validationRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), ValidationRS.class);

        assertEquals(ErrorType.INVALID_INFORMATION.getCode(), validationRS.getErrorCode());
    }

    @Test
    void suchUsernameAlreadyExists_registerTest() throws Exception {

        RegisterUserRQ registerUserRQ = new RegisterUserRQ();
        registerUserRQ.setUsername(existingUsername);
        registerUserRQ.setEmail("suchUsernameAlreadyExists@todoapp.com");
        registerUserRQ.setPassword("123123");

        MvcResult result = mockMvc.perform(post("/auth/register")
                .with(csrf())
                .content(objectMapper.writeValueAsString(registerUserRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationRS validationRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), ValidationRS.class);

        assertEquals(ErrorType.USER_WITH_USERNAME_EXISTS.getCode(), validationRS.getErrorCode());
    }


    @Test
    void invalidEmail_registerTest() throws Exception {

        RegisterUserRQ registerUserRQ = new RegisterUserRQ();
        registerUserRQ.setUsername("invalidEmail_registerTest");
        registerUserRQ.setEmail("invalidEmail");
        registerUserRQ.setPassword("123123");

        MvcResult result = mockMvc.perform(post("/auth/register")
                .with(csrf())
                .content(objectMapper.writeValueAsString(registerUserRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationRS validationRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), ValidationRS.class);

        assertEquals(ErrorType.INVALID_INFORMATION.getCode(), validationRS.getErrorCode());
    }

    @Test
    void emailAlreadyExists_registerTest() throws Exception {

        RegisterUserRQ registerUserRQ = new RegisterUserRQ();
        registerUserRQ.setUsername("emailAlreadyExists_registerTe");
        registerUserRQ.setEmail(existingUserEmail);
        registerUserRQ.setPassword("123123");

        MvcResult result = mockMvc.perform(post("/auth/register")
                .with(csrf())
                .content(objectMapper.writeValueAsString(registerUserRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationRS validationRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), ValidationRS.class);

        assertEquals(ErrorType.USER_WITH_EMAIL_EXISTS.getCode(), validationRS.getErrorCode());
    }

    @Test
    void passwordTooShort_registerTest() throws Exception {
        RegisterUserRQ registerUserRQ = new RegisterUserRQ();
        registerUserRQ.setUsername("passwordTooShort_test");
        registerUserRQ.setEmail("passwordTooShort_test@todoapp.com");
        registerUserRQ.setPassword("123");

        MvcResult result = mockMvc.perform(post("/auth/register")
                .with(csrf())
                .content(objectMapper.writeValueAsString(registerUserRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationRS validationRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), ValidationRS.class);

        assertEquals(ErrorType.INVALID_INFORMATION.getCode(), validationRS.getErrorCode());
    }

    @Test
    void passwordTooLong_registerTest() throws Exception {

        StringBuilder tooLongPassword = new StringBuilder();
        for (int i = 0; i < 35; i++) {
            tooLongPassword.append("a");
        }
        RegisterUserRQ registerUserRQ = new RegisterUserRQ();
        registerUserRQ.setUsername("passwordTooLong_registerTest");
        registerUserRQ.setEmail("passwordTooLong_test@todoapp.com");
        registerUserRQ.setPassword(tooLongPassword.toString());

        MvcResult result = mockMvc.perform(post("/auth/register")
                .with(csrf())
                .content(objectMapper.writeValueAsString(registerUserRQ))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationRS validationRS =
                objectMapper.readValue(result.getResponse().getContentAsString(), ValidationRS.class);

        assertEquals(ErrorType.INVALID_INFORMATION.getCode(), validationRS.getErrorCode());
    }
}