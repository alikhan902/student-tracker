package com.alok.studentTracker.controller;

import com.alok.studentTracker.dto.TrainingSubjectDTO;
import com.alok.studentTracker.entity.TrainingSubject;
import com.alok.studentTracker.service.TrainingSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/training-subjects")
@RequiredArgsConstructor
public class TrainingSubjectController {

    private final TrainingSubjectService trainingSubjectService;

    @PostMapping
    public ResponseEntity<TrainingSubject> createTrainingSubject(@Valid @RequestBody TrainingSubjectDTO trainingSubjectDTO) {
        TrainingSubject trainingSubject = trainingSubjectService.createTrainingSubject(trainingSubjectDTO);
        return new ResponseEntity<>(trainingSubject, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingSubject> getTrainingSubjectById(@PathVariable Long id) {
        return trainingSubjectService.getTrainingSubjectById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<TrainingSubject>> getTrainingSubjectsByGroupId(@PathVariable Long groupId) {
        List<TrainingSubject> trainingSubjects = trainingSubjectService.getTrainingSubjectsByGroupId(groupId);
        return ResponseEntity.ok(trainingSubjects);
    }

    @GetMapping("/group/{groupId}/latest")
    public ResponseEntity<List<TrainingSubject>> getTrainingSubjectsByGroupIdLatest(@PathVariable Long groupId) {
        List<TrainingSubject> trainingSubjects = trainingSubjectService.getTrainingSubjectsByGroupIdOrderByCreatedAtDesc(groupId);
        return ResponseEntity.ok(trainingSubjects);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainingSubject>> searchTrainingSubjects(@RequestParam String title) {
        List<TrainingSubject> trainingSubjects = trainingSubjectService.searchTrainingSubjectsByTitle(title);
        return ResponseEntity.ok(trainingSubjects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingSubject> updateTrainingSubject(@PathVariable Long id, @Valid @RequestBody TrainingSubjectDTO trainingSubjectDTO) {
        try {
            TrainingSubject trainingSubject = trainingSubjectService.updateTrainingSubject(id, trainingSubjectDTO);
            return ResponseEntity.ok(trainingSubject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingSubject(@PathVariable Long id) {
        trainingSubjectService.deleteTrainingSubject(id);
        return ResponseEntity.noContent().build();
    }
}
