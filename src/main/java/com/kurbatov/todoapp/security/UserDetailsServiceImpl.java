package com.kurbatov.todoapp.security;

import com.kurbatov.todoapp.exception.TodoAppException;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user;
        try {
            user = userService.findByUsername(username);
        } catch (TodoAppException e) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new CustomUserDetails(user);
    }

    /**
     * Method is used by JWTAuthenticationFilter
     */
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userService.findById(id);

        return new CustomUserDetails(user);
    }
}

