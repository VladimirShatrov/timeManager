package vova.time_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vova.time_manager.model.TaskView;
import vova.time_manager.service.TaskService;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @CrossOrigin
    @GetMapping("/start/{userId}/{dateStart}/{name}")
    public void startTask(@PathVariable Long userId, @PathVariable LocalTime dateStart, @PathVariable String name) {
        taskService.startTask(userId, dateStart, name);
    }

    @CrossOrigin
    @GetMapping("/stop/{id}/{dateStop}")
    public void stopTask(@PathVariable Long id, @PathVariable LocalTime dateStop) {
        taskService.stopTask(id, dateStop);
    }

    @CrossOrigin
    @GetMapping("/delete/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @CrossOrigin
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskView>> findByUserId (@PathVariable Long userId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.taskService.findTaskByUserId(userId));
    }
}
