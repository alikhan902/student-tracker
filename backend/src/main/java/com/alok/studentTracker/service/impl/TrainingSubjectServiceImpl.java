package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.Repository.GroupRepository;
import com.alok.studentTracker.Repository.TrainingSubjectRepository;
import com.alok.studentTracker.dto.TrainingSubjectDTO;
import com.alok.studentTracker.entity.Group;
import com.alok.studentTracker.entity.TrainingSubject;
import com.alok.studentTracker.service.TrainingSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingSubjectServiceImpl implements TrainingSubjectService {

    private final TrainingSubjectRepository trainingSubjectRepository;
    private final GroupRepository groupRepository;

    @Override
    public TrainingSubject createTrainingSubject(TrainingSubjectDTO trainingSubjectDTO) {
        Group group = groupRepository.findById(trainingSubjectDTO.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + trainingSubjectDTO.getGroupId()));

        TrainingSubject trainingSubject = TrainingSubject.builder()
                .title(trainingSubjectDTO.getTitle())
                .description(trainingSubjectDTO.getDescription())
                .group(group)
                .build();

        return trainingSubjectRepository.save(trainingSubject);
    }

    @Override
    public Optional<TrainingSubject> getTrainingSubjectById(Long id) {
        return trainingSubjectRepository.findById(id);
    }

    @Override
    public List<TrainingSubject> getTrainingSubjectsByGroupId(Long groupId) {
        return trainingSubjectRepository.findByGroupId(groupId);
    }

    @Override
    public List<TrainingSubject> getTrainingSubjectsByGroupIdOrderByCreatedAtDesc(Long groupId) {
        return trainingSubjectRepository.findByGroupIdOrderByCreatedAtDesc(groupId);
    }

    @Override
    public List<TrainingSubject> searchTrainingSubjectsByTitle(String title) {
        return trainingSubjectRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public TrainingSubject updateTrainingSubject(Long id, TrainingSubjectDTO trainingSubjectDTO) {
        return trainingSubjectRepository.findById(id).map(trainingSubject -> {
            trainingSubject.setTitle(trainingSubjectDTO.getTitle());
            trainingSubject.setDescription(trainingSubjectDTO.getDescription());
            return trainingSubjectRepository.save(trainingSubject);
        }).orElseThrow(() -> new IllegalArgumentException("TrainingSubject not found with id: " + id));
    }

    @Override
    public void deleteTrainingSubject(Long id) {
        trainingSubjectRepository.deleteById(id);
    }
}
