package com.alok.studentTracker.controller;

import com.alok.studentTracker.dto.EducationalMaterialAllDTO;
import com.alok.studentTracker.dto.EducationalMaterialResponseDTO;
import com.alok.studentTracker.dto.EducationalMaterialUploadDTO;
import com.alok.studentTracker.service.EducationalMaterialService;
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
@RequestMapping("/api/educational-materials")
@RequiredArgsConstructor
public class EducationalMaterialController {

    private final EducationalMaterialService educationalMaterialService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEducationalMaterial(
            @RequestPart("data") String data,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            EducationalMaterialUploadDTO dto = objectMapper.readValue(data, EducationalMaterialUploadDTO.class);

            Set<ConstraintViolation<EducationalMaterialUploadDTO>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        violations.stream().map(ConstraintViolation::getMessage).toList()
                );
            }

            educationalMaterialService.createEducationalMaterial(dto, file);
            return ResponseEntity.ok("Educational material created successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid multipart request: " + e.getMessage());
        }
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateEducationalMaterial(
            @PathVariable Long id,
            @RequestPart("data") String data,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            EducationalMaterialUploadDTO dto = objectMapper.readValue(data, EducationalMaterialUploadDTO.class);

            Set<ConstraintViolation<EducationalMaterialUploadDTO>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        violations.stream().map(ConstraintViolation::getMessage).toList()
                );
            }

            educationalMaterialService.updateEducationalMaterial(id, dto, file);
            return ResponseEntity.ok("Educational material updated successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid multipart request: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationalMaterialResponseDTO> getEducationalMaterialById(@PathVariable Long id) {
        return educationalMaterialService.getEducationalMaterialById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // списки: по ID предмета (trainingSubjectId)
    @GetMapping("/subject/{id}")
    public ResponseEntity<List<EducationalMaterialAllDTO>> getByTrainingSubjectId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                educationalMaterialService.getEducationalMaterialsByTrainingSubjectId(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationalMaterial(@PathVariable Long id) {
        educationalMaterialService.deleteEducationalMaterial(id);
        return ResponseEntity.noContent().build();
    }
}

