package com.kurbatov.todoapp;

import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class TestUtils {

    public static UsernamePasswordAuthenticationToken buildAuthentication(User user) {
        CustomUserDetails userDetails = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(userDetails, null);
    }

}
