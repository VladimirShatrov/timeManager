package vova.time_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vova.time_manager.model.TaskView;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskViewRepository extends JpaRepository<TaskView, Long> {

    @Query("SELECT t FROM TaskView t WHERE t.userId = :userId AND t.dateStart BETWEEN :from AND :to ORDER BY t.duration DESC")
    List<TaskView> findByUserIdOrderByDurationDesc(
            @Param("userId") Long userId,
            @Param("from") Date from,
            @Param("to") Date to
    );
}
