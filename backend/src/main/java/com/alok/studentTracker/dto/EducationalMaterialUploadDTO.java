package com.alok.studentTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalMaterialUploadDTO {

    // trainingSubjectId
    private Long trainingSubjectId;

    private String title;

    private String description;
}


