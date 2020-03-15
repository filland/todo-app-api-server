package com.kurbatov.todoapp.persistence.init;

import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.service.TodoService;
import com.kurbatov.todoapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

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

    @Override
    public void run(String... args) throws Exception {
        try {
            User user = new User();
            user.setUsername("user1");
            user.setEmail("dddd@asdf.com");
            user.setPassword("$2a$10$Es6k9yWE9a0HoYYQ.ivwWuwA3IYJfi.Ryy/oQCuNVYSdmnIAoKVNS");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setRole("ROLE_USER");
            user.setActive(true);
            user.setEmailConfirmed(true);

            User saveUser = userService.saveUser(user);

            for (int i = 0; i < 10; i++) {

                Todo todo = new Todo();
                todo.setTitle("some title for test tod");
//                    todo.setDescription("some desc for test todo");
                todo.setDescription(UUID.randomUUID().toString());
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
                todo3.setDescription("no excuses. done or failed");
                todo3.setOwner(saveUser);
                todo3.setActive(true);
                todo3.setDone(false);

                Todo todo4 = new Todo();
                todo4.setTitle("Some title");
//                    todo4.setDescription("Lorem ipsum dolor sit amet, pri iudico percipit et, purto eruditi deleniti pri in. Ad his paulo civibus, sed te aeque prodesset, qui cu affert aperiam. Mea discere reformidans no, rebum nonumy nostro mel ut. Sumo mollis democritum per eu, duo ex forensibus delicatissimi. Eu has altera admodum albucius, feugiat deserunt incorrupte eum ei, eos id pertinacia mediocritatem. Et per alterum laoreet corpora, pro eu sumo explicari maluisset.");
                todo4.setDescription(UUID.randomUUID().toString());
                todo4.setOwner(saveUser);
                todo4.setDone(false);
                todo4.setActive(true);

                todoService.save(todo);
                todoService.save(todo2);
                todoService.save(todo3);
                todoService.save(todo4);
            }

        } catch (Throwable e) {
            LOGGER.error("Not managed to populate tables with init data", e);
        }
    }
}
