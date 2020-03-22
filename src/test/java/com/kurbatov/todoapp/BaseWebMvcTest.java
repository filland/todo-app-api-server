package com.kurbatov.todoapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kurbatov.todoapp.persistence.dao.UserRepository;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static com.kurbatov.todoapp.TestUtils.buildAuthentication;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseWebMvcTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected User createNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    protected String generateJwtToken(User user) {
        UsernamePasswordAuthenticationToken authentication = buildAuthentication(user);
        return jwtTokenProvider.generateToken(authentication);
    }

    protected RequestPostProcessor token(String tokenValue) {
        return mockRequest -> {
            mockRequest.addHeader("Authorization", "Bearer " + tokenValue);
            return mockRequest;
        };
    }
}
