package vova.time_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vova.time_manager.model.Task;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT fnc_start_task(:userId, CAST(:dateStart AS timestamp with time zone), :name)", nativeQuery = true)
    Long startTask(@Param("userId") Long userId, @Param("dateStart") Date dateStart, @Param("name") String name);

    @Procedure(procedureName = "prc_stop_task")
    void stopTask(Long taskId, Date dateStop);

    List<Task> findTaskByUserId(Long userId);
}
