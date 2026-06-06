package com.example.studyplanner.repository;

import com.example.studyplanner.model.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {
    List<StudySchedule> findByUserId(Long userId);

    @Modifying
    @Query(value = "DELETE FROM study_schedules WHERE subject_id = :subjectId", nativeQuery = true)
    void deleteBySubjectId(@Param("subjectId") Long subjectId);
}