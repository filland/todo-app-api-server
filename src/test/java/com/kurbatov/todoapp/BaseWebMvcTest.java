package com.kurbatov.todoapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kurbatov.todoapp.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class BaseWebMvcTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    protected RequestPostProcessor token(String tokenValue) {
        return mockRequest -> {
            mockRequest.addHeader("Authorization", "Bearer " + tokenValue);
            return mockRequest;
        };
    }
}
