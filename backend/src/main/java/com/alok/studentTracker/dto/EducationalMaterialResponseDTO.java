package com.alok.studentTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalMaterialResponseDTO {

    private Long id;
    private String title;
    private String description;

    private String filePath;
    private String originalFileName;

    private LocalDateTime uploadedAt;
    private LocalDateTime updatedAt;
}

