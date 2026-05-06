package com.alok.studentTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSubjectResponseDTO {

    private Long id;
    private String title;

    private String description;
    private String photoUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

