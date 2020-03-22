package com.kurbatov.todoapp.persistence.init;

import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.security.oauth2.AuthProvider;
import com.kurbatov.todoapp.service.TodoService;
import com.kurbatov.todoapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class is only for populating the database with
 * initial data for testing purposes
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseLoader.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TodoService todoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try {
            User user = new User();
            user.setUsername("user1");
            user.setEmail("todoappuser1@todoapp.com");
            user.setPassword(passwordEncoder.encode("123123"));
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setRole("ROLE_USER");
            user.setActive(true);
            user.setEmailConfirmed(true);
            user.setProvider(AuthProvider.LOCAL);

            User saveUser = userService.saveUser(user);

            for (int i = 0; i < 10; i++) {

                Todo todo = new Todo();
                todo.setTitle("some title for test tod");
                todo.setDescription(i + "some desc for test todo");
                todo.setOwner(saveUser);
                todo.setActive(true);
                todo.setDone(false);
                todoService.save(todo);

                Tag tag1 = new Tag();
                tag1.setActive(true);
                tag1.setName("tag1");
                todoService.addNewTag(todo.getTodoId(), tag1, new CustomUserDetails(user));

                Tag tag2 = new Tag();
                tag2.setActive(true);
                tag2.setName("tag2");
                todoService.addNewTag(todo.getTodoId(), tag2, new CustomUserDetails(user));

                Todo todo2 = new Todo();
                todo2.setTitle("Сфокусируйся на бекенде");
                todo2.setDescription(i + "Сначала делай бек, а фронт выучишь на курсах!");
                todo2.setOwner(saveUser);
                todo2.setActive(true);
                todo2.setDone(false);

                Todo todo3 = new Todo();
                todo3.setTitle("Just get this thing done!");
                todo3.setDescription(i + "no excuses. done or failed");
                todo3.setOwner(saveUser);
                todo3.setActive(true);
                todo3.setDone(false);

                Todo todo4 = new Todo();
                todo4.setTitle("Some title");
                todo4.setDescription(i + "Lorem ipsum dolor sit amet, pri iudico percipit et, purto eruditi deleniti pri in. Ad his paulo civibus, sed te aeque prodesset, qui cu affert aperiam. Mea discere reformidans no, rebum nonumy nostro mel ut. Sumo mollis democritum per eu, duo ex forensibus delicatissimi. Eu has altera admodum albucius, feugiat deserunt incorrupte eum ei, eos id pertinacia mediocritatem. Et per alterum laoreet corpora, pro eu sumo explicari maluisset.");
                todo4.setOwner(saveUser);
                todo4.setDone(false);
                todo4.setActive(true);

                todoService.save(todo2);
                todoService.save(todo3);
                todoService.save(todo4);
            }

        } catch (Throwable e) {
            LOGGER.error("Not managed to populate tables with init data", e);
        }
    }
}
