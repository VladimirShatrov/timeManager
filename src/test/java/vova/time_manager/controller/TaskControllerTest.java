package vova.time_manager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import vova.time_manager.model.Task;
import vova.time_manager.model.TaskView;
import vova.time_manager.model.User;
import vova.time_manager.service.TaskService;

import java.net.URI;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    TaskService taskService;

    @InjectMocks
    TaskController controller;

    @Test
    void findByUserId_ReturnsValidResponseEntity() {

         var task = List.of(new TaskView(1L, "xdd",null , Date.from(Instant.parse("2024-05-10T16:25:00Z")), Date.from(Instant.parse("2024-05-10T17:25:00Z")), LocalTime.parse("00:00"), 1L),
                 new TaskView(2L, "jokerge",null, Date.from(Instant.parse("2024-05-10T16:26:00Z")), Date.from(Instant.parse("2024-05-10T16:27:00Z")), LocalTime.parse("00:01"), 1L));
         Date from = Date.from(Instant.parse("2023-01-01T00:00:00Z"));
         Date to = Date.from(Instant.parse("2025-01-01T00:00:00Z"));
         Mockito.doReturn(task).when(taskService).findTaskByUserId(1L, from, to);

         var tasks = controller.findByUserId(1L, from, to);
         assertNotNull(tasks);
         assertEquals(HttpStatus.OK, tasks.getStatusCode());
         assertEquals(MediaType.APPLICATION_JSON, tasks.getHeaders().getContentType());
         assertEquals(task, tasks.getBody());
    }
    @Test
    void startTask_ReturnsValidResponseEntity() {
        Long userId = 1L;
        Date date = Date.from(Instant.now());
        String name = "new task";
        Long taskId = 1L;

        Mockito.when(taskService.startTask(userId, date, name)).thenReturn(taskId);
        User user = new User(userId, "user", "email", "password");
        Task mockTask = new Task(taskId, user, name, null, date, null);
        Mockito.when(taskService.findById(taskId)).thenReturn(mockTask);
        var responseEntity = controller.startTask(userId, date, name,
                UriComponentsBuilder.fromUriString("http://localhost:8080/task/start"));

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        if (responseEntity.getBody() != null) {
            Task task = responseEntity.getBody();
            assertNotNull(task.getId());
            assertEquals(userId, task.getUser().getId());
            assertEquals(date, task.getDateStart());
            assertEquals(name, task.getName());
            assertNull(task.getDateStop());

            assertEquals(URI.create("http://localhost:8080/task/start/" + task.getId()),
                    responseEntity.getHeaders().getLocation());
        }
        else {
            assertInstanceOf(Task.class, responseEntity.getBody());
        }
    }

    @Test
    void stopTask_ReturnValidResponseEntity() {
        Long userId = 1L;
        Date date = Date.from(Instant.now());
        String name = "task";
        Long taskId = 1L;
        User user = new User(userId, "user", "email", "password");
        Task mockTask = new Task(taskId, user, name, null, date, null);
        Mockito.when(taskService.findById(taskId)).thenReturn(mockTask);

        Date stopTime = Date.from(Instant.now());
        var responseEntity = controller.stopTask(taskId, stopTime);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        mockTask.setDateStop(stopTime);
        assertEquals(responseEntity.getBody(), mockTask);
        assertEquals(responseEntity.getBody().getDateStop(), stopTime);
        assertNotNull(responseEntity.getBody().getDateStop());
    }

    @Test
    void findById_ReturnsValidResponseEntity() {
        Long userId = 1L;
        Date date = Date.from(Instant.now());
        String name = "task";
        Long taskId = 1L;
        User user = new User(userId, "user", "email", "password");
        Task mockTask = new Task(taskId, user, name, null, date, null);
        Mockito.when(taskService.findById(taskId)).thenReturn(mockTask);

        var task = controller.findById(taskId);

        assertNotNull(task);
        assertEquals(HttpStatus.OK, task.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, task.getHeaders().getContentType());
        assertNotNull(task.getBody());
        assertEquals(task.getBody(), mockTask);
    }
}