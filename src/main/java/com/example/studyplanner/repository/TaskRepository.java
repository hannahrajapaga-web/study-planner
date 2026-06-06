package com.example.studyplanner.repository;

import com.example.studyplanner.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndCompleted(Long userId, boolean completed);

    @Modifying
    @Query(value = "DELETE FROM tasks WHERE subject_id = :subjectId", nativeQuery = true)
    void deleteBySubjectId(@Param("subjectId") Long subjectId);
}