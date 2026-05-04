package com.alok.studentTracker.service;

import com.alok.studentTracker.dto.TrainingItemDTO;
import com.alok.studentTracker.entity.TrainingItem;

import java.util.List;
import java.util.Optional;

public interface TrainingItemService {
    TrainingItem createTrainingItem(TrainingItemDTO trainingItemDTO);
    Optional<TrainingItem> getTrainingItemById(Long id);
    List<TrainingItem> getTrainingItemsByGroupId(Long groupId);
    List<TrainingItem> getTrainingItemsByGroupIdOrderByCreatedAtDesc(Long groupId);
    List<TrainingItem> searchTrainingItemsByTitle(String title);
    TrainingItem updateTrainingItem(Long id, TrainingItemDTO trainingItemDTO);
    void deleteTrainingItem(Long id);
}
