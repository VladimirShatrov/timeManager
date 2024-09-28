package vova.time_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vova.time_manager.model.Task;
import vova.time_manager.model.TaskView;
import vova.time_manager.model.User;
import vova.time_manager.repository.TaskRepository;
import vova.time_manager.repository.TaskViewRepository;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskViewRepository taskViewRepository;

    public Long startTask(Long userId, LocalTime dateStart, String name) {
        return taskRepository.startTask(userId, dateStart, name);
    }

    public void stopTask(Long id, LocalTime dateStop) {
        taskRepository.stopTask(id, dateStop);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteTask(id);
    }

    public List<TaskView> findTaskByUserId(Long userId) {
        return taskViewRepository.findByUserId(userId);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Task with id: " + id + " not found."));
    }
}
