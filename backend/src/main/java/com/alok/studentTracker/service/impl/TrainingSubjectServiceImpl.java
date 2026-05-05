package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.Repository.GroupRepository;
import com.alok.studentTracker.Repository.TrainingSubjectRepository;
import com.alok.studentTracker.Repository.UserRepository;
import com.alok.studentTracker.entity.User;
import com.alok.studentTracker.service.ValidationService;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;
    private final ValidationService validationService;

    @Override
    public TrainingSubject createTrainingSubject(TrainingSubjectDTO trainingSubjectDTO) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        validationService.validateHeadmanAccess(currentUser.getGroup().getId());

        TrainingSubject trainingSubject = TrainingSubject.builder()
                .title(trainingSubjectDTO.getTitle())
                .description(trainingSubjectDTO.getDescription())
                .group(currentUser.getGroup())
                .build();

        return trainingSubjectRepository.save(trainingSubject);
    }

    @Override
    public Optional<TrainingSubject> getTrainingSubjectById(Long id) {
        return trainingSubjectRepository.findById(id);
    }

    @Override
    public List<TrainingSubject> getTrainingSubjectsByGroup() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getGroup() == null) {
            throw new RuntimeException("User is not assigned to any group");
        }

        return trainingSubjectRepository.findByGroupId(currentUser.getGroup().getId());
    }

    @Override
    public TrainingSubject updateTrainingSubject(Long id, TrainingSubjectDTO trainingSubjectDTO) {
        TrainingSubject trainingSubject = trainingSubjectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("TrainingSubject not found with id: " + id));
        
        validationService.validateHeadmanAccess(trainingSubject.getGroup().getId());
        
        trainingSubject.setTitle(trainingSubjectDTO.getTitle());
        trainingSubject.setDescription(trainingSubjectDTO.getDescription());
        
        return trainingSubjectRepository.save(trainingSubject);
    }

    @Override
    public void deleteTrainingSubject(Long id) {
        Optional<TrainingSubject> trainingSubjectOpt = trainingSubjectRepository.findById(id);
        if (trainingSubjectOpt.isPresent()) {
            validationService.validateHeadmanAccess(trainingSubjectOpt.get().getGroup().getId());
        }
        trainingSubjectRepository.deleteById(id);
    }
}
