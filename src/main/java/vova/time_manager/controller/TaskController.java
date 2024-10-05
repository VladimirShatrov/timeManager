package vova.time_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vova.time_manager.dto.LaborCost;
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

    @GetMapping("/user/{userId}/sum")
    public ResponseEntity<LaborCost> sumLaborCost (
            @PathVariable Long userId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date to
    ) {
        List<TaskView> tasks = taskService.findTaskByUserId(userId, from, to);
        LaborCost laborCost = new LaborCost(0, 0);
        for (TaskView task: tasks
             ) {
            LocalTime duration = task.getDuration();

            laborCost.setHours(laborCost.getHours() + duration.getHour());
            laborCost.setMinutes(laborCost.getMinutes() + duration.getMinute());
        }

        if (laborCost.getMinutes() >= 60) {
            laborCost.setHours(laborCost.getHours() + laborCost.getMinutes() / 60);
            laborCost.setMinutes(laborCost.getMinutes() % 60);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(laborCost);
    }
}
