package com.alok.studentTracker.service;

import com.alok.studentTracker.dto.TrainingSubjectDTO;
import com.alok.studentTracker.dto.TrainingSubjectResponseDTO;
import com.alok.studentTracker.entity.TrainingSubject;


import java.util.List;
import java.util.Optional;

public interface TrainingSubjectService {
    TrainingSubject createTrainingSubjectWithPhoto(com.alok.studentTracker.dto.TrainingSubjectUploadDTO trainingSubjectUploadDTO, org.springframework.web.multipart.MultipartFile photo);
    TrainingSubject updateTrainingSubjectWithPhoto(Long id, com.alok.studentTracker.dto.TrainingSubjectUploadDTO trainingSubjectUploadDTO, org.springframework.web.multipart.MultipartFile photo);
    TrainingSubject createTrainingSubject(TrainingSubjectDTO trainingSubjectDTO);
    Optional<TrainingSubjectResponseDTO> getTrainingSubjectById(Long id);

    List<TrainingSubject> getTrainingSubjectsByGroup();
    TrainingSubject updateTrainingSubject(Long id, TrainingSubjectDTO trainingSubjectDTO);
    void deleteTrainingSubject(Long id);
}
