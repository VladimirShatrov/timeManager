package vova.time_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vova.time_manager.model.Task;
import vova.time_manager.model.TaskView;
import vova.time_manager.service.TaskService;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/start/{userId}/{name}")
    public ResponseEntity<Task> startTask(
            @PathVariable Long userId,
            @RequestParam("dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateStart,
            @PathVariable String name,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long id = taskService.startTask(userId, dateStart, name);
        Task task = taskService.findById(id);
        return ResponseEntity.created(uriComponentsBuilder
                        .path("/{id}")
                        .build(Map.of("id", id)))
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<Task> stopTask(
            @PathVariable Long id,
            @RequestParam("dateStop") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateStop) {
        taskService.stopTask(id, dateStop);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskService.findById(id));
    }

    @PostMapping("/delete/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @CrossOrigin
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskView>> findByUserId (
            @PathVariable Long userId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date to
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskService.findTaskByUserId(userId, from, to));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById (@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskService.findById(id));
    }
}
