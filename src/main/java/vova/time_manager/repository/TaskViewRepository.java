package vova.time_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vova.time_manager.model.TaskView;

import java.util.List;

@Repository
public interface TaskViewRepository extends JpaRepository<TaskView, Long> {

    List<TaskView> findByUserId(Long user_id);
}
