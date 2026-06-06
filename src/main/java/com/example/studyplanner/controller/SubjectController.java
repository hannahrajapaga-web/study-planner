package com.example.studyplanner.controller;

import com.example.studyplanner.model.Subject;
import com.example.studyplanner.model.User;
import com.example.studyplanner.service.SubjectService;
import com.example.studyplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "http://localhost:5173")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Subject>> getSubjects(Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        return ResponseEntity.ok(subjectService.getSubjectsByUser(user.getId()));
    }

    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject, Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        subject.setUser(user);
        return ResponseEntity.ok(subjectService.createSubject(subject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok().build();
    }
}