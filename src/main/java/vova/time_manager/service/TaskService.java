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
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskViewRepository taskViewRepository;

    public Long startTask(Long userId, Date dateStart, String name) {
        return taskRepository.startTask(userId, dateStart, name);
    }

    public void stopTask(Long id, Date dateStop) {
        taskRepository.stopTask(id, dateStop);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteTask(id);
    }

    public List<TaskView> findTaskByUserId(Long userId, Date from, Date to) {
        return taskViewRepository.findByUserIdOrderByDurationDesc(userId, from, to);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Task with id: " + id + " not found."));
    }
}
