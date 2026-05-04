package com.alok.studentTracker.service;

import com.alok.studentTracker.dto.TrainingSubjectDTO;
import com.alok.studentTracker.entity.TrainingSubject;

import java.util.List;
import java.util.Optional;

public interface TrainingSubjectService {
    TrainingSubject createTrainingSubject(TrainingSubjectDTO trainingSubjectDTO);
    Optional<TrainingSubject> getTrainingSubjectById(Long id);
    List<TrainingSubject> getTrainingSubjectsByGroupId(Long groupId);
    List<TrainingSubject> getTrainingSubjectsByGroupIdOrderByCreatedAtDesc(Long groupId);
    List<TrainingSubject> searchTrainingSubjectsByTitle(String title);
    TrainingSubject updateTrainingSubject(Long id, TrainingSubjectDTO trainingSubjectDTO);
    void deleteTrainingSubject(Long id);
}
