package vova.time_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import vova.time_manager.model.Task;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Procedure(procedureName = "prc_start_task")
    Long startTask(Long userId, Date dateStart, String name);

    @Procedure(procedureName = "prc_stop_task")
    void stopTask(Long taskId, Date dateStop);

    List<Task> findTaskByUserId(Long userId);
}
