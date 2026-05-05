package com.alok.studentTracker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSubjectUploadDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
}

