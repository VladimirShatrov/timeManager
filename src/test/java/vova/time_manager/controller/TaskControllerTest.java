package vova.time_manager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import vova.time_manager.model.TaskView;
import vova.time_manager.service.TaskService;

import java.time.LocalTime;
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
         var task = List.of(new TaskView(1L, "xdd", LocalTime.parse("14:32"), LocalTime.parse("15:32"), LocalTime.parse("01:00"), 1L),
                 new TaskView(2L, "jokerge", LocalTime.parse("23:01"), LocalTime.parse("23:02"), LocalTime.parse("00:01"), 1L));
         Mockito.doReturn(task).when(this.taskService).findTaskByUserId(1L);

         var tasks = this.controller.findByUserId(1L);
         assertNotNull(tasks);
         assertEquals(HttpStatus.OK, tasks.getStatusCode());
         assertEquals(MediaType.APPLICATION_JSON, tasks.getHeaders().getContentType());
         assertEquals(task, tasks.getBody());
    }
}