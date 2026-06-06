package com.example.studyplanner.service;
import com.example.studyplanner.model.Subject;
import com.example.studyplanner.repository.SubjectRepository;
import com.example.studyplanner.repository.TaskRepository;
import com.example.studyplanner.repository.StudyScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StudyScheduleRepository studyScheduleRepository;

    public List<Subject> getSubjectsByUser(Long userId) {
        return subjectRepository.findByUserId(userId);
    }

    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Transactional
    public void deleteSubject(Long id) {
        studyScheduleRepository.deleteBySubjectId(id);
        taskRepository.deleteBySubjectId(id);
        subjectRepository.deleteById(id);
    }
}