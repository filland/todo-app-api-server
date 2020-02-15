package com.kurbatov.todoapp.persistence.init;

import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.service.TodoService;
import com.kurbatov.todoapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * This class is only for populating the database with
 * initial data for testing purposes
 */
@Component
public class PopulateTables {

    private static final Logger LOGGER = LoggerFactory.getLogger(PopulateTables.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TodoService todoService;

    @PostConstruct
    public void populate() {
        try {
            Optional<User> test = userService.findById(1L);
            if (!test.isPresent()) {

                User user = new User();
                user.setUsername("user");
                user.setEmail("dddd@asdf.com");
                user.setPassword("$2a$10$Es6k9yWE9a0HoYYQ.ivwWuwA3IYJfi.Ryy/oQCuNVYSdmnIAoKVNS");
                user.setFirstName("John");
                user.setLastName("Doe");
                user.setRole("ROLE_USER");
                user.setActive(true);
                user.setEmailConfirmed(true);

                User saveUser = userService.saveUser(user);

                Todo todo = new Todo();
                todo.setTitle("some title for test tod");
                todo.setDescription("some desc for test todo");
                todo.setOwner(saveUser);
                todo.setActive(true);
                todo.setDone(false);

                Todo todo2 = new Todo();
                todo2.setTitle("Сфокусируйся на бекенде");
                todo2.setDescription("Сначала делай бек, а фронт выучишь на курсах!");
                todo2.setOwner(saveUser);
                todo2.setActive(true);
                todo2.setDone(false);

                Todo todo3 = new Todo();
                todo3.setTitle("Just get this thing done!");
                todo3.setDescription("no excuse. done or failed");
                todo3.setOwner(saveUser);
                todo3.setActive(true);
                todo3.setDone(false);

                todoService.save(todo);
                todoService.save(todo2);
                todoService.save(todo3);
            }

        } catch (Throwable e) {
            LOGGER.error("Not managed to populate tables with init data", e);
        }

    }
}
