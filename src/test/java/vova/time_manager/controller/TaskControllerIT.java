package vova.time_manager.controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import vova.time_manager.model.Task;
import vova.time_manager.model.User;
import vova.time_manager.repository.TaskRepository;
import vova.time_manager.repository.TaskViewRepository;
import vova.time_manager.repository.UserRepository;

import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class TaskControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskViewRepository taskViewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        taskViewRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();

        entityManager.createNativeQuery("ALTER SEQUENCE app_user_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE task_id_seq RESTART WITH 1").executeUpdate();

        entityManager.createNativeQuery("CREATE OR REPLACE FUNCTION fnc_start_task(" +
                        "IN userid bigint, " +
                        "IN datestart timestamp with time zone, " +
                        "IN taskname character varying) " +
                        "RETURNS bigint AS " +
                        "$$ " +
                        "DECLARE " +
                        "new_id bigint; " +
                        "BEGIN " +
                        "INSERT INTO task(date_start, name, user_id) VALUES (datestart, taskname, userid) " +
                        "RETURNING id INTO new_id; " +
                        "RETURN new_id; " +
                        "END; " +
                        "$$ LANGUAGE plpgsql;")
                .executeUpdate();
    }

    @Test
    void findByUserId_ReturnsValidResponseEntity() throws Exception {

        User user = new User( "user", "@mail.com", "password");
        userRepository.save(user);

        Task task1 = new Task(null, user, "xdd", null,
                Date.from(Instant.parse("2024-05-10T16:25:00Z")),
                Date.from(Instant.parse("2024-05-10T17:25:00Z")));
        Task task2 = new Task(null, user, "jokerge", null,
                Date.from(Instant.parse("2024-05-10T16:26:00Z")),
                Date.from(Instant.parse("2024-05-10T16:27:00Z")));

        taskRepository.save(task1);
        taskRepository.save(task2);

        var requestBuilder = MockMvcRequestBuilders.get("/task/user/{id}", 1L)
                .param("from", "2023-01-01T00:00:00Z")
                .param("to", "2025-01-01T00:00:00Z");

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                    [
                                    {
                                        "id": 1,
                                        "name": "xdd",
                                        "details": null,
                                        "dateStart": "16:25",
                                        "dateStop": "17:25",
                                        "duration": "01:00",
                                        "userId": 1
                                    },
                                    {
                                        "id": 2,
                                        "name": "jokerge",
                                        "details": null,
                                        "dateStart": "16:26",
                                        "dateStop": "16:27",
                                        "duration": "00:01",
                                        "userId": 1
                                    }
                                    ]
                                    """)
                );
    }

    @Test
    void startTask_ReturnsValidResponseEntity() throws Exception{
        User user = new User("user", "@mail.com", "password");
        userRepository.save(user);

        var requestBuilder = MockMvcRequestBuilders.post("/task/start/{userId}/{name}", 1L, "xdd")
                .param("dateStart", "2024-07-10T16:09:00Z");

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                    {
                                        "id": 1,
                                        "user": {
                                            "id": 1,
                                            "username": "user",
                                            "email": "@mail.com",
                                            "password": "password",
                                            "authorities": [],
                                            "enabled": true,
                                            "accountNonExpired": true,
                                            "credentialsNonExpired": true,
                                            "accountNonLocked": true
                                        },
                                        "name": "xdd",
                                        "details": null,
                                        "dateStart": "2024-07-10T16:09:00.000+00:00",
                                        "dateStop": null
                                    }
                                    """)
                );
    }
}
