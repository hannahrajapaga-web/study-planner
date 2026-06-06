package com.example.studyplanner.service;

import com.example.studyplanner.model.StudySchedule;
import com.example.studyplanner.repository.StudyScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudyScheduleService {

    @Autowired
    private StudyScheduleRepository scheduleRepository;

    public List<StudySchedule> getSchedulesByUser(Long userId) {
        return scheduleRepository.findByUserId(userId);
    }

    public StudySchedule createSchedule(StudySchedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}