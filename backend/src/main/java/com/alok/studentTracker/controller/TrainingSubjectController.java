package com.alok.studentTracker.controller;

import com.alok.studentTracker.dto.TrainingSubjectAllDTO;
import com.alok.studentTracker.dto.TrainingSubjectUploadDTO;
import com.alok.studentTracker.dto.TrainingSubjectResponseDTO;

import com.alok.studentTracker.service.TrainingSubjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/training-subjects")
@RequiredArgsConstructor
public class TrainingSubjectController {

    private final TrainingSubjectService trainingSubjectService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createTrainingSubject(
            @RequestPart("data") String data,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            TrainingSubjectUploadDTO dto = objectMapper.readValue(data, TrainingSubjectUploadDTO.class);

            Set<ConstraintViolation<TrainingSubjectUploadDTO>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .toList()
                );
            }

            trainingSubjectService.createTrainingSubjectWithPhoto(dto, photo);
            return ResponseEntity.ok("Training subject created successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid multipart request: " + e.getMessage());
        }
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateTrainingSubject(
            @PathVariable Long id,
            @RequestPart("data") String data,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            TrainingSubjectUploadDTO dto = objectMapper.readValue(data, TrainingSubjectUploadDTO.class);

            Set<ConstraintViolation<TrainingSubjectUploadDTO>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .toList()
                );
            }

            trainingSubjectService.updateTrainingSubjectWithPhoto(id, dto, photo);
            return ResponseEntity.ok("Training subject updated successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid multipart request: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingSubjectResponseDTO> getTrainingSubjectById(@PathVariable Long id) {
        return trainingSubjectService.getTrainingSubjectById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping()
    public ResponseEntity<List<TrainingSubjectAllDTO>> searchTrainingSubjects() {
        List<TrainingSubjectAllDTO> trainingSubjects =
                trainingSubjectService.getTrainingSubjectsByGroup()
                        .stream()
                        .map(subject -> new TrainingSubjectAllDTO(
                                subject.getId(),
                                subject.getTitle(),
                                subject.getPhotoUrl()
                        ))
                        .toList();

        return ResponseEntity.ok(trainingSubjects);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingSubject(@PathVariable Long id) {
        trainingSubjectService.deleteTrainingSubject(id);
        return ResponseEntity.noContent().build();
    }
}