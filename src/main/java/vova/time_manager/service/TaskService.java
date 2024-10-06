package vova.time_manager.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @PersistenceContext
    private final EntityManager entityManager;

    public Long startTask(Long userId, Date dateStart, String name) {
        return taskRepository.startTask(userId, dateStart, name);
    }

    public void stopTask(Long id, Date dateStop) {
        taskRepository.stopTask(id, dateStop);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = entityManager.find(Task.class, id);
        if (task != null) {
            entityManager.remove(task);
        }
        else {
            throw new EntityNotFoundException("Task not found");
        }
    }

    public List<TaskView> findTaskViewByUserId(Long userId, Date from, Date to) {
        return taskViewRepository.findByUserIdOrderByDurationDesc(userId, from, to);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Task with id: " + id + " not found."));
    }

    public List<Task> findTaskByUserId(Long userId) {
        return taskRepository.findTaskByUserId(userId);
    }

    @Transactional
    public void deleteAllUserTask(Long userId) {
        List<Task> tasks = this.findTaskByUserId(userId);
        for (Task task: tasks
        ) {
            entityManager.remove(task);
        }
    }
}
