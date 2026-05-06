package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.Repository.EducationalMaterialRepository;
import com.alok.studentTracker.Repository.TrainingSubjectRepository;
import com.alok.studentTracker.Repository.UserRepository;
import com.alok.studentTracker.dto.EducationalMaterialAllDTO;
import com.alok.studentTracker.dto.EducationalMaterialResponseDTO;
import com.alok.studentTracker.dto.EducationalMaterialUploadDTO;
import com.alok.studentTracker.entity.EducationalMaterial;
import com.alok.studentTracker.entity.TrainingSubject;
import com.alok.studentTracker.entity.User;
import com.alok.studentTracker.service.EducationalMaterialService;
import com.alok.studentTracker.service.FileService;
import com.alok.studentTracker.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EducationalMaterialServiceImpl implements EducationalMaterialService {

    private final EducationalMaterialRepository educationalMaterialRepository;
    private final TrainingSubjectRepository trainingSubjectRepository;
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final FileService fileService;

    @Override
    public void createEducationalMaterial(EducationalMaterialUploadDTO dto, MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        validationService.validateHeadmanAccess(currentUser.getGroup().getId());

        TrainingSubject trainingSubject = trainingSubjectRepository.findById(dto.getTrainingSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("TrainingSubject not found"));

        // safety: headman can only add material to subjects within his group
        if (currentUser.getGroup() == null || !currentUser.getGroup().getId().equals(trainingSubject.getGroup().getId())) {
            throw new RuntimeException("You are not member of this group");
        }

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is required");
        }

        String storedPath = fileService.saveFile(file, "educational-materials");

        EducationalMaterial material = EducationalMaterial.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .trainingSubject(trainingSubject)
                .filePath(storedPath)
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .uploadedAt(java.time.LocalDateTime.now())
                .build();

        educationalMaterialRepository.save(material);
    }

    @Override
    public void updateEducationalMaterial(Long id, EducationalMaterialUploadDTO dto, MultipartFile file) {
        EducationalMaterial material = educationalMaterialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("EducationalMaterial not found with id: " + id));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        validationService.validateHeadmanAccess(currentUser.getGroup().getId());

        if (currentUser.getGroup() == null || !currentUser.getGroup().getId().equals(material.getTrainingSubject().getGroup().getId())) {
            throw new RuntimeException("You are not member of this group");
        }

        material.setTitle(dto.getTitle());
        material.setDescription(dto.getDescription());

        if (dto.getTrainingSubjectId() != null && !dto.getTrainingSubjectId().equals(material.getTrainingSubject().getId())) {
            TrainingSubject newTs = trainingSubjectRepository.findById(dto.getTrainingSubjectId())
                    .orElseThrow(() -> new IllegalArgumentException("TrainingSubject not found"));
            material.setTrainingSubject(newTs);
        }

        if (file != null && !file.isEmpty()) {
            if (material.getFilePath() != null) {
                fileService.deleteFile(material.getFilePath());
            }

            String storedPath = fileService.saveFile(file, "educational-materials");
            material.setFilePath(storedPath);
            material.setOriginalFileName(file.getOriginalFilename());
            material.setContentType(file.getContentType());
            material.setFileSize(file.getSize());
            material.setUploadedAt(java.time.LocalDateTime.now());
        }

        educationalMaterialRepository.save(material);
    }

    @Override
    public Optional<EducationalMaterialResponseDTO> getEducationalMaterialById(Long id) {
        return educationalMaterialRepository.findById(id)
                .map(m -> new EducationalMaterialResponseDTO(
                        m.getId(),
                        m.getTitle(),
                        m.getDescription(),
                        m.getFilePath(),
                        m.getOriginalFileName(),
                        m.getUploadedAt(),
                        m.getUpdatedAt()
                ));
    }

    @Override
    public List<EducationalMaterialAllDTO> getEducationalMaterialsByTrainingSubjectId(Long trainingSubjectId) {
        return educationalMaterialRepository.findByTrainingSubjectId(trainingSubjectId)
                .stream()
                .map(m -> new EducationalMaterialAllDTO(m.getId(), m.getTitle(), m.getFilePath()))
                .toList();
    }

    @Override
    public void deleteEducationalMaterial(Long id) {
        Optional<EducationalMaterial> opt = educationalMaterialRepository.findById(id);
        if (opt.isEmpty()) {
            return;
        }

        EducationalMaterial material = opt.get();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        validationService.validateHeadmanAccess(currentUser.getGroup().getId());

        if (currentUser.getGroup() == null || !currentUser.getGroup().getId().equals(material.getTrainingSubject().getGroup().getId())) {
            throw new RuntimeException("You are not member of this group");
        }

        if (material.getFilePath() != null) {
            fileService.deleteFile(material.getFilePath());
        }

        educationalMaterialRepository.deleteById(id);
    }
}

