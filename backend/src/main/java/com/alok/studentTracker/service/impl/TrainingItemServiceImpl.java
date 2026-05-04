package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.Repository.GroupRepository;
import com.alok.studentTracker.Repository.TrainingItemRepository;
import com.alok.studentTracker.dto.TrainingItemDTO;
import com.alok.studentTracker.entity.Group;
import com.alok.studentTracker.entity.TrainingItem;
import com.alok.studentTracker.service.TrainingItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingItemServiceImpl implements TrainingItemService {

    private final TrainingItemRepository trainingItemRepository;
    private final GroupRepository groupRepository;

    @Override
    public TrainingItem createTrainingItem(TrainingItemDTO trainingItemDTO) {
        Group group = groupRepository.findById(trainingItemDTO.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + trainingItemDTO.getGroupId()));

        TrainingItem trainingItem = TrainingItem.builder()
                .title(trainingItemDTO.getTitle())
                .description(trainingItemDTO.getDescription())
                .group(group)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return trainingItemRepository.save(trainingItem);
    }

    @Override
    public Optional<TrainingItem> getTrainingItemById(Long id) {
        return trainingItemRepository.findById(id);
    }

    @Override
    public List<TrainingItem> getTrainingItemsByGroupId(Long groupId) {
        return trainingItemRepository.findByGroupId(groupId);
    }

    @Override
    public List<TrainingItem> getTrainingItemsByGroupIdOrderByCreatedAtDesc(Long groupId) {
        return trainingItemRepository.findByGroupIdOrderByCreatedAtDesc(groupId);
    }

    @Override
    public List<TrainingItem> searchTrainingItemsByTitle(String title) {
        return trainingItemRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public TrainingItem updateTrainingItem(Long id, TrainingItemDTO trainingItemDTO) {
        return trainingItemRepository.findById(id).map(trainingItem -> {
            trainingItem.setTitle(trainingItemDTO.getTitle());
            trainingItem.setDescription(trainingItemDTO.getDescription());
            trainingItem.setUpdatedAt(LocalDateTime.now());
            return trainingItemRepository.save(trainingItem);
        }).orElseThrow(() -> new IllegalArgumentException("TrainingItem not found with id: " + id));
    }

    @Override
    public void deleteTrainingItem(Long id) {
        trainingItemRepository.deleteById(id);
    }
}
