package com.alok.studentTracker.service;

import com.alok.studentTracker.dto.EducationalMaterialAllDTO;
import com.alok.studentTracker.dto.EducationalMaterialResponseDTO;
import com.alok.studentTracker.dto.EducationalMaterialUploadDTO;
import com.alok.studentTracker.entity.EducationalMaterial;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface EducationalMaterialService {

    void createEducationalMaterial(EducationalMaterialUploadDTO dto, MultipartFile file);

    void updateEducationalMaterial(Long id, EducationalMaterialUploadDTO dto, MultipartFile file);

    Optional<EducationalMaterialResponseDTO> getEducationalMaterialById(Long id);

    List<EducationalMaterialAllDTO> getEducationalMaterialsByTrainingSubjectId(Long trainingSubjectId);

    void deleteEducationalMaterial(Long id);
}

