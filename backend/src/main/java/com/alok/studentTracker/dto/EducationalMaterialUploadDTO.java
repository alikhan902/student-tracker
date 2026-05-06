package com.alok.studentTracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalMaterialUploadDTO {

    // trainingSubjectId
    private Long trainingSubjectId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
}

